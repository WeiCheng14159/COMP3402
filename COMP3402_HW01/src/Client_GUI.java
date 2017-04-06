import java.awt.*;
import java.awt.event.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import javax.swing.*;
import java.util.List; 

public class Client_GUI{

	//System state 
	private State sys_state;
	private ArrayList <JFrame> views = new ArrayList<JFrame>();
	private int MouseX, MouseY;
	
	//GUI
	private int Window_Width = 500;
	private int Window_Height = 400;
	private JTextArea user_name;
	private JTextArea password;
	JPanel content; 
	//RMI 
	private String host = "localhost";
	private Communication comm;
	public Client_GUI() {
		init();
		init_gui();
		update_gui();
	}

	private void update_gui(){
		String prev_state = "";
		while( true ){
			if ( sys_state.get_state() != prev_state){
				List<String> tmp_list = Arrays.asList( sys_state.all_states() );
				int idx = tmp_list.indexOf(sys_state.get_state());
				for(JFrame f : views){
					if (f != views.get(idx))
						f.setVisible(false);
				}
				views.get(idx).setVisible(true);
				prev_state = sys_state.get_state();
			}
			try{
				Thread.sleep(100);
			}catch(Exception e){
				System.out.println("Fail to update views cuz" + e.getMessage());
			}
		}
	}
	
	public void init(){
		sys_state = new State();
	}

