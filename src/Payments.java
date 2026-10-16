import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
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

public class Payments extends JFrame{
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
	
	Payments(){
		init();
	txname.requestFocus(true);	
	ptable_update("");
//	hh.iconhere(this);	
	}
	Payments(JFrame parent, String wfrom){
	sfrom =wfrom;
	pframe = parent;
	init();
	txname.requestFocus(true);	
	ptable_update("");
	}	
	
	private void init() {
		UIManager.put("ComboBox.selectionBackground", hh.lpiros);
		UIManager.put("ComboBox.selectionForeground", hh.feher);
		UIManager.put("ComboBox.background", new ColorUIResource(hh.homok));
		UIManager.put("ComboBox.foreground", Color.BLACK);
		UIManager.put("ComboBox.border", new LineBorder(Color.green, 1));
		setTitle("Payments");
		getContentPane().setBackground(hh.zgreen1);
		setSize(497, 500);
		setLayout(null);
		setLocationRelativeTo(null);
		setUndecorated(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				dispose();
			}
		});	
		
		basePanel = new JPanel(null);
		basePanel.setBounds(2, 2, 491, 496);
		basePanel.setBackground(hh.zgreen1);
		basePanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green));
		add(basePanel);
		
		exitPanel = gr.makeexitpanel(this);
		exitPanel.setBounds(375, 10, 100, 35);
		exitPanel.setOpaque(true);
		exitPanel.setBackground(hh.zgreen1);		
		basePanel.add(exitPanel);
		
		lbheader = new JLabel("Payments");
		lbheader.setBounds(30, 15, 290, 30);
		lbheader.setForeground(Color.yellow);
		lbheader.setFont(new Font("tahoma", Font.BOLD, 24));
		basePanel.add(lbheader);
		
		mPanel = new JPanel(null);
		mPanel.setBounds(15, 50, 460, 430);
		mPanel.setBorder(hh.ztroundborder(Color.yellow));
		mPanel.setBackground(hh.zgreen1);
		basePanel.add(mPanel);
		
		lbname = hh.clabel("Name");
		lbname.setBounds(10, 40, 70, 30);
		mPanel.add(lbname);

		txname = hh.cTextField(40);
		txname.setBounds(100, 40, 280, 30);
		mPanel.add(txname);
		txname.addKeyListener(hh.MUpper());

		btnsave = gr.sbcs("Save");
		btnsave.setBounds(100, 100, 90, 35);
		btnsave.setBackground(hh.lpiros);
		mPanel.add(btnsave);
	    btnsave.addActionListener(e -> savebutt());

		btncancel = gr.sbcs("Cancel");
		btncancel.setBounds(195, 100, 90, 35);		
		btncancel.setBackground(Color.green);
		mPanel.add(btncancel);
	    btncancel.addActionListener(e -> data_cancel());
		
		btndelete = gr.sbcs("Delete");
		btndelete.setBounds(290, 100, 90, 35);		
		btndelete.setBackground(Color.yellow);
		mPanel.add(btndelete);
     	btndelete.addActionListener(e -> data_delete());

		ptable = hh.ztable();
		ptable.setTableHeader(new JTableHeader(ptable.getColumnModel()) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});
		ptable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = ptable.getSelectedRow();
				if (row >= 0) {
					rowid = ptable.getValueAt(row, 0).toString();
					txname.setText(ptable.getValueAt(row, 1).toString());
					myrow = row;
				}
			}
		});

		hh.madeheader(ptable);
		ptable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				ptable.scrollRectToVisible(ptable.getCellRect(ptable.getRowCount() - 1, 0, true));
			}
		});

		jScrollPane = new JScrollPane(ptable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		ptable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "pid", "Payment name" }));
		hh.setJTableColumnsWidth(ptable, 360, 0, 100);
		jScrollPane.setViewportView(ptable);
		jScrollPane.setBounds(55, 160, 360, 200);	
		mPanel.add(jScrollPane);	
		
		btnsendto = hh.cbutton("Send to Transactions");
		btnsendto.setBounds(125, 375, 220, 30);
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
		String sql = " delete from payments where pid=";
		int flag = 0;
		DefaultTableModel d1 = (DefaultTableModel) ptable.getModel();
		int sIndex = ptable.getSelectedRow();
		if (sIndex < 0) {
			return flag;
		}
		String gid = d1.getValueAt(sIndex, 0).toString();
		if (gid.equals("")) {
			return flag;
		}
		int a = JOptionPane.showConfirmDialog(null, "Do you really want to delete ?");
		if (a == JOptionPane.YES_OPTION) {
			int selections[] = ptable.getSelectedRows();
			int lastIndex = selections[0];
			String vsql = sql + "'" + gid + "'";
			try {
			flag = dh.Insupdel(vsql);
			d1.removeRow(sIndex);
			if (ptable.getRowCount() > 0) {
				ptable.setRowSelectionInterval(lastIndex - 1, lastIndex - 1);
			}
			} catch (Exception e1) {
				if (ptable.getRowCount() > 0)
					ptable.setRowSelectionInterval(0, 0);
			}
			clearFields();
		}
		return flag;
	}
	private void savebutt() {
		DefaultTableModel d1 = (DefaultTableModel) ptable.getModel();
		String sql = "";
		int gid = 0;
		String name = txname.getText();
		if (hh.zempty(name) == true) {
			return;
		}
		try {
			if (rowid != "") {
				sql = "update  payments set name= '" + name + "' where pid = " + rowid;
			} else {
				sql = "insert into payments (name) values" + " ('" + name + "')";
			}
			int flag = dh.Insupdel(sql);
			if (flag > 0) {
				hh.ztmessage("Success", "Message");
				if (rowid == "") {
					int myid = dd.table_maxid("SELECT MAX(pid) AS max_id from payments");
					d1.insertRow(d1.getRowCount(), new Object[] { myid, name });
					hh.gotolastrow(ptable);
					if (ptable.getRowCount() > 0) {
						int row = ptable.getRowCount() - 1;
					ptable.setRowSelectionInterval(row, row);
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
		DefaultTableModel d1 = (DefaultTableModel) ptable.getModel();
		d1.setValueAt(name, myrow, 1);
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
	private void ptable_update(String what) {
		DefaultTableModel m1 = (DefaultTableModel) ptable.getModel();
		m1.setRowCount(0);
		String Sql = "";
		if (what == "") {
			Sql = "select  pid, name from payments order by upper( name)";
		} else {
			Sql = "select  pid, name  from payments where " + what +" order by upper(name)";
		}
		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String cid = rs.getString("pid");
				String name = rs.getString("name");
				m1.addRow(new Object[] { cid, name });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
		hh.setJTableColumnsWidth(ptable, 360, 0, 100);
		ptable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				ptable.scrollRectToVisible(ptable.getCellRect(ptable.getRowCount() - 1, 0, true));
			}
		});
		if (ptable.getRowCount() > 0) {
			int row = ptable.getRowCount() - 1;
			ptable.setRowSelectionInterval(row, row);
		}
	}
	
	private void data_send() {
		DefaultTableModel d1 = (DefaultTableModel) ptable.getModel();
		int row = ptable.getSelectedRow();
		int number = 0;
		String cnum = "";
		if (row > -1) {
			cnum = d1.getValueAt(row, 0).toString();
			if (!hh.zempty(cnum)) {
				number = Integer.parseInt(cnum);
			}
			((Transactions) pframe).passtocmbx(number);
			pframe.setVisible(true);	
			dispose();
		}
	}


	
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Payments();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
JLabel lbheader, lbname;	
JTextField txname;
JPanel mPanel, exitPanel, basePanel;
JButton btnsave, btncancel, btndelete, btnsendto;
JTable ptable;
JScrollPane jScrollPane;
}
