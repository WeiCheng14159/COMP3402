import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class InfoPage extends Page{
	
	private JLabel message;
	public InfoPage (ActionListener login_btn_listener) {
		frame = new JFrame( "Error" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setSize( new Dimension( Window_Width/2, Window_Height/2 ) );
		frame.setLocation(window_x, window_y);
		
		JButton login_btn = new JButton("Back to Login Page");
		login_btn.addActionListener(login_btn_listener);
		message = new JLabel("Fail to login/register");
		
		frame.add(BorderLayout.CENTER, message);
		frame.add(BorderLayout.SOUTH, login_btn);
	}
	
	public void err_msg(String s){
		message.setText(s);
	}
}
