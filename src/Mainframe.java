import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Classes.Grlib;
import Classes.Hhelper;

public class Mainframe extends JFrame {
	Grlib gr = new Grlib();
	Hhelper hh = new Hhelper();
	 int newWidth = 0;

	Mainframe() {
		init();
	}

	private void init() {
		setLayout(null);
		setMinimumSize(new Dimension(100, 100));
		setSize(new java.awt.Dimension(1307, 686));
		setLocationRelativeTo(null);
		setUndecorated(true);
		lbheader = hh.fflabel("MY MONEY");
		lbheader.setFont(new java.awt.Font("Lucida Handwriting", 1, 50));
		lbheader.setBounds(100, 305, 300, 60);	
		add(lbheader);

		btntran = cbutton("Transactions");
		btntran.setBounds(535, 170, 230, 60);	
		add(btntran);
		btntran.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				new Transactions();
			}
		});

		btnbud = cbutton("Budgets");
		btnbud.setBounds(535, 240, 230, 60);	
		add(btnbud);
		btnbud.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				new Budgets();
			}
		});
		
		btncategories = cbutton("Categories");
		btncategories.setBounds(535, 310, 230, 60);	
		add(btncategories);
		btncategories.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				new Categoriespanel();
			}
		});
		
		btncharts = cbutton("Charts");
		btncharts.setBounds(535, 380, 230, 60);	
		add(btncharts);
		btncharts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
			new Chartspanel();
			}
		});
	
		btnsetting = cbutton("Settings");
		btnsetting.setBounds(535, 450, 230, 60);
		add(btnsetting);
		btnsetting.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				new Settings();
			}
		});

		btnexit = cbutton("Exit");
		btnexit.setBounds(535, 520, 230, 60);
		add(btnexit);
		btnexit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				dispose();
			}
		});

		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/base4.png"));
		Image i2 = i1.getImage().getScaledInstance(1307, 686, Image.SCALE_DEFAULT);
		ImageIcon i3 = new ImageIcon(i2);
		JLabel imagex = new JLabel(i3);
		imagex.setBounds(0, 0, 1307, 686);
		add(imagex);
		 final int labelWidth = lbheader.getWidth();	
		 lbheader.setSize(0,60);
		 setVisible(true);
		 
		 Timer timer = new Timer(30, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                newWidth = newWidth + 30;
	                lbheader.setSize(newWidth, 60);	   
	                if (newWidth >= labelWidth) {
	                    ((Timer) e.getSource()).stop();          
	                      System.out.print("kill");
	                }
	            }
	        });
	         timer.start();
	    }	

	public JButton cbutton(String string) {
		JButton bbutton = new JButton(string);
		bbutton.setFont(new java.awt.Font("Lucida Handwriting", 1, 24));
		bbutton.setBackground(new Color(159, 255, 155));
		bbutton.setBorder(BorderFactory.createMatteBorder(1, 1, 4, 4, Color.BLACK));
		bbutton.setForeground(hh.fekete);
		bbutton.setPreferredSize(new Dimension(100, 30));
		bbutton.setFocusable(false);
		bbutton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		return bbutton;
	}

	public static void main(String args[]) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mainframe ts = new Mainframe();
					//ts.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JButton btnexit, btntran, btnbud, btncharts, btnsetting, btncategories;
    JLabel  lbheader;
}
