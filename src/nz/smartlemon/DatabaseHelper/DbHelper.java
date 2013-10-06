package nz.smartlemon.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import nz.smartlemon.DatabaseHelper.DatabaseSchema.DatabaseLocation;

public class DbHelper{

	public interface OnDatabaseLoadedListener {
		public abstract void onDatabaseLoaded();
		public abstract void onDatabaseError(String error);
	}
	
	private boolean mAsynchronous = true;
	
	public boolean isAsynchronous(){
		return mAsynchronous;
	}
	
	public void setAsynchronous(boolean asynchronous){
		mAsynchronous = asynchronous;
	}
	
	public boolean isAsync(){
		return isAsynchronous();
	}
	
	public void setAsync(boolean async){
		setAsynchronous(async);
	}
	
	private boolean mError = false;
	private String mErrorMessage = null;
	private boolean mLoaded = false;

	private List<OnDatabaseLoadedListener> mOnDatabaseLoadedListeners = new ArrayList<OnDatabaseLoadedListener>();

	public void setOnDatabaseLoadedListener(OnDatabaseLoadedListener listener) {
		if(!mOnDatabaseLoadedListeners.contains(listener)){
			mOnDatabaseLoadedListeners.add(listener);
			if(mLoaded && !mError){
				listener.onDatabaseLoaded();
			}else if(mError){
				listener.onDatabaseError(mErrorMessage);
			}
		}
	}

	private DatabaseSchema mSchema = new DatabaseSchema();;

	private ApplicationDataDbHelper mAppDbHelper = null;
	private SDCardDbHelper mSDCardDbHelper = null;

	public DbHelper(DatabaseSchema schema) {
		this(schema, true, null);
	}
	
	public DbHelper(DatabaseSchema schema, OnDatabaseLoadedListener listener) {
		this(schema, true, listener);
	}

	public DbHelper(DatabaseSchema schema, boolean async, OnDatabaseLoadedListener listener) {
		if(listener != null){
			this.setOnDatabaseLoadedListener(listener);
		}
		mAsynchronous = async;
		if (schema != null) {
			mSchema = schema;
		}
		if (async) {
			AsyncLoadDatabase load = new AsyncLoadDatabase();
			load.execute(mSchema);
		} else {
			mErrorMessage = null;
			if (schema == null) {
				mErrorMessage = "No Database Schema given";
			} else {
				if (schema.Location.equals(DatabaseLocation.ApplicationData)) {
					mAppDbHelper = new ApplicationDataDbHelper(schema.Context,
							schema.Name, schema.CursorFactory, schema.Version,
							schema.OnCreateScrips, schema.OnUpgradeScripts);
					mAppDbHelper.open();
				} else {
					mSDCardDbHelper = new SDCardDbHelper(schema.Directory,
							schema.Name, schema.CursorFactory, schema.Version,
							schema.OnCreateScrips, schema.OnUpgradeScripts);
					mSDCardDbHelper.open();
				}
			}
			if(mErrorMessage == null){
				onDatabaseLoaded();
			}else{
				onDatabaseError();
			}
		}
	}
	
	public boolean isOpen() {
		if (mAppDbHelper != null) {
			return mAppDbHelper.isOpen();
		}
		if (mSDCardDbHelper != null) {
			return mSDCardDbHelper.isOpen();
		}
		return false;
	}

	public void close() {
		if (mAppDbHelper != null) {
			mAppDbHelper.close();
		}
		if (mSDCardDbHelper != null) {
			mSDCardDbHelper.close();
		}
	}

