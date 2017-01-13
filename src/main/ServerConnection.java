package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ServerConnection {

	public static void main(String[] args) throws ClassNotFoundException{
		Object[] data = accessDatabase();
		String[] cols = (String[]) data[0];
		Object[][] d = (Object[][]) data[1];
		
		for (int i = 0; i < d.length; i++) {
			for (int j = 0; j < d[0].length; j++) {
				System.out.print(d[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		for (int i = 0; i < cols.length; i++) {
			System.out.println(cols[i]);
		}
	}
	
	private enum SQLiteDataTypes {
		NULL, INTEGER, REAL, TEXT, BLOB;
	}
	
	/**
	 * Connect to a database, CURRENTLY HARDCODED, then return the data stored
	 * there in the form of an array for parsing by the gui
	 * @return An array of size 2. First element is an String array of column names,
	 * Second is a 2D Object array of the table data.
	 * @throws ClassNotFoundException
	 */
	public static Object[] accessDatabase() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		
		Connection connection = null;
		Object[] returnArr = new Object[2];
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:/Users/christian/Documents/workspace/SQLGUI/src/main/test.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(15);
			
			ResultSet rs = null;
			ArrayList<String> tables = new ArrayList<String>();
			// Will contain data for the whole table
			Object[][] data = null;
			// Column names for the table
			String[] columnNames = null;
			// Types for loading the SQL data into an object
			SQLiteDataTypes[] types = null;
			
			// GET TABLE NAMES - CURRENTLY ONLY WORKS WITH ONE TABLE!
			rs = connection.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
			while(rs.next()) {
				// sqlite_sequence is created when a table is created and uses AUTOINCREMENT
				String name = rs.getString("TABLE_NAME");
				if (!name.equals("sqlite_sequence"))
					tables.add(name);
			}
			
			// Load data for table
			for (String t : tables) {
				// Get the number of rows
				int numRows = 0;
				rs = statement.executeQuery("SELECT COUNT(*) FROM " + t);
				// Should only run once
				while (rs.next()) {
					numRows = rs.getInt(1);
				}
				// Get the number of columns
				int numCols = 0;
				rs = statement.executeQuery("SELECT * FROM " + t + " LIMIT 1");
				ResultSetMetaData rsmd = rs.getMetaData();
				numCols = rsmd.getColumnCount();
				
				data = new Object[numRows][numCols];
				columnNames = new String[numCols];
				types = new SQLiteDataTypes[numCols];
				
				
				// Populate the columns
				rs = connection.getMetaData().getColumns(null, null, t, "%");
				int index = 0;
				while(rs.next()) {
					columnNames[index] = rs.getString("COLUMN_NAME");
					String type_name = rs.getString("TYPE_NAME");
					switch (type_name) {
						case "NULL": 	types[index] = SQLiteDataTypes.NULL;
										break;
						case "INTEGER": types[index] = SQLiteDataTypes.INTEGER;
										break;
						case "REAL": 	types[index] = SQLiteDataTypes.REAL;
										break;
						case "TEXT": 	types[index] = SQLiteDataTypes.TEXT;
										break;
						case "BLOB": 	types[index] = SQLiteDataTypes.BLOB;
										break;
						default:		System.err.println("Something went terribly wrong with the types.");
										break;
						
					}
					index++;
				}
				
				// Load the data into the 2d data array
				rs = statement.executeQuery("SELECT * FROM " + t);
				index = 0;
				while(rs.next()) {
					Object[] row = new Object[numCols];
					// Fill in the row with data from the SELECT statement
					for (int i = 0; i < numCols; i++) {
						String colName = columnNames[i];
						Object d = null;
						// Create each data point based on its type
						switch (types[i]) {
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
					data[index] = row;
					index++;
				}
			}
			// Prepare the return array
			returnArr[0] = columnNames;
			returnArr[1] = data;
			
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
		
		// Return null if error or data if success
		return returnArr;
	}

}
