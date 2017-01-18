package exceptions;

import java.sql.SQLException;

/**
 * There's actually not really a big need for this subclass :)
 * @author christian
 */
public class NoDatabaseException extends SQLException {
	// Default serial uid
	private static final long serialVersionUID = 1L;
	
	public NoDatabaseException() {
		super();
	}
	
	@Override
	public String getMessage() {
		return "An empty or non-existant database was accessed. This may be due "
				+ "to the provided path to the database was bad, which caused "
				+ "an empty database to be created at that path and attempted "
				+ "to be used. Or accessDataBase() has not been called yet to "
				+ "initialize the connection";
	}
}
