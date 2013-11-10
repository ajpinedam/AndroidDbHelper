package nz.smartlemon.DatabaseHelper.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import nz.smartlemon.DatabaseHelper.DbHelper;
import nz.smartlemon.DatabaseHelper.Interfaces.OnDatabaseLoadedListener;
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
	
	public DbHelper getDbHelper(){
		if(mDbHelper == null){
			DatabaseSchema schema = getDatabaseSchema();
			mDbHelper = DbHelper.getInstance(schema, true, mOnDatabaseLoadedListener);
		}
		return mDbHelper;
	}
	
	public SQLiteEntityDatabase() {
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
	
	public final void close(){
		
	}
	
	public final void save(Class<? extends SQLiteEntity> cls, Object instance){
		
	}
	
	public final SelectStatement load(Class<? extends SQLiteEntity> cls){
		return new SelectStatement(cls);
	}
	
	public final Object load(Class<? extends SQLiteEntity> cls, long id){
		return null;
	}
	
	public class SelectStatement {
		
		private Class<? extends SQLiteEntity> mClass;
		
		SelectStatement(Class<? extends SQLiteEntity> cls){
			mClass = cls;
		}
		
		private SQLiteSelectStatement mStatement = new SQLiteSelectStatement();
		//private boolean mDistinct = false;
		//private int mTop = -1;
		public SelectStatement where(String whereClause){
			if(whereClause.toUpperCase(Locale.ENGLISH).contains("WHERE")){
				whereClause = whereClause.replace("WHERE", "").replace("Where", "").replace("where", "");
			}
			mStatement.setWhereClause(whereClause);
			return this;
		}
		
		public SelectStatement where(String whereClause, String ... arguments){
			this.where(whereClause);
			mStatement.setArguments(arguments);
			return this;
		}
		
		public SelectStatement distinct(){
			//mDistinct = true;
			return this;
		}
		
		public SelectStatement top(){
			//mTop = 1;
			return this;
		}
		
		public SelectStatement top(int limit){
			if(limit == 0){
				throw new IllegalArgumentException("parameter 1 must be either -1 or over 0");
			}
			//mTop = limit;
			return this;
		}
		
		public void execute(){
			
		}
		
		public void executeSingle(){
			
		}
		
		public Object[] result(){
			//DbHelper helper = getDbHelper();
			//helper.setAsync(false);
			//helper.Execute(mStatement);
			
			SQLiteEntity entity = null;
			try {
				entity = mClass.newInstance();
				return new Object[]{entity};
			} catch (InstantiationException e) {
				throw new EntityException("The contructor of '" + mClass.getName() + "' is not accessable");
			} catch (IllegalAccessException e) {
				throw new EntityException("The contructor of '" + mClass.getName() + "' is not accessable");
			}
			//return new Object[0];
		}
		
		public Object firstResult(){
			//mTop = 1;
			Object[] res = result();
			if(res == null || res.length == 0){
				return null;
			}
			return res[0];
		}
	}
}
