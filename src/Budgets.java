import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.MaskFormatter;

import Classes.Grlib;
import Classes.Hhelper;
import Classes.Hhelper.MyTablePrintable;
import Classes.Hhelper.StringUtils;
import Classes.Allvalid;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;
import Databaseop.Databaseop.Category;
import Databaseop.Databaseop.Subcategory;
import Databaseop.Databaseop.Payment;
import java.util.Map;

public class Budgets extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();	
	Allvalid allv = new Allvalid();
	StringUtils hss = new StringUtils();
	String rowid = "";
	int myrow = 0;
	protected String[] period = {"Monthly","Yearly"};	
	JFrame myframe = this;
	String typmark = "E";
	String firstdate ="";
	String lastdate ="";
	
Budgets(){
	date_offer(1);
	init();	
	dd.subcategoriescombofill(cmbsubcategories, typmark);
	cmbsubcategories.addItemListener(new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			String eitem = cmbperiod.getSelectedItem().toString();
			if (eitem=="Monthly" && hh.zempty(rowid) ) {
				date_offer(1);
				txstartdate.setText(firstdate);
		    	txenddate.setText(lastdate);
		    }else if (eitem=="Yearly" && hh.zempty(rowid) ) {
		    	date_offer(2);
		    	txstartdate.setText(firstdate);
		    	txenddate.setText(lastdate);
		    }	
		}
		}		
		});
	btable_update("");
 //    totalize();
	cmbsubcategories.requestFocus(true);
	cmbperiod.setModel(new DefaultComboBoxModel(period));
	cmbperiod.addItemListener(new ItemListener() {
		@Override
		public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			String eitem =(String) e.getItem();
			if (eitem=="Monthly" && hh.zempty(rowid) ) {
				date_offer(1);
				txstartdate.setText(firstdate);
		    	txenddate.setText(lastdate);
		    }else if (eitem=="Yearly" && hh.zempty(rowid) ) {
		    	date_offer(2);
		    	txstartdate.setText(firstdate);
		    	txenddate.setText(lastdate);
		    }	
		}
		}		
		});
	cmbyear.addItem("");
	for (int i = 2025; i <= 2050; i++) {
		cmbyear.addItem(hh.itos(i));
	}
	dd.subcategoriescombofill(cmbsubcategoriess, typmark);
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
	setSize(new java.awt.Dimension(1268, 650));
	setLocationRelativeTo(null);
	setUndecorated(true);

	exitPanel = gr.makeexitpanel(this);
	exitPanel.setBounds(1160, 10, 100, 35);
	exitPanel.setOpaque(true);
	exitPanel.setBackground(new Color(51, 255, 51));
	add(exitPanel);

	lbheader = hh.fflabel("Budgets");
	lbheader.setBounds(25, 10, 250, 35);
	add(lbheader);
	
	lPanel = new JPanel(null);
	lPanel.setBounds(10, 50, 330, 580);
	lPanel.setBorder(hh.ztroundborder(Color.yellow));
	lPanel.setOpaque(false);
	add(lPanel);

	rPanel = new JPanel(null);
