import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;
import Databaseop.Databaseop.Category;

public class Categories extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	String rowid = "";
	int myrow = 0;
	int myid = 0;
	String sfrom="";
	JFrame pframe;
	String stypmark="";
	String sheader="";
	
	Categories(String ptypmark) {
		stypmark = ptypmark;
		init();
	txname.requestFocus(true);	
	ctable_update(stypmark);
//	hh.iconhere(this);
	}
	Categories(JFrame parent, String wfrom, String ptypmark) {
		sfrom =wfrom;
		pframe = parent;
		stypmark = ptypmark;
		init();
		txname.requestFocus(true);	
		ctable_update(stypmark);
	}

	private void init(){
		UIManager.put("ComboBox.selectionBackground", hh.lpiros);
		UIManager.put("ComboBox.selectionForeground", hh.feher);
		UIManager.put("ComboBox.background", new ColorUIResource(hh.homok));
		UIManager.put("ComboBox.foreground", Color.BLACK);
		UIManager.put("ComboBox.border", new LineBorder(Color.green, 1));
		//setTitle("Categories");
		getContentPane().setBackground(hh.zgreen1);
		setSize(492, 530);
		setLayout(null);
		setLocationRelativeTo(null);
		setUndecorated(true);
//		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.out.println("Itt vagyok");
				dispose();
			}
		});
		basePanel = new JPanel(null);
		basePanel.setBounds(2,2,485, 525);
		basePanel.setBackground(hh.zgreen1);
     	basePanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green));
		add(basePanel);
		
		exitPanel = gr.makeexitpanel(this);
		exitPanel.setBounds(375, 15, 100, 35);
		exitPanel.setOpaque(true);
		exitPanel.setBackground(hh.zgreen1);		
		basePanel.add(exitPanel);
		
		if (stypmark == "E") {
        sheader = "EXPENSE CATEGORIES";
		}else {
			sheader = "INCOME CATEGORIES";
		}
		lbheader = hh.fflabel(sheader);
		lbheader.setBounds(30, 19, 290, 30);
		lbheader.setForeground(Color.yellow);
		lbheader.setFont(new Font("tahoma", Font.BOLD, 24));
		basePanel.add(lbheader);
		
		mPanel = new JPanel(null);
		mPanel.setBounds(14, 60, 460, 450);
		mPanel.setBorder(hh.ztroundborder(Color.yellow));
		mPanel.setBackground(hh.zgreen1);
		basePanel.add(mPanel);
		
		lbname = hh.clabel("Name");
		lbname.setBounds(10, 70, 70, 30);
		mPanel.add(lbname);

		txname = hh.cTextField(40);
		txname.setBounds(90, 70, 290, 30);
		mPanel.add(txname);
		txname.addKeyListener(hh.MUpper());

		btnsave = gr.sbcs("Save");
		btnsave.setBounds(100, 125, 90, 35);
		btnsave.setBackground(hh.lpiros);
		mPanel.add(btnsave);
	    btnsave.addActionListener(e -> savebutt());

		btncancel = gr.sbcs("Cancel");
		btncancel.setBounds(195, 125, 90, 35);		
		btncancel.setBackground(Color.green);
		mPanel.add(btncancel);
		btncancel.addActionListener(e -> data_cancel());
		
		btndelete = gr.sbcs("Delete");
		btndelete.setBounds(290, 125, 90, 35);		
		btndelete.setBackground(Color.yellow);
		mPanel.add(btndelete);
    	btndelete.addActionListener(e -> data_delete());

		ctable = hh.ztable();
		ctable.setTableHeader(new JTableHeader(ctable.getColumnModel()) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});
		ctable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = ctable.getSelectedRow();
				if (row >= 0) {
					rowid = ctable.getValueAt(row, 0).toString();
					txname.setText(ctable.getValueAt(row, 1).toString());
					myrow = row;
				}
			}
		});

		hh.madeheader(ctable);
		ctable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				ctable.scrollRectToVisible(ctable.getCellRect(ctable.getRowCount() - 1, 0, true));
			}
		});

		jScrollPane1 = new JScrollPane(ctable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		ctable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {},
				new String[] { "cid", "Category name","Type" }));
		hh.setJTableColumnsWidth(ctable, 360, 0, 100, 0);
		jScrollPane1.setViewportView(ctable);
		jScrollPane1.setBounds(50, 180, 360, 200);	
		mPanel.add(jScrollPane1);
		
		btnsendto = hh.cbutton("Send to Transactions");
		btnsendto.setBounds(125, 395, 220, 30);
		btnsendto.setBackground(hh.narancs);

		if (sfrom == "transactions") {
			mPanel.add(btnsendto);
		}
		
		btnsendto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				data_send();
			}
		});			
		
		setVisible(true);
	}
	
	private int data_delete() {
		String sql = " delete from categories where cid=";
		int flag = 0;
		DefaultTableModel d1 = (DefaultTableModel) ctable.getModel();
		int sIndex = ctable.getSelectedRow();
		if (sIndex < 0) {
			return flag;
		}
		String gid = d1.getValueAt(sIndex, 0).toString();
		if (gid.equals("")) {
			return flag;
		}
		int a = JOptionPane.showConfirmDialog(null, "Do you really want to delete ?");
		if (a == JOptionPane.YES_OPTION) {
			int selections[] = ctable.getSelectedRows();
			int lastIndex = selections[0];
			String vsql = sql + "'" + gid + "'";
			try {
			flag = dh.Insupdel(vsql);
			d1.removeRow(sIndex);
			if (ctable.getRowCount() > 0) {
				ctable.setRowSelectionInterval(lastIndex - 1, lastIndex - 1);
			}
			} catch (Exception e1) {
				if (ctable.getRowCount() > 0)
					ctable.setRowSelectionInterval(0, 0);
			}
			clearFields();
		}
		return flag;
	}
	private void savebutt() {
		DefaultTableModel d1 = (DefaultTableModel) ctable.getModel();
		String sql = "";
		int gid = 0;
		String name = txname.getText();
		if (hh.zempty(name) == true) {
			return;
		}
		try {
			if (rowid != "") {
				sql = "update  categories set cname= '" + name + "', ctype= '" + stypmark 
						+"' where cid = " + rowid;
			} else {
				sql = "insert into categories (cname, ctype) values" + " ('" + name + "','" + stypmark +"')";
			}
			int flag = dh.Insupdel(sql);
			if (flag > 0) {
				hh.ztmessage("Success", "Message");
				if (rowid == "") {
					int myid = dd.table_maxid("SELECT MAX(cid) AS max_id from categories");
					d1.insertRow(d1.getRowCount(), new Object[] { myid, name, stypmark });
					hh.gotolastrow(ctable);
					if (ctable.getRowCount() > 0) {
						int row = ctable.getRowCount() - 1;
						ctable.setRowSelectionInterval(row, row);
					}

				} else {
					table_rowrefresh(name);
				}
			} else {
				JOptionPane.showMessageDialog(null, "sql error !");
			}
		} catch (Exception e) {
			System.err.println("SQLException: " + e.getMessage());
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "sql insert hiba");
		}
		clearFields();
	}
	private void table_rowrefresh(String name) {
		DefaultTableModel d1 = (DefaultTableModel) ctable.getModel();
		d1.setValueAt(name, myrow, 1);
		d1.setValueAt(stypmark, myrow, 2);
	}
	private void data_cancel() {
		clearFields();
		txname.requestFocus(true);
	}


	private void clearFields() {
		txname.setText("");
		myrow = 0;
		rowid = "";
		txname.requestFocus(true);
	}
	private void ctable_update(String what) {
		DefaultTableModel m1 = (DefaultTableModel) ctable.getModel();
		m1.setRowCount(0);
		String Sql = "select  cid, cname, ctype from categories  "
				+ "where ctype ='" + stypmark +  "' order by upper( cname)";
		
		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String cid = rs.getString("cid");
				String cname = rs.getString("cname");
				String ctype = rs.getString("ctype");
				m1.addRow(new Object[] { cid, cname, ctype });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}

		hh.setJTableColumnsWidth(ctable, 360, 0, 100, 0);

		ctable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				ctable.scrollRectToVisible(ctable.getCellRect(ctable.getRowCount() - 1, 0, true));
			}
		});
		if (ctable.getRowCount() > 0) {
			int row = ctable.getRowCount() - 1;
			ctable.setRowSelectionInterval(row, row);
		}
	}
	private void data_send() {
		DefaultTableModel d1 = (DefaultTableModel) ctable.getModel();
		int row = ctable.getSelectedRow();
		int number = 0;
		String cnum = "";
		if (row > -1) {
			cnum = d1.getValueAt(row, 0).toString();
			if (!hh.zempty(cnum)) {
				number = Integer.parseInt(cnum);
			}
			((Transactions) pframe).passtocmbc(number);
			pframe.setVisible(true);	
			dispose();
		}
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Categories("E");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
JLabel lbheader, lbname;
JTextField txname;
JTable ctable;
JButton btnsave, btncancel, btndelete, btnsendto;
JPanel mPanel, exitPanel, basePanel;
JScrollPane jScrollPane1;
}
