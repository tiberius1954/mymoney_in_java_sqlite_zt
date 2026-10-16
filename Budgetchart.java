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
import java.util.Arrays;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import Classes.Calhelper;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Budgetchart extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	JPanel Mhpanel;
	JFrame myframe = this;
	Calhelper cc = new Calhelper();
	String headerpart = "year";
	String Chartheader = "";
	String curryear = "";
	int sumrow=0;
	String fyear="";
	String fmonth="";

	Budgetchart() {
		init();	
		for (int i = 2025; i <= 2050; i++) {
			cmbyear.addItem(hh.itos(i));
		}
		int currentyear = cc.mynow("year");
		curryear = hh.itos(currentyear);
		cmbyear.setSelectedItem(curryear);
		cmbmonth.addItem("");
		for (int i = 1; i <= 12; i++) {
			cmbmonth.addItem(hh.itos(i));
		}

		sqlgyart();
		makepanel();
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

		lbheader = hh.fflabel("Budget by categories");
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

		btnsearch = hh.cbutton("Filter");
		btnsearch.setForeground(hh.skek);
		btnsearch.setBackground(Color.ORANGE);
		btnsearch.setBounds(65, 50, 90, 25);
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
		btnclear.setBounds(162, 50, 90, 25);
		searchPanel.add(btnclear);
		btnclear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cmbyear.setSelectedItem(curryear);
				cmbmonth.setSelectedIndex(0);
				sqlgyart();
				refreshChart();
			}
		});

		btable = hh.ztable();
		DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) btable.getDefaultRenderer(Object.class);
		renderer.setHorizontalAlignment(SwingConstants.LEFT);
		btable.setTableHeader(new JTableHeader(btable.getColumnModel()) {
			@Override
			public Dimension getPreferredSize() {
				Dimension d = super.getPreferredSize();
				d.height = 25;
				return d;
			}
		});

		hh.madeheader(btable);
		btable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		btable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		bPane = new JScrollPane(btable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		btable.setModel(new javax.swing.table.DefaultTableModel(new Object[][] {},
				new String[] { "Category", "Amount", "Spent" }));

		bPane.setViewportView(btable);
		bPane.setBounds(20, 150, 435, 330);
		bPane.setBorder(hh.myRaisedBorder);
		lPanel.add(bPane);

		int[] wcols0 = {};
		hh.cellswidth_0(btable, wcols0);

		int[][] wcols = { { 0, 235 }, { 1, 99 }, { 2, 99 } };
		hh.cellswidth(btable, wcols);

		int[] cols = { 1, 2 };
		hh.cellsright(btable, cols);

		rPanel = new JPanel(null);
		rPanel.setBackground(new Color(178, 216, 216));
		rPanel.setBounds(496, 50, 680, 530);
		rPanel.setBorder(hh.ztroundborder(Color.yellow));
		rPanel.setOpaque(false);
		add(rPanel);

		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/grad2.png"));
		Image i2 = i1.getImage().getScaledInstance(1200, 600, Image.SCALE_DEFAULT);
		ImageIcon i3 = new ImageIcon(i2);
		JLabel imagex = new JLabel(i3);
		imagex.setBounds(0, 0, 1200, 600);
		add(imagex);
		setVisible(true);
	}

	private void makepanel() {
		mPanel = new JPanel(null);
		mPanel.setBounds(10, 10, 670, 520);
		mPanel.setOpaque(false);
		Mhpanel = new Makepanelx();
		Mhpanel.setBounds(20, 10, 630, 480);
		mPanel.add(Mhpanel);
		rPanel.add(mPanel);
	}

	private class Makepanelx extends JPanel {

		public Makepanelx() {
			this.add(createGUI());
		}

		protected JPanel createGUI() {
			JPanel dPanel = new JPanel(true);
			dPanel.setBorder(BorderFactory.createLineBorder(SystemColor.activeCaption));
			dPanel.setLayout(new BorderLayout());
			dPanel.setBackground(Color.WHITE);
			dPanel.setForeground(Color.BLACK);
			int rowCount = btable.getRowCount();
			if (rowCount>0) {  
				dPanel.add(createChartGUI(), BorderLayout.CENTER);
			}		
			return dPanel;
		}

		protected JScrollPane createChartGUI() {
			sumrow =0;
			CategoryDataset dataset = createDataset();
			// Create chart
			JFreeChart chart = ChartFactory.createBarChart(Chartheader, // Chart title
					"Categories", // X-Axis Label
					"Values", // Y-Axis Label
					dataset, PlotOrientation.HORIZONTAL, true, true, false);

			// Customize plot appearance
			CategoryPlot plot = chart.getCategoryPlot();
			plot.setBackgroundPaint(Color.WHITE);
			plot.setRangeGridlinePaint(Color.GRAY);

			LegendTitle legend = chart.getLegend();
			if (legend != null) {
				legend.setItemFont(new Font("Arial", Font.BOLD, 14)); // Change font
			}

			NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
			BarRenderer r = (BarRenderer) chart.getCategoryPlot().getRenderer();
			r.setSeriesPaint(0, new Color(0, 255, 0));
			r.setSeriesPaint(1, Color.red);
			r.setShadowVisible(true);
			r.setBarPainter(new StandardBarPainter());
			r.setItemMargin(0.1); // 0.0 = bars touch, higher = more space
			CategoryAxis categoryAxis = new CategoryAxis("Categories");
			categoryAxis.setLowerMargin(.01);
			categoryAxis.setCategoryMargin(0.1);
			categoryAxis.setUpperMargin(.01);
			categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
			CategoryAxis domainAxis = plot.getDomainAxis();
			domainAxis.setCategoryMargin(0.2); // No space between categories
			domainAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 14)); // X-axis label
			domainAxis.setTickLabelFont(new Font("SansSerif", Font.BOLD, 12)); // X-axis tick labels
			domainAxis.setLowerMargin(0.01);
			domainAxis.setUpperMargin(0.01);
			rangeAxis.setLabelFont(new Font("SansSerif", Font.BOLD, 16)); // Y-axis label
			rangeAxis.setTickLabelFont(new Font("SansSerif", Font.BOLD, 12)); // Y-axis tick labels
            ChartPanel cPanel = new ChartPanel(chart);
			cPanel = new ChartPanel(chart);
			int nheight = 460;
		//	cPanel.setPreferredSize(new Dimension(550, 1500));
		//	cPanel.setPreferredSize(new Dimension(450, 460));
			if (sumrow >5) {
				nheight = 460 +( sumrow - 5) * 60;
			}
			cPanel.setPreferredSize(new Dimension(450, nheight));	
			JScrollPane scrollPane = new JScrollPane(cPanel);		
			scrollPane.setPreferredSize(new Dimension(600, 460));
			scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
          return scrollPane;
		}

		private CategoryDataset createDataset() {
			double[] amountarr = new double[30];
			double[] spentarr = new double[30];
			String[] catearr = new String[30];
			int rowCount = btable.getRowCount();
			for (int row = 0; row < rowCount; row++) {
				String category = btable.getValueAt(row, 0).toString();
				catearr[row] = category;
				String amount = btable.getValueAt(row, 1).toString();
				double damount = hh.stodd(amount);
				amountarr[row] = damount;
				String spent = btable.getValueAt(row, 2).toString();
				double dspent = hh.stodd(spent);
				spentarr[row] = dspent;
			}
			int isize = btable.getRowCount();
			sumrow = isize;
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			for (int i = 0; i < isize; i++) {
				dataset.addValue(amountarr[i], "Amount", catearr[i]);
			}
			for (int i = 0; i < isize; i++) {
				dataset.addValue(spentarr[i], "Spent", catearr[i]);
			}
           return dataset;
		}
	}

	private void btable_update(String swhere) {
		DefaultTableModel m1 = (DefaultTableModel) btable.getModel();
		m1.setRowCount(0);
		double damount = 0;
		double dspent = 0;
		String Sql = "SELECT c.cname as category,  SUM(b.amount) AS amount, "
				+ " SUM(b.spent) AS spent, b.period from budgets b "
				+ " left join subcategories s on  b.catid = s.scid "
				+ " left join categories c on  s.cid = c.cid "
				+ swhere + " group by category order by category";
		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String category = rs.getString("category");
				String amount = rs.getString("amount");
				String spent = rs.getString("spent");
				String period = rs.getString("period");
				if (period == "Yearly" && !hh.zempty(fmonth)) {
					damount = hh.stodd(amount);
					damount = damount / 12;
					amount = hh.d(damount);
					dspent = hh.stodd(spent);
					dspent = dspent / 12;
					spent = hh.d(dspent);
				}
				m1.addRow(new Object[] { category, hh.bsf(amount), hh.bsf(spent) });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
		String[] fej = { "Category", "Amount", "Spent" };
		((DefaultTableModel) btable.getModel()).setColumnIdentifiers(fej);
		int[] wcols0 = {};
		hh.cellswidth_0(btable, wcols0);
		int[][] wcols = { { 0, 235 }, { 1, 99 }, { 2, 99 } };
		hh.cellswidth(btable, wcols);
		int[] cols = { 1, 2 };
		hh.cellsright(btable, cols);
	}

	private void refreshChart() {
		rPanel.remove(Mhpanel);
		rPanel.repaint();
		rPanel.revalidate();
		Mhpanel.removeAll();
		mPanel.removeAll();
		Mhpanel = new Makepanelx();
		Mhpanel.setBounds(20, 10, 630, 480);
		Mhpanel.repaint();
		Mhpanel.revalidate();
		mPanel.add(Mhpanel);
		rPanel.add(mPanel);	
	}

	private void sqlgyart() {
		Boolean done = false;
		String swhere = "";
		fyear = cmbyear.getSelectedItem().toString();
		fmonth = cmbmonth.getSelectedItem().toString();
		Chartheader = "Amounts and Spents yearly in budget ";
		if (!hh.zempty(fmonth)) {
			int nmonth = hh.stoi(fmonth);
			fmonth = hh.padLeftZeros(fmonth, 2);
			Chartheader = "Monthly amounts and Spents in budget " + cc.monthNames[nmonth - 1];
		}

		if (!hh.zempty(fyear)) {
			if (done == false) {
				swhere = " where strftime('%Y', date(b.start_date))  = '" + fyear + "' ";
			} else {
				swhere = swhere + " and strftime('%Y', date(b.start_date))  = '" + fyear + "' ";
			}
			done = true;
		}

		if (!hh.zempty(fmonth)) {
			if (done == false) {
				swhere = " where strftime('%m', date(b.start_date))  = '" + fmonth + "' ";
			} else {
				swhere = swhere + " and strftime('%m', date(b.start_date))  = '" + fmonth + "' ";
			}
			done = true;
		}
		if (done == true) {
			btable_update(swhere);
		}
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Budgetchart ts = new Budgetchart();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JPanel exitPanel, searchPanel, lPanel, rPanel, mPanel;
	JLabel lbheader, lbyear, lbmonth;
	JComboBox cmbyear, cmbmonth;
	JButton btnclear, btnsearch;
	JTable btable;
	JScrollPane bPane, jScrollPane;
}