// rPanel.setBackground(new Color(178, 216, 216));
	rPanel.setBounds(344, 50, 911, 580);
	rPanel.setBorder(hh.ztroundborder(Color.yellow));
	rPanel.setOpaque(false);
	add(rPanel);

	searchPanel = new JPanel(null);
	searchPanel.setBounds(27, 10, 864, 80);
	searchPanel.setBackground(hh.zgreen);
	searchPanel.setOpaque(true);
	rPanel.add(searchPanel);
	
	lbcategorys = hh.clabel("Subcategory");
	lbcategorys.setBounds(10, 30, 120, 30);
	searchPanel.add(lbcategorys);

	cmbsubcategoriess = hh.cbcombo();
	cmbsubcategoriess.setBounds(140, 30, 180, 30);
	searchPanel.add(cmbsubcategoriess);
	
	lbyear = hh.clabel("Years");
	lbyear.setBounds(320, 30, 60, 30);
	searchPanel.add(lbyear);
	
	cmbyear = hh.cbcombo();
	cmbyear.setBounds(390, 30, 70, 30);
	cmbyear.setFont(hh.textf);
	searchPanel.add(cmbyear);
	
	btnsearch = hh.cbutton("Filter");
	btnsearch.setForeground(hh.skek);
	btnsearch.setBackground(Color.ORANGE);
	btnsearch.setBounds(470, 30, 90, 30);
	btnsearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
	searchPanel.add(btnsearch);
	btnsearch.addActionListener((ActionListener) new ActionListener() {
		public void actionPerformed(ActionEvent e) {
    		sqlgyart();
		}
	});

	btnclear = hh.cbutton("Clear");
	btnclear.setForeground(hh.skek);
	btnclear.setBackground(Color.ORANGE);
	btnclear.setBounds(570, 30, 90, 30);
	searchPanel.add(btnclear);
	btnclear.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {		
			cmbyear.setSelectedIndex(0);
			cmbsubcategoriess.setSelectedIndex(0);		
			btable_update("");
		}
	});	
	
	btnprint = hh.cbutton("Print");
	btnprint.setForeground(hh.skek);
	btnprint.setBackground(Color.ORANGE);
	btnprint.setBounds(670, 30, 90, 30);
	searchPanel.add(btnprint);
	btnprint.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		   budgets_print();
		}
	});

	lbcategory = hh.clabel("Subcategory");
	lbcategory.setBounds(10, 50, 110, 25);
	lPanel.add(lbcategory);	

	cmbsubcategories = hh.cbcombo();
	cmbsubcategories.setBounds(130, 50, 180, 30);
	cmbsubcategories.setName("subcategory");
	lPanel.add(cmbsubcategories);	
	
	lbamount = hh.clabel("Amount");
	lbamount.setBounds(10, 110, 100, 25);
	lPanel.add(lbamount);	

	txamount = hh.cTextField(100);	
	txamount.setBounds(130, 100, 105, 25);
	txamount.setHorizontalAlignment(JTextField.RIGHT);
	lPanel.add(txamount);	
	txamount.addKeyListener(hh.Onlynum());
	
	lbperiod = hh.clabel("period");
	lbperiod.setBounds(10, 150, 110, 25);
	lPanel.add(lbperiod);	
	
	cmbperiod = hh.cbcombo();
	cmbperiod.setBounds(130, 150, 150, 30);
	lPanel.add(cmbperiod);	

	lbstartdate = hh.clabel("Start date");
	lbstartdate.setBounds(10, 200, 110, 25);
	lPanel.add(lbstartdate);	
	
	txstartdate = DatetextField();
	txstartdate.setBounds(130, 200, 105, 30);
	lPanel.add(txstartdate); 	
    
    lbstartdate = hh.clabel("End date");
	lbstartdate.setBounds(10, 250, 110, 25);
	lPanel.add(lbstartdate);	
	
	txenddate = hh.DatetextField();
	txenddate.setBounds(130, 250, 105, 30);
	lPanel.add(txenddate);
   
	lbspent = hh.clabel("Spent");
	lbspent.setBounds(10, 300, 110, 25);
	lPanel.add(lbspent);	

	txspent = hh.cTextField(100);	
	txspent.setBounds(130, 300, 105, 25);
	txspent.setHorizontalAlignment(JTextField.RIGHT);
	lPanel.add(txspent);	
	txamount.addKeyListener(hh.Onlynum());		
    
    btnsave = gr.sbcs("Save");
	btnsave.setBounds(130, 390, 100, 30);
	btnsave.setForeground(Color.black);
	btnsave.setBackground(hh.lpiros);
	lPanel.add(btnsave);
	btnsave.addActionListener(e -> savebuttrun());

	btncancel = gr.sbcs("Cancel");
	btncancel.setBounds(130, 425, 100, 30);
	btncancel.setBackground(hh.vvzold);
	lPanel.add(btncancel);
	btncancel.addActionListener(e -> clearFields());

	btndelete = gr.sbcs("Delete");
	btndelete.setBounds(130, 460, 100, 30);
	btndelete.setForeground(Color.black);
	btndelete.setBackground(Color.yellow);
	lPanel.add(btndelete);
   btndelete.addActionListener(e -> data_delete());
	
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
	btable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
		public void valueChanged(ListSelectionEvent event) {
		}
	});
	btable.addMouseListener(new java.awt.event.MouseAdapter() {
		public void mouseClicked(java.awt.event.MouseEvent evt) {
			if (evt.getClickCount() == 2) {
		 		rowreader();
			}
		}
	});	
	
	hh.madeheader(btable);
	btable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	btable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	bPane = new JScrollPane(btable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	String[] pheader = new String[] { "bid","Catid","Subcategory","Period", "Start_date", "End_date", "Amount",
			"Spent","Difference"};

	btable.setModel(new javax.swing.table.DefaultTableModel(
			new Object[][] { { null, null, null, null, null, null, null, null, null } }, pheader));

	bPane.setViewportView(btable);
	bPane.setBounds(25, 90, 865, 300);
	bPane.setBorder(hh.myRaisedBorder);
	rPanel.add(bPane);

	int[] wcols0 = { 0, 1};
	hh.cellswidth_0(btable, wcols0);

	int[][] wcols = { { 2, 220 }, { 3, 100 }, { 4, 100 }, { 5, 100}, { 6, 110 }, { 7, 110 },{ 8, 110 }};
	hh.cellswidth(btable, wcols);

	int[] cols = { 6, 7, 8 };
	hh.cellsright(btable, cols); 
	
	
	lbsum_amount = new JLabel("Amount", SwingConstants.CENTER);
	lbsum_amount.setBackground(new Color(255, 93, 42));
	lbsum_amount.setFont(hh.textfn);
	lbsum_amount.setOpaque(true);
	lbsum_amount.setBounds(330, 430, 180, 100);
	rPanel.add(lbsum_amount);

	lbsum_spent = new JLabel("Expense", SwingConstants.CENTER);
	lbsum_spent.setBackground(new Color(35, 157, 135));
	lbsum_spent.setFont(hh.textfn);
	lbsum_spent.setOpaque(true);
	lbsum_spent.setBounds(520, 430, 180, 100);
	rPanel.add(lbsum_spent);

	lbbalance = new JLabel("Balance", SwingConstants.CENTER);
	lbbalance.setBackground(new Color(255, 238, 88));
	lbbalance.setFont(hh.textfn);
	lbbalance.setOpaque(true);
	lbbalance.setBounds(710, 430, 180, 100);
	rPanel.add(lbbalance);
	
	btntrans = gr.sbcs(" Spent calculate from Transactions");
	btntrans.setBounds(10, 460, 300, 30);
	btntrans.setForeground(Color.black);	
	btntrans.setBackground(Color.ORANGE);
	rPanel.add(btntrans);
	btntrans.addActionListener(e -> transactions_call());
	
    
    ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/grad2.png"));
	Image i2 = i1.getImage().getScaledInstance(1294, 680, Image.SCALE_DEFAULT);

	ImageIcon i3 = new ImageIcon(i2);
	JLabel imagex = new JLabel(i3);
	imagex.setBounds(0, 0, 1294, 680);
	add(imagex);	
	setVisible(true);
}

