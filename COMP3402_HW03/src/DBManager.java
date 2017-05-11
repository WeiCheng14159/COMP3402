import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DBManager {
	private static final String DB_HOST = "localhost";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "m1996623";
	private static final String DB_NAME = "hw03";
	private static final String DB_TABLE = "user_info";
	
//	CREATE TABLE user_info (
//			  name varchar(32) NOT NULL,
//			  password varchar(32) NOT NULL,
//			  played_game INT NOT NULL,
//			  win_game INT NOT NULL,
//			  logged_in BOOL,
//			  PRIMARY KEY name (name)
//			);
	
	public static void main(String [] argv ){
		try {
			DBManager d = new DBManager();
			d.add_user("A", "12");
			d.remove_user("A");
			d.add_played_game("A");
			d.add_win("A");
			d.get_win_rate("A");
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Connection conn;
	public DBManager() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection("jdbc:mysql://"+DB_HOST+
				"/"+DB_NAME+
				"?user="+DB_USER+
				"&password="+DB_PASS);
		System.out.println("Database connection successful.");
	}

	public void init_db() {}
	
	private boolean login_user(String name, String password) {
		//check if user exist
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT name FROM "+DB_TABLE+" WHERE name = ? password = ?");
			stmt.setString(1, name);
			stmt.setString(2, password);
			
			ResultSet rs = stmt.executeQuery();			
			
			if(rs.next()) {
				System.out.printf("User: %s logged in !\n", name);
				online_user(name);
				return true;
			} else {
				System.out.printf("User: %s not found\n", name);
				return false;
			}
			
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
			return false;
		}
	}
	
	private void logout_user(String name, String password) {
		//check if user exist//check if user exist
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT name FROM "+DB_TABLE+" WHERE name = ? password = ?");
			stmt.setString(1, name);
			stmt.setString(2, password);
			
			ResultSet rs = stmt.executeQuery();			
			
			if(rs.next()) {
				System.out.printf("User: %s log out !\n", name);
				offline_user(name);
			} else {
				System.out.printf("User: %s not found\n", name);
			}
		} catch (SQLException e) {
			System.err.println("Cannot logout user: "+e);
		}
	}
	
	private void online_user(String name){
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE "+DB_TABLE+" SET logged_in WHERE name = ?");
			stmt.setBoolean(1, true);
			stmt.setString(2, name);
					
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.printf("User: %s login status updated\n", name);
			} else {
				System.out.printf("User: %s not found !\n", name);
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}
	
	private void offline_user(String name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE "+DB_TABLE+" SET logged_in WHERE name = ?");
			stmt.setBoolean(1, false);
			stmt.setString(2, name);
					
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.printf("User: %s login status updated\n", name);
			} else {
				System.out.printf("User: %s not found !\n", name);
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}
	
	private void add_user(String name, String password) {
		try {
			PreparedStatement stmt = 
					conn.prepareStatement("INSERT INTO "+DB_TABLE+" (name, password, played_game, win_game, logged_in) VALUES (?, ?, ?, ?, ?)");
			stmt.setString(1, name);
			stmt.setString(2, password);
			stmt.setInt(3, 0);
			stmt.setInt(4, 0);
			stmt.setBoolean(5, false);
			stmt.execute();
			System.out.printf("New user: %s created !\n", name);

		} catch (SQLException | IllegalArgumentException e) {
			System.err.println("Error inserting record: "+e);
		}	
	}
	
	private void remove_user(String name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM "+DB_TABLE+" WHERE name = ?");
			stmt.setString(1, name);
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.printf("User: %s removed\n", name);
			} else {
				System.out.println(name+" not found!\n");
			}
		} catch (SQLException | IllegalArgumentException e) {
			System.err.println("Error deleting record: "+e);
		}
	}
	
	private void add_win(String name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE "+DB_TABLE+" SET win_game = win_game + 1 WHERE name = ?");
			stmt.setString(1, name);
					
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.printf("User: %s win game updated\n", name);
			} else {
				System.out.printf("User: %s not found !\n", name);
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}
	
	private void add_played_game(String name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE "+DB_TABLE+" SET played_game = played_game + 1 WHERE name = ?");
			stmt.setString(1, name);
					
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.printf("User: %s played game updated\n", name);
			} else {
				System.out.printf("User: %s not found !\n", name);
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}
	
	private double get_win_rate(String name) {
		double r = 0f;
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT played_game, win_game FROM "+DB_TABLE+" WHERE name = ?");
			stmt.setString(1, name);
			
			ResultSet rs = stmt.executeQuery();			
			if(rs.next()) {
				r = ( (float)rs.getInt(2) ) / (float)( rs.getInt(1) );
				System.out.printf("Win rate for user %s is %f \n", name, r);
				return r;
			} else {
				System.out.printf("User: %s not found !\n", name);
				return -1.0f;
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
			return -1.0f;
		}
	}
	
}
