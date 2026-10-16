package Classes;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.SQLException;
import java.sql.DriverManager;
import Classes.Hhelper;
import Databaseop.Databaseop;

public class Allvalid {
	Hhelper hh = new Hhelper();
	Databaseop dd = new Databaseop();
	public String mess = "";
	String ss;
	Boolean yes = false;

	public boolean namevalid(String content) {
		if (hh.zempty(content)) {
			mess= "Name is empty !";
			return false;
		}
		return true;
	}
	public boolean amountvalid(String content) {
		if (hh.zempty(content)) {
			mess= "Amount is empty !";
			return false;
		}
		return true;
	}
	public Boolean phonevalid(String content) {
		if (hh.zempty(content)) {
			mess = "Phone is empty !";
			return false;
		} else if (content.length() < 7) {
			mess = "Phone is short !";
			return false;
		}
		return true;
	}

	public boolean emailvalid(String content) {
		if (hh.zempty(content)) {
		     mess = "Email is empty !";
			return false;
		} else if (content.indexOf(".") == -1 || content.indexOf("@") == -1) {
			mess = "Incorrect email  !";
			return false;
		}
		return true;
	}
	public boolean descriptionvalid(String content) {
		if (hh.zempty(content)) {
			mess = "Description is empty !";
			return false;
		}
		return true;
	}
	public boolean periodvalid(String content) {
		if (hh.zempty(content)) {
			mess = "Period is empty !";
			return false;
		}
		return true;
	}
	public boolean payvalid(String content) {
		if (hh.zempty(content)) {
			mess = "Payment is empty !";
			return false;
		}
		return true;
	}
	public Boolean catvalid(String content) {
		if (hh.zempty(content)) {
			mess = "Category is empty !";
			return false;	
		}
	return true;
	}	
	
	public boolean datevalidr(String content) {
		if (hh.zempty(content)) {
			mess = "Date is empty !";
			return false;
		} else {
			if (!hh.validatedate(content, "N")) {
				mess= "Incorrect date !";
				return false;
			}
		}
		return true;
	}
	public boolean datevalid(String content) {
		if (hh.zempty(content) || content.equals("____-__-__" ) ) {
			mess = "Date is empty !";
			return false;
		} else {
			if (!hh.validatedate(content, "N")) {
				mess= "Incorrect  date !";
				return false;
			}
		}
		return true;
	}
	public boolean createatvalid(String content) {
		if (hh.zempty(content)|| content.equals("____-__-__" ) ) {
			mess = "Create at is empty !";
			return false;
		} else {
			if (!hh.validatedate(content, "N")) {
				mess= "Incorrect Date !";
				return false;
			}
		}
		return true;
	}
	
	public boolean twodate(String startdate, String enddate) {	
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = simpleDateFormat.parse(startdate);
			Date date2 = simpleDateFormat.parse(enddate);
			if (date2.compareTo(date1) < 0) {
				mess = " startdate bigger then enddate !";
			//	JOptionPane.showMessageDialog(null, mess, "Alert", JOptionPane.ERROR_MESSAGE);
				return false;
			}
		} catch (Exception e) {
			mess = "Date error!";
			return false;
		}
		return true;
	}	
}
