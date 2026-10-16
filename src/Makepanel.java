import java.awt.BorderLayout;
import java.awt.*;
import java.awt.SystemColor;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.util.Rotation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Makepanel extends JPanel {
	Grlib gr = new Grlib();
	Hhelper hh = new Hhelper();
	private JFrame pframe;
	JTable dtable; 
	String sheader ="";

	public Makepanel(JFrame parent, JTable ptable, String header) {
		pframe = parent;
		dtable = ptable;
		sheader =header;
		this.add(createGUI());
	}

	protected JPanel createGUI() {
		JPanel mPanel = new JPanel(null);
		mPanel.setBorder(BorderFactory.createLineBorder(SystemColor.activeCaption));
		mPanel.setLayout(new BorderLayout());
		mPanel.setBackground(Color.WHITE);
		mPanel.setForeground(Color.BLACK);
		int rowCount = dtable.getRowCount();
		if (rowCount>0) {  
	      	mPanel.add(createChartGUI(), BorderLayout.NORTH);
		}
		return mPanel;
	}

	protected JPanel createChartGUI() {
		DefaultPieDataset dataset = createDataset();
	   JFreeChart pieChart = ChartFactory.createPieChart(sheader, dataset, false, true, false);
		// Get the plot of the pie chart
		PiePlot plot = (PiePlot) pieChart.getPlot();
		plot.setDirection(Rotation.CLOCKWISE);
		// Define custom colors
		Color[] colors =  gr.color_collect();
		int size = dataset.getKeys().size();		
		for (int i = 0; i < size; i++) {
			Comparable key = dataset.getKey(i);
			plot.setSectionPaint(key, colors[i % colors.length]);
		}
        plot.setLabelFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 12));
		NumberFormat valueFormat = NumberFormat.getNumberInstance();
		DecimalFormat percentFormat = new DecimalFormat("0.0%");		
	    plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}: {2}", valueFormat, percentFormat));
	//	 PieSectionLabelGenerator generator = new StandardPieSectionLabelGenerator("{0} = {1} ({2})", new DecimalFormat("0.0"), new DecimalFormat("0.0%"));
	   //  plot.setLabelGenerator(generator);
		ChartPanel cPanel = new ChartPanel(pieChart);
		cPanel.setPreferredSize(new Dimension(637, 494));
		cPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 5, 3)); 
		return cPanel;
	}

	private DefaultPieDataset createDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();		
		int rowCount = dtable.getRowCount();
		if (rowCount> 30) {
			rowCount = 30;
		}
		for (int row = 0; row < rowCount; row++) {
		    String svalue = dtable.getValueAt(row, 1).toString();
		    String samount = dtable.getValueAt(row, 2).toString();		 
			dataset.setValue(svalue, hh.stodd(samount));
		}
		return dataset;
	}

}

