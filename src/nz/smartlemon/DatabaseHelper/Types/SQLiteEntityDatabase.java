package nz.smartlemon.DatabaseHelper.Types;

import java.util.ArrayList;
import java.util.List;

import nz.smartlemon.DatabaseHelper.DbHelper;
import nz.smartlemon.DatabaseHelper.DbHelper.OnDatabaseLoadedListener;
import android.content.Context;

public abstract class SQLiteEntityDatabase implements OnDatabaseLoadedListener {
	
	private OnDatabaseLoadedListener mOnDatabaseLoadedListener = new OnDatabaseLoadedListener(){

		@Override
		public void onDatabaseLoaded() {
			pvtOnDatabaseLoaded();
		}

		@Override
		public void onDatabaseError(String error) {
			pvtOnDatabaseError(error);
		}
		
	};
	
	private void pvtOnDatabaseLoaded(){
		this.onDatabaseLoaded();
	}
	
	private void pvtOnDatabaseError(String error){
		this.onDatabaseError(error);
	}
	
	public abstract int getVersion();
	
	public abstract String getDatabaseName();

	public abstract DatabaseLocation getDatabaseLocation();
	
	private static List<SQLiteEntityDatabase> mInstances = new ArrayList<SQLiteEntityDatabase>();
	
	private DbHelper mDbHelper = null;
	
	public Context getContext(){
		return null;
	}
	
	public String getDirectory() {
		return null;
	}
	
	public SQLiteEntityDatabase() {
		DatabaseSchema schema = getDatabaseSchema();
		mDbHelper = DbHelper.getInstance(schema, true, mOnDatabaseLoadedListener);
		mInstances.add(this);
	}
	
	private DatabaseSchema getDatabaseSchema(){
		DatabaseSchema schema = new DatabaseSchema();
		if(this.getDatabaseLocation().equals(DatabaseLocation.ApplicationData)){
			if(this.getContext() == null){
				throw new EntityException("getContext() is not implemented by the sub class or it has returned null");
			}
		}else if(this.getDatabaseLocation().equals(DatabaseLocation.Directory)){
			if(this.getDirectory() == null){
				throw new EntityException("getDirectory() is not implemented by the sub class or it has returned null");
			}
		}
		return schema;
	}
	
	public final void open(){
		
	}
	
	public final void close(){
		
	}
	
	
}
