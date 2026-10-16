import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import Classes.Calhelper;
import Classes.Grlib;
import Classes.Hhelper;

public class Chartspanel extends JFrame{	
	Grlib gr = new Grlib();	
	Hhelper hh = new Hhelper();	
	Calhelper cc = new Calhelper();
	JFrame myframe = this;
	
	Chartspanel(){
	init();
	}
	
	private void init() {
		getContentPane().setBackground(hh.zgreen1);
		setSize(380, 460);
		setLayout(null);
		setLocationRelativeTo(null);	
		setUndecorated(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				dispose();
			}
		});
		exitPanel = gr.makeexitpanel(this);
		exitPanel.setBounds(275, 15, 95, 35);
		exitPanel.setOpaque(true);
		exitPanel.setBackground(hh.zgreen1);		
		add(exitPanel);
		
		mPanel = new JPanel(null);
    	mPanel.setBounds(2, 2, 374, 456);
		mPanel.setBackground(new Color(178, 216, 216));
	//	mPanel.setBorder(hh.ztroundborder(Color.yellow));
		mPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green));
		mPanel.setBackground(hh.zgreen1);
		add(mPanel);
		
		lbheader = new JLabel("Charts panel");
		lbheader.setForeground(Color.yellow);
		lbheader.setFont(new Font("tahoma", Font.BOLD, 24));
		lbheader.setBounds(20, 10, 300, 35);
		mPanel.add(lbheader);
		
		btnchart1 = gr.sbcs("Expense by subcategories");
		btnchart1.setText("<html>Expense or income<br /><center> by subcategories"
		 		+ "</center></html>");

		btnchart1.setBounds(50, 70, 280, 60);
		btnchart1.setBackground(cc.psarga);
		mPanel.add(btnchart1);
		btnchart1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Mchart1 pa =  new Mchart1();	
			    pa.setVisible(true);
			}
	});
		
		btnchart11 = gr.sbcs("Expense by categories");
		btnchart11.setText("<html>Expense or income<br /><center> by categories"
		 		+ "</center></html>");

		btnchart11.setBounds(50, 140, 280, 60);
		btnchart11.setBackground(cc.psarga);
		mPanel.add(btnchart11);
		btnchart11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Mchart11 pa =  new Mchart11();	
			    pa.setVisible(true);
			}
	});
		
		btnchart3 = gr.sbcs("Monthly Incomes and Expenses");
		btnchart3.setBounds(50, 210, 280, 60);
		btnchart3.setBackground(cc.psarga);
		mPanel.add(btnchart3);
		btnchart3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Mchart3 pa =  new Mchart3();	
			    pa.setVisible(true);
			}
	});
		btnincome = gr.sbcs("Income by subcategories");
		btnincome.setBounds(50, 280, 280, 60);
		btnincome.setBackground(cc.psarga);
		mPanel.add(btnincome);
		btnincome.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Donutchart pa =  new Donutchart();	
			    pa.setVisible(true);
			}
	});
		
		btnbudget = gr.sbcs("Budget by categories");
		btnbudget.setBounds(50, 350,280, 60);
		btnbudget.setBackground(cc.psarga);
		mPanel.add(btnbudget);
		btnbudget.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Budgetchart pa =  new Budgetchart();	
			    pa.setVisible(true);
			}
	});
		
		setVisible(true);
	}	
	
	
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Chartspanel();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} 
	
	JPanel exitPanel, mPanel;
JLabel lbheader;
JButton btnchart1, btnchart11, btnchart3, btnbudget, btnincome;
}