private void savebuttrun() {
	DefaultTableModel d1 = (DefaultTableModel) btable.getModel();
	String sql = "";
	String spent =  txspent.getText();
	String amount = txamount.getText();
	amount = hh.bsf(amount);
	String startdate = txstartdate.getText();
	String enddate = txenddate.getText();	
	String period = cmbperiod.getSelectedItem().toString();
	int scid = (int) ((Subcategory) cmbsubcategories.getSelectedItem()).getScid();
	String subcategory= (String) ((Subcategory) cmbsubcategories.getSelectedItem()).toString();
	String createdat = hh.currentDate();

	if (bvalidation(subcategory, period, amount, startdate, enddate) == false) {
		return;
	}

	if (rowid != "") {
		sql = "update budgets set catid= '" + scid + "',  amount='" + amount + "', period='" + period
				+ "', start_date='" + startdate + "', end_date='" + enddate + "',  spent = '" + spent 
				+ "', createdat ='"+ createdat + "' where bid = " + rowid;
	} else {
		sql = "insert into budgets  (catid, amount,  period, start_date, end_date, spent, createdat ) " + " values" + " ('"
				+ scid + "','" + amount + "','" + period + "', '" + startdate + "','" + enddate+ "', '" + spent + "', '"
				+ createdat + "')";
	}

	try {
		int flag = dh.Insupdel(sql);
		if (flag > 0) {
			hh.ztmessage("Success", "Message");
			if (rowid == "") {
				int myid = dd.table_maxid("SELECT MAX(bid) AS max_id from budgets");
				d1.insertRow(d1.getRowCount(),
						new Object[] { myid, scid, subcategory,period,  startdate, enddate, amount, spent, "0"});

				hh.gotolastrow(btable);
				if (btable.getRowCount() > 0) {
					int row = btable.getRowCount() - 1;
					btable.setRowSelectionInterval(row, row);
				}
			} else {
			table_rowrefresh( scid, subcategory, period, startdate, enddate, amount, spent, "0");
			}
		} else {
			JOptionPane.showMessageDialog(null, "sql error !");
		}
	} catch (Exception e) {
		System.err.println("SQLException: " + e.getMessage());
		e.printStackTrace();
		JOptionPane.showMessageDialog(null, "sql insert hiba");
	}
	totalize();
   clearFields();
}

	private void data_delete() {
		String sql = " delete from budgets where bid= ";
		int flag = dd.data_delete(btable, sql);
		if (flag > 0) {
       
		}	
}