	public void Execute(SQLiteSelectStatement... statements) {
		if (!mLoaded || mError) {
			return;
		}
		if(isAsync()){
			AsyncExecuteSelectStatement exec = new AsyncExecuteSelectStatement();
			exec.mAsyncAppDbHelper = mAppDbHelper;
			exec.mAsyncSDCardDbHelper = mSDCardDbHelper;
			exec.execute(statements);
		}else{
			SQLiteDatabase db = null;
			if (mAppDbHelper != null) {
				db = mAppDbHelper.getDatabase();
			} else if (mSDCardDbHelper != null) {
				db = mSDCardDbHelper.getDatabase();
			}
			db.beginTransaction();
			for (SQLiteSelectStatement s : statements) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
	}

	public void Execute(SQLiteUpdateStatement... statements) {
		if (!mLoaded || mError) {
			return;
		}
		if(isAsync()){
			AsyncExecuteUpdateStatement exec = new AsyncExecuteUpdateStatement();
			exec.mAsyncAppDbHelper = mAppDbHelper;
			exec.mAsyncSDCardDbHelper = mSDCardDbHelper;
			exec.execute(statements);
		}else{
			SQLiteDatabase db = null;
			if (mAppDbHelper != null) {
				db = mAppDbHelper.getDatabase();
			} else if (mSDCardDbHelper != null) {
				db = mSDCardDbHelper.getDatabase();
			}
			db.beginTransaction();
			for (SQLiteUpdateStatement s : statements) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
	}

	public void Execute(SQLiteInsertStatement... statements) {
		if (!mLoaded || mError) {
			return;
		}
		if(isAsync()){
			AsyncExecuteInsertStatement exec = new AsyncExecuteInsertStatement();
			exec.mAsyncAppDbHelper = mAppDbHelper;
			exec.mAsyncSDCardDbHelper = mSDCardDbHelper;
			exec.execute(statements);
		}else{
			SQLiteDatabase db = null;
			if (mAppDbHelper != null) {
				db = mAppDbHelper.getDatabase();
			} else if (mSDCardDbHelper != null) {
				db = mSDCardDbHelper.getDatabase();
			}
			db.beginTransaction();
			for (SQLiteInsertStatement s : statements) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
	}

	public void Execute(SQLiteDeleteStatement... statements) {
		if (!mLoaded || mError) {
			return;
		}
		if(isAsync()){
			AsyncExecuteDeleteStatement exec = new AsyncExecuteDeleteStatement();
			exec.mAsyncAppDbHelper = mAppDbHelper;
			exec.mAsyncSDCardDbHelper = mSDCardDbHelper;
			exec.execute(statements);
		}else{
			SQLiteDatabase db = null;
			if (mAppDbHelper != null) {
				db = mAppDbHelper.getDatabase();
			} else if (mSDCardDbHelper != null) {
				db = mSDCardDbHelper.getDatabase();
			}
			db.beginTransaction();
			for (SQLiteDeleteStatement s : statements) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
	}

	public void Execute(SQLiteExecStatement... statements) {
		if (!mLoaded || mError) {
			return;
		}
		if(isAsync()){
			AsyncExecuteExecStatement exec = new AsyncExecuteExecStatement();
			exec.mAsyncAppDbHelper = mAppDbHelper;
			exec.mAsyncSDCardDbHelper = mSDCardDbHelper;
			exec.execute(statements);
		}else{
			SQLiteDatabase db = null;
			if (mAppDbHelper != null) {
				db = mAppDbHelper.getDatabase();
			} else if (mSDCardDbHelper != null) {
				db = mSDCardDbHelper.getDatabase();
			}
			db.beginTransaction();
			for (SQLiteExecStatement s : statements) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		}
	}

	private class AsyncExecuteSelectStatement extends
			AsyncTask<SQLiteSelectStatement, Boolean, Boolean> {

		private ApplicationDataDbHelper mAsyncAppDbHelper = null;
		private SDCardDbHelper mAsyncSDCardDbHelper = null;

		@Override
		protected Boolean doInBackground(SQLiteSelectStatement... params) {
			SQLiteDatabase db = null;
			if (mAsyncAppDbHelper != null) {
				db = mAsyncAppDbHelper.getDatabase();
			} else if (mAsyncSDCardDbHelper != null) {
				db = mAsyncSDCardDbHelper.getDatabase();
			}
			db.beginTransaction();
			for (SQLiteSelectStatement s : params) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			return true;
		}
	}

	private class AsyncExecuteUpdateStatement extends
			AsyncTask<SQLiteUpdateStatement, Boolean, Boolean> {

		private ApplicationDataDbHelper mAsyncAppDbHelper = null;
		private SDCardDbHelper mAsyncSDCardDbHelper = null;

		@Override
		protected Boolean doInBackground(SQLiteUpdateStatement... params) {
			SQLiteDatabase db = null;
			if (mAsyncAppDbHelper != null) {
				db = mAsyncAppDbHelper.getDatabase();
			} else if (mAsyncSDCardDbHelper != null) {
				db = mAsyncSDCardDbHelper.getDatabase();
			}
			db.beginTransaction();
			for (SQLiteUpdateStatement s : params) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			return true;
		}
	}

	private class AsyncExecuteInsertStatement extends
			AsyncTask<SQLiteInsertStatement, Boolean, Boolean> {

		private ApplicationDataDbHelper mAsyncAppDbHelper = null;
		private SDCardDbHelper mAsyncSDCardDbHelper = null;

		@Override
		protected Boolean doInBackground(SQLiteInsertStatement... params) {
			SQLiteDatabase db = null;
			if (mAsyncAppDbHelper != null) {
				db = mAsyncAppDbHelper.getDatabase();
			} else if (mAsyncSDCardDbHelper != null) {
				db = mAsyncSDCardDbHelper.getDatabase();
			}
			db.beginTransaction();
			for (SQLiteInsertStatement s : params) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			return true;
		}
	}

	private class AsyncExecuteDeleteStatement extends
			AsyncTask<SQLiteDeleteStatement, Boolean, Boolean> {

		private ApplicationDataDbHelper mAsyncAppDbHelper = null;
		private SDCardDbHelper mAsyncSDCardDbHelper = null;

		@Override
		protected Boolean doInBackground(SQLiteDeleteStatement... params) {
			SQLiteDatabase db = null;
			if (mAsyncAppDbHelper != null) {
				db = mAsyncAppDbHelper.getDatabase();
			} else if (mAsyncSDCardDbHelper != null) {
				db = mAsyncSDCardDbHelper.getDatabase();
			}
			db.beginTransaction();
			for (SQLiteDeleteStatement s : params) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			return true;
		}
	}

	private class AsyncExecuteExecStatement extends
			AsyncTask<SQLiteExecStatement, Boolean, Boolean> {

		private ApplicationDataDbHelper mAsyncAppDbHelper = null;
		private SDCardDbHelper mAsyncSDCardDbHelper = null;

		@Override
		protected Boolean doInBackground(SQLiteExecStatement... params) {
			SQLiteDatabase db = null;
			if (mAsyncAppDbHelper != null) {
				db = mAsyncAppDbHelper.getDatabase();
			} else if (mAsyncSDCardDbHelper != null) {
				db = mAsyncSDCardDbHelper.getDatabase();
			}
			db.beginTransaction();
			for (SQLiteExecStatement s : params) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			return true;
		}
	}

	private class AsyncLoadDatabase extends
			AsyncTask<DatabaseSchema, Boolean, Boolean> {

		private ApplicationDataDbHelper mAsyncAppDbHelper = null;
		private SDCardDbHelper mAsyncSDCardDbHelper = null;

		@Override
		protected Boolean doInBackground(DatabaseSchema... params) {
			if (params == null || params.length == 0) {
				mErrorMessage = "No Database Schema given";
				return false;
			}
			DatabaseSchema schema = params[0];
			if (schema.Location.equals(DatabaseLocation.ApplicationData)) {
				mAsyncAppDbHelper = new ApplicationDataDbHelper(schema.Context,
						schema.Name, schema.CursorFactory, schema.Version,
						schema.OnCreateScrips, schema.OnUpgradeScripts);
				mAsyncAppDbHelper.open();
			} else {
				mAsyncSDCardDbHelper = new SDCardDbHelper(schema.Directory,
						schema.Name, schema.CursorFactory, schema.Version,
						schema.OnCreateScrips, schema.OnUpgradeScripts);
				mAsyncSDCardDbHelper.open();
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mAppDbHelper = mAsyncAppDbHelper;
			mSDCardDbHelper = mAsyncSDCardDbHelper;
			if(result){
				onDatabaseLoaded();
			}else{
				onDatabaseError();
			}
		}
	}

	public void onDatabaseLoaded() {
		mLoaded = true;
		mError = false;
		for(OnDatabaseLoadedListener s : mOnDatabaseLoadedListeners){
			s.onDatabaseLoaded();
		}
	}

	public void onDatabaseError() {
		mError = true;
		for(OnDatabaseLoadedListener s : mOnDatabaseLoadedListeners){
			s.onDatabaseError(mErrorMessage);
		}
	}
}
