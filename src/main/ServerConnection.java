package main;

import exceptions.NoDatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Contains static methods that are used for connecting to a SQLite DB, querying
 * from it, etc.
 * @author christian
 */
public class ServerConnection {
	
	static {
		try {
			// Do in static initializer so we don't have to do it in every method
			Class.forName("org.sqlite.JDBC");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	private static Connection connection = null;
	private static Statement statement = null;
	
	/**
	 * Connect to the database in the provided path, and set up the private
	 * static variables
	 * @param fullPath
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static void accessDatabase(String fullPath) throws ClassNotFoundException, SQLException {
		connection = DriverManager.getConnection("jdbc:sqlite:" + fullPath);
		statement = connection.createStatement();
		statement.setQueryTimeout(15);
	}
	
	/**
	 * Connect to a database, then return the data stored in each table in an
	 * array of DataTable objects
	 * Pre-req that accessDataBase() was called to initialize the connection
	 * @return An array of DataTable objects with length according to number
	 * of tables found in the database.
	 * @throws ClassNotFoundException
	 */
	public static DataTable[] getTableInfo() {
		
		// List of DataTable objects corresponding to each table
		DataTable[] DTarray = null;
		
		try {
			if (connection == null) 
				throw new NoDatabaseException();
				
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
				DTarray[i] = populateDataTables(tables.get(i), statement, rs);
			}
			
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		// Return null if error or data if success. MAKE THIS AN OBJECT LATER
		return DTarray;
	}
	
	/**
	 * Create The DataTable objects that contain data about each table.
	 * Pre-req that accessDataBase() was called to initialize the connection
	 * @param tableName - String
	 * @param statement - Statement Object
	 * @param rs - ResultSet Object
	 * @return DataTable object with info about parameter table
	 * @throws SQLException
	 */
	private static DataTable populateDataTables(String tableName, Statement statement, 
			ResultSet rs) throws SQLException {
		
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

	/**
	 * Execute a query inputed by a user. Return whatever output is produced.
	 * @param query
	 * @return
	 */
	public static String customQuery(String query) {
		String returnString = "";
		try {
			if (connection == null) 
				throw new NoDatabaseException();
			
			// Execute the user's query
			statement.execute(query);
			ResultSet rs = statement.getResultSet();
			
			if (rs != null) {
				// Get column count for iterating through the output
				ResultSetMetaData rsmd = rs.getMetaData();
				int numCols = rsmd.getColumnCount();
				// Iterate through each column and format + append the output
				while (rs.next()) {
					for (int i = 1; i <= numCols; i++) {
						returnString += rs.getString(i) + "\t";
					}
					returnString += "\n";
				}
			} else {
				// If the query is not SELECT, will return how many items were updated
				int count = statement.getUpdateCount();
				returnString += Integer.toString(count) + " Entries Updated";
			}
		} catch (SQLException e) {
			returnString = e.getMessage();
		}
		
		return returnString;
	}
	
	/**
	 * Close the connection to the database. Should be done on program exit,
	 * as queries will require this connection to be open
	 */
	public static void closeConnection() {
		try {
			if (connection != null) {
				connection.close();
				System.out.println("Connection Closed!");
			}
		} catch (SQLException e) {
			System.err.println(e);
		}
	}
	
	
	/////////////////////////////
	//// Debugging + Testing ////
	/////////////////////////////
	
	/*
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
	
		// cleanup
		Runtime.getRunTime().addShutdownHook(new Thread() {
			public void run() {
				closeConnection();
			}
		});
		
		
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
		String fullPath = "/Users/christian/Documents/workspace/SQLGUI/src/main/test.db";
		accessDatabase(fullPath);
		customQuery("SELECT * FROM second");
	}*/
}