private void table_rowrefresh(int scid, String subcategory, String period, String startdate
		, String enddate, String amount, String spent, String diff) {	
	DefaultTableModel d1 = (DefaultTableModel) btable.getModel();
	d1.setValueAt(scid, myrow, 1);
	d1.setValueAt(subcategory, myrow, 2);
	d1.setValueAt(period, myrow, 3);
	d1.setValueAt(startdate, myrow, 4);
	d1.setValueAt(enddate, myrow, 5);
	d1.setValueAt(amount, myrow, 6);
	d1.setValueAt(spent, myrow, 7);
	d1.setValueAt(diff, myrow, 8);
}

private void rowreader() {
	DefaultTableModel model = (DefaultTableModel) btable.getModel();
	try {
		int row = btable.getSelectedRow();
		if (row > -1) {
			rowid = btable.getValueAt(row, 0).toString();
			myrow = row;		
			String scid = btable.getValueAt(row, 1).toString();
			int icid = hh.stoi(scid);
			hh.setSelectedValue(cmbsubcategories, icid);
			
			String amount = btable.getValueAt(row, 6).toString();
			txamount.setText(amount);
			
			String period = btable.getValueAt(row, 3).toString();
		     cmbperiod.setSelectedItem(period);
		     
			txstartdate.setText(btable.getValueAt(row, 4).toString());
			txenddate.setText(btable.getValueAt(row, 5).toString());		
		
			String spent = btable.getValueAt(row, 7).toString();
        	txspent.setText(spent);			
		}
	} catch (Exception e) {
		System.out.println("sql error!!!");
	}
}
private void  clearFields() {
	rowid = "";
	myrow = 0;
	cmbsubcategories.setSelectedIndex(0);
	txamount.setText("");
	txstartdate.setText("");
	txenddate.setText("");
	txspent.setText("");
	cmbsubcategories.requestFocus(true);
}

private void btable_update(String swhere) {
	DefaultTableModel m1 = (DefaultTableModel) btable.getModel();
	m1.setRowCount(0);

	String Sql = "select  b.bid, b.catid, c.sname as category, b.period, b.start_date, b.end_date,"
	+ " b.amount, b.spent from  budgets b "
	+ "  left join subcategories c on  b.catid = c.scid "  + swhere +" order by start_date";
	try {
		rs = dh.GetData(Sql);
		while (rs.next()) {
			String bid = rs.getString("bid");
			String catid = rs.getString("catid");
			String category = rs.getString("category");
			String period = rs.getString("period");			
			String startdate = rs.getString("start_date");
			String enddate = rs.getString("end_date");		
			String amount = rs.getString("amount");
			String spent = rs.getString("spent");
			String createdat = hh.currentDate();
			m1.addRow(new Object[] { bid, catid, category, period, startdate, enddate, amount,
					spent, "0" });
		}

	} catch (SQLException e) {
		e.printStackTrace();
	} finally {
		dh.CloseConnection();
	}
	String[] fej = { "bid","Catid","Subcategory","Period", "Start_date", "End_date", "Amount",
		"Spent","Difference"};

	((DefaultTableModel) btable.getModel()).setColumnIdentifiers(fej);
	int[] wcols0 = { 0, 1};
	hh.cellswidth_0(btable, wcols0);

	int[][] wcols = { { 2, 220 }, { 3, 100 }, { 4, 100 }, { 5, 100}, { 6, 120 }, { 7, 120 },{ 8, 100 }};
	hh.cellswidth(btable, wcols);

	int[] cols = { 6, 7, 8 };
	hh.cellsright(btable, cols); 

	btable.addComponentListener(new ComponentAdapter() {
		public void componentResized(ComponentEvent e) {
			btable.scrollRectToVisible(btable.getCellRect(btable.getRowCount() - 1, 0, true));
		}
	});
	if (btable.getRowCount() > 0) {
		int row = btable.getRowCount() - 1;
		btable.setRowSelectionInterval(row, row);
	}
	totalize();
}

