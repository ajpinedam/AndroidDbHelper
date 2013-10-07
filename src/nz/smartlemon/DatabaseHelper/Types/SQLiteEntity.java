package nz.smartlemon.DatabaseHelper.Types;

import android.content.ContentValues;

public class SQLiteEntity {
	
	private Class<?> mClass = null;
	private Object mInstance = null;
	
	public SQLiteEntity(Class<?> cls, Object instance){
		mClass = cls;
		mInstance = instance;
	}
	
	public void update(){
		
	}
	
	public void delete(){
		
	}
	
	public void insert(){
		
	}
	
	private ContentValues getContentValues(){
		return null;
	}
	
}
