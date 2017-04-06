import java.awt.event.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import javax.swing.*;
import java.util.List; 

public class ClientGUI{

	//System state 
	private State sys_state = new State();
	private String user_name;
	private String password;
	//GUI
	private ArrayList<Page> pages = new ArrayList<Page>();
	private ArrayList <JFrame> views = new ArrayList<JFrame>();
	//RMI 
	private String host = "localhost";
	private Poker app;
	
	/**
	 * CLIENT_GUI CONSTRUTOR
	 */
	public ClientGUI() {
		init_gui();
		update_gui();
	}

	/**
	 * UPDATE GUI MAIN THREAD
	 */
	private void update_gui(){
		String _prev_state = "";
		while( true ){
			if ( sys_state.get_state() != _prev_state){
				List<String> _state_list = Arrays.asList( sys_state.all_states() );
				int _idx = _state_list.indexOf(sys_state.get_state());
				for(JFrame f : views){
					if (f != views.get(_idx))
						f.setVisible(false);
				}
				views.get(_idx).setVisible(true);
				_prev_state = sys_state.get_state();
			}
			
			for (Page p : pages){
				p.set_position(100, 100);
			}
			
			try{
				Thread.sleep(10);
			}catch(Exception e){
				System.out.println("Fail to update views cuz" + e.getMessage());
			}
		}
	}
	
	/**
	 * INiTIALIZE GUI
	 */
	private void init_gui(){
		// create login frame
		pages.add(new LoginPage	     ( new RequestButton("login", host, "profile"),    new ChangeStateButton("register", "register")));
		// create register frame
		pages.add(new RegisterPage	 ( new RequestButton("register", host, "profile"), new ChangeStateButton("cancel", "login")));
		// create profile frame
		pages.add(new ProfilePage	 ( 	new RequestButton("profile", host, "profile"), new RequestButton("play_game", host, "play_game"), new RequestButton("leader_board", host, "leader_board"), new AuthButton("logout", host, "login") ));
		// create leader_board frame 
		pages.add(new LeaderBoardPage( new RequestButton("profile", host, "profile"),  new RequestButton("play_game", host, "play_game"), new RequestButton("leader_board", host, "leader_board"), new AuthButton("logout", host, "login") ));
		//create play_game frame 
		pages.add(new PlayGamePage	 ( new RequestButton("profile", host, "profile"),  new RequestButton("play_game", host, "play_game"), new RequestButton("leader_board", host, "leader_board"), new AuthButton("logout", host, "login") ));
		// create info frame 
		pages.add(new InfoPage	     ( new ChangeStateButton("login", "login")) );
		
		//add views 
		for(Page p : pages){
			views.add(p.get_frame());
		}
	}
	
	/**
	 * CHANGE_STATE_BUTTON CLASS: ONLY CHANGE THE STATE OF A CLIENT 
	 * @author chengwei
	 *
	 */
	 class ChangeStateButton implements ActionListener{
		protected String target_state;
		protected String button_command;
		
		public ChangeStateButton(String _command, String _target_state){
			target_state = _target_state;
			button_command = _command;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			sys_state.set_state(target_state);
		}
	 }
	 
	 /**
	  * AUTHORIZATION BUTTON CLASS: A CLIENT CONTACTS SERVER FOR AUTHORIZATION
	  * @author chengwei
	  *
	  */
	 class AuthButton extends ChangeStateButton implements ActionListener{
		private String host_addr;
		
		public AuthButton (String _command, String _host_addr, String _target_state) {
			super(_command, _target_state);
			host_addr = _host_addr;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// Get remote object 
			try {
		        Registry registry = LocateRegistry.getRegistry( host_addr );
		        app = (Poker)registry.lookup("Poker");
			} catch(Exception ex) {
			    System.err.println("Failed accessing RMI: "+ex);
			    return;
			}
			// State transition logic
			if ( button_command.equals("logout") ){
				
				try {
			        if ( app.authenticate(button_command, user_name, password) == true ){
			        	LoginPage _login_page = (LoginPage)pages.get(0);
						_login_page.reset();
				    	sys_state.change_state( target_state );
				    }else{
				    	System.out.println("Fail to change state, button_command: "+button_command+ " user_name: "+user_name+" password: "+password);
				    }
				} catch(Exception ex) {
				    System.err.println("Failed accessing RMI: "+ex);
				    return;
				}
			}else{
				System.out.println( button_command+ " is invalid Send button");
				return;
			}
		}
	 }
	 
	/**
	 * REQUEST BUTTON: SEND REQUEST TO SERVER AND GET A JPANEL RESPONSE
	 * @author chengwei
	 *
	 */
	 class RequestButton extends ChangeStateButton implements ActionListener{
		private String host_addr;
		
