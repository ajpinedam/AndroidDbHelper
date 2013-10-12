package nz.smartlemon.DatabaseHelper.Types;

public class EntityException extends RuntimeException {

	private static final long serialVersionUID = -3322581896589762391L;
	
	public EntityException(){
		super();
	}
	
	public EntityException(String message){
		super(message);
	}
	
}
