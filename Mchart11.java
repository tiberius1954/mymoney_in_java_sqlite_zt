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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import Classes.Calhelper;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Mchart11 extends JFrame{	

	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	protected String[] types = dd.typesnames();
	JPanel Mhpanel;
	JFrame myframe = this;
	Calhelper cc = new Calhelper();
   String Typename ="Expense";
   String headerpart = "year";
   String syear="";
	
   Mchart11(){
		init();
//		cmbyear.addItem("");
		for (int i = 2025; i <= 2050; i++) {
			cmbyear.addItem(hh.itos(i));
		}
	   int currentyear = cc.mynow("year");
        syear = hh.itos(currentyear);
        cmbyear.setSelectedItem(syear);
		cmbmonth.addItem("");
		for (int i = 1; i <= 12; i++) {
			cmbmonth.addItem(hh.itos(i));
		}
		cmbtypes.setModel(new DefaultComboBoxModel(types));
		cmbtypes.setSelectedItem("Expense");
		 sqlgyart();		
		refreshChart();
   }
   private void init() {
	   
	   
	   UIManager.put("ComboBox.selectionBackground", hh.lpiros);
		UIManager.put("ComboBox.selectionForeground", hh.feher);
		UIManager.put("ComboBox.background", new ColorUIResource(hh.homok));
		UIManager.put("ComboBox.foreground", Color.BLACK);
		UIManager.put("ComboBox.border", new LineBorder(Color.green, 1));
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				dispose();
			}
		});
		setLayout(null);
		setMinimumSize(new Dimension(100, 100));
		setSize(new java.awt.Dimension(1200, 600));
		setLocationRelativeTo(null);
		setUndecorated(true);

		exitPanel = gr.makeexitpanel(this);
		exitPanel.setBounds(1090, 10, 100, 35);
		exitPanel.setOpaque(true);
		exitPanel.setBackground(new Color(51, 255, 51));
		add(exitPanel);

		lbheader = hh.fflabel("Expense by categories");
		lbheader.setBounds(25, 10, 330, 35);
		add(lbheader);
		lPanel = new JPanel(null);
		// lPanel.setBackground(new Color(178, 216, 216));
		lPanel.setBounds(20, 50, 470, 530);
		lPanel.setBorder(hh.ztroundborder(Color.yellow));
		lPanel.setOpaque(false);
		add(lPanel);	
	
		searchPanel = new JPanel(null);
		searchPanel.setBounds(83, 25, 310, 120);
		searchPanel.setBackground(hh.zgreen);
		searchPanel.setOpaque(false);
		lPanel.add(searchPanel);

		lbyear = hh.clabel("Years");
		lbyear.setBounds(10, 10, 60, 25);
		searchPanel.add(lbyear);

		cmbyear = hh.cbcombo();
		cmbyear.setBounds(80, 10, 70, 25);
		cmbyear.setFont(hh.textf);
		searchPanel.add(cmbyear);
		cmbyear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				if (cmbyear.getSelectedItem() != null) {
					String b = cmbyear.getSelectedItem().toString();
					// currentYear= Integer.parseInt(b);
				}
			}
		});
		lbmonth = hh.clabel("Month");
		lbmonth.setBounds(160, 10, 60, 25);
		searchPanel.add(lbmonth);

		cmbmonth = hh.cbcombo();
		cmbmonth.setBounds(230, 10, 50, 25);
		cmbmonth.setFont(hh.textf);
		((JLabel) cmbmonth.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
		searchPanel.add(cmbmonth);
		cmbmonth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				if (cmbmonth.getSelectedItem() != null) {
					String b = cmbmonth.getSelectedItem().toString();
					// currentYear= Integer.parseInt(b);
				}
			}
		});

		lbtype = hh.clabel("Type");
		lbtype.setBounds(0, 50, 100, 25);
		searchPanel.add(lbtype);

		cmbtypes = hh.cbcombo();
		cmbtypes.setBounds(110, 50, 150, 25);
		cmbtypes.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String stype = (String) cmbtypes.getSelectedItem();
				Typename = stype;
				lbheader.setText(Typename +" by categories");
			}
		});	

		searchPanel.add(cmbtypes);
		btnsearch = hh.cbutton("Filter");
		btnsearch.setForeground(hh.skek);
		btnsearch.setBackground(Color.ORANGE);
		btnsearch.setBounds(65, 90, 90, 25);
		btnsearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		searchPanel.add(btnsearch);
		btnsearch.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 sqlgyart();
				refreshChart();
			}
		});

		btnclear = hh.cbutton("Clear");
		btnclear.setForeground(hh.skek);
		btnclear.setBackground(Color.ORANGE);
		btnclear.setBounds(162, 90, 90, 25);
		searchPanel.add(btnclear);
		btnclear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			//	cmbyear.setSelectedIndex(0);
				cmbmonth.setSelectedIndex(0);
				 sqlgyart();		
				 refreshChart();			
			}
		});
		
		
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
		ttable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] { },
				new String[] { "Type", "Subcategory", "Amount" }));

		tPane.setViewportView(ttable);
		tPane.setBounds(35, 150, 405, 330);
		tPane.setBorder(hh.myRaisedBorder);
		lPanel.add(tPane);

		int[] wcols0 = { 0 };
		hh.cellswidth_0(ttable, wcols0);

		int[][] wcols = { { 1, 300 }, { 2, 101 } };
		hh.cellswidth(ttable, wcols);

		int[] cols = { 2 };
		hh.cellsright(ttable, cols);	
		
		totalPanel = new JPanel(null);
		totalPanel.setBackground(new Color(255, 93, 42));	
		totalPanel.setBounds(290, 482, 150, 30);	
		totalPanel.setOpaque(true);
		lPanel.add(totalPanel);
		
		lbtotal = hh.clabel("Total:");
		lbtotal.setForeground(Color.BLACK);
		lbtotal.setBackground(new Color(255, 93, 42));
		lbtotal.setOpaque(true);
		lbtotal.setFont(new Font("Tahoma", 1, 14));
		lbtotal.setBounds(5, 3, 50, 25);
		totalPanel.add(lbtotal);
		
		lbtotalw = hh.clabel("");
		lbtotalw.setForeground(Color.black);
		lbtotalw.setBackground(new Color(255, 93, 42));
		lbtotalw.setOpaque(true);
		lbtotalw.setFont(new Font("Tahoma", 1, 14));
		lbtotalw.setBounds(55, 3, 90, 25);
		totalPanel.add(lbtotalw);
		
		rPanel = new JPanel(null);