		public RequestButton (String _command, String _host_addr, String _target_state) {
			super(_command, _target_state);
			host_addr = _host_addr;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			try {
				Registry registry = LocateRegistry.getRegistry( host_addr );
				app = (Poker)registry.lookup("Poker");
			} catch(Exception ex) {
			    System.err.println("Failed accessing RMI: "+ex);
			    return;
			}
			
			if( button_command.equals("login") ){
				LoginPage _login_page = (LoginPage)pages.get(0);
				InfoPage _err_page = (InfoPage)pages.get(5);
		    	
				user_name = _login_page.get_user_name();
				password = _login_page.get_password();
				
				
				try {  
				    if ( app.authenticate( button_command, user_name, password ) == true){
				    	ProfilePage _profile_page = (ProfilePage)pages.get(2);
				    	_profile_page.set_content( app.request(target_state, user_name, password ) );
				    	sys_state.change_state( target_state );
				    }else{
				    	_login_page.reset();
				    	_err_page.err_msg("UserName/Password Mismatch");
				    	sys_state.set_state("info");
				    	System.out.println("Fail to change state, button_command:"+button_command+ " user_name:"+user_name+" password:"+password);
				    }
				} catch(Exception ex) {
				    System.err.println("Failed accessing RMI: "+ex);
				    return;
				}
			}else if ( button_command.equals("register") ){
				RegisterPage _reg_page = (RegisterPage)pages.get(1);
				InfoPage _err_page = (InfoPage)pages.get(5);
				ProfilePage _profile_page = (ProfilePage)pages.get(2);
				
				user_name = _reg_page.get_user_name();
				password = _reg_page.get_pw();
				String _confirm_password = _reg_page.get_pw_1();
				
				if ( !password.equals(_confirm_password) ){
					_err_page.err_msg("Password & Confirm Password Mismatch");
					sys_state.set_state("info");
					System.out.println("Password mismatch");
					return;
				}else if( password.length() < 5 ){
					_err_page.err_msg("Password should be >= 5 characters ");
					sys_state.set_state("info");
					System.out.println("Password too short");
				}
				try {
				    if ( app.authenticate(button_command, user_name, password ) == true){
				    	_profile_page.set_content( app.request(target_state, user_name, password) );
				    	sys_state.change_state( target_state );
				    }else{
				    	_reg_page.reset();
				    	_err_page.err_msg("User Name Taken");
				    	sys_state.set_state("info");
				    	System.out.println("Fail to change state, button_command:"+button_command+ " user_name:"+user_name+" password:"+password);
				    }
				} catch(Exception ex) {
				    System.err.println("Failed accessing RMI: "+ex);
				    return;
				}
			}else if ( button_command.equals("profile") ){
				ProfilePage _profile_page = (ProfilePage)pages.get(2);
				
				try {
					if ( app.authenticate( button_command, user_name, password ) == true){
						_profile_page.set_content( app.request(button_command, user_name, password ) );
				    	sys_state.change_state( target_state );
				    }else{
				    	System.out.println("Fail to change state, button_command:"+button_command+ " user_name:"+user_name+" password:"+password);
				    }
				} catch(Exception ex) {
				    System.err.println("Failed accessing RMI: "+ex);
				    return;
				}
			}else if ( button_command.equals("leader_board")){
				LeaderBoardPage _leader_page = (LeaderBoardPage)pages.get(3);
				
				try {
					if ( app.authenticate( button_command, user_name, password ) == true){
						_leader_page.set_content( app.request(button_command, user_name, password ) );
				    	sys_state.change_state( target_state );
				    }else{
				    	System.out.println("Fail to change state, button_command:"+button_command+ " user_name:"+user_name+" password:"+password);
				    }
				} catch(Exception ex) {
				    System.err.println("Failed accessing RMI: "+ex);
				    return;
				}
			}else if ( button_command.equals("play_game")){
				PlayGamePage _play_page = (PlayGamePage)pages.get(4);
				
				try {
					if ( app.authenticate( button_command, user_name, password ) == true){
						_play_page.set_content( app.request(button_command, user_name, password ) );
				    	sys_state.change_state( target_state );
				    }else{
				    	System.out.println("Fail to change state, button_command:"+button_command+ " user_name:"+user_name+" password:"+password);
				    }
				} catch(Exception ex) {
				    System.err.println("Failed accessing RMI: "+ex);
				    return;
				}
			}else{
				System.out.println( button_command+ " is invalid Request button");
				return;
			}
		}
	}	 
}