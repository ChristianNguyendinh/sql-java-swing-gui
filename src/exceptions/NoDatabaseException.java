package exceptions;

public class NoDatabaseException extends Exception {
	// Default serial uid
	private static final long serialVersionUID = 1L;
	
	public NoDatabaseException() {
		super();
	}
	
	@Override
	public String getMessage() {
		return "An empty database was accessed. This may be due to the provided path "
				+ "to the database was bad, which caused an empty database"
				+ "to be created at that path and attempted to be used.";
	}
}
