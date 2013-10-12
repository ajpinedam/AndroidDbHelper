package nz.smartlemon.DatabaseHelper;

import nz.smartlemon.DatabaseHelper.Types.DatabaseSchemaUpgrade;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

class SDCardDbHelper extends SDCardSQLiteOpenHelper {

	private OnSDCardDbHelperRequestListener mRequestListener = null;

	protected interface OnSDCardDbHelperRequestListener {
		abstract void onRequestOpen();

		abstract boolean onRequestClose();
	}

	protected void setOnSDCardDbHelperRequestListener(
			OnSDCardDbHelperRequestListener listener) {
		mRequestListener = listener;
	}

	private String[] mCreateScripts = new String[0];
	private DatabaseSchemaUpgrade[] mUpdateScripts = new DatabaseSchemaUpgrade[0];

	private SQLiteDatabase mWritable = null;

	public SQLiteDatabase getDatabase() {
		if (mWritable == null) {
			this.open();
		}
		return mWritable;
	}

	public boolean isOpen() {
		return mWritable != null && mWritable.isOpen();
	}

	public void open() {
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

	@Override
	public void close() {
		boolean close = true;
		if (mRequestListener != null) {
			close = mRequestListener.onRequestClose();
		}
		if (close) {
			if (mWritable != null) {
				if (mWritable.isOpen()) {
					if (mWritable.inTransaction()) {
						mWritable.endTransaction();
						Log.w("DatabaseHelper",
								"Transaction forcefully stopped!");
					}
					mWritable.close();
				}
			}
			super.close();
		}
	}

	public SDCardDbHelper(String dir, String name, CursorFactory factory,
			int version, String[] createScripts,
			DatabaseSchemaUpgrade[] updateScripts) {
		super(dir, name, factory, version);
		mCreateScripts = createScripts;
		mUpdateScripts = updateScripts;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String script : mCreateScripts) {
			db.execSQL(script);
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