private Boolean bvalidation(String subcategory, String period, String amount,
		String startdate, String enddate) {
	Boolean ret = true;
	ArrayList<String> err = new ArrayList<String>();

	if (!allv.datevalid(startdate)) {
		err.add(allv.mess);
		ret = false;
	}
	if (!allv.datevalid(enddate)) {
		err.add(allv.mess);
		ret = false;
	}
	if (!allv.amountvalid(amount)) {
		err.add(allv.mess);
		ret = false;
	}
	if (!allv.catvalid(subcategory)) {
		err.add(allv.mess);
		ret = false;
	}
	if (!allv.periodvalid(period)) {
		err.add(allv.mess);
		ret = false;
	}
	if (!allv.twodate(startdate, enddate)){
		err.add(allv.mess);
		ret=false;
	}	
	if (err.size() > 0) {
		JOptionPane.showMessageDialog(null, err.toArray(), "Error message", 1);
	}
	return ret;
}
private void sqlgyart() {
	Boolean done = false;
	String swhere = "";
	String sscid="";
	String syear = cmbyear.getSelectedItem().toString();
	int scid = (int) ((Subcategory) cmbsubcategoriess.getSelectedItem()).getScid();
 if ( scid !=0) {
	sscid = hh.itos(scid);
 }	
	
  if (!hh.zempty(syear)) {
		swhere = " where strftime('%Y', date(b.start_date))  = '" + syear + "' ";
	    done = true;
	}

	if (!hh.zempty(sscid)) {
		if (done == false) {
			swhere = " where b.catid  = '" + sscid + "' ";
		} else {
			swhere = swhere + " and b.catid = '" + sscid + "'";
		}
		done = true;
	}
	if (done == true) {
		btable_update(swhere);
	}
}

private void transactions_call(){

		//String pcatid = btable.getValueAt(row,1 ).toString();
	       int scid = (int) ((Subcategory) cmbsubcategories.getSelectedItem()).getScid();
	       String pcatid = hh.itos(scid);
		   String startdate = txstartdate.getText();
		   String pyear = hh.datesplit(startdate, 1);		     
	       if (!hh.zempty(pyear) && !hh.zempty(pcatid)){
            Transcollect tt = new Transcollect(pcatid, pyear, this);
	       }
}

public void passtobtable(String snumber) {	
      txspent.setText(snumber);
}

private void date_offer(int which) {
String currdate = hh.currentDate();
String syear = hh.datesplit(currdate, 1);
String smonth = hh.datesplit(currdate, 2);
int iyear = hh.stoi(syear);
int nlastday = hh.hohossza(hh.stoi(syear), hh.stoi(smonth));
String lastday = hh.itos(nlastday);
if (which == 1) {
      firstdate = syear +"-"+ smonth +"-01";
      lastdate = syear +"-"+ smonth +"-" + lastday;
} else {
	firstdate = syear +"-01-01";
	lastdate = syear +"-12-31";
}
return;
}

