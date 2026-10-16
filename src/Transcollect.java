import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;


public class Transcollect extends JFrame{
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	JFrame pframe;
	
	
	Transcollect(String catid, String year, JFrame parent){
		pframe = parent;
		init();
		txtotal.setText("0.00");
		collectfv( catid, year);		
	}
	Transcollect(){
		init();
		txtotal.setText("0.00");		
	}
	
	private void init() {
		UIManager.put("ComboBox.selectionBackground", hh.lpiros);
		UIManager.put("ComboBox.selectionForeground", hh.feher);
		UIManager.put("ComboBox.background", new ColorUIResource(hh.homok));
		UIManager.put("ComboBox.foreground", Color.BLACK);
		UIManager.put("ComboBox.border", new LineBorder(Color.green, 1));
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		//setAlwaysOnTop(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				dispose();
			}
		});

		setLayout(null);
		setMinimumSize(new Dimension(100, 100));
		setSize(new java.awt.Dimension(600, 400));
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		basePanel = new JPanel(null);
		basePanel.setBounds(2,2,594, 395);
		basePanel.setBackground(hh.zgreen1);
     	basePanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green));
		add(basePanel);
		
		exitPanel = gr.makeexitpanel(this);
		exitPanel.setBounds(490, 10, 100, 35);
		exitPanel.setOpaque(true);
		exitPanel.setBackground(new Color(51, 255, 51));
		basePanel.add(exitPanel);

		lbheader = hh.fflabel("Choose transactions");
		lbheader.setBounds(25, 10, 300, 35);
		basePanel.add(lbheader);
		
		lPanel = new JPanel(null);
		// lPanel.setBackground(new Color(178, 216, 216));
		lPanel.setBounds(10, 50, 580, 340);
		lPanel.setBorder(hh.ztroundborder(Color.yellow));
		lPanel.setOpaque(false);
		basePanel.add(lPanel);
		ttable = hh.ztable();
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) ttable.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment(SwingConstants.LEFT);
		ttable.setTableHeader(new JTableHeader(ttable.getColumnModel()) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});	

		hh.madeheader(ttable);
		ttable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		ttable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		tPane = new JScrollPane(ttable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		String[] pheader = new String[] { "Date",  "Catid", "Subcategory", "Amount","Mark" };

		Object[][] data = {};	
		 model = new DefaultTableModel(data, pheader) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Boolean.class; // Checkbox column
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only checkbox column editable
            }
        };

		ttable.setModel(model);
		tPane.setViewportView(ttable);
		tPane.setBounds(22, 25, 540, 235);
		tPane.setBorder(hh.myRaisedBorder);
		lPanel.add(tPane);

		int[] wcols0 = { 1};
		hh.cellswidth_0(ttable, wcols0);

		int[][] wcols = { { 0, 120 }, { 2, 220 }, { 3, 120}, { 4, 80 } };
		hh.cellswidth(ttable, wcols);

		int[] cols = { 3 };
		hh.cellsright(ttable, cols);		
		
		 model.addTableModelListener(new TableModelListener() {
	            @Override
	            public void tableChanged(TableModelEvent e) {	              
	                if (e.getType() == TableModelEvent.UPDATE) {
	                    int row = e.getFirstRow();
	                    int col = e.getColumn();
	                    // Check if checkbox column changed
	                    if (col == 4) {
	                    	String stotal = txtotal.getText();
	                    	double dtotal = hh.stodd(stotal);
	                        Boolean checked = (Boolean) model.getValueAt(row, col);	                    
	                        String samount = (String) model.getValueAt(row, 3);
	                        double damount = hh.stodd(samount);
	                        if (checked== true) {
	                        	 dtotal = dtotal + damount;
	                        }else {
	                        	 dtotal = dtotal - damount;
	                        }
	                        stotal= hh.d(dtotal);
	                        txtotal.setText(stotal);	                      
	                    }
	                }
	            }
	        });
		
		
		lbtotal = hh.clabel("Total");
		lbtotal.setBounds(110, 280, 60, 25);
		lbtotal.setForeground(Color.black);	
		lPanel.add(lbtotal);	
		
		txtotal = hh.cTextField(420);	
		txtotal.setBounds(180, 280, 120, 25);
		txtotal.setHorizontalAlignment(JTextField.RIGHT);
		txtotal.setEditable(false);
		lPanel.add(txtotal);		
		
		btnsend = gr.sbcs("Send amounts");
		btnsend.setBounds(310, 278, 145, 30);
		btnsend.setForeground(Color.black);	
		btnsend.setBackground(Color.ORANGE);
		lPanel.add(btnsend);
	   btnsend.addActionListener(e -> send_to_budgets());
		
		
		 ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/grad2.png"));
			Image i2 = i1.getImage().getScaledInstance(1300, 650, Image.SCALE_DEFAULT);
			ImageIcon i3 = new ImageIcon(i2);
			JLabel imagex = new JLabel(i3);
			imagex.setBounds(0, 0, 1000, 600);
			add(imagex);
		
		setVisible(true);
	}	

	private void counter() {
		String samount;
		double dtotal = 0.0;
	
		int rowCount = model.getRowCount();
		for (int row = 0; row < rowCount; row++) {
			if (model.getValueAt(row, 0) != null) {
				Boolean bbb = ((Boolean) model.getValueAt(row, 4));
				if (bbb == true) {
					samount = model.getValueAt(row, 3).toString();
					dtotal = dtotal + hh.stodd(samount);		
				}
			}
		}
		String stotal = hh.d(dtotal);
		txtotal.setText(stotal);
	}
	
	private void collectfv( String pcatid, String pyear) {
		String sql=" select t.date,  strftime('%Y', date(t.date)) as year,"
				+ "t.catid, c.sname, amount from transactions t "
				+ "left join subcategories c on  t.catid = c.scid "
				+ "where catid ='"+ pcatid +"' and year='"+pyear
				+"' and type='Expense'  order by t.date";
				
		try {
			ResultSet rs = dh.GetData(sql);
			while (rs.next()) {
				String sdate  = rs.getString("date");			
				String scatid = rs.getString ("catid");
			    String sname = rs.getString ("sname");
				String samount = rs.getString("amount");
				model.addRow(new Object[] { sdate, scatid, sname, samount, false});
			}
		} catch (Exception e) {			
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
	}
 
	private void send_to_budgets() {
	String  snumber =  txtotal.getText();
	((Budgets) pframe).passtobtable(snumber);
	pframe.setVisible(true);	
	dispose();
	}

	
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Transcollect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
JPanel exitPanel, lPanel, basePanel;
JLabel lbheader,lbtotal;
JTable ttable;
JScrollPane tPane;
DefaultTableModel model;
JButton btnsend;
JTextField txtotal;
}
