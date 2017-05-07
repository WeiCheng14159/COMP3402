import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.util.*;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.swing.*;

public class GameServer extends UnicastRemoteObject implements PokerGameApp
{
	private FileWriter db_info_writer;
	private FileReader db_info_reader;
	private BufferedReader info_reader;
	private BufferedWriter info_writer;
	
	private FileWriter db_online_writer;
	private FileReader db_online_reader;
	private BufferedReader online_reader;
	private BufferedWriter online_writer;
	
	private ArrayList<String> user_info_list = new ArrayList<String>(); 
	private ArrayList<String> user_password_list = new ArrayList<String>();
	private ArrayList<String> online_user_list = new ArrayList<String>();
	
	/**
	 * PUBLIC GAMESERVER CONSTRUCTOR
	 * @throws RemoteException
	 */
	public GameServer() throws RemoteException {
		init_db();
	}

	/**
	 * SERVER MAIN METHOD
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			GameServer app = new GameServer();
			System.setSecurityManager(new SecurityManager());
			Naming.rebind("PokerGameApp", app);
			System.out.println("Service registered with name PokerGameApp");
		} catch(Exception e) {
			System.err.println("Exception thrown: "+e);
		}
	}
	
	/**
	 * INITIALIZE DATA BASE
	 */
	private void init_db(){
		
		try{
			db_info_reader = new FileReader("UserInfo.txt");
			info_reader = new BufferedReader(db_info_reader);
		}catch(Exception ex) {
		    System.err.println("Failed access UserInfo.txt "+ex);
		    return;
		}
		try{
			db_online_reader = new FileReader("OnlineUser.txt");
			online_reader = new BufferedReader(db_online_reader);		
		}catch(Exception ex) {
		    System.err.println("Failed access OnlineUser.txt "+ex);
		    return;
		}
		
		String line = null;
		try{
			System.out.println("Reading UserInfo.txt ... ");
			while ((line = info_reader.readLine()) != null) {          
				String [] info = line.split(":");
				user_info_list.add(info[0]);
				user_password_list.add(info[1]);
				System.out.println("user name: " + info[0] + ", password: " + info[1]);
			}
			info_reader.close();
		}catch(Exception ex) {
		    System.err.println("Failed access UserInfo.txt "+ex);
		    return;
		}
		line = null;
		try{
			System.out.println("Reading OnlineUser.txt ... ");
			while ((line = online_reader.readLine()) != null) {
				online_user_list.add(line);
				System.out.println("Online user: " + line); 
			}
			online_reader.close();
		}catch(Exception ex) {
		    System.err.println("Failed access OnlineUser.txt "+ex);
		    return;
		}
	}
	
	/**
	 * LOGIN METHOD: HANDLE CLIENT LOGIN
	 * @param _user_name
	 * @param _user_password
	 * @return
	 */
	private boolean login(String _user_name, String _user_password){
		
		int idx = user_info_list.indexOf(_user_name); 
		
		if ( idx != -1 && user_password_list.get(idx).equals(_user_password)){
			System.out.println("User: " + _user_name + " log in with password: " + _user_password );
			
			if ( online_user_list.indexOf(_user_name) == -1){
				// add this user to online list
				online_user_list.add(_user_name);
				//write to database
				try{
					db_online_writer = new FileWriter("OnlineUser.txt");
					online_writer = new BufferedWriter(db_online_writer);
					for (String s : online_user_list){
						online_writer.write(s+"\n");
					}
					online_writer.flush();
					online_writer.close();
				}catch(Exception ex) {
				    System.err.println("Failed to write OnlineUser.txt "+ex);
				    return false;
				}
			}
			return true;
		}else if ( idx == -1 ){
			System.out.println("User doesn't exist");
			return false;
		}else{
			System.out.println("password is wrong");
			return false;
		}
	}
	
