import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import Classes.Grlib;
import Classes.Hhelper;
import Databaseop.DatabaseHelper;
import Databaseop.Databaseop;

public class Settings extends JFrame {
	ResultSet rs;
	Connection con = null;
	Grlib gr = new Grlib();
	DatabaseHelper dh = new DatabaseHelper();
	Databaseop dd = new Databaseop();
	Hhelper hh = new Hhelper();

	Settings() {
		init();

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
		setSize(new java.awt.Dimension(500, 500));
		setLocationRelativeTo(null);
		setUndecorated(true);

		basePanel = new JPanel(null);
		basePanel.setBounds(2, 2, 494, 495);
		basePanel.setBackground(hh.zgreen1);
		basePanel.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green));
		add(basePanel);

		exitPanel = gr.makeexitpanel(this);
		exitPanel.setBounds(380, 10, 95, 35);
		exitPanel.setOpaque(true);
		exitPanel.setBackground(new Color(51, 255, 51));
		basePanel.add(exitPanel);

		lbheader = hh.fflabel("Settings");
		lbheader.setBounds(25, 10, 250, 35);
		basePanel.add(lbheader);

		mPanel = new JPanel(null);
		mPanel.setBounds(10, 60, 470, 415);
		mPanel.setBorder(hh.ztroundborder(Color.yellow));
		mPanel.setOpaque(true);
		mPanel.setBackground(hh.zgreen1);
		basePanel.add(mPanel);

		lbheader1 = hh.fflabel("Delete tables");
		lbheader1.setBounds(25, 40, 200, 35);
		mPanel.add(lbheader1);

		c1 = new JCheckBox("Transaction table");
		c1.setIcon(new SimpleCheckboxStyle(20));
		c1.setBounds(50, 100, 230, 30);
	//	c1.setPreferredSize(new Dimension(200, 30));
        c1.setHorizontalTextPosition(SwingConstants.RIGHT);		
		c1.setMargin(new Insets(0, 30, 0, 0));
		c1.setFont(new Font("Tahoma", 1, 20));
		c1.setForeground(Color.yellow);
    	c1.setBorderPainted(true);
	    c1.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));	
	//	c1.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
	    c1.setOpaque(false);
		mPanel.add(c1);

		c2 = new JCheckBox("      Budget Table");
		c2.setIcon(new SimpleCheckboxStyle(20));
		c2.setBounds(50, 140, 230, 30);
	//	c2.setHorizontalTextPosition(SwingConstants.LEFT);
		 c2.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));	
		c2.setMargin(new Insets(0, 30, 0, 0));
		c2.setFont(new Font("Tahoma", 1, 20));
		c2.setForeground(Color.yellow);
		c2.setBorderPainted(true);
		c2.setOpaque(false);
		mPanel.add(c2);

		btndelete = gr.sbcs("Delete tables");
		btndelete.setBounds(50, 200, 180, 40);
		btndelete.setForeground(Color.black);
		btndelete.setBackground(Color.yellow);
		mPanel.add(btndelete);
		btndelete.addActionListener(e -> data_tables());

		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("images/grad2.png"));
		Image i2 = i1.getImage().getScaledInstance(1300, 650, Image.SCALE_DEFAULT);
		ImageIcon i3 = new ImageIcon(i2);
		JLabel imagex = new JLabel(i3);
		imagex.setBounds(2, 2, 486, 488);
		basePanel.add(imagex);

		setVisible(true);

	}

	private void data_tables() {
		if (c1.isSelected()) {
			String sql = "delete from transactions";
			int flag = dh.Insupdel(sql);
			if (flag > 0) {
				hh.ztmessage("Success", "Message");
			}
		}
		if (c2.isSelected()) {
			String sql = "delete from budgets";
			int flag = dh.Insupdel(sql);
			if (flag > 0) {
				hh.ztmessage("Success", "Message");
			}
		}
	}
	
	class SimpleCheckboxStyle implements Icon {

	    int dim = 10;
	    int fontsize = 20;

	    public SimpleCheckboxStyle (int dimension){
	        this.dim = dimension;
	    }

	    protected int getDimension() {
	        return dim;
	    }

	    public void paintIcon(Component component, Graphics g, int x, int y) {
	        ButtonModel buttonModel = ((AbstractButton) component).getModel();

	        int y_offset = (int) (component.getSize().getHeight() / 2) - (int) (getDimension() / 2);
	        int x_offset = 2;

	        if (buttonModel.isRollover()) {
	            g.setColor(new Color(0, 60, 120));
	        } else if (buttonModel.isRollover()) {
	            g.setColor(Color.BLACK);
	        } else {
	            g.setColor(Color.DARK_GRAY);
	        }
	        g.fillRect(x_offset, y_offset, fontsize, fontsize);
	        if (buttonModel.isPressed()) {
	            g.setColor(Color.GRAY);
	        } else if (buttonModel.isRollover()) {
	            g.setColor(new Color(240, 240, 250));
	        } else {
	            g.setColor(Color.WHITE);
	        }
	        g.fillRect(1 + x_offset, y_offset + 1, fontsize - 2, fontsize - 2);
	        if (buttonModel.isSelected()) {
	            int r_x = 1;
	            g.setColor(Color.GRAY);
	            g.fillRect(x_offset + r_x + 3, y_offset + 3 + r_x, fontsize - (7 + r_x), fontsize - (7 + r_x));
	        }
	    }

	    public int getIconWidth() {
	        return getDimension();
	    }

	    public int getIconHeight() {
	        return getDimension();
	    }
	}


	public static void main(String args[]) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new Settings();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	JLabel lbheader, lbheader1;
	JPanel exitPanel, basePanel, mPanel;
	JCheckBox c1, c2;
	JButton btndelete;
}
