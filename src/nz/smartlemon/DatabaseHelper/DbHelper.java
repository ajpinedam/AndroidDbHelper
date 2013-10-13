package nz.smartlemon.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import nz.smartlemon.DatabaseHelper.ApplicationDataDbHelper.OnApplicationDataDbHelperRequestListener;
import nz.smartlemon.DatabaseHelper.SDCardDbHelper.OnSDCardDbHelperRequestListener;
import nz.smartlemon.DatabaseHelper.Types.DatabaseLocation;
import nz.smartlemon.DatabaseHelper.Types.DatabaseSchema;
import nz.smartlemon.DatabaseHelper.Types.SQLiteDeleteStatement;
import nz.smartlemon.DatabaseHelper.Types.SQLiteExecStatement;
import nz.smartlemon.DatabaseHelper.Types.SQLiteInsertStatement;
import nz.smartlemon.DatabaseHelper.Types.SQLiteSelectStatement;
import nz.smartlemon.DatabaseHelper.Types.SQLiteUpdateStatement;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

public class DbHelper implements OnApplicationDataDbHelperRequestListener,
		OnSDCardDbHelperRequestListener {

	public interface OnDatabaseLoadedListener {
		public abstract void onDatabaseLoaded();

		public abstract void onDatabaseError(String error);
	}

	private boolean mAsynchronous = true;

	public boolean isAsynchronous() {
		return mAsynchronous;
	}

	public void setAsynchronous(boolean asynchronous) {
		mAsynchronous = asynchronous;
	}

	public boolean isAsync() {
		return isAsynchronous();
	}

	public void setAsync(boolean async) {
		setAsynchronous(async);
	}

	private boolean mError = false;
	private String mErrorMessage = null;
	private boolean mLoaded = false;

	private List<OnDatabaseLoadedListener> mOnDatabaseLoadedListeners = new ArrayList<OnDatabaseLoadedListener>();

	public void setOnDatabaseLoadedListener(OnDatabaseLoadedListener listener) {
		if (listener == null) {
			return;
		}
		if (!mOnDatabaseLoadedListeners.contains(listener)) {
			mOnDatabaseLoadedListeners.add(listener);
			if (mLoaded && !mError) {
				listener.onDatabaseLoaded();
			} else if (mError) {
				listener.onDatabaseError(mErrorMessage);
			}
		}
	}

	private DatabaseSchema mSchema = new DatabaseSchema();;

	private ApplicationDataDbHelper mAppDbHelper = null;
	private SDCardDbHelper mSDCardDbHelper = null;

	private static List<DbHelper> mInstances = new ArrayList<DbHelper>();

	public static DbHelper getInstance(DatabaseSchema schema,
			OnDatabaseLoadedListener listener) {
		DbHelper instance = getInstance(schema);
		instance.setOnDatabaseLoadedListener(listener);
		return instance;
	}

	public static DbHelper getInstance(DatabaseSchema schema, boolean async,
			OnDatabaseLoadedListener listener) {
		DbHelper instance = getInstance(schema, async);
		instance.setOnDatabaseLoadedListener(listener);
		return instance;
	}

	public static DbHelper getInstance(DatabaseSchema schema, boolean async) {
		DbHelper instance = getInstance(schema);
		instance.setAsync(async);
		return instance;
	}

	public static DbHelper getInstance(DatabaseSchema schema) {
		// Only one instance for each database can exist at each time
		// Otherwise you will get a database lock
		DbHelper instance = null;
		for (DbHelper helper : mInstances) {
			if (helper.getSchema().equals(schema)) {
				instance = helper;
				break;
			} else {
				DatabaseSchema a = helper.getSchema();
				DatabaseSchema b = schema;
				if (a.Name.equals(b.Name)) {
					if (a.Location.equals(b.Location)) {
						if (a.Directory.equals(b.Location)) {
							instance = helper;
							break;
						}
					}
				}
			}
		}
		if (instance == null) {
			instance = new DbHelper(schema);
			mInstances.add(instance);
		}
		return instance;
	}

	private DatabaseSchema getSchema() {
		return mSchema;
	}

	private DbHelper(DatabaseSchema schema) {
		this(schema, true, null);
	}

	private DbHelper(DatabaseSchema schema, OnDatabaseLoadedListener listener) {
		this(schema, true, listener);
	}

	private DbHelper(DatabaseSchema schema, boolean async,
			OnDatabaseLoadedListener listener) {
		if (listener != null) {
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
					mAppDbHelper
							.setOnApplicationDataDbHelperRequestListener(this);
				} else {
					mSDCardDbHelper = new SDCardDbHelper(schema.Directory,
							schema.Name, schema.CursorFactory, schema.Version,
							schema.OnCreateScrips, schema.OnUpgradeScripts);
					mSDCardDbHelper.setOnSDCardDbHelperRequestListener(this);
				}
			}
			if (mErrorMessage == null) {
				onDatabaseLoaded();
			} else {
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

	/**
	 * This should be called when you no longer want to use the current database
	 * object. The database will not be re-opened if you do not call this, it
	 * will simply use the old object. Call this to free up some memory.
	 */
	public void close() {
		if (mAppDbHelper != null) {
			mAppDbHelper.close();
		}
		if (mSDCardDbHelper != null) {
			mSDCardDbHelper.close();
		}
	}

	/**
	 * This will insure that you don't close the database object if it is in a
	 * transaction by something else, it is recommended to use this method
	 * instead
	 */
	public void closeIfNotInTransaction() {
		if (mAppDbHelper != null) {
			mAppDbHelper.closeIfNotInTransaction();
		}
		if (mSDCardDbHelper != null) {
			mSDCardDbHelper.closeIfNotInTransaction();
		}
	}

	public SQLiteSelectStatement Execute(SQLiteSelectStatement statement) {
		if (!mLoaded || mError) {
			return null;
		}
		return (SQLiteSelectStatement) Execute(new SQLiteSelectStatement[] { statement })[0];
	}

	public SQLiteSelectStatement[] Execute(SQLiteSelectStatement... statements) {
		if (!mLoaded || mError) {
			return null;
		}
		if (isAsync()) {
			AsyncExecuteSelectStatement exec = new AsyncExecuteSelectStatement();
			exec.execute(statements);
		} else {
			SQLiteDatabase db = getDatabase();
			db.beginTransaction();
			for (SQLiteSelectStatement s : statements) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			closeIfNotInTransaction();
		}
		return statements;
	}

	public SQLiteUpdateStatement Execute(SQLiteUpdateStatement statement) {
		if (!mLoaded || mError) {
			return null;
		}
		return (SQLiteUpdateStatement) Execute(new SQLiteUpdateStatement[] { statement })[0];
	}

	public SQLiteUpdateStatement[] Execute(SQLiteUpdateStatement... statements) {
		if (!mLoaded || mError) {
			return null;
		}
		if (isAsync()) {
			AsyncExecuteUpdateStatement exec = new AsyncExecuteUpdateStatement();
			exec.execute(statements);
		} else {
			SQLiteDatabase db = getDatabase();
			db.beginTransaction();
			for (SQLiteUpdateStatement s : statements) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			closeIfNotInTransaction();
		}
		return statements;
	}

	public SQLiteInsertStatement Execute(SQLiteInsertStatement statement) {
		if (!mLoaded || mError) {
			return null;
		}
		return (SQLiteInsertStatement) Execute(new SQLiteInsertStatement[] { statement })[0];
	}

	public SQLiteInsertStatement[] Execute(SQLiteInsertStatement... statements) {
		if (!mLoaded || mError) {
			return null;
		}
		if (isAsync()) {
			AsyncExecuteInsertStatement exec = new AsyncExecuteInsertStatement();
			exec.execute(statements);
		} else {
			SQLiteDatabase db = getDatabase();
			db.beginTransaction();
			for (SQLiteInsertStatement s : statements) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			closeIfNotInTransaction();
		}
		return statements;
	}

	public SQLiteDeleteStatement Execute(SQLiteDeleteStatement statement) {
		if (!mLoaded || mError) {
			return null;
		}
		return (SQLiteDeleteStatement) Execute(new SQLiteDeleteStatement[] { statement })[0];
	}

	public SQLiteDeleteStatement[] Execute(SQLiteDeleteStatement... statements) {
		if (!mLoaded || mError) {
			return null;
		}
		if (isAsync()) {
			AsyncExecuteDeleteStatement exec = new AsyncExecuteDeleteStatement();
			exec.execute(statements);
		} else {
			SQLiteDatabase db = getDatabase();
			db.beginTransaction();
			for (SQLiteDeleteStatement s : statements) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			closeIfNotInTransaction();
		}
		return statements;
	}

	public SQLiteExecStatement Execute(SQLiteExecStatement statement) {
		if (!mLoaded || mError) {
			return null;
		}
		return (SQLiteExecStatement) Execute(new SQLiteExecStatement[] { statement })[0];
	}

	public SQLiteExecStatement[] Execute(SQLiteExecStatement... statements) {
		if (!mLoaded || mError) {
			return null;
		}
		if (isAsync()) {
			AsyncExecuteExecStatement exec = new AsyncExecuteExecStatement();
			exec.execute(statements);
		} else {
			SQLiteDatabase db = getDatabase();
			db.beginTransaction();
			for (SQLiteExecStatement s : statements) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			closeIfNotInTransaction();
		}
		return statements;
	}

	private class AsyncExecuteSelectStatement extends
			AsyncTask<SQLiteSelectStatement, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(SQLiteSelectStatement... params) {
			SQLiteDatabase db = getDatabase();
			db.beginTransaction();
			for (SQLiteSelectStatement s : params) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			closeIfNotInTransaction();
			return true;
		}
	}

	private class AsyncExecuteUpdateStatement extends
			AsyncTask<SQLiteUpdateStatement, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(SQLiteUpdateStatement... params) {
			SQLiteDatabase db = getDatabase();
			db.beginTransaction();
			for (SQLiteUpdateStatement s : params) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			closeIfNotInTransaction();
			return true;
		}
	}

	private class AsyncExecuteInsertStatement extends
			AsyncTask<SQLiteInsertStatement, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(SQLiteInsertStatement... params) {
			SQLiteDatabase db = getDatabase();
			db.beginTransaction();
			for (SQLiteInsertStatement s : params) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			closeIfNotInTransaction();
			return true;
		}
	}

	private class AsyncExecuteDeleteStatement extends
			AsyncTask<SQLiteDeleteStatement, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(SQLiteDeleteStatement... params) {
			SQLiteDatabase db = getDatabase();
			db.beginTransaction();
			for (SQLiteDeleteStatement s : params) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			closeIfNotInTransaction();
			return true;
		}
	}

	private class AsyncExecuteExecStatement extends
			AsyncTask<SQLiteExecStatement, Boolean, Boolean> {

		@Override
		protected Boolean doInBackground(SQLiteExecStatement... params) {
			SQLiteDatabase db = getDatabase();
			db.beginTransaction();
			for (SQLiteExecStatement s : params) {
				s.execute(db);
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			closeIfNotInTransaction();
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
			} else {
				mAsyncSDCardDbHelper = new SDCardDbHelper(schema.Directory,
						schema.Name, schema.CursorFactory, schema.Version,
						schema.OnCreateScrips, schema.OnUpgradeScripts);
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mAppDbHelper = mAsyncAppDbHelper;
			mSDCardDbHelper = mAsyncSDCardDbHelper;
			if (result) {
				onDatabaseLoaded();
			} else {
				onDatabaseError();
			}
		}
	}

	public void onDatabaseLoaded() {
		mLoaded = true;
		mError = false;
		for (OnDatabaseLoadedListener s : mOnDatabaseLoadedListeners) {
			s.onDatabaseLoaded();
		}
	}

	public void onDatabaseError() {
		mError = true;
		for (OnDatabaseLoadedListener s : mOnDatabaseLoadedListeners) {
			s.onDatabaseError(mErrorMessage);
		}
	}

	public SQLiteDatabase getDatabase() {
		if (mAppDbHelper != null) {
			return mAppDbHelper.getDatabase();
		} else if (mSDCardDbHelper != null) {
			return mSDCardDbHelper.getDatabase();
		}
		return null;
	}

	@Override
	public void onRequestOpen() {

	}

	@Override
	public boolean onRequestClose() {
		return true;
	}
}
