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
	
	private enum SQLiteDataTypes {
		NULL, INTEGER, REAL, TEXT, BLOB;
	}
	
	public static void accessDatabase(String[] columnNames, Object[][] data) throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		
		Connection connection = null;
		data = null;
		columnNames = null;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:/Users/christian/Documents/workspace/SQLGUI/src/main/test.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(15);
			
			ResultSet rs = null;
			ArrayList<String> tables = new ArrayList<String>();
			ArrayList<String> columns = new ArrayList<String>();
			ArrayList<Object[]> table_data = new ArrayList<Object[]>();
			// Types for loading the SQL data into an object
			ArrayList<SQLiteDataTypes> types = new ArrayList<SQLiteDataTypes>();
			
			// GET TABLE NAMES - CURRENTLY ONLY WORKS WITH ONE TABLE!
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
					columns.add(rs.getString("COLUMN_NAME"));
					String type_name = rs.getString("TYPE_NAME");
					switch (type_name) {
						case "NULL": 	types.add(SQLiteDataTypes.NULL);
										break;
						case "INTEGER": types.add(SQLiteDataTypes.INTEGER);
										break;
						case "REAL": 	types.add(SQLiteDataTypes.REAL);
										break;
						case "TEXT": 	types.add(SQLiteDataTypes.TEXT);
										break;
						case "BLOB": 	types.add(SQLiteDataTypes.BLOB);
										break;
						default:		System.err.println("Something went terribly wrong with the types.");
										break;
						
					}
				}
				
				System.out.println("Data =========");
				
				// Get the number of rows
				rs = statement.executeQuery("SELECT COUNT(*) FROM " + t);
				int numRows = 0;
				int numCols = columns.size();
				// Should only run once
				while (rs.next()) {
					numRows = rs.getInt(1);
				}
				
				data = new Object[numRows][numCols];
				
				// Load the data into the 2d data array
				rs = statement.executeQuery("SELECT * FROM " + t);
				int currRow = 0;
				while(rs.next()) {
					Object[] row = new Object[numCols];
					// Fill in the row with data from the SELECT statement
					for (int i = 0; i < numCols; i++) {
						String colName = columns.get(i);
						Object d = null;
						// Create each data point based on its type
						switch (types.get(i)) {
							case INTEGER: 	d = new Integer(rs.getInt(colName));
											break;
							case REAL: 		d = new Double(rs.getDouble(colName));
											break;
							case TEXT: 		d = rs.getString(colName);
											break;
							case BLOB: 		d = rs.getObject(colName);
											break;
							default: 		d = null;
											break;
						}
						// Assemble the row
						row[i] = d;
					}
					// Assemble the table data of rows
					data[currRow] = row;
					currRow++;
				}
			}
			
			for (int i = 0; i < data.length; i++) {
				for (int j = 0; j < data[0].length; j++) {
					System.out.print(data[i][j] + " ");
				}
				System.out.println();
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
