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
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CenterTextMode;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.util.UnitType;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import Classes.Calhelper;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Donutchart extends JFrame {
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
	int sumrow = 0;	
	String curryear = "";
	String Chartheader = "";

	Donutchart() {
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
		setSize(new java.awt.Dimension(1200, 620));
		setLocationRelativeTo(null);
		setUndecorated(true);
		exitPanel = gr.makeexitpanel(this);
		exitPanel.setBounds(1080, 20, 100, 35);
		exitPanel.setOpaque(true);
		exitPanel.setBackground(new Color(51, 255, 51));
		add(exitPanel);

		lbheader = hh.fflabel("Monthly Incomes");
		lbheader.setBounds(25, 25, 400, 35);
		add(lbheader);

		lPanel = new JPanel(null);
		// lPanel.setBackground(new Color(178, 216, 216));
		lPanel.setBounds(20, 60, 470, 530);
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
				// cmbyear.setSelectedIndex(0);
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
		ttable.setModel(
				new javax.swing.table.DefaultTableModel(new Object[][] {}, new String[] { "Category", "Amount" }));

		tPane.setViewportView(ttable);
		tPane.setBounds(35, 150, 405, 330);
		tPane.setBorder(hh.myRaisedBorder);
		lPanel.add(tPane);

		int[] wcols0 = { 0 };
		hh.cellswidth_0(ttable, wcols0);

		int[][] wcols = { { 0, 250 }, { 1, 151 } };
		hh.cellswidth(ttable, wcols);

		int[] cols = { 1 };
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
		rPanel.setBackground(new Color(178, 216, 216));
		rPanel.setBounds(496, 60, 680, 530);
		rPanel.setBorder(hh.ztroundborder(Color.yellow));
		rPanel.setOpaque(false);
		add(rPanel);

		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/grad2.png"));
		Image i2 = i1.getImage().getScaledInstance(1300, 650, Image.SCALE_DEFAULT);
		ImageIcon i3 = new ImageIcon(i2);
		JLabel imagex = new JLabel(i3);
		imagex.setBounds(0, 0, 1200, 620);
		add(imagex);

		setVisible(true);
	}

	private void makepanel() {
		Mhpanel = new Makepanelx();
		Mhpanel.setBounds(20, 10, 645, 510);
		rPanel.add(Mhpanel);
	}

	private void ttable_update(String swhere) {
		DefaultTableModel m1 = (DefaultTableModel) ttable.getModel();
		m1.setRowCount(0);
		double damount = 0;
		String Sql = "SELECT s.sname as subcategory,  SUM(t.amount) AS amount" + " from transactions t  "
				+ "left join subcategories s on  t.catid = s.scid " + swhere
				+ " group by subcategory order by subcategory";
		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String subcategory = rs.getString("subcategory");
				String amount = rs.getString("amount");
				m1.addRow(new Object[] { subcategory, hh.bsf(amount) });
				damount = damount + hh.stodd(amount);
			}
			lbtotalw.setText(hh.d(damount));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
		String[] fej = { "Subcategory", "Amount" };
		((DefaultTableModel) ttable.getModel()).setColumnIdentifiers(fej);

		int[][] wcols = { { 0, 250 }, { 1, 151 } };
		hh.cellswidth(ttable, wcols);

		int[] cols = { 1 };
		hh.cellsright(ttable, cols);

		ttable.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				ttable.scrollRectToVisible(ttable.getCellRect(ttable.getRowCount() - 1, 0, true));
			}
		});
		if (ttable.getRowCount() > 0) {
			int row = ttable.getRowCount() - 1;
			ttable.setRowSelectionInterval(row, row);
		}
	}

	private void sqlgyart() {
		Boolean done = false;
		String swhere = "";
		String fyear = cmbyear.getSelectedItem().toString();
		String fmonth = cmbmonth.getSelectedItem().toString();
		Chartheader = "Income by categories year: " + fyear;

		if (!hh.zempty(fmonth)) {
			int nmonth = hh.stoi(fmonth);
			fmonth = hh.padLeftZeros(fmonth, 2);
			Chartheader = Chartheader + " month: " + cc.monthNames[nmonth - 1];
		}

		if (!hh.zempty(fyear)) {
			if (done == false) {
				swhere = " where strftime('%Y', date(t.date))  = '" + fyear + "' ";
			} else {
				swhere = swhere + " and strftime('%Y', date(t.date))  = '" + fyear + "' ";
			}
			done = true;
		}

		if (!hh.zempty(fmonth)) {
			if (done == false) {
				swhere = " where strftime('%m', date(t.date))  = '" + fmonth + "' ";
			} else {
				swhere = swhere + " and strftime('%m', date(t.date))  = '" + fmonth + "' ";
			}
			done = true;
		}

		if (done == false) {
			swhere = " where t.type = 'Income'";
		} else {
			swhere = swhere + " and t.type = 'Income'";
		}
		done = true;

		if (done == true) {
			ttable_update(swhere);
		}
	}

	private void refreshChart() {
		rPanel.remove(Mhpanel);
		rPanel.repaint();
		rPanel.revalidate();
		Mhpanel.removeAll();
		Mhpanel = new Makepanelx();
		Mhpanel.setBounds(20, 10, 645, 510);
		Mhpanel.repaint();
		Mhpanel.revalidate();
		rPanel.add(Mhpanel);
	}

	private class Makepanelx extends JPanel {

		Makepanelx() {
			this.add(doit());
		}

		private JPanel doit() {
			sumrow = 0;
			DefaultPieDataset dataset = createDataset();
			if (sumrow == 0) {
				JPanel dpanel = new JPanel();
				return dpanel;
			}
			// 2. Create the ring chart
			JFreeChart chart = ChartFactory.createRingChart(Chartheader, // Chart title
					dataset, // Data
					true, // Include legend
					true, // Tooltips
					false // URLs
			);

			// 3. Customize the ring (the hole in the center)
			RingPlot plot = (RingPlot) chart.getPlot();
			plot.setSectionDepth(0.40); // 40% of the ring thickness
			plot.setCircular(true); // Keep the chart perfectly round
			NumberFormat valueFormat = NumberFormat.getNumberInstance();
			DecimalFormat percentFormat = new DecimalFormat("0.0%");
			plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}", valueFormat, percentFormat));
			Color[] colors = gr.color_collect();
			int size = dataset.getKeys().size();
			for (int i = 0; i < size; i++) {
				Comparable key = dataset.getKey(i);
				plot.setSectionPaint(key, colors[i % colors.length]);
			}
//		        plot.setSectionDepth(1f);
			    plot.setSeparatorsVisible(false);
//				plot.setSeparatorPaint(Color.BLACK);
//				plot.setSeparatorStroke(new BasicStroke(1.0f));
//				plot.setSectionOutlinesVisible(true);
		//	    plot.setSectionDepth(0.30);
			plot.setInsets(new RectangleInsets(0.0, 5.0, 5.0, 5.0));
			plot.setSimpleLabelOffset(new RectangleInsets(
			         UnitType.RELATIVE, 0.09, 0.09, 0.09, 0.09));
		     
			// Optional: Put text in the center
			plot.setCenterTextMode(CenterTextMode.FIXED);
			String stotal = lbtotalw.getText();
			plot.setCenterText("Total: " + stotal);
			plot.setCenterTextFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 16));
			ChartPanel cPanel = new ChartPanel(chart);
			cPanel.setPreferredSize(new Dimension(645, 500));
			return cPanel;
		}

		private DefaultPieDataset createDataset() {
			double[] amountarr = new double[20];
			String[] catearr = new String[30];
			int rowCount = ttable.getRowCount();
			for (int row = 0; row < rowCount; row++) {
				String category = ttable.getValueAt(row, 0).toString();
				catearr[row] = category;
				String amount = ttable.getValueAt(row, 1).toString();
				double damount = hh.stodd(amount);
				amountarr[row] = damount;
			}
			int isize = ttable.getRowCount();
			sumrow = isize;
			DefaultPieDataset dataset = new DefaultPieDataset();
			for (int i = 0; i < isize; i++) {
				dataset.setValue(catearr[i], amountarr[i]);
			}

			return dataset;
		}
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Donutchart();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JPanel exitPanel, lPanel, searchPanel, totalPanel, rPanel, mPanel;
	JLabel lbheader, lbyear, lbmonth, lbtotal, lbtotalw;
	JComboBox cmbmonth, cmbyear;
	JTable ttable;
	JScrollPane tPane;
	JButton btnclear, btnsearch;
}
