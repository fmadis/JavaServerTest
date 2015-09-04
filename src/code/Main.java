package code;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;

public class Main {
	static Connection cn;
	static PreparedStatement stmt;
	static ResultSet data;
	
	static final String DB_URL = "jdbc:mysql://localhost:3306/JavaDB";
	static final String DB_USER = "root";
	static final String DB_PASS = "";

	public static void main(String[] args) {
		Main.connect();
		Main.query(1);
		System.out.println("");
		Main.query(1);
	}
	
	public static void connect() {
		try {
			cn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
		} catch (Exception e) {
			System.err.println("Database connection failed: " + e.getMessage());
		}
	}
	
	public static void query(int index) {
		try {
			stmt = cn.prepareStatement("SELECT `JavaDB`.`other_stuff`.`data`, `JavaDB`.`users`.`name`,`JavaDB`.`users`.`email` " +
					"FROM `JavaDB`.`users` JOIN `JavaDB`.`other_stuff` " +
					"ON `JavaDB`.`users`.`id` = `JavaDB`.`other_stuff`.`id`" +
					"WHERE `JavaDB`.`users`.`id` = ?");
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
	
	public static void disconnect() {
		try {
			cn.close();
		} catch (Exception e) {
			System.err.println("Database close failed: " + e.getMessage());
		}
	}

}