	public void init_gui(){
	// create login frame
		JFrame frame = new JFrame( "Login" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setSize( new Dimension( Window_Width, Window_Height ) );
		//create swing component 
		JButton register_btn = new JButton("Register");
		JButton login_btn = new JButton("Login");
		user_name = new JTextArea(1, 10);
		password = new JTextArea(1, 10);
		//add listener
		register_btn.addActionListener( new NormalButton("register", "register") );
		login_btn.addActionListener( new SendButton("login", host, "profile") );
		//create panels 
		JPanel input_panel = new JPanel();
		JPanel btn_panel = new JPanel();
		//add them to frame/panel 
		input_panel.add(user_name);
		input_panel.add(password);
		input_panel.setLayout( new BoxLayout(input_panel, BoxLayout.Y_AXIS) );
		btn_panel.add(login_btn);
		btn_panel.add(register_btn);
		btn_panel.setLayout( new BoxLayout(btn_panel, BoxLayout.X_AXIS) );
		//add to frame
		frame.add(BorderLayout.NORTH, input_panel);
		frame.add(BorderLayout.SOUTH, btn_panel);
		views.add(frame);
		
	// create register frame
		frame = new JFrame( "Register" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setSize( new Dimension( Window_Width, Window_Height ) );
		//create swing component 
		register_btn = new JButton("Register");
		JButton cancel_btn = new JButton("Cancel");
		user_name = new JTextArea(1, 10);
		password = new JTextArea(1, 10);
		JTextArea password_1 = new JTextArea(1, 10);
		//add listener
		register_btn.addActionListener( new SendButton("register", host, "profile") );
		cancel_btn.addActionListener( new NormalButton("cancel", "login") );
		//create panels 
		input_panel = new JPanel();
		btn_panel = new JPanel();
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
		views.add(frame);
		
// create profile frame
		frame = new JFrame( "Profile" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setSize( new Dimension( Window_Width, Window_Height ) );
		//create swing component 
		JButton profile_btn = new JButton("User Profile");
		JButton playgame_btn = new JButton("Play Game");
		JButton leader_board_btn = new JButton("Leader Board");
		JButton logout_btn = new JButton("Logout");
		//add listener
		profile_btn.addActionListener( new SendButton("profile", host, "profile") );
		playgame_btn.addActionListener( new SendButton("play_game", host, "play_game") );
		leader_board_btn.addActionListener( new SendButton("leader_board", host, "leader_board") );
		logout_btn.addActionListener( new SendButton("logout", host, "logout") );
		//create panels 
		JPanel menu_bar = new JPanel();
		content = new JPanel();
		//add them to frame/panel 
		menu_bar.add(profile_btn);
		menu_bar.add(playgame_btn);
		menu_bar.add(leader_board_btn);
		menu_bar.add(logout_btn);
		menu_bar.setLayout( new BoxLayout(menu_bar, BoxLayout.X_AXIS) );
		//add to frame
		frame.add(BorderLayout.NORTH, menu_bar);
		frame.add(BorderLayout.SOUTH, content);
		views.add(frame);
// create leader_board frame 
		frame = new JFrame( "Leader Board" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setSize( new Dimension( Window_Width, Window_Height ) );
		//create swing component 
		profile_btn = new JButton("User Profile");
		playgame_btn = new JButton("Play Game");
		leader_board_btn = new JButton("Leader Board");
		logout_btn = new JButton("Logout");
		//add listener
		profile_btn.addActionListener( new SendButton("profile", host, "profile") );
		playgame_btn.addActionListener( new SendButton("play_game", host, "play_game") );
		leader_board_btn.addActionListener( new SendButton("leader_board", host, "leader_board") );
		logout_btn.addActionListener( new SendButton("logout", host, "logout") );
		//create panels 
		menu_bar = new JPanel();
		content = new JPanel();
		//add them to frame/panel 
		menu_bar.add(profile_btn);
		menu_bar.add(playgame_btn);
		menu_bar.add(leader_board_btn);
		menu_bar.add(logout_btn);
		menu_bar.setLayout( new BoxLayout(menu_bar, BoxLayout.X_AXIS) );
		//add to frame
		frame.add(BorderLayout.NORTH, menu_bar);
		frame.add(BorderLayout.SOUTH, content);
		views.add(frame);
//create play_game frame 
		frame = new JFrame( "Play Game" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setSize( new Dimension( Window_Width, Window_Height ) );
		//create swing component 
		profile_btn = new JButton("User Profile");
		playgame_btn = new JButton("Play Game");
		leader_board_btn = new JButton("Leader Board");
		logout_btn = new JButton("Logout");
		//add listener
		profile_btn.addActionListener( new SendButton("profile", host, "profile") );
		playgame_btn.addActionListener( new SendButton("play_game", host, "play_game") );
		leader_board_btn.addActionListener( new SendButton("leader_board", host, "leader_board") );
		logout_btn.addActionListener( new SendButton("logout", host, "logout") );
		//create panels 
		menu_bar = new JPanel();
		content = new JPanel();
		//add them to frame/panel 
		menu_bar.add(profile_btn);
		menu_bar.add(playgame_btn);
		menu_bar.add(leader_board_btn);
		menu_bar.add(logout_btn);
		menu_bar.setLayout( new BoxLayout(menu_bar, BoxLayout.X_AXIS) );
		//add to frame
		frame.add(BorderLayout.NORTH, menu_bar);
		frame.add(BorderLayout.SOUTH, content);
		views.add(frame);
// create error frame 
		frame = new JFrame( "Error" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setSize( new Dimension( Window_Width/2, Window_Height/2 ) );
		JLabel message = new JLabel("Fail to login/register");
		frame.add(BorderLayout.CENTER, message);
		views.add(frame);
	}
	
	/*
	 * Normal button inner class 
	 */
	 class NormalButton implements ActionListener{
		protected String dest_state;
		protected String command;
		public NormalButton(String cmd, String dest){
			dest_state = dest;
			command = cmd;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			sys_state.set_state(dest_state);
		}
	 }
	/*
	 * Send button inner class
	 */
	 class SendButton extends NormalButton implements ActionListener{
		private String host_addr;
		public SendButton (String cmd, String host, String d) {
			super(cmd, d);
			host_addr = host;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
		        Registry registry = LocateRegistry.getRegistry( host_addr );
			    comm = (Communication)registry.lookup("Poker");
			    if ( comm.checking(command, user_name.getText(), password.getText() ) == true){
			    	System.out.println("Changing state");
			    	sys_state.change_state( dest_state );
			    }else{
			    	System.out.println("Fail to change state");
			    }
			} catch(Exception ex) {
			    System.err.println("Failed accessing RMI: "+ex);
			    return;
			}
		}
	 }
	/*
	 * request button inner class
	 */
	 class RequestButton extends NormalButton implements ActionListener{
		private String host_addr;
		public RequestButton (String cmd, String host, String d) {
			super(cmd, d);
			host_addr = host;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				Registry registry = LocateRegistry.getRegistry( host_addr );
				comm = (Communication)registry.lookup("Poker");
			    if ( comm.checking( command, user_name.getText(), password.getText() ) == true){
			    	content = comm.request(command, user_name.getText(), password.getText() );
			    	System.out.println("Changing state");
			    	sys_state.change_state( dest_state );
			    }else{
			    	System.out.println("Fail to change state");
			    }
			} catch(Exception ex) {
			    System.err.println("Failed accessing RMI: "+ex);
			    return;
			}
		}
	}	 
}