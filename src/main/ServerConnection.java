package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ServerConnection {
	/*
	// Debugging + Testing
	public static void main(String[] args) throws ClassNotFoundException{
		DataTable[] data = accessDatabase();
		
		for (int i = 0; i < data.length; i++) {
			System.out.println(data[i].tableName);
			System.out.println(data[i].numRows);
			System.out.println(data[i].numCols);
			System.out.println(data[i].getTypesArrayString());
			
			System.out.println("Col Names:");
			String[] cn = data[i].getColumnNames();
			for (int j = 0; j < cn.length; j++) {
				System.out.println(cn[j]);
			}
			System.out.println("Table:");
			Object[][] d = data[i].getData();
			for (int j = 0; j < d.length; j++) {
				for (int k = 0; k < d[0].length; k++) {
					System.out.print(d[j][k] + " ");
				}	
				System.out.println();
			}
			System.out.println("===================");
		}
	}
	*/
	
	/**
	 * Connect to a database, CURRENTLY HARDCODED, then return the data stored
	 * there in the form of an array for parsing by the gui
	 * @return An array of DataTable objects with length according to number
	 * of tables found in the database.
	 * @throws ClassNotFoundException
	 */
	public static DataTable[] accessDatabase() throws ClassNotFoundException {
		Class.forName("org.sqlite.JDBC");
		
		Connection connection = null;
		// List of DataTable objects corresponding to each table
		DataTable[] DTarray = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:/Users/christian/Documents/workspace/SQLGUI/src/main/test.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(15);
			
			ResultSet rs = null;
			// List of tables
			ArrayList<String> tables = new ArrayList<String>();
			
			// GET TABLE NAMES
			rs = connection.getMetaData().getTables(null, null, null, new String[] {"TABLE"});
			while(rs.next()) {
				// sqlite_sequence is created when a table is created and uses AUTOINCREMENT
				String name = rs.getString("TABLE_NAME");
				if (!name.equals("sqlite_sequence"))
					tables.add(name);
			}

			DTarray = new DataTable[tables.size()];
			// Load data for each table
			for (int i = 0; i < tables.size(); i++) {
				DTarray[i] = populateDataTables(tables.get(i), statement, rs, connection);
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
		
		// Return null if error or data if success. MAKE THIS AN OBJECT LATER
		return DTarray;
	}
	
	/**
	 * Create The DataTable objects that contain data about each table
	 * @param tableName - String
	 * @param statement - Statement Object
	 * @param rs - ResultSet Object
	 * @param connection - Connection Object
	 * @return DataTable object with info about parameter table
	 * @throws SQLException
	 */
	private static DataTable populateDataTables(String tableName, Statement statement, 
			ResultSet rs, Connection connection) throws SQLException {
		
		DataTable dt = new DataTable();
		// Will contain data for the whole table
		Object[][] data = null;
		// Column names for the table
		String[] columnNames = null;
		// Types for loading the SQL data into an object
		SQLiteDataTypes[] types = null;
		int numRows = 0;
		int numCols = 0;
		
		// Get the number of rows
		rs = statement.executeQuery("SELECT COUNT(*) FROM " + tableName);
		// Should only run once
		while (rs.next()) {
			numRows = rs.getInt(1);
		}
		// Get the number of columns
		rs = statement.executeQuery("SELECT * FROM " + tableName + " LIMIT 1");
		ResultSetMetaData rsmd = rs.getMetaData();
		numCols = rsmd.getColumnCount();
		
		data = new Object[numRows][numCols];
		columnNames = new String[numCols];
		types = new SQLiteDataTypes[numCols];
		
		
		// Populate the columns
		rs = connection.getMetaData().getColumns(null, null, tableName, "%");
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
		rs = statement.executeQuery("SELECT * FROM " + tableName);
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
		
		// Create the data table object to return
		dt.tableName = tableName;
		dt.setColumnNames(columnNames);
		dt.setData(data);
		dt.numRows = numRows;
		dt.numCols = columnNames.length;
		dt.setTypesArrayString(types);
		return dt;
	}

}