	/**
	 * REGISTER METHOD: HANDLE NEW REGISTER CLIENT 
	 * @param _user_name
	 * @param _password
	 * @return
	 */
	public boolean register(String _user_name, String _password){
		
		if ( !user_info_list.contains(_user_name) ){
			System.out.println("Adding new User: " + _user_name + " with password: " + _password );
			user_info_list.add(_user_name);
			user_password_list.add(_password);
			online_user_list.add(_user_name);
			//update user info db
			try{
				db_info_writer = new FileWriter("UserInfo.txt");
				info_writer = new BufferedWriter(db_info_writer);
				for (int idx = 0; idx < user_info_list.size(); idx++){
					info_writer.write(user_info_list.get(idx)+":"+user_password_list.get(idx)+"\n");
				}
				info_writer.flush();
				info_writer.close();
			}catch(Exception ex) {
			    System.err.println("Failed to write UserInfo.txt "+ex);
			    return false;
			}
			//update online user db
			try{
				db_online_writer = new FileWriter("OnlineUser.txt");
				online_writer = new BufferedWriter(db_online_writer);
				for (String s : online_user_list){
					online_writer.write(s+"\n");
				}
				online_writer.flush();
				online_writer.close();
			}catch(Exception ex) {
			    System.err.println("Failed to write OnlineUser.txt "+ex);
			    return false;
			}
			return true;
		}else{
			System.out.println("User" + _user_name + "exists");
			return false;
		}
	}
	
	/**
	 * PROFILE METHOD: PROVIDE CLIENT PROFILE
	 * @param u_name
	 * @param pw
	 * @return
	 */
	public JPanel profile(String u_name, String pw){
		return new JPanel();
	}
	
	/**
	 * PLAY_GAME METHOD: PROVIDE CLIENT GAME PANEL
	 * @param u_name
	 * @param pw
	 * @return
	 */
	public JPanel play_game(String u_name, String pw){
		return new JPanel();
	}
	
	/**
	 * LEADER_BOARD METHOD: PROVIDE CLIENT LEADER BOARD
	 * @param u_name
	 * @param pw
	 * @return
	 */
	public JPanel leader_board(String u_name, String pw){
		return new JPanel();
	}
	
	/**
	 * LOGOUT METHOD: HANDLE CLIENT LOGOUT
	 * @param _user_name
	 * @return
	 */
	public boolean logout(String _user_name){
		if ( online_user_list.contains(_user_name) ){
			System.out.println("Logout User: " + _user_name + "...");
			online_user_list.remove(_user_name);
			try{
				db_online_writer = new FileWriter("OnlineUser.txt");
				online_writer = new BufferedWriter(db_online_writer);
				online_writer.write("");
				for (String s : online_user_list){
					online_writer.write(s+"\n");
				}
				online_writer.flush();
			}catch(Exception ex) {
			    System.err.println("Failed to write OnlineUser.txt "+ex);
			    return false;
			}
			return true;
		}else{
			System.out.println("Offline User: " + _user_name + " logging out !?");
			return false;
		}
	}
	
	/**
	 * AUTHENTICATE METHOD: HANDLE CLIENT AUTHENTICATEION
	 */
	public boolean authenticate(String _command_code, String _user_name, String _user_password) throws RemoteException {
		if ( _command_code.equals("login") || _command_code.equals("profile") || _command_code.equals("leader_board") || _command_code.equals("play_game")){
			return login(_user_name, _user_password);
		}else if ( _command_code.equals("register") ){
			return register(_user_name, _user_password);
		}else if (_command_code.equals("logout")){
			return logout(_user_name);
		}else{
			System.out.println("Wrong command code from client, I got " + _command_code );
			return false; 
		}
	}
	
	/**
	 * REQUEST METHOD: HANDLE CLIENT REQUEST 
	 */
	public String request(String cmd_code, String u_name, String pw) throws RemoteException {
		
		if( !login(u_name, pw)){
			System.out.println("Request invalid from unknown client");
			return ("Please login again");
		}
		if (cmd_code.equals("profile") ){
			return ("User name: " + u_name + "\n Number of wins: 10 \n Number of games: 10 \n Average time to win: 2 min");
		}else if(cmd_code.equals("leader_board") ){
			return ("First Place: " + u_name + "\n Second Place: Apple \n Third Place: Banana " );
		}else if (cmd_code.equals("play_game") ){
			return ("Hi, " + u_name + " ! Game time ! ");
		}else{
			System.out.println("Unknow command code");
			return ("Unknown command code");
		}
	}
	
//	String host = "localhost";
//	QueueReceiverExample receiver = null;
//	try {
//		receiver = new QueueReceiverExample(host);
//		receiver.receiveMessages();
//	} catch (NamingException | JMSException e) {
//		System.err.println("Program aborted");
//	} finally {
//		if(receiver != null) {
//			try {
//				receiver.close();
//			} catch (Exception e) { }
//		}
//	}

}
