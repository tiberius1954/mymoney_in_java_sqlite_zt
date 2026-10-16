package Databaseop;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import Classes.Hhelper;


public class Databaseop {
	Connection con;
	Statement stmt;
	PreparedStatement pst;
	ResultSet rs;
	DatabaseHelper dh = new DatabaseHelper();
	Hhelper hh = new Hhelper();	
	Payment payment;
	Category category;
	Subcategory subcategory;

	private String[] typesnames = new String[] { "Expense", "Income" };
	public String[] typesnames() {		
		return typesnames;
	}

	public String myidfv(JTable dtable) {
		String retid = "";
		try {
			DefaultTableModel model = (DefaultTableModel) dtable.getModel();
			int row = dtable.getSelectedRow();
			if (row > -1) {
				retid = model.getValueAt(row, 0).toString();
			}
		} catch (Exception e) {
			System.out.println("sql error!!!");
		}
		return retid;
	}

	public int data_delete(JTable dtable, String sql) {
		int flag = 0;
		DefaultTableModel d1 = (DefaultTableModel) dtable.getModel();
		int sIndex = dtable.getSelectedRow();
		if (sIndex < 0) {
			return flag;
		}
		String iid = d1.getValueAt(sIndex, 0).toString();
		if (iid.equals("")) {
			return flag;
		}
		int a = JOptionPane.showConfirmDialog(null, "Do you really want to delete ?");
		if (a == JOptionPane.YES_OPTION) {
			int selections[] = dtable.getSelectedRows();
			int lastIndex = selections[0];
			String vsql = sql + iid;
			flag = dh.Insupdel(vsql);
			if (flag > 0) {
				d1.removeRow(sIndex);
			}
			if (dtable.getRowCount() > 0 && lastIndex> 0) {
				dtable.setRowSelectionInterval(lastIndex - 1, lastIndex - 1);
			}
		}
		return flag;
	}

	public void rtable_delete(JTable dtable, String pid) {
		DefaultTableModel d1 = (DefaultTableModel) dtable.getModel();
		String sql;
		int flag = 0;
		int sIndex = dtable.getSelectedRow();
		int selections[] = dtable.getSelectedRows();
		int lastIndex = selections[0];
		sql = "delete from properties where pid='" + pid + "'";
		try {
			flag = dh.Insupdel(sql);
			d1.removeRow(sIndex);
			if (dtable.getRowCount() > 0) {
				dtable.setRowSelectionInterval(lastIndex - 1, lastIndex - 1);
			}
		} catch (Exception e1) {
			if (dtable.getRowCount() > 0)
				dtable.setRowSelectionInterval(0, 0);
		}
	}

	public int tdata_delete(JTable dtable, String sql, int row) {
		int flag = 0;
		DefaultTableModel d1 = (DefaultTableModel) dtable.getModel();
		flag = dh.Insupdel(sql);
		if (flag == 1) {
			d1.removeRow(row);
		}
		return flag;
	}

	public int table_maxid(String sql) {
		int myid = 0;
		try {
			rs = dh.GetData(sql);
			if (!rs.next()) {
				System.out.println("Error.");
			} else {
				myid = rs.getInt("max_id");
			}
			dh.CloseConnection();
		} catch (SQLException ex) {
			System.err.println("SQLException: " + ex.getMessage());
			ex.printStackTrace();
		}
		return myid;
	}

	public Boolean cannotdelete(String sql) {
		Boolean found = false;
		rs = dh.GetData(sql);
		try {
			if (rs.next()) {
				found = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dh.CloseConnection();
		return found;
	}

	public boolean recordfound(String Query) {
		boolean found = false;
		int count = 0;
		try {
			rs = dh.GetData(Query);
			while (rs.next()) {
				count = rs.getInt("count(*)");
			}
			dh.CloseConnection();
		} catch (SQLException SQLe) {
			System.out.println("Count(*) error " + SQLe.toString());
		}
		if (count > 0) {
			found = true;
		}
		return found;
	}

	public int manyrecord(String Query) {
		int count = 0;
		try {
			rs = dh.GetData(Query);
			while (rs.next()) {
				count = rs.getInt("count(*)");
			}
			dh.CloseConnection();
		} catch (SQLException SQLe) {
			System.out.println("Count(*) error " + SQLe.toString());
		}
		return count;
	}

	public void cmbpaymentcombofill(JComboBox mycombo) {
		String sql;
		mycombo.removeAllItems();
//		Payment A = new Payment(0, "");
		Payment A = new Payment();
	//	mycombo.addItem(A);
		sql = "select pid, name from payments order by upper(name)";
		ResultSet rs = dh.GetData(sql);
		try {
			while (rs.next()) {
				A = new Payment(rs.getInt("pid"), rs.getString("name"));
				mycombo.addItem(A);
			}
			dh.CloseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void cmbcategoriescombofill(JComboBox mycombo, String type) {
		String sql;
		mycombo.removeAllItems();
		Category A = new Category(0, "");
		//Category A = new Category();
	 	mycombo.addItem(A);
		sql = "select cid, cname  from categories  where ctype= '" + type +"' order by upper(cname)";
		ResultSet rs = dh.GetData(sql);
		try {
			while (rs.next()) {
				A = new Category(rs.getInt("cid"), rs.getString("cname"));
				mycombo.addItem(A);
			}
			dh.CloseConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
		public class Category {
			  int cid;	
			  String cname;	
			  public Category() {			  
			  }
			  public Category (int cid, String cname) {
			    this.cid = cid;
			    this.cname = cname;			 
			  }
			  @Override
			  public String toString() 
			  { 	
			     	return cname; 	 
			  } 
			 public int getCid() {
				  return cid;
			  }		
			 public void setCid(int scid) {
				  this.cid = cid;
			  }
			
			 public void setCname(String cname) {
				  this.cname = cname;
			  }
			 public String getCname() {
				 return cname;
			 }		
			}
		
		public void subcategoriescombofill(JComboBox mycombo, String type) {
			mycombo.removeAllItems();
			Subcategory  A = new Subcategory (0, "",0);
			mycombo.addItem(A);
			String sql = "select scid, sname, cid  from subcategories where "
					+ "stype= '" + type +"'order by upper(sname)";
			ResultSet rs = dh.GetData(sql);
			try {
				while (rs.next()) {
					A = new Subcategory(rs.getInt("scid"), rs.getString("sname"), rs.getInt("cid"));
					mycombo.addItem(A);
				}
			} catch (SQLException e) {			
				e.printStackTrace();
			}
			dh.CloseConnection();		
		}
		
		public class Subcategory {
			  int scid;
			  int cid;
			  String sname;	
			  public Subcategory() {			  
			  }
			  public Subcategory (int scid, String sname, int cid) {
			    this.scid = scid;
			    this.sname = sname;		
			    this.cid = cid;
			  }
			  @Override
			  public String toString() 
			  { 	
			     	return sname; 	 
			  } 
			 public int getScid() {
				  return scid;
			  }		
			 public void setScid(int scid) {
				  this.scid = scid;
			  }
			 
			 public int getCid() {
				  return cid;
			  }
			 public void setSname(String sname) {
				  this.sname = sname;
			  }
			 public String getSname() {
				 return sname;
			 }		
			}
	
	public class Payment {
		String name;
		int pid;

		public Payment() {
		}

		public Payment(int pid, String name) {
			this.name = name;
			this.pid = pid;
		}

		@Override
		public String toString() {
			return name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getPid() {
			return pid;
		}

		public void setPid(int pid) {
			this.pid = pid;
		}
	}
	
}
