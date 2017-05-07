import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LeaderBoardPanel extends JPanel{
	private JTextArea content;
	
	public LeaderBoardPanel() {
		content = new JTextArea();
		this.add(content);
	}
	public void set_content(String s){
		content.setText(s);
	}

}
