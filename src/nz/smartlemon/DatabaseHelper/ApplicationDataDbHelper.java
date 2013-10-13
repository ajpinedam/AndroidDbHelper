package nz.smartlemon.DatabaseHelper;

import nz.smartlemon.DatabaseHelper.Types.DatabaseSchemaUpgrade;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class ApplicationDataDbHelper extends SQLiteOpenHelper {

	private OnApplicationDataDbHelperRequestListener mRequestListener = null;

	protected interface OnApplicationDataDbHelperRequestListener {
		abstract void onRequestOpen();

		abstract boolean onRequestClose();
	}

	protected void setOnApplicationDataDbHelperRequestListener(
			OnApplicationDataDbHelperRequestListener listener) {
		mRequestListener = listener;
	}

	private String[] mCreateScripts = new String[0];
	private DatabaseSchemaUpgrade[] mUpdateScripts = new DatabaseSchemaUpgrade[0];

	private SQLiteDatabase mWritable = null;
	
	
	public SQLiteDatabase getDatabase() {
		if (!this.isOpen()) {
			this.open();
		}
		return mWritable;
	}

	public boolean isOpen() {
		return mWritable != null && mWritable.isOpen();
	}

	private void open() {
		if (mRequestListener != null) {
			mRequestListener.onRequestOpen();
		}
		if (mWritable != null) {
			if (!mWritable.isOpen()) {
				mWritable = this.getWritableDatabase();
			}
		} else {
			mWritable = this.getWritableDatabase();
		}
	}

	public void closeIfNotInTransaction(){
		if (mWritable != null) {
			if (mWritable.isOpen()) {
				if (mWritable.inTransaction()) {
					return;
				}
				mWritable.close();
			}
			mWritable = null;
		}
		super.close();
	}
	
	@Override
	public void close() {
		boolean close = true;
		if (mRequestListener != null) {
			close = mRequestListener.onRequestClose();
		}
		if (close) {
			this.forceClose();
		}
	}
	
	
	private void forceClose(){
		if (mWritable != null) {
			if (mWritable.isOpen()) {
				if (mWritable.inTransaction()) {
					mWritable.endTransaction();
					Log.w("DatabaseHelper",
							"Transaction forcefully stopped!");
				}
				mWritable.close();
			}
			mWritable = null;
		}
		super.close();
	}

	public ApplicationDataDbHelper(Context context, String name,
			CursorFactory factory, int version, String[] createScripts,
			DatabaseSchemaUpgrade[] updateScripts) {
		super(context, name, factory, version);
		mCreateScripts = createScripts;
		mUpdateScripts = updateScripts;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (mCreateScripts != null) {
			for (String script : mCreateScripts) {
				db.execSQL(script);
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		boolean createPost = false;
		if (mUpdateScripts != null) {
			for (DatabaseSchemaUpgrade script : mUpdateScripts) {
				if (script.UpgradeVersion <= newVersion
						&& oldVersion < script.UpgradeVersion) {
					if (!createPost && script.CreatePost) {
						createPost = true;
					}
					for (String s : script.OnUpgradeScripts) {
						db.execSQL(s);
					}
				}
			}
		}
		if (createPost) {
			this.onCreate(db);
		}
	}

}
