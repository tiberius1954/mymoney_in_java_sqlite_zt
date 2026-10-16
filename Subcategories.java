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

public class Subcategories extends JFrame{
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
	String stypmark="I";
	String sheader ="";

	Subcategories(String ptype) {
		stypmark = ptype;
		init();
		dd.cmbcategoriescombofill(cmbcategories, stypmark );		
	  ctable_update(stypmark);
	
	}
	Subcategories(JFrame parent, String wfrom, String ptypmark) {
		sfrom =wfrom;
		pframe = parent;	
		stypmark = ptypmark;
		init();
		dd.cmbcategoriescombofill(cmbcategories, stypmark);
		//	txproductcode.requestFocus(true);	
		ctable_update(stypmark);
	}

	private void init(){
		UIManager.put("ComboBox.selectionBackground", hh.lpiros);
		UIManager.put("ComboBox.selectionForeground", hh.feher);
		UIManager.put("ComboBox.background", new ColorUIResource(hh.homok));
		UIManager.put("ComboBox.foreground", Color.BLACK);
		UIManager.put("ComboBox.border", new LineBorder(Color.green, 1));
		setTitle("Subcategories");
		getContentPane().setBackground(hh.citrom);
		setSize(497, 560);
		setLayout(null);
		setLocationRelativeTo(null);
		setUndecorated(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				dispose();
			}
		});
		basePanel = new JPanel(null);
		basePanel.setBounds(2,2,491, 555);
		basePanel.setBackground(hh.zgreen1);
     	basePanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green));
		add(basePanel);
		
		exitPanel = gr.makeexitpanel(this);
		exitPanel.setBounds(385, 15, 100, 35);
		exitPanel.setOpaque(true);
		exitPanel.setBackground(hh.zgreen1);		
		basePanel.add(exitPanel);
		
		if (stypmark == "E") {
	        sheader = "EXPENSE SUBCATEGORIES";
			}else {
				sheader = "INCOME SUBCATEGORIES";
			}		

		lbheader = new JLabel(sheader);
		lbheader.setBounds(30, 20, 350, 30);
		lbheader.setForeground(Color.yellow);
		lbheader.setFont(new Font("tahoma", Font.BOLD, 24));
		basePanel.add(lbheader);
		
		mPanel = new JPanel(null);
		mPanel.setBounds(10, 60, 470, 480);
		mPanel.setBorder(hh.ztroundborder(Color.yellow));
		mPanel.setOpaque(true);
		mPanel.setBackground(hh.zgreen1);
		basePanel.add(mPanel);	
		
		lbcategories = hh.clabel("Categories");
		lbcategories.setBounds(10, 30, 120, 25);
		mPanel.add(lbcategories);

		cmbcategories = hh.cbcombo();
		cmbcategories.setBounds(140, 30, 260, 25);
		cmbcategories.setName("category");
		mPanel.add(cmbcategories);
		
		
		lbname = hh.clabel("Subcategory");
		lbname.setBounds(10, 80, 120, 30);
		mPanel.add(lbname);

		txsname = hh.cTextField(40);
		txsname.setBounds(140, 80, 260, 25);
		mPanel.add(txsname);
		txsname.addKeyListener(hh.MUpper());
		
		
		btnsave = gr.sbcs("Save");
		btnsave.setBounds(130, 135, 90, 35);
		btnsave.setBackground(hh.lpiros);
		mPanel.add(btnsave);
    	btnsave.addActionListener(e -> savebutt());

		btncancel = gr.sbcs("Cancel");
		btncancel.setBounds(225, 135, 90, 35);		
		btncancel.setBackground(Color.green);
		mPanel.add(btncancel);
	 	btncancel.addActionListener(e -> data_cancel());
		
		btndelete = gr.sbcs("Delete");
		btndelete.setBounds(330, 135, 90, 35);		
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
					rowid = ctable.getValueAt(row, 2).toString();					
					txsname.setText(ctable.getValueAt(row, 3).toString());
					int number = 0;
					String cnum = ctable.getValueAt(row, 0).toString();
					if (!hh.zempty(cnum)) {
						number = Integer.parseInt(cnum);
					}
					hh.setSelectedValue(cmbcategories, number);
					cmbcategories.updateUI();
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
				new String[] { "cid","Category name","scid", "Subcategory name",  }));
		hh.setJTableColumnsWidth(ctable, 360, 0, 50,0,50);
		jScrollPane1.setViewportView(ctable);
		jScrollPane1.setBounds(20, 195, 423, 210);	
		mPanel.add(jScrollPane1);
		
		btnsendto = hh.cbutton("Send to transactions");
		btnsendto.setBounds(150, 425, 180, 30);
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
		
		 ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/grad2.png"));
			Image i2 = i1.getImage().getScaledInstance(1300, 650, Image.SCALE_DEFAULT);
			ImageIcon i3 = new ImageIcon(i2);
			JLabel imagex = new JLabel(i3);
			imagex.setBounds(0, 0, 497, 560);
			add(imagex);
		
       setVisible(true);
	}
	private void savebutt() {
		DefaultTableModel d1 = (DefaultTableModel) ctable.getModel();
		String sql = "";
		int gid = 0;
		String name = txsname.getText();
		String category =  (String) ((Category) cmbcategories.getSelectedItem()).getCname();		
		int cid = (int) ((Category) cmbcategories.getSelectedItem()).getCid();		
		if (hh.zempty(name) == true) {
			return;
		}
		try {
			if (rowid != "") {
				sql = "update  subcategories set sname= '" + name + "', stype ='" + stypmark 
						+ "', cid= '"+ cid + "'  where scid = " + rowid;
			} else {
				sql = "insert into subcategories (sname, stype, cid) values  ('" + name 
						+ "','" + stypmark +"','" + cid + "')";
			}
			int flag = dh.Insupdel(sql);
			if (flag > 0) {
				hh.ztmessage("Success", "Message");
				if (rowid == "") {
					int myid = dd.table_maxid("SELECT MAX(scid) AS max_id from subcategories");
					d1.insertRow(d1.getRowCount(), new Object[] { cid, category, myid, name,  });
					hh.gotolastrow(ctable);
					if (ctable.getRowCount() > 0) {
						int row = ctable.getRowCount() - 1;
						ctable.setRowSelectionInterval(row, row);
					}
				} else {
					table_rowrefresh(cid, category, name);
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
	
	private void table_rowrefresh(int cid, String category, String name) {
		DefaultTableModel d1 = (DefaultTableModel) ctable.getModel();
		d1.setValueAt(cid, myrow, 0);
		d1.setValueAt(category, myrow, 1);
		d1.setValueAt(name, myrow, 3);
		
	}
	private void data_cancel() {
		clearFields();		
	}
	private int data_delete() {
		String sql = " delete from subcategories where scid=";
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
//			if (ctable.getRowCount() > 0) {
//				ctable.setRowSelectionInterval(lastIndex - 1, lastIndex - 1);
//			}
			} catch (Exception e1) {
//				if (ctable.getRowCount() > 0) {
//					ctable.setRowSelectionInterval(0, 0);
//			}
			}
			clearFields();
		}
		return flag;
	}
	

	
	private void clearFields() {
		txsname.setText("");
		cmbcategories.setSelectedIndex(0);
		myrow = 0;
		rowid = "";
		txsname.requestFocus(true);
	}
	private void ctable_update(String what) {
		DefaultTableModel m1 = (DefaultTableModel) ctable.getModel();
		m1.setRowCount(0);
		String Sql = "";
	
			Sql = "select  scid, s.sname, c.cid, c.cname  from subcategories s "
					+ " left join categories c on s.cid = c.cid  where ctype='" + what 
					+"' order by upper(cname), upper(sname)";

		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String cid = rs.getString("cid");
				String cname = rs.getString("cname");		
				String scid = rs.getString("scid");
				String sname = rs.getString("sname");
					
				m1.addRow(new Object[] { cid, cname, scid, sname });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
	
		hh.setJTableColumnsWidth(ctable, 360, 0, 50, 0,50);
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
					new Subcategories("E");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	JLabel lbheader, lbname, lbcategories;
	JTextField txsname;
	JTable ctable;
	JButton btnsave, btncancel, btndelete, btnsendto;
	JPanel mPanel, exitPanel, basePanel;
	JScrollPane jScrollPane1;
	JComboBox cmbcategories;

}
