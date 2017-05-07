import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RegisterPage extends Page{
	private JTextField user_name;
	private JTextField password;
	private JTextField password_1;
	
	public RegisterPage(ActionListener register_btn_listener, ActionListener cancel_btn_listener) {
		frame = new JFrame( "Register" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setSize( new Dimension( Window_Width, Window_Height ) );
		frame.setLocation(window_x, window_y);
		//create swing component 
		JButton register_btn = new JButton("Register");
		JButton cancel_btn = new JButton("Cancel");
		user_name = new JTextField("User_Name", 10);
		password = new JTextField("Password", 10);
		password_1 = new JTextField("Confirm Password", 10);
		//add listener
		register_btn.addActionListener( register_btn_listener );
		cancel_btn.addActionListener( cancel_btn_listener );
		//create panels 
		JPanel input_panel = new JPanel();
		JPanel btn_panel = new JPanel();
		//add them to frame/panel 
		input_panel.add(user_name);
		input_panel.add(password);
		input_panel.add(password_1);
		input_panel.setLayout( new BoxLayout(input_panel, BoxLayout.Y_AXIS) );
		btn_panel.add(cancel_btn);
		btn_panel.add(register_btn);
		btn_panel.setLayout( new BoxLayout(btn_panel, BoxLayout.X_AXIS) );
		//add to frame
		frame.add(BorderLayout.NORTH, input_panel);
		frame.add(BorderLayout.SOUTH, btn_panel);
	}
	public String get_user_name(){
		return user_name.getText();
	}
	public String get_pw(){
		return password.getText();
	}
	public String get_pw_1(){
		return password_1.getText();
	}
	public void reset(){
		user_name.setText("user name");
		password.setText("password here");
		password_1.setText("confirm password here");
	}
}
