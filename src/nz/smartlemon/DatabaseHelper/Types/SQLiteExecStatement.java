package nz.smartlemon.DatabaseHelper.Types;

import nz.smartlemon.DatabaseHelper.Interfaces.OnSQLiteExecCompleteListener;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteExecStatement extends SQLiteStatement {
	
	private boolean mComplete = false;
	
	private void onComplete() {
		mComplete = true;
		if (mOnCompleteListener != null) {
			mOnCompleteListener.onComplete(this);
		}
	}

	private OnSQLiteExecCompleteListener mOnCompleteListener = null;

	public void setOnCompleteListener(OnSQLiteExecCompleteListener listener) {
		mOnCompleteListener = listener;
		if(mComplete){
			onComplete();
		}
	}

	public SQLiteExecStatement() {
		super(SQLiteStatmentType.Exec);
	}

	public SQLiteExecStatement(String query) {
		super(SQLiteStatmentType.Exec, query);
	}
	
	@Override
	public void setQuery(String query){
		super.setQuery(query);
	}
	
	@Override
	public String getQuery(){
		return super.getQuery();
	}

	public void execute(SQLiteDatabase db) {
		mComplete = false;
		db.beginTransaction();
		db.execSQL(this.getQuery());
		onComplete();
		db.setTransactionSuccessful();
		db.endTransaction();

	}

}
