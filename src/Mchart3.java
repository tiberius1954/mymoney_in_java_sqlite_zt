import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import Classes.Calhelper;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Mchart3 extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();
	protected String[] types = dd.typesnames();
	JFrame myframe = this;
	Calhelper cc = new Calhelper();
	JPanel Mhpanel;
	 String[] Months = cc.monthnames();
	 String pyear ="" ;
	 JPanel exitPanel, searchPanel, mPanel;
		JLabel lbheader, lbyear;	
		JButton btnsearch;
		JComboBox cmbyear;

	Mchart3() {
		int currentyear = cc.mynow("year");
		pyear = hh.itos(currentyear);	
		init();		
		cmbyear.setSelectedItem(pyear);		
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
		setSize(new java.awt.Dimension(1175, 630));
		setLocationRelativeTo(null);
		setUndecorated(true);
		
		exitPanel = gr.makeexitpanel(this);
		exitPanel.setBounds(1060, 25, 100, 35);
		exitPanel.setOpaque(true);
		exitPanel.setBackground(new Color(51, 255, 51));
		add(exitPanel);

		lbheader = hh.fflabel("Monthly Incomes and Expenses");
		lbheader.setBounds(25, 25, 400, 35);
		add(lbheader);

		searchPanel = new JPanel(null);
		searchPanel.setBounds(435, 15, 270, 55);
		searchPanel.setBackground(hh.zgreen);
		searchPanel.setBorder(hh.ztroundborder(Color.yellow));
		searchPanel.setOpaque(false);

		lbyear = hh.clabel("Years");
		lbyear.setBounds(10, 15, 60, 30);
		searchPanel.add(lbyear);

		cmbyear = hh.cbcombo();
		for (int i = 2025; i <= 2050; i++) {
			cmbyear.addItem(hh.itos(i));
		}	
		cmbyear.setBounds(80, 15, 70, 30);	
		cmbyear.setFont(hh.textf);
		searchPanel.add(cmbyear);
		cmbyear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				if (cmbyear.getSelectedItem() != null) {
					pyear = cmbyear.getSelectedItem().toString();			
				}
			}
		});

		btnsearch = hh.cbutton("Filter");
		btnsearch.setForeground(hh.skek);
		btnsearch.setBackground(Color.ORANGE);
		btnsearch.setBounds(160, 15, 90, 30);
		btnsearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
		searchPanel.add(btnsearch);
		btnsearch.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {			
				 refreshChart();
			}
		});
		add(searchPanel);
		
		mPanel = new JPanel(null);
		mPanel.setBounds(20, 80, 1140, 520);
		mPanel.setBorder(hh.ztroundborder(Color.yellow));
		mPanel.setOpaque(false);
		Mhpanel = new Makepanelx();
		Mhpanel.setBounds(20, 10, 1100, 500);
		mPanel.add(Mhpanel);
		add(mPanel);

		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/grad2.png"));
		Image i2 = i1.getImage().getScaledInstance(1175, 630, Image.SCALE_DEFAULT);
		ImageIcon i3 = new ImageIcon(i2);
		JLabel imagex = new JLabel(i3);
		imagex.setBounds(0, 0, 1175, 630);
	
		add(imagex);
		setVisible(true);
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
			dPanel.add(createChartGUI(), BorderLayout.NORTH);
			return dPanel;
		}

		protected JPanel createChartGUI() {
			CategoryDataset dataset = createDataset();
			int size = dataset.getRowCount();
			if (size <=0){
				JPanel retPanel = new JPanel();
				return retPanel;
			}
			// Create chart
			JFreeChart chart = ChartFactory.createBarChart("Monthly Incomes and Expenses", // Chart title
					"Months", // X-Axis Label
					"Amounts", // Y-Axis Label
					dataset, PlotOrientation.VERTICAL, true, true, false);

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
		    cPanel.setPreferredSize(new Dimension(1100, 500));
			return cPanel;
		}
	}

		private CategoryDataset createDataset() {
			double [] incomearr = new double[12];
			Arrays.fill(incomearr, 0);
			double [] expensearr = new double[12];
			Arrays.fill(expensearr, 0);
			int size1 = 0;
			int size2 = 0;
			String query = " SELECT  strftime('%Y', date(date)) as year, "
					+ "strftime('%m', date(date)) as month , SUM(amount) AS income"
					+ " from transactions WHERE type='Income' and "
					+ "year = '" + pyear +"' group by month";

			try {
				ResultSet rs = dh.GetData(query);
				while (rs.next()) {
					size1++;
					int nmonth = rs.getInt("month");				
					String income = rs.getString ("income");
				    Double dincome = hh.stodd(income);
				      incomearr[nmonth-1] = dincome;
				}
			} catch (Exception e) {			
				e.printStackTrace();
			} finally {
				dh.CloseConnection();
			}
		
			query = " Select strftime('%Y', date(date)) as year, "
					+ " strftime('%m', date(date)) as month, SUM(amount) "
					+ " AS expense from transactions WHERE "
					+ "type = 'Expense' and  year ='" + pyear +"' group by month";
			try {
			ResultSet rs = dh.GetData(query);
			while (rs.next()) {
				size2++;
				int nmonth = rs.getInt("month");			
				String expense = rs.getString("expense");
				Double dexpense = hh.stodd(expense);
				expensearr[nmonth-1] = dexpense;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}			
			DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			if ( size1 >0 || size2>0) {		
	       for (int i=0; i < 12 ; i++) {
				dataset.addValue( incomearr[i], "Income",  Months[i] );
			}
			for (int i=0; i < 12 ; i++) {
				dataset.addValue( expensearr[i], "Expense",  Months[i] );
			}
		}			
			return dataset;
}
	
	private void refreshChart() {		
		mPanel.remove(Mhpanel);
		mPanel.repaint();
	    mPanel.revalidate();
		Mhpanel.removeAll();	
		Mhpanel = new Makepanelx();
		Mhpanel.setBounds(20, 10, 1100, 500);
		Mhpanel.repaint();
		Mhpanel.revalidate();
		mPanel.add(Mhpanel);
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mchart3 ts = new Mchart3();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	}

	
	
