package main;

/**
 * Information about a specific table in a database. Contains things like
 * table name, row & column count, an array of the column names, an array of
 * each type that corresponds to each column, and a 2D array representing the
 * data of each table. Currently only contains variables + getters/setters. No
 * methods for now
 * @author christian
 */
public class DataTable {
	
	public String tableName;
	public int numRows;
	public int numCols;
	
	/** Column names of a table, ordered corresponding to data */
	private String[] columnNames;
	/** 2D array representing data of a table */
	private Object[][] data;
	/** Used for tool tip display of each column type */
	private String typesArrayString;

	
	public DataTable() {
		this.tableName = null;
		this.columnNames = null;
		this.data = null;
		this.numRows = 0;
		this.numCols = 0;
		this.typesArrayString = null;
	}
	
	// GETTERS
	
	public String[] getColumnNames() {
		return columnNames;
	}
	
	public Object[][] getData() {
		return data;
	}
	
	public String getTypesArrayString() {
		return typesArrayString;
	}
	
	// SETTERS. besides the typesArrayString, in the future we may need to validate other inputs

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public void setData(Object[][] data) {
		this.data = data;
	}

	public void setTypesArrayString(String typesArrayString) {
		this.typesArrayString = typesArrayString;
	}
	
	public void setTypesArrayString(SQLiteDataTypes[] typesArray) {
		// Make the array into a string
		this.typesArrayString = "[";
		int i;
		for (i = 0; i < typesArray.length - 1; i++) {
			typesArrayString += typesArray[i] + ", ";
		}
		this.typesArrayString += typesArray[i] + "]";
	}
}
