import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ProfilePage extends Page{
	private JTextArea content;
	
	public ProfilePage(ActionListener profile_btn_listener, ActionListener playgame_btn_listener, ActionListener leaderboard_btn_listener, ActionListener logout_btn_listener) {
		frame = new JFrame( "Profile" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setSize( new Dimension( Window_Width, Window_Height ) );
		frame.setLocation(window_x, window_y);
		//create swing component 
		JButton profile_btn = new JButton("User Profile");
		JButton playgame_btn = new JButton("Play Game");
		JButton leaderboard_btn = new JButton("Leader Board");
		JButton logout_btn = new JButton("Logout");
		//add listener
		profile_btn.addActionListener( profile_btn_listener );
		playgame_btn.addActionListener( playgame_btn_listener );
		leaderboard_btn.addActionListener( leaderboard_btn_listener );
		logout_btn.addActionListener( logout_btn_listener );
		//create panels 
		JPanel menu_bar = new JPanel();
		content = new JTextArea();
		//add them to frame/panel 
		menu_bar.add(profile_btn);
		menu_bar.add(playgame_btn);
		menu_bar.add(leaderboard_btn);
		menu_bar.add(logout_btn);
		menu_bar.setLayout( new BoxLayout(menu_bar, BoxLayout.X_AXIS) );
		//add to frame
		frame.add(BorderLayout.NORTH, menu_bar);
		frame.add(BorderLayout.CENTER, content);
	}
	public void set_content(String s){
		content.setText(s);
	}

}
