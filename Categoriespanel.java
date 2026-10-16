import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import Classes.Calhelper;
import Classes.Grlib;
import Classes.Hhelper;

public class Categoriespanel extends JFrame{
	Grlib gr = new Grlib();	
	Hhelper hh = new Hhelper();	
	Calhelper cc = new Calhelper();
	JFrame myframe = this;

	Categoriespanel(){
		init();
	}
	private void init() {
		getContentPane().setBackground(hh.zgreen1);
		setSize(340, 380);
		setLayout(null);
		setLocationRelativeTo(null);	
		setUndecorated(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				dispose();
			}
		});
		exitPanel = gr.makeexitpanel(this);
		exitPanel.setBounds(235, 15, 95, 35);
		exitPanel.setOpaque(true);
		exitPanel.setBackground(hh.zgreen1);		
		add(exitPanel);
		
		mPanel = new JPanel(null);
    	mPanel.setBounds(2, 2, 334, 375);
		mPanel.setBackground(new Color(178, 216, 216));
	//	mPanel.setBorder(hh.ztroundborder(Color.yellow));
		mPanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green));
		mPanel.setBackground(hh.zgreen1);
		add(mPanel);
		
		lbheader = new JLabel("Categories panel");
		lbheader.setForeground(Color.yellow);
		lbheader.setFont(new Font("tahoma", Font.BOLD, 24));
		lbheader.setBounds(20, 10, 300, 35);
		mPanel.add(lbheader);
		
		btnincomecat = gr.sbcs("Income categories");
		btnincomecat.setBounds(50, 70, 240, 60);
		btnincomecat.setBackground(cc.psarga);
		mPanel.add(btnincomecat);
		btnincomecat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Categories pa =  new Categories("I");	
			    pa.setVisible(true);
			}
	});
		
		btnincomescat = gr.sbcs("Income subcategories");
		btnincomescat.setBounds(50, 140, 240, 60);
		btnincomescat.setBackground(cc.psarga);
		mPanel.add(btnincomescat);
		btnincomescat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Subcategories pa = new Subcategories("I");	
		        pa.setVisible(true);
			}
	});
		
		btnexpensecat = gr.sbcs("Expense categories");
		btnexpensecat.setBounds(50, 210, 240, 60);
		btnexpensecat.setBackground(cc.psarga);
		mPanel.add(btnexpensecat);
		btnexpensecat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				Categories pa =  new Categories("E");	
		       pa.setVisible(true);
			}
	});	
		
		btnexpensescat = gr.sbcs("Expense subcategories");
		btnexpensescat.setBounds(50, 280, 240, 60);
		btnexpensescat.setBackground(cc.psarga);
		mPanel.add(btnexpensescat);
		btnexpensescat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
			Subcategories pa =	new Subcategories("E");
			pa.setVisible(true);
			}
	});
		
		

		

		setVisible(true);
	}
	
	
	
	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Categoriespanel();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JLabel lbheader;
	JPanel mPanel, exitPanel;
	JButton btnincomecat, btnexpensecat, btnincomescat, btnexpensescat;

}
