package code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
	static Connection cn;
	static PreparedStatement stmt;
	static ResultSet data;
	
	static final String DB_URL = "jdbc:mysql://localhost:3306/JavaDB";
	static final String DB_USER = "root";
	static final String DB_PASS = "";

	public static void main(String[] args) {
		Main.connect();
		Main.selectQuery(1);
		Main.insertQuery("testing", "MF", "testing@hot.ee", "12345678");
	}
	
	public static void connect() {
		try {
			cn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
		} catch (Exception e) {
			System.err.println("Database connection failed: " + e.getMessage());
		}
	}
	
	public static void selectQuery(int index) {
		try {
			stmt = cn.prepareStatement(
					"SELECT `other_stuff`.`data`, `users`.`name`,`users`.`email` FROM `users` " +
					"JOIN `other_stuff` ON `users`.`id` = `other_stuff`.`id`" +
					"WHERE .`users`.`id` = ?");
			stmt.setInt(1, index);
			data = stmt.executeQuery();
			while (data.next()) {
				System.out.println(data.getString("name"));
				System.out.println(data.getString("email"));
				System.out.println(data.getString("data"));
			}
			if (data != null) {
				data.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			System.err.println("Database query failed: " + e.getMessage());
		}
	}
	
	public static void insertQuery(String dt, String name, String email, String pw) {
		try {
			stmt = cn.prepareStatement("INSERT INTO `users` (`name`, `email`, `password`) VALUES (?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, name);
			stmt.setString(2, email);
			stmt.setString(3, pw);
			stmt.execute();
			int id = Main.autoIncrement();
			stmt = cn.prepareStatement("INSERT INTO `other_stuff` (`id`, `data`) VALUES (?, ?)");
			stmt.setInt(1, id);
			stmt.setString(2, dt);
			stmt.execute();
			if (data != null) {
				data.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			System.err.println("Database query failed: " + e.getMessage());
		}
	}
	
	public static void disconnect() {
		try {
			cn.close();
		} catch (Exception e) {
			System.err.println("Database close failed: " + e.getMessage());
		}
	}
	
	private static int autoIncrement() {
		try {
			ResultSet dt = stmt.getGeneratedKeys();
		    if (dt.next()) {
		        return dt.getInt(1);
		    } else {
		    	return -1;
		    }
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return -1;
		}
	}

}
