import java.awt.event.*;
import javax.swing.*;

public class LogoutPanel extends JPanel {
	private JButton btn;
	public LogoutPanel( ActionListener a ) {
		btn = new JButton("Click to logout");
		btn.addActionListener(a);
		this.add( btn );
	}
}
