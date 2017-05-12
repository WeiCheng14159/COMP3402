import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class DBManager {
	private static final String DB_HOST = "localhost";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "m1996623";
	private static final String DB_NAME = "hw03";
	private static final String DB_TABLE = "user_info";
	
//	CREATE TABLE user_info (
//			  name varchar(32) NOT NULL,
//			  passwd varchar(32) NOT NULL,
//			  played_game INT default 0,
//			  win_game INT default 0,
//			  avg_time double default 0,
//			  logged_in BOOL default false,
//			  win_rate double default 0.0,
//			  PRIMARY KEY name (name)
//			);
	
//	public static void main(String [] argv ){
//		try {
//			DBManager d = new DBManager();
//			d.add_user("A", "12");
//			d.add_user("B", "12");
//			d.add_user("C", "12");
//			d.add_user("D", "12");
//			d.add_user("E", "12");
//			d.add_user("F", "12");
//			d.login_user("A", "12");
//			d.logout_user("A");
//			d.get_full_rank();
//			System.out.printf("Rank for this user is %d", d.get_rank("A") );
//		} catch (InstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	private Connection conn;
	public DBManager() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection("jdbc:mysql://"+DB_HOST+
				"/"+DB_NAME+
				"?user="+DB_USER+
				"&password="+DB_PASS);
		System.out.println("Database connection successful.");
	}
	
	public boolean login_user(String _name, String _password) {
		//check if user exist
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT name FROM "+DB_TABLE+" WHERE name = ? AND passwd = ?");
			stmt.setString(1, _name);
			stmt.setString(2, _password);
			
			ResultSet rs = stmt.executeQuery();			
			
			if(rs.next()) {
				System.out.printf("User: %s logged in !\n", _name);
				online_user(_name);
				return true;
			} else {
				System.out.printf("User: %s not found\n", _name);
				return false;
			}
			
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
			return false;
		}
	}
	
	public boolean logout_user(String _name) {
		//check if user exist//check if user exist
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT name FROM "+DB_TABLE+" WHERE name = ? ");
			stmt.setString(1, _name);
			
			ResultSet rs = stmt.executeQuery();			
			
			if(rs.next()) {
				System.out.printf("User: %s log out !\n", _name);
				offline_user(_name);
				return true;
			} else {
				System.out.printf("User: %s not found\n", _name);
				return false;
			}
		} catch (SQLException e) {
			System.err.println("Cannot logout user: "+e);
			return false;
		}
	}
	
	public void online_user(String _name){
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE "+DB_TABLE+" SET logged_in = ? WHERE name = ?");
			stmt.setBoolean(1, true);
			stmt.setString(2, _name);
					
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.printf("User: %s online\n", _name);
			} else {
				System.out.printf("User: %s not found !\n", _name);
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}
	
	public void offline_user(String _name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE "+DB_TABLE+" SET logged_in = ? WHERE name = ?");
			stmt.setBoolean(1, false);
			stmt.setString(2, _name);
					
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.printf("User: %s offline\n", _name);
			} else {
				System.out.printf("User: %s not found !\n", _name);
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}
	
	public boolean add_user(String _name, String _password) {
		try {
			PreparedStatement stmt = 
					conn.prepareStatement("INSERT INTO "+DB_TABLE+" (name, passwd) VALUES (?, ?)");
			stmt.setString(1, _name);
			stmt.setString(2, _password);
			
			stmt.execute();
			System.out.printf("New user: %s created !\n", _name);
			return true;
		} catch (SQLException | IllegalArgumentException e) {
			System.err.println("Error inserting record: "+e);
			return false;
		}	
	}
	
	public void remove_user(String _name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM "+DB_TABLE+" WHERE name = ?");
			stmt.setString(1, _name);
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.printf("User: %s removed\n", _name);
			} else {
				System.out.println(_name+" not found!\n");
			}
		} catch (SQLException | IllegalArgumentException e) {
			System.err.println("Error deleting record: "+e);
		}
	}
	
	public void add_win(String _name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE "+DB_TABLE+" SET win_game = win_game + 1 WHERE name = ?");
			stmt.setString(1, _name);
					
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.printf("User: %s win game updated\n", _name);
			} else {
				System.out.printf("User: %s not found !\n", _name);
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
		update_win_rate(_name);
	}
	
	public void add_played_game(String _name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE "+DB_TABLE+" SET played_game = played_game + 1 WHERE name = ?");
			stmt.setString(1, _name);
					
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.printf("User: %s played game updated\n", _name);
			} else {
				System.out.printf("User: %s not found !\n", _name);
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
		update_win_rate(_name);
	}
	
	public float get_win_rate(String _name) {
		float r = 0f;
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT win_rate FROM "+DB_TABLE+" WHERE name = ?");
			stmt.setString(1, _name);
			
			ResultSet rs = stmt.executeQuery();			
			if(rs.next()) {
				r = (float) rs.getDouble(1);
				System.out.printf("Win rate for user %s is %f \n", _name, r);
				return r;
			} else {
				System.out.printf("User: %s not found !\n", _name);
				return -1.0f;
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
			return -1.0f;
		}
	}	
	
	public PlayerInfoMessage get_info(String _name){
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT played_game, win_game, avg_time FROM "+DB_TABLE+" WHERE name = ?");
			stmt.setString(1, _name);
			
			ResultSet rs = stmt.executeQuery();			
			if(rs.next()) {
				return new PlayerInfoMessage(_name, rs.getInt(2), rs.getInt(1), get_win_rate(_name), rs.getDouble(3), get_rank(_name) );
			} else {
				System.out.printf("User: %s not found !\n", _name);
				return null;
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
			return null;
		}
	}
	
	private void update_win_rate(String _name){
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE "+DB_TABLE+" SET win_rate = win_game / played_game WHERE name = ?");
			stmt.setString(1, _name);
			
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.printf("User: %s win rate updated\n", _name);
			} else {
				System.out.printf("User: %s not found !\n", _name);
			}
		} catch (SQLException e) {
//			System.err.println("Error reading record: "+e);
		}
	}
	
	public int get_rank(String _name){
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT name, @curRank := @curRank + 1 AS rank FROM user_info p, (SELECT @curRank := 0 ) q ORDER BY win_rate DESC;");
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				if( rs.getString(1).equals(_name) ){
					return rs.getInt(2);
				}
			}
			return -1;
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
			return -1;
		}
	}
	
	public ArrayList<PlayerInfoMessage> get_full_rank(){
		ArrayList<PlayerInfoMessage> list = new ArrayList<PlayerInfoMessage>();
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT name, win_game, played_game, avg_time, win_rate, @curRank := @curRank + 1 AS rank FROM user_info p, (SELECT @curRank := 0 ) q ORDER BY win_rate DESC;");
			
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				list.add( new PlayerInfoMessage( rs.getString(1), rs.getInt(2), rs.getInt(3), (float)rs.getDouble(5), rs.getDouble(4), rs.getInt(6) ) );
			}
			return list;
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
			return null;
		}
	}
}
