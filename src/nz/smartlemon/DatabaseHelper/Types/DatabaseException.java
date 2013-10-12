package nz.smartlemon.DatabaseHelper.Types;

public class DatabaseException extends RuntimeException{

	private static final long serialVersionUID = -6282329680257385704L;
	
	public DatabaseException(){
		super("Unknown");
	}
	
	public DatabaseException(String message){
		super(message);
	}
	
}
