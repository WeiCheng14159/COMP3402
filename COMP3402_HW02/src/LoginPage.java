import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class LoginPage extends Page{

	private JTextField user_name;
	private JTextField password;

	public LoginPage(ActionListener login_btn_listener, ActionListener register_btn_listener) {
		frame = new JFrame( "Login" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setSize( new Dimension( Window_Width, Window_Height ) );
		frame.setLocation(window_x, window_y);
		//create swing component 
		JButton register_btn = new JButton("Register");
		JButton login_btn = new JButton("Login");
		user_name = new JTextField("User_name", 10);
		password = new JTextField("Password", 10);
		//add listener
		login_btn.addActionListener( login_btn_listener );
		register_btn.addActionListener( register_btn_listener );
		//create panels 
		JPanel input_panel = new JPanel();
		JPanel btn_panel = new JPanel();
		//add them to frame/panel 
		input_panel.add(user_name);
		input_panel.add(password);
		input_panel.setLayout( new BoxLayout(input_panel, BoxLayout.PAGE_AXIS) );
		
		btn_panel.add(login_btn);
		btn_panel.add(register_btn);
		btn_panel.setLayout( new BoxLayout(btn_panel, BoxLayout.X_AXIS) );
		//add to frame
		frame.add(BorderLayout.NORTH, input_panel);
		frame.add(BorderLayout.SOUTH, btn_panel);
	}
	
	public String get_user_name(){
		// Do user name checking
		
		return user_name.getText();
	}
	public String get_password(){
		// Do user password checking
		return password.getText();
	}
	public void reset(){
		user_name.setText("");
		password.setText("");
	}
}
