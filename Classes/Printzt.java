package Classes;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JTextArea;
import Classes.Hhelper.StringUtils;

public class Printzt {
	static StringUtils hss = new StringUtils();
	private Vector vector_line;
	int num_lines;
	int line_Index;
	ArrayList<Integer> pageBreaks = new ArrayList<Integer>();
	private static final int TAB_SIZE = 5;
	
	public JTextArea text_area;		
	
	public void ztprintel(JTextArea txt_area) {
		 text_area = txt_area;
		try {
			PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
//             aset.add(MediaSizeName.ISO_A4);
			aset.add(OrientationRequested.PORTRAIT);
			aset.add(new JobName("My job", null));
			aset.add(new MediaPrintableArea(10, 10, 190, 275, MediaPrintableArea.MM));
			// 210 , 297 A4 mm
			PrinterJob prnJob = PrinterJob.getPrinterJob();
			PageFormat pf = prnJob.defaultPage();
			prnJob.print(aset);
			prnJob.setPrintable(new MyPrintable(text_area));
			if (!prnJob.printDialog())
				return;
			prnJob.print();

		} catch (PrinterException ex) {
			System.out.println("Printing error: " + ex.toString());
		}
	}

	class MyPrintable implements Printable {
		JTextArea str;

		public MyPrintable(JTextArea getStr) {
			str = getStr;
		}

		public int print(Graphics g, PageFormat page_format, int pageIndex) throws PrinterException {
			g.translate((int) page_format.getImageableX(), (int) page_format.getImageableY());
			int wpage = (int) page_format.getImageableWidth();
			int hpage = (int) page_format.getImageableHeight();
			g.setClip(0, 0, wpage, hpage);
			g.setFont(new Font("Monospaced", Font.BOLD, 6));
			FontMetrics fm = g.getFontMetrics();
			int fontDescent = g.getFontMetrics().getDescent();
			int lineHeight = fm.getHeight() + fontDescent;
			if (vector_line == null) {
				vector_line = getLines(fm, wpage);
				line_Index = 0;
				num_lines = vector_line.size();
				while (line_Index < num_lines) {
					String str = (String) vector_line.get(line_Index);
					System.out.println(str);
					char c = str.charAt(0);
					System.out.println(c);
					if (Character.compare(c, '*') == 0) {
						pageBreaks.add(line_Index);
					}
					line_Index++;
				}
			}

			if (pageIndex > pageBreaks.size()) {
				return NO_SUCH_PAGE;
			}

			int x = 10;
			int y = fm.getAscent();
			int start = (pageIndex == 0) ? 0 : pageBreaks.get(pageIndex - 1);
			int end = (pageIndex == pageBreaks.size()) ? vector_line.size() : pageBreaks.get(pageIndex);
			for (int line = start; line < end; line++) {
				String str = (String) vector_line.get(line);
				char c = str.charAt(0);
				if (Character.compare(c, '*') == 0) {
					g.drawString("", x, y);
				}else {
				g.drawString(str, x, y);
				}			
				y += lineHeight;
			}
			y += lineHeight;
			String pattern = hss.center("Page {0,number,integer}", 170);
			String footerText = MessageFormat.format(pattern, pageIndex + 1);
			g.drawString(footerText, x, y);
			return PAGE_EXISTS;
		}

		protected Vector getLines(FontMetrics fm, int wpage) {
			Vector vctr = new Vector();
			String txt = text_area.getText();
			String prevToken = "";
			StringTokenizer stokn = new StringTokenizer(txt, "\n\r", true);
			while (stokn.hasMoreTokens()) {
				String line = stokn.nextToken();
				if (line.equals("\r"))
					continue;
				// Stringtokenizer will be ignore empty lines
				if (line.equals("\n") && prevToken.equals("\n"))
					vctr.add("");
				prevToken = line;
				if (line.equals("\n"))
					continue;
				StringTokenizer stokn2 = new StringTokenizer(line, " \t", true);
				String line2 = "";
				while (stokn2.hasMoreTokens()) {
					String token = stokn2.nextToken();
					if (token.equals("\t")) {
						int num_space = TAB_SIZE - line2.length() % TAB_SIZE;
						token = "";
						for (int i = 0; i < num_space; i++)
							token += " ";
					}
					int line_len = fm.stringWidth(line2 + token);
					if (line_len > wpage && line2.length() > 0) {
						vctr.add(line2);
						line2 = token.trim();
						continue;
					}
					line2 += token;
				}
				vctr.add(line2);
			}
			return vctr;
		}
	}
}