//		 rPanel.setBackground(new Color(178, 216, 216));
			rPanel.setBounds(496, 50, 680, 530);
			rPanel.setBorder(hh.ztroundborder(Color.yellow));
			rPanel.setOpaque(false);
			String pheader = lbheader.getText() +headerpart;
			Mhpanel = new Makepanel(myframe, ttable, pheader);
			Mhpanel.setBounds(20, 10, 645, 500);
			rPanel.add(Mhpanel);
			add(rPanel);				

		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/grad2.png"));
		Image i2 = i1.getImage().getScaledInstance(1200, 600, Image.SCALE_DEFAULT);
		ImageIcon i3 = new ImageIcon(i2);
		JLabel imagex = new JLabel(i3);
		imagex.setBounds(0, 0, 1200, 600);
		add(imagex);
		setVisible(true);
   }
	
   private void ttable_update(String swhere) {
		DefaultTableModel m1 = (DefaultTableModel) ttable.getModel();
		m1.setRowCount(0);
		double damount =0;
		String Sql = "SELECT t.type, c.cname as category,  SUM(t.amount) AS amount"
				+ " from transactions t "
				+ " left join subcategories s on  t.catid = s.scid "
				+ " left join categories c on  s.cid = c.cid "
				+ swhere + " group by category order by category";
		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String type = rs.getString("type");
				String category = rs.getString("category");
				String amount = rs.getString("amount");
				m1.addRow(new Object[] { type, category, hh.bsf(amount) });
				damount = damount + hh.stodd(amount);
			}
			lbtotalw.setText(hh.d(damount));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
		String[] fej = { "Type", "Category", "Amount" };
		((DefaultTableModel) ttable.getModel()).setColumnIdentifiers(fej);
		int[] wcols0 = { 0 };
		hh.cellswidth_0(ttable, wcols0);

		int[][] wcols = { { 1, 300 }, { 2, 101 } };
		hh.cellswidth(ttable, wcols);

		int[] cols = { 2 };
		hh.cellsright(ttable, cols);
	}
   
	private void refreshChart() {		
		rPanel.remove(Mhpanel);
		rPanel.repaint();
		rPanel.revalidate();
		Mhpanel.removeAll();
		String pheader = lbheader.getText() + headerpart;		
		Mhpanel = new Makepanel(myframe,ttable, pheader);
		Mhpanel.setBounds(20, 10, 645, 500);
		Mhpanel.repaint();
		Mhpanel.revalidate();
		rPanel.add(Mhpanel);
	}
	
   private void sqlgyart() {
		Boolean done = false;
		headerpart=" year";
		String swhere = "";		
		String syear = cmbyear.getSelectedItem().toString();
		String smonth =  cmbmonth.getSelectedItem().toString();
		if(!hh.zempty(smonth)) {
		  int nmonth =hh.stoi(smonth);	
		   headerpart = " " + cc.monthNames[nmonth-1];
		  smonth = hh.padLeftZeros(smonth,2);
		}
		String stype = cmbtypes.getSelectedItem().toString();
		if (!hh.zempty(syear)) {
			   if (done == false) {
				       swhere = " where strftime('%Y', date(t.date))  = '" +syear + "' ";
			   } else {
				      swhere = swhere +" and strftime('%Y', date(t.date))  = '" +syear+ "' " ;
			   }
			   done = true;
			   }
			
			if (!hh.zempty(smonth)) {
				   if (done == false) {
					       swhere =  " where strftime('%m', date(t.date))  = '" + smonth+ "' ";
				   } else {
					      swhere = swhere +" and strftime('%m', date(t.date))  = '" +smonth +"' ";
				   }
				   done = true;
				   }
			if (!hh.zempty(stype)) {
				   if (done == false) {
					       swhere =  " where t.type = '" +stype + "'";
				   } else {
					      swhere = swhere +" and t.type = '" + stype +"'";
				   }
				   done = true;
				   }
			if (done==true) {
				 ttable_update(swhere);
			}
	}
	
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mchart11 ts = new Mchart11();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	JPanel exitPanel, lPanel, rPanel, searchPanel, totalPanel;
	JLabel lbheader, lbmonth, lbyear, lbtype, lbtotal, lbtotalw;
	JTable ttable;
	JScrollPane tPane;
	JComboBox cmbmonth, cmbyear, cmbtypes;
	JButton btnclear, btnsearch;
}
