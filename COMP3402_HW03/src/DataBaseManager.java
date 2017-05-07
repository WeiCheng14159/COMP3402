import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DataBaseManager {
	private static final String DB_HOST = "localhost";
	private static final String DB_USER = "root";
	private static final String DB_PASS = "m1996623";
	private static final String DB_NAME = "COMP3402";
	private Connection conn;
	
	public DataBaseManager() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		conn = DriverManager.getConnection("jdbc:mysql://"+DB_HOST+
				"/"+DB_NAME+
				"?user="+DB_USER+
				"&password="+DB_PASS);
		System.out.println("Database connection successful.");
	}
		
	private void insert(String name, String birthday) {
		try {
			PreparedStatement stmt = 
					conn.prepareStatement("INSERT INTO COMP3402_T (name, birthday) VALUES (?, ?)");

			stmt.setString(1, name);
			stmt.setDate(2, java.sql.Date.valueOf(birthday));
			stmt.execute();

			System.out.println("Record created");

		} catch (SQLException | IllegalArgumentException e) {
			System.err.println("Error inserting record: "+e);
		}
	}
	private void read(String name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT birthday FROM COMP3402_T WHERE name = ?");
			stmt.setString(1, name);
					
			ResultSet rs = stmt.executeQuery();			
			if(rs.next()) {
				System.out.println("Birthday of "+name+" is on "+rs.getDate(1).toString());
			} else {
				System.out.println(name+" not found!");
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}
	private void list() {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT name, birthday FROM DB_TABLE ");
			while(rs.next()) {
				System.out.println("Birthday of "+rs.getString(1)+" is on "+rs.getDate(2).toString());
			}
		} catch (SQLException e) {
			System.err.println("Error listing records: "+e);
		}
	}
	private void update(String name, String birthday) {
		try {
			PreparedStatement stmt = conn.prepareStatement("UPDATE COMP3402_T SET birthday = ? WHERE name = ?");
			stmt.setDate(1, java.sql.Date.valueOf(birthday));
			stmt.setString(2, name);
					
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.println("Birthday of "+name+" updated");
			} else {
				System.out.println(name+" not found!");
			}
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}
	private void delete(String name) {
		try {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM COMP3402_T WHERE name = ?");
			stmt.setString(1, name);
			int rows = stmt.executeUpdate();
			if(rows > 0) {
				System.out.println("Record of "+name+" removed");
			} else {
				System.out.println(name+" not found!");
			}
		} catch (SQLException | IllegalArgumentException e) {
			System.err.println("Error deleting record: "+e);
		}
	}
	
	private void search_birthday(String birthday){
		try {
			PreparedStatement stmt = conn.prepareStatement("SELECT name FROM COMP3402_T WHERE birthday = ?");
			stmt.setString(1, birthday);
			
			ResultSet rs = stmt.executeQuery();			
			
			while(rs.next()) {
				System.out.println( rs.getString(1)+"has birthday on " + birthday);
			}
			
		} catch (SQLException e) {
			System.err.println("Error reading record: "+e);
		}
	}
	
}
