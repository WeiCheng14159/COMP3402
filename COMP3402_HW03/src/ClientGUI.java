import java.awt.event.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

public class ClientGUI{

	//System state 
	private State sys_state = new State();
	private String user_name;
	private String password;
	//GUI
	private LoginPage login_page ;
	private InfoPage info_page;
	private GamePage game_page;
	private RegisterPage reg_page;
	private ArrayList<Page> pages;
	//RMI 
	private String host = "localhost";
	private PokerGameApp app;
	
	/**
	 * CLIENT_GUI CONSTRUTOR
	 */
	public ClientGUI() {
		init_gui();
		update_gui();
	}
	
	/**
	 * INiTIALIZE GUI
	 */
	private void init_gui(){
		// create login page
		login_page = new LoginPage	     ( new RequestButton("login", host, "login"),    new ChangeStateButton("register", "register"));
		login_page.show();
		//create info page 
		info_page = new InfoPage	     ( new ChangeStateButton("login", "login"));
		// create register frame
		reg_page = new RegisterPage	     ( new RequestButton("register", host, "register"), new ChangeStateButton("cancel", "login"));
				
		//create new game page 
		game_page = new GamePage         ( new AuthButton("logout" ,host , "login") );

		pages = new ArrayList<Page>();
		pages.add(login_page); 
		pages.add(info_page); pages.add(reg_page); pages.add(game_page);
	}
	
	/**
	 * UPDATE GUI MAIN THREAD
	 */
	private void update_gui(){
		String _prev_state = "";
		while( true ){
			if ( sys_state.get_state() != _prev_state){
				for(Page p : pages){
					p.hide();
				}

				if( sys_state.get_state() == "login"){
					login_page.show();
				}else if(sys_state.get_state() == "register"){
					reg_page.show();
				}else if(sys_state.get_state() == "profile" || sys_state.get_state() == "leader_board" || sys_state.get_state() == "play_game"){
					game_page.show();
				}else if(sys_state.get_state() == "info"){
					info_page.show();
				}else{
					
				}
				_prev_state = sys_state.get_state();
			}
			
//			for (Page p : pages){
//				p.set_position(100, 100);
//			}
			
			try{
				Thread.sleep(10);
			}catch(Exception e){
				System.out.println("Fail to update views cuz" + e.getMessage());
			}
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
				if ( button_command == "login" && target_state == "login"){
					sys_state.set_state("login");
				}else if(button_command == "register" && target_state == "register"){
					sys_state.set_state("register");
				}else if(button_command == "cancel" && target_state == "login"){
					sys_state.set_state("login");
				}
				else{
					try {
						throw new Exception("unknow combination of command & target state ");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
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
		        app = (PokerGameApp)registry.lookup("PokerGameApp");
			} catch(Exception ex) {
			    System.err.println("Failed accessing RMI: "+ex);
			    return;
			}
			// State transition logic
			if ( button_command.equals("logout") ){
				try {
			        if ( app.authenticate(button_command, user_name, password) == true ){
			        	sys_state.set_state("login");
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
				app = (PokerGameApp)registry.lookup("PokerGameApp");
				System.out.println( app.toString() );
			} catch(Exception ex) {
			    System.err.println("Failed accessing RMI:"+ex);
			    return;
			}
			
			if( button_command.equals("login") ){
				
				try {  
				    if ( app.authenticate( button_command, login_page.get_user_name(), login_page.get_password() ) == true){
				    	user_name =  login_page.get_user_name();
						password = login_page.get_password();
						game_page.set_user_name(user_name);	
						sys_state.set_state("profile");
				    }
				    else{
				    	login_page.reset();
				    	info_page.err_msg("UserName/Password Mismatch");
				    	sys_state.set_state("info");
				    	System.out.println("Fail to change state, button_command:"+button_command+ " user_name:"+user_name+" password:"+password);
				    }
				} catch(Exception ex) {
				    System.err.println("Failed accessing RMI rere: "+ex);
				    return;
				}
			}else if ( button_command.equals("register") ){

				if ( !reg_page.get_pw().equals(reg_page.get_pw_1()) ){
					reg_page.reset();
					info_page.err_msg("Password & Confirm Password Mismatch");
					sys_state.set_state("info");
					System.out.println("Password mismatch");
					return;
				}else if( reg_page.get_pw().length() < 5 ){
					reg_page.reset();
					info_page.err_msg("Password should be >= 5 characters ");
					sys_state.set_state("info");
					System.out.println("Password too short");
				}
				try {
				    if ( app.authenticate(button_command, reg_page.get_user_name(), reg_page.get_pw() ) == true){
				    	sys_state.set_state("profile");
				    	user_name = reg_page.get_user_name();
				    	password = reg_page.get_pw();
				    	game_page.set_user_name(user_name);
				    }else{
				    	info_page.err_msg("User Name Taken");
				    	sys_state.set_state("info");
				    	System.out.println("Fail to change state, button_command:"+button_command+ " user_name:"+user_name+" password:"+password);
				    }
				} catch(Exception ex) {
				    System.err.println("Failed accessing RMI: "+ex);
				    return;
				}
			}
		}
	}	 
	 
	 /**
	  * INFORM SERVER BUTTON 
	  */
//	 class InformServerButton implements MouseListener{
//
//		@Override
//		public void mouseClicked(MouseEvent e) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void mousePressed(MouseEvent e) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void mouseReleased(MouseEvent e) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void mouseEntered(MouseEvent e) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void mouseExited(MouseEvent e) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		 
//	 }
}