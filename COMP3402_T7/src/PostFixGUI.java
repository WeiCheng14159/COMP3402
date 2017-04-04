import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PostFixGUI {

	public static void main(String [] args) {
		new PostFixGUI().go();
	}
	private PostFix [] postfixes;
	public PostFixGUI() {
		postfixes = new PostFix[]{
				new PostFix(new String[]{ "1", "2", "+" }),
				new PostFix(new String[]{ "1", "2", "-" }),
				new PostFix(new String[]{ "1", "2", "*" }),
				new PostFix(new String[]{ "1", "2", "/" }),
				new PostFix(new String[]{ "6", "1", "3", "4", "/", "-", "/" })
			};
	}
	public void go() {
		
		JFrame frame = new JFrame("PostFix GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(getLogo(), BorderLayout.CENTER);
		frame.add(getButtons(), BorderLayout.PAGE_END);
		frame.pack();
		frame.setVisible(true);
	}
	private Component getLogo() {
		return new JLabel(new ImageIcon("logo.png"));
	}
	private Component getButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		for(PostFix p: postfixes) {
			JButton btn = new JButton(p.toString());
			btn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JOptionPane.showMessageDialog(panel,"Postfix " + p + " evaluates to "+p.evaluate());
				}
			});
			panel.add(btn);
		}
		return panel;
	}
}
