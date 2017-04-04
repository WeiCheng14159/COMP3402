import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
public class Game_Server extends UnicastRemoteObject implements Communication
{
	private FileWriter db_info_writer;
	private FileReader db_info_reader;
	private BufferedReader info_reader;
	private BufferedWriter info_writer;
	
	private FileWriter db_online_writer;
	private FileReader db_online_reader;
	private BufferedReader online_reader;
	private BufferedWriter online_writer;
	
	private ArrayList<String> user_list = new ArrayList<String>(); 
	private ArrayList<String> pw_list = new ArrayList<String>();
	private ArrayList<String> online_list = new ArrayList<String>();
	
	public Game_Server() throws RemoteException {
		init_db();
	}
	public static void main(String[] args) {
		try {
			Game_Server app = new Game_Server();
			System.setSecurityManager(new SecurityManager());
			Naming.rebind("Poker", app);
			System.out.println("Service registered");
		} catch(Exception e) {
			System.err.println("Exception thrown: "+e);
		}
	}
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
				user_list.add(info[0]);
				pw_list.add(info[1]);
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
				online_list.add(line);
				System.out.println("Online user: " + line); 
			}
			online_reader.close();
		}catch(Exception ex) {
		    System.err.println("Failed access OnlineUser.txt "+ex);
		    return;
		}
	}
	
	public boolean login(String u_name, String pw){
		if (user_list.contains(u_name) && pw_list.contains(pw)){
			System.out.println("User: " + u_name + " with pw: " + pw + "logged in");
			return true;
		}else{
			return false;
		}
	}
	
	public boolean register(String u_name, String pw){
		if ( ! user_list.contains(u_name) ){
			System.out.println("Add new User: " + u_name + " with pw: " + pw );
			try{
				info_writer.write(u_name+":"+pw);
			}catch(Exception ex) {
			    System.err.println("Failed to write OnlineUser.txt "+ex);
			    return false;
			}
			return true;
		}else{
			return false;
		}
	}
	
	public void profile(String u_name, String pw){
		if (login(u_name, pw)){
			
		}
	}
	public void  play_game(String u_name, String pw){
		
	}
	public void leader_board(String u_name, String pw){
		
	}
	
	@Override
	public boolean checking(String cmd_code, String u_name, String pw) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public JPanel request(String cmd_code, String u_name, String pw) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}
	
}
