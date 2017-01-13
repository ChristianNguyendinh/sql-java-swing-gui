package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ServerConnection {

	public static void main(String[] args) throws ClassNotFoundException{
		String[] col = null;
		Object[][] data = null;
		accessDatabase(col, data);
	}
	
	public static void accessDatabase(String[] columnNames, Object[][] data) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:/Users/christian/Documents/workspace/SQLGUI/src/main/test.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(15);
			
			ResultSet rs = null;
			
			// GET TABLE NAMES
			ArrayList<String> tables = new ArrayList<String>();
			rs = connection.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
			while(rs.next()) {
				// sqlite_sequence is created when a table is created and uses AUTOINCREMENT
				String name = rs.getString("TABLE_NAME");
				if (!name.equals("sqlite_sequence"))
					tables.add(name);
			}
			
			for (String t : tables) {
				System.out.println("Columns =========");
				rs = connection.getMetaData().getColumns(null, null, t, "%");
				while(rs.next()) {
					System.out.println(rs.getString("COLUMN_NAME"));
					System.out.println(rs.getString("TYPE_NAME"));
				}
				
				System.out.println("Data =========");
				rs = statement.executeQuery("SELECT * FROM " + t);
				while(rs.next()) {
					System.out.print("id : " + rs.getString("id") + " | ");
					System.out.println("name : " + rs.getString("name"));
				}
			}
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				System.err.println(e);
			}
		}
	}

}
