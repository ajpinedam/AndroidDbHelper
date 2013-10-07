package nz.smartlemon.DatabaseHelper.Types;

public class SQLiteValue {
	private SQLiteType mType = SQLiteType.TEXT;
	private Object mValue = null;
	
	public void setValue(Object value){
		mValue = value;
	}
	
	public void setValue(Object value, SQLiteType type){
		mValue = value;
		mType = type;
	}
	
	public void setValue(SQLiteType type){
		mType = type;
	}
	
	public Object getValue(){
		return mValue;
	}
	
	public SQLiteType getSQLiteType(){
		return mType;
	}
	
	public void setSQLiteType(SQLiteType type){
		mType = type;
	}
	
	public static SQLiteValue getObject(){
		return new SQLiteValue();
	}
}