private void totalize() {
	double balance = 0;
	double sum_amount = 0;
	double sum_spent = 0;
	double rowdiff = 0;
	String srowdiff ="";
	int rowCount = btable.getRowCount();
	for (int row = 0; row < rowCount; row++) {	
		String samount = btable.getValueAt(row, 6).toString();
		Double damount = hh.stodd(samount);
		String sspent = btable.getValueAt(row, 7).toString();
		Double dspent = hh.stodd(sspent);	
		sum_amount = sum_amount + damount;
		sum_spent = sum_spent + dspent;	
		rowdiff = damount-dspent;	
		srowdiff = hh.d(rowdiff);
		DefaultTableModel d1 = (DefaultTableModel) btable.getModel();
		btable.setValueAt(srowdiff, row, 8);
	}
	
      balance = sum_amount - sum_spent;
     lbsum_amount.setText("<html>Amount<br /><center> " + hh.d(sum_amount) + "</center></html>");
     lbsum_spent.setText("<html>Spent<br /><center> " + hh.d(sum_spent) + "</center></html>");
     lbbalance.setText("<html>Balance<br /><center> " + hh.d(balance) + "</center></html>");
}
private void budgets_print() {
	
	try {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setJobName("Budgets");
		HashPrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
		// attr.add(new MediaPrintableArea(5, 5, 200, 287, MediaPrintableArea.MM));
		attr.add(new MediaPrintableArea(10, 10, 190, 275, MediaPrintableArea.MM));
		job.print(attr);

		MessageFormat[] header = new MessageFormat[3];
		header[0] = new MessageFormat(hss.center(" BUDGETS LIST ", 165));
		
		String syear = cmbyear.getSelectedItem().toString();
		String category = (String) ((Subcategory) cmbsubcategories.getSelectedItem()).toString();
	
		String sheader = "Year: " + syear +" Category: "+ category ;
		header[1] = new MessageFormat(sheader);
		header[2] = new MessageFormat("Date: " + hh.currentDate()+ " Time: " + hh.currenttime());
		MessageFormat[] footer = new MessageFormat[1];
		// footer[0] = new MessageFormat(hss.center(" -{0}-",160));
		footer[0] = new MessageFormat(hss.center("Page {0,number,integer}", 185));
		job.setPrintable(hh.new MyTablePrintable(btable, JTable.PrintMode.FIT_WIDTH, header, footer));
		job.printDialog();
		job.print();
		
		} catch (java.awt.print.PrinterAbortException e) {
		} catch (PrinterException ex) {
			System.err.println("Error printing: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
public JFormattedTextField DatetextField() {
	JFormattedTextField  textField = null;
	MaskFormatter dateMask;
	try {
		dateMask = new MaskFormatter("####-##-##");
		dateMask.setPlaceholderCharacter('_');
		textField = new JFormattedTextField(dateMask);
		textField.setColumns(10);
		textField.setText("");
	//	textField.setBorder(bb);
		textField.setFont(new Font("Tahoma", 1, 16));
		textField.setPreferredSize(new Dimension(150, 25));
		textField.setBackground(Color.white);
		textField.setForeground(Color.black);	
		textField.setDisabledTextColor(Color.PINK);		
		textField.setCaretColor(Color.RED);
		textField.putClientProperty("caretAspectRatio", 0.1);
		Border margin = new EmptyBorder(4, 4, 4, 4);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        textField.setBorder(BorderFactory.createCompoundBorder(border, margin));
    	textField.addFocusListener(dFocusListener);
	} catch (ParseException e) {
	e.printStackTrace();
	}	  
	
	return textField;
} 
private final FocusListener dFocusListener = new FocusListener() {
	@Override
	public void focusGained(FocusEvent e) {
		JComponent c = (JComponent) e.getSource();
		//c.setBorder(hh.borderz);
	}
	@Override
	public void focusLost(FocusEvent e) {
		String tartalom = "";
		boolean vissza = true;
		JTextField Txt = (JTextField) e.getSource();
		tartalom = Txt.getText();
		if  (Txt == txstartdate && tartalom != " ____-__-__") {
			if (hh.validatedate(tartalom, "N")) {
				date_offer_enddate(tartalom);			
			}		
		}		
	}
};
private void date_offer_enddate(String startdate) {
String syear = hh.datesplit(startdate, 1);
String smonth = hh.datesplit(startdate, 2);
int iyear = hh.stoi(syear);
int nlastday = hh.hohossza(hh.stoi(syear), hh.stoi(smonth));
String lastday = hh.itos(nlastday);
String sitem = cmbperiod.getSelectedItem().toString();
if (sitem=="Monthly") {	
      txenddate.setText( syear +"-"+ smonth +"-" + lastday);
} else {	
	txstartdate.setText(syear +"-01-01");
	txenddate.setText(syear +"-12-31");
}
return;
}

public static void main(String args[]) {
	EventQueue.invokeLater(new Runnable() {
		public void run() {
			try {
				new Budgets();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
}

JLabel lbheader, lbcategory, lbperiod, lbamount, lbstartdate, lbenddate, lbspent, lbyear, lbcategorys;
JLabel lbsum_amount, lbsum_spent, lbbalance;
JPanel exitPanel, searchPanel, lPanel, rPanel;
JComboBox cmbtypes, cmbsubcategories, cmbperiod, cmbsubcategoriess, cmbyear;
JTextField txamount, txstartdate, txenddate, txspent;
JButton btnsave, btndelete, btncancel, btntrans, btnsearch, btnclear,btnprint;
JTable btable;
JScrollPane bPane;
}
