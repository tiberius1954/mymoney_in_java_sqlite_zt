import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import com.toedter.calendar.JDateChooser;
import java.util.Date;
import Classes.Grlib;
import Classes.Hhelper;
import Classes.Hhelper.StringUtils;
import Classes.Allvalid;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;
import Databaseop.Databaseop.Category;
import Databaseop.Databaseop.Subcategory;
import Databaseop.Databaseop.Payment;
import java.util.Map;

public class Transactions extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();	
	Allvalid allv = new Allvalid();
	StringUtils hss = new StringUtils();
	JDateChooser tdate = new JDateChooser(new Date());
	String rowid = "";
	int myrow = 0;
	protected String[] types = dd.typesnames();
	String[] typess = { "", "Expense", "Income" };
	JFrame myframe = this;
	String typmark = "E";

	Transactions() {
		init();
		cmbtypes.setModel(new DefaultComboBoxModel(types));
		dd.subcategoriescombofill(cmbsubcategories, typmark);
		dd.subcategoriescombofill(cmbsubcategoriess, typmark);
		cmbtypsearch.setModel(new DefaultComboBoxModel(typess));
		dd.cmbpaymentcombofill(cmbpay);
		cmbyear.addItem("");
		for (int i = 2025; i <= 2050; i++) {
			cmbyear.addItem(hh.itos(i));
		}
		cmbmonth.addItem("");
		for (int i = 1; i <= 12; i++) {
			cmbmonth.addItem(hh.itos(i));
		}
		ttable_update("");
	//	txdate.setText(hh.currentDate());
	 tdate.requestFocus(true);
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
		setSize(new java.awt.Dimension(1300, 650));
		setLocationRelativeTo(null);
		setUndecorated(true);

		exitPanel = gr.makeexitpanel(this);
		exitPanel.setBounds(1200, 10, 100, 35);
		exitPanel.setOpaque(true);
		exitPanel.setBackground(new Color(51, 255, 51));
		add(exitPanel);

		lbheader = hh.fflabel("Transactions");
		lbheader.setBounds(25, 10, 250, 35);
		add(lbheader);

		lPanel = new JPanel(null);
		// lPanel.setBackground(new Color(178, 216, 216));
		lPanel.setBounds(20, 50, 400, 580);
		lPanel.setBorder(hh.ztroundborder(Color.yellow));
		lPanel.setOpaque(false);
		add(lPanel);

		rPanel = new JPanel(null);
//	 rPanel.setBackground(new Color(178, 216, 216));
		rPanel.setBounds(426, 50, 855, 580);
		rPanel.setBorder(hh.ztroundborder(Color.yellow));
		rPanel.setOpaque(false);
		add(rPanel);

		searchPanel = new JPanel(null);
		searchPanel.setBounds(10, 10, 837, 80);
		searchPanel.setBackground(hh.zgreen);
		rPanel.add(searchPanel);

		lbdate = hh.clabel("Date");
		lbdate.setBounds(10, 20, 100, 25);
		lPanel.add(lbdate);
		
		tdate.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
		tdate.setDateFormatString("yyyy-MM-dd");
		tdate.setFont(new Font("Arial", Font.BOLD, 16));
		tdate.setBounds(120, 20, 200, 30);
		lPanel.add(tdate);
//
//
//		txdate = hh.DatetextField();
//		txdate.setBounds(120, 20, 105, 30);
//		lPanel.add(txdate);
//		// txdate.addKeyListener(hh.Onlydate());

		lbtype = hh.clabel("Type");
		lbtype.setBounds(10, 60, 100, 30);
		lPanel.add(lbtype);

		cmbtypes = hh.cbcombo();
		cmbtypes.setBounds(120, 60, 150, 30);
		lPanel.add(cmbtypes);

		cmbtypes.addActionListener((ActionListener) new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String stype = (String) cmbtypes.getSelectedItem();
				if (stype == "Income") {
					typmark = "I";
					dd.subcategoriescombofill(cmbsubcategories, "I");
					dd.subcategoriescombofill(cmbsubcategoriess, "I");
				} else {
					typmark = "E";
					dd.subcategoriescombofill(cmbsubcategories, "E");
					dd.subcategoriescombofill(cmbsubcategoriess, "E");
				}
			}
		});

		lbdescription = hh.clabel("Description");
		lbdescription.setBounds(10, 100, 100, 30);
		lPanel.add(lbdescription);

		txdescription = hh.cTextField(350);
		txdescription.setBounds(120, 100, 265, 25);
		lPanel.add(txdescription);
		txdescription.addKeyListener(hh.MUpper());

		lbcategory = hh.clabel("Subcategory");
		lbcategory.setBounds(0, 140, 110, 30);
		lPanel.add(lbcategory);

		cmbsubcategories = hh.cbcombo();
		cmbsubcategories.setName("subcategory");
		cmbsubcategories.setBounds(120, 140, 250, 30);
		lPanel.add(cmbsubcategories);

		btnnewcat = gr.sbcs("New subcategory");
		btnnewcat.setBounds(120, 180, 170, 30);
		btnnewcat.setBackground(hh.narancs);
		lPanel.add(btnnewcat);
		btnnewcat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Subcategories ca = new Subcategories(myframe, "transactions", typmark);
				ca.setLayout(null);
				ca.setLocationRelativeTo(null);
				ca.setVisible(true);
			}
		});

		lbamount = hh.clabel("Amount");
		lbamount.setBounds(10, 215, 100, 30);
		lPanel.add(lbamount);

		txamount = hh.cTextField(350);
		txamount.setBounds(120, 215, 150, 30);
		txamount.setHorizontalAlignment(JTextField.RIGHT);
		lPanel.add(txamount);
		txamount.addKeyListener(hh.Onlynum());

		lbpayment = hh.clabel("Payment");
		lbpayment.setBounds(10, 260, 100, 30);
		lPanel.add(lbpayment);

		cmbpay = hh.cbcombo();
		cmbpay.setName("payment");
		cmbpay.setBounds(120, 260, 250, 30);
		lPanel.add(cmbpay);

		btnnewpay = gr.sbcs("New payment");
		btnnewpay.setBounds(120, 300, 140, 30);
		btnnewpay.setBackground(hh.narancs);
		lPanel.add(btnnewpay);
		btnnewpay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Payments ca = new Payments(myframe, "transactions");
				ca.setLayout(null);
				ca.setLocationRelativeTo(null);
				ca.setVisible(true);
			}
		});

		lbnote = hh.clabel("Note");
		lbnote.setBounds(10, 385, 50, 30);
		lPanel.add(lbnote);

		txanote = hh.cTextarea();
		notePane = new JScrollPane(txanote, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		notePane.setBorder(hh.borderf);
		notePane.setBounds(70, 340, 300, 120);
		notePane.setViewportView(txanote);
		lPanel.add(notePane);

		btnsave = gr.sbcs("Save");
		btnsave.setBounds(150, 470, 120, 30);
		btnsave.setForeground(Color.black);
		btnsave.setBackground(hh.lpiros);
		lPanel.add(btnsave);
		btnsave.addActionListener(e -> savebuttrun());

		btncancel = gr.sbcs("Cancel");
		btncancel.setBounds(150, 505, 120, 30);
		btncancel.setBackground(hh.vvzold);
		lPanel.add(btncancel);
		btncancel.addActionListener(e -> clearFields());

		btndelete = gr.sbcs("Delete");
		btndelete.setBounds(150, 540, 120, 30);
		btndelete.setForeground(Color.black);
		btndelete.setBackground(Color.yellow);
		lPanel.add(btndelete);
		btndelete.addActionListener(e -> data_delete());

		lbsearch = hh.clabel("Search");
		lbsearch.setBounds(70, 10, 80, 25);
		searchPanel.add(lbsearch);

		txsearch = hh.cTextField(150);
		txsearch.setBounds(160, 10, 250, 25);
		searchPanel.add(txsearch);

		btnsearch = hh.cbutton("Filter");
		btnsearch.setForeground(hh.skek);
		btnsearch.setBackground(Color.ORANGE);
		btnsearch.setBounds(415, 10, 90, 25);
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
		btnclear.setBounds(510, 10, 90, 25);
		searchPanel.add(btnclear);
		btnclear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				txsearch.setText("");
				cmbyear.setSelectedIndex(0);
				cmbmonth.setSelectedIndex(0);
				cmbsubcategoriess.setSelectedIndex(0);
				cmbtypsearch.setSelectedIndex(0);
				ttable_update("");
			}
		});
		
		btnprint = hh.cbutton("Print");
		btnprint.setForeground(hh.skek);
		btnprint.setBackground(Color.ORANGE);
		btnprint.setBounds(605, 10, 90, 25);
		searchPanel.add(btnprint);
		btnprint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			   trans_print();
			}
		});
		
		lbtypsearch = hh.clabel("Type");
		lbtypsearch.setBounds(10, 45, 60, 25);
		searchPanel.add(lbtypsearch);

		cmbtypsearch = hh.cbcombo();
		cmbtypsearch.setBounds(80, 45, 120, 25);
		cmbtypsearch.setFont(hh.textf);
		searchPanel.add(cmbtypsearch);

		
		lbcategorys = hh.clabel("Subcategory");
		lbcategorys.setBounds(205, 45, 110, 25);
		searchPanel.add(lbcategorys);

		cmbsubcategoriess = hh.cbcombo();
		cmbsubcategoriess.setBounds(325, 45, 190, 25);
		searchPanel.add(cmbsubcategoriess);

		lbyear = hh.clabel("Years");
		lbyear.setBounds(525, 45, 50, 25);
		searchPanel.add(lbyear);

		cmbyear = hh.cbcombo();
		cmbyear.setBounds(585, 45, 70, 25);
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
		lbmonth.setBounds(655, 45, 60, 25);
		searchPanel.add(lbmonth);

		cmbmonth = hh.cbcombo();
		cmbmonth.setBounds(725, 45, 50, 25);
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
		ttable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {

			}
		});

		ttable.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				if (evt.getClickCount() == 2) {
					rowreader();
				}
			}
		});

		hh.madeheader(ttable);
		ttable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		ttable.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		tPane = new JScrollPane(ttable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		String[] pheader = new String[] { "Tid", "Date", "Type", "Description", "Catid", "Subcategory", "Amount", "pid",
				"Payment mode", "Note" };

		ttable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] { { null, null, null, null, null, null, null, null, null, null } }, pheader));

		tPane.setViewportView(ttable);
		tPane.setBounds(10, 90, 835, 300);
		tPane.setBorder(hh.myRaisedBorder);
		rPanel.add(tPane);

		int[] wcols0 = { 0, 4, 7 };
		hh.cellswidth_0(ttable, wcols0);

		int[][] wcols = { { 1, 90 }, { 2, 80 }, { 3, 250 }, { 5, 200 }, { 6, 80 }, { 8, 180 }, { 9, 300 } };
		hh.cellswidth(ttable, wcols);

		int[] cols = { 6 };
		hh.cellsright(ttable, cols);

		lbsumincome = new JLabel("Income", SwingConstants.CENTER);
		lbsumincome.setBackground(new Color(255, 93, 42));
		lbsumincome.setFont(hh.textfn);
		lbsumincome.setOpaque(true);
		lbsumincome.setBounds(115, 430, 200, 100);
		rPanel.add(lbsumincome);

		lbsumexpense = new JLabel("Expense", SwingConstants.CENTER);
		lbsumexpense.setBackground(new Color(35, 157, 135));
		lbsumexpense.setFont(hh.textfn);
		lbsumexpense.setOpaque(true);
		lbsumexpense.setBounds(325, 430, 200, 100);
		rPanel.add(lbsumexpense);

		lbbalance = new JLabel("Balance", SwingConstants.CENTER);
		lbbalance.setBackground(new Color(255, 238, 88));
		lbbalance.setFont(hh.textfn);
		lbbalance.setOpaque(true);
		lbbalance.setBounds(535, 430, 200, 100);
		rPanel.add(lbbalance);

		
		 ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/grad2.png"));
			Image i2 = i1.getImage().getScaledInstance(1300, 650, Image.SCALE_DEFAULT);
			ImageIcon i3 = new ImageIcon(i2);
			JLabel imagex = new JLabel(i3);
			imagex.setBounds(0, 0, 1300, 650);
			add(imagex);

		setVisible(true);
	}

	private void rowreader() {
		DefaultTableModel model = (DefaultTableModel) ttable.getModel();
		try {
			int row = ttable.getSelectedRow();
			if (row > -1) {
				rowid = ttable.getValueAt(row, 0).toString();
				myrow = row;
				String mynote = model.getValueAt(row, 9).toString();
				txanote.setText(mynote);
				String sdate = model.getValueAt(row, 1).toString();	
				Date date;
				if (!hh.zempty(sdate)) {
					   date = new SimpleDateFormat("yyyy-MM-dd").parse(sdate);
				  	  tdate.setDate(date);
				} else {
					tdate.setCalendar(null);
				}

			//	txdate.setText(sdate);
				String description = ttable.getValueAt(row, 3).toString();
				txdescription.setText(description);
				String amount = ttable.getValueAt(row, 6).toString();
				txamount.setText(amount);
				cmbtypes.setSelectedItem(ttable.getValueAt(row, 2).toString());
				String cid = ttable.getValueAt(row, 4).toString();
				int icid = hh.stoi(cid);
				hh.setSelectedValue(cmbsubcategories, icid);
				String pid = ttable.getValueAt(row, 7).toString();
				int ipid = hh.stoi(pid);
				hh.setSelectedValue(cmbpay, ipid);
			}
		} catch (Exception e) {
			System.out.println("sql error!!!");
		}
	}

	private void savebuttrun() {
		DefaultTableModel d1 = (DefaultTableModel) ttable.getModel();
		String sql = "";
		String description = txdescription.getText();
		String amount = txamount.getText();
		amount  = hh.bsf(amount);	
		String sdate = ((JTextField) tdate.getDateEditor().getUiComponent()).getText();
		String note = txanote.getText();
		int scid = (int) ((Subcategory) cmbsubcategories.getSelectedItem()).getScid();
		String catname = (String) ((Subcategory) cmbsubcategories.getSelectedItem()).toString();
		int pid = (int) ((Payment) cmbpay.getSelectedItem()).getPid();
		String payname = (String) ((Payment) cmbpay.getSelectedItem()).toString();
		String stype = cmbtypes.getSelectedItem().toString();

		if (validation(sdate, description, amount, catname, payname) == false) {
			return;
		}

		if (rowid != "") {
			sql = "update transactions set date= '" + sdate + "',  type='" + stype + "', catid='" + scid
					+ "', description='" + description + "', pid='" + pid + "',  amount = '" + amount + "', note ='"
					+ note + "' where tid = " + rowid;
		} else {
			sql = "insert into transactions  (date, type,  catid, description, pid, amount, note ) " + " values" + " ('"
					+ sdate + "','" + stype + "','" + scid + "', '" + description + "'," + pid + ", '" + amount + "', '"
					+ note + "')";
		}

		try {
			int flag = dh.Insupdel(sql);
			if (flag > 0) {
				hh.ztmessage("Success", "Message");
				if (rowid == "") {
					int myid = dd.table_maxid("SELECT MAX(tid) AS max_id from transactions");
					d1.insertRow(d1.getRowCount(),
							new Object[] { myid, sdate, stype, description, scid, catname, amount, pid, payname, note });

					hh.gotolastrow(ttable);
					if (ttable.getRowCount() > 0) {
						int row = ttable.getRowCount() - 1;
						ttable.setRowSelectionInterval(row, row);
					}
				} else {
					table_rowrefresh(sdate, stype, description, scid, catname, amount, pid, payname, note);
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
		totalize();
	}

	private void table_rowrefresh(String date, String type, String description, int scid, String catname, String amount,
			int pid, String payname, String note) {
		DefaultTableModel d1 = (DefaultTableModel) ttable.getModel();
		d1.setValueAt(date, myrow, 1);
		d1.setValueAt(type, myrow, 2);
		d1.setValueAt(description, myrow, 3);
		d1.setValueAt(scid, myrow, 4);
		d1.setValueAt(catname, myrow, 5);
		d1.setValueAt(amount, myrow, 6);
		d1.setValueAt(pid, myrow, 7);
		d1.setValueAt(payname, myrow, 8);
		d1.setValueAt(note, myrow, 9);
	}

	private void data_delete() {
		String sql = " delete from transactions where tid= ";
		int flag = dd.data_delete(ttable, sql);
		if (flag > 0) {
             totalize();
		}
	}

	private Boolean validation(String sdate, String description, String amount, String catname, String payname) {
		Boolean ret = true;
		ArrayList<String> err = new ArrayList<String>();

		if (!allv.descriptionvalid(description)) {
			err.add(allv.mess);
			ret = false;
		}
		if (!allv.datevalid(sdate)) {
			err.add(allv.mess);
			ret = false;
		}
		if (!allv.amountvalid(amount)) {
			err.add(allv.mess);
			ret = false;
		}
		if (!allv.catvalid(catname)) {
			err.add(allv.mess);
			ret = false;
		}
		if (!allv.payvalid(payname)) {
			err.add(allv.mess);
			ret = false;
		}

		if (err.size() > 0) {
			JOptionPane.showMessageDialog(null, err.toArray(), "Error message", 1);
		}
		return ret;
	}

	private void clearFields() {
		rowid = "";
		myrow = 0;
		txanote.setText("");
		txdescription.setText("");
		txamount.setText("");
		cmbsubcategories.setSelectedIndex(0);
		cmbpay.setSelectedIndex(0);
		tdate.setCalendar(null);
		tdate.requestFocus(true);
	}

	private void ttable_update(String swhere) {
		DefaultTableModel m1 = (DefaultTableModel) ttable.getModel();
		m1.setRowCount(0);
		String Sql = "select  t.tid, t.date, t.type, t.description, t.catid, c.sname as category,"
				+ " t.amount, t.pid, p.name as payment, t.note  from  transactions t"
				+ " left join subcategories c on  t.catid = c.scid " + " left join payments p on t.pid = p.pid " + swhere
				+ " order by date";
		try {
			rs = dh.GetData(Sql);
			while (rs.next()) {
				String tid = rs.getString("tid");
				String date = rs.getString("date");
				String type = rs.getString("type");
				String catid = rs.getString("catid");
				String category = rs.getString("category");
				String description = rs.getString("description");
				String pid = rs.getString("pid");
				String payment = rs.getString("payment");
				String amount = rs.getString("amount");
				String note = rs.getString("note");
				m1.addRow(new Object[] { tid, date, type, description, catid, category, amount, pid, payment, note });
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dh.CloseConnection();
		}
		String[] fej = { "Tid", "Date", "Type", "Description", "Catid", "Subcategory", "Amount", "pid", "Payment mode",
				"Note" };
		((DefaultTableModel) ttable.getModel()).setColumnIdentifiers(fej);
		int[] wcols0 = { 0, 4, 7 };
		hh.cellswidth_0(ttable, wcols0);

		int[][] wcols = { { 1, 90 }, { 2, 80 }, { 3, 250 }, { 5, 200 }, { 6, 80 }, { 8, 180 }, { 9, 300 } };
		hh.cellswidth(ttable, wcols);

		int[] cols = { 6 };
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
		totalize();
	}

	private void sqlgyart() {
		Boolean done = false;
		String swhere = "";
		String ssearch = txsearch.getText().trim().toLowerCase();
		String syear = cmbyear.getSelectedItem().toString();
		String smonth = cmbmonth.getSelectedItem().toString().trim();
		String sscid="";
		int scid = (int) ((Subcategory) cmbsubcategoriess.getSelectedItem()).getScid();
		 if ( scid !=0) {
			sscid = hh.itos(scid);
		 }	
		 if (!hh.zempty(sscid)) {
				if (done == false) {
					swhere = " where t.catid  = '" + sscid + "' ";
				} else {
					swhere = swhere + " and t.catid = '" + sscid + "'";
				}
				done = true;
			}
		if (!hh.zempty(smonth)) {
			smonth = hh.padLeftZeros(smonth, 2);
		}
	
		if (!hh.zempty(ssearch)) {
			swhere = " where lower(t.description) LIKE '%" + ssearch + "%' ";
			done = true;
		}
		if (!hh.zempty(syear)) {
			if (done == false) {
				swhere = " where strftime('%Y', date(t.date))  = '" + syear + "' ";
			} else {
				swhere = swhere + " and strftime('%Y', date(t.date))  = '" + syear + "' ";
			}
			done = true;
		}

		if (!hh.zempty(smonth)) {
			if (done == false) {
				swhere = " where strftime('%m', date(t.date))  = '" + smonth + "' ";
			} else {
				swhere = swhere + " and strftime('%m', date(t.date))  = '" + smonth + "' ";
			}
			done = true;
		}	

		if (!hh.zempty(ssearch)) {
			swhere = " where lower(t.description) LIKE '%" + ssearch + "%' ";
			done = true;
		}
		
		String stype = cmbtypsearch.getSelectedItem().toString();

		if (!hh.zempty(stype)) {
			if (done == false) {
				swhere = " where t.type = '" + stype + "'";
			} else {
				swhere = swhere + " and t.type = '" + stype + "'";
			}
			done = true;
		}
		if (done == true) {
			ttable_update(swhere);
		}
	}

	private void totalize() {
		double balance = 0;
		double sum_income = 0;
		double sum_expense = 0;
		int rowCount = ttable.getRowCount();
		for (int row = 0; row < rowCount; row++) {
			String type = ttable.getValueAt(row, 2).toString();
			String samount = ttable.getValueAt(row, 6).toString();
			Double damount = hh.stodd(samount);
			if (type.equals("Income")) {
				sum_income = sum_income + damount;
			} else {
				sum_expense = sum_expense + damount;
			}
		}
		
       balance = sum_income - sum_expense;
       lbsumincome.setText("<html>Income<br /><center> " + hh.d(sum_income) + "</center></html>");
       lbsumexpense.setText("<html>Expense<br /><center> " + hh.d(sum_expense) + "</center></html>");
       lbbalance.setText("<html>Balance<br /><center> " + hh.d(balance) + "</center></html>");
}
	
	private void trans_print() {
		
		try {
			PrinterJob job = PrinterJob.getPrinterJob();
			job.setJobName("Transactions");
			HashPrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
			// attr.add(new MediaPrintableArea(5, 5, 200, 287, MediaPrintableArea.MM));
			attr.add(new MediaPrintableArea(10, 10, 190, 275, MediaPrintableArea.MM));
			job.print(attr);
	
			MessageFormat[] header = new MessageFormat[3];
			header[0] = new MessageFormat(hss.center(" TRANSACTION LIST ", 165));			
			String syear = cmbyear.getSelectedItem().toString();
			String smonth = cmbmonth.getSelectedItem().toString().trim();		
			String sheader = "Year: " + syear +" Month: "+ smonth;
			header[1] = new MessageFormat(sheader);
			header[2] = new MessageFormat("Date: " + hh.currentDate()+ " Time: " + hh.currenttime());
			MessageFormat[] footer = new MessageFormat[1];
			// footer[0] = new MessageFormat(hss.center(" -{0}-",160));
			footer[0] = new MessageFormat(hss.center("Page {0,number,integer}", 185));
			job.setPrintable(hh.new MyTablePrintable(ttable, JTable.PrintMode.FIT_WIDTH, header, footer));
			job.printDialog();
			job.print();
			
			} catch (java.awt.print.PrinterAbortException e) {
			} catch (PrinterException ex) {
				System.err.println("Error printing: " + ex.getMessage());
				ex.printStackTrace();
			}	
}

	public void passtocmbc(int number) {
		dd.subcategoriescombofill(cmbsubcategories, typmark);
		hh.setSelectedValue(cmbsubcategories, number);
		cmbsubcategories.updateUI();
	}

	public void passtocmbx(int number) {
		dd.cmbpaymentcombofill(cmbpay);
		hh.setSelectedValue(cmbpay, number);
		cmbpay.updateUI();
	}

	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Transactions();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JButton exitbutton, minbutton, maxbutton, btnnewcat, btnnewpay, btnsave, btndelete, btncancel, btnsearch, btnclear,
			btnprint;
	JPanel exitPanel, rPanel, lPanel, searchPanel;
	JLabel lbheader, lbsearch, lbdate, lbtype, lbdescription, lbpayment, lbcategory, lbamount, lbnote;
	JLabel lbyear, lbmonth, lbsumincome, lbsumexpense, lbbalance, lbcategorys, lbtypsearch;
	JTextField txdescription, txamount, txsearch;
	JComboBox cmbtypes, cmbtypsearch,  cmbsubcategories, cmbpay, cmbyear, cmbmonth,
	cmbsubcategoriess;
	JTextArea txanote;
	JScrollPane notePane, tPane;
	JTable ttable;
}
