package nz.smartlemon.DatabaseHelper.Types;

import java.util.ArrayList;
import java.util.List;

import nz.smartlemon.DatabaseHelper.Interfaces.OnSQLiteSelectResultListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteSelectStatement extends SQLiteStatement {
	
	public Cursor cursorResult(){
		return (Cursor)mResult;
	}
	
	public String stringResult(){
		return (String)mResult;
	}
	
	public String[] stringArrayResult(){
		return (String[])mResult;
	}
	
	public Object result(){
		return mResult;
	}

	private Object mResult = null;
	private boolean mComplete = false;
	
	private void onResult(){
		if(mSelectType.equals(SQLiteSelectType.String)){
			onStringResult((String)mResult);
		}else if(mSelectType.equals(SQLiteSelectType.StringArray)){
			onStringArrayResult((String[])mResult);
		}else if(mSelectType.equals(SQLiteSelectType.Cursor)){
			Cursor c = (Cursor)mResult;
			if(c == null || c.isClosed()){
				c = null;
			}
			onCursorResult(c);
		}
	}
	
	private void onStringResult(String result) {
		mComplete = true;
		mResult = result;
		if (mOnResultListener != null) {
			mOnResultListener.onStringResult(this, result);
		}
	}

	private void onStringArrayResult(String[] result) {
		mComplete = true;
		mResult = result;
		if (mOnResultListener != null) {
			mOnResultListener.onStringArrayResult(this, result);
		}
	}

	private boolean onCursorResult(Cursor result) {
		mComplete = true;
		mResult = result;
		if (mOnResultListener != null) {
			return mOnResultListener.onCursorResult(this, result);
		}
		//They may want to get the result... therefore we can't close it
		return false;
		//return true;
	}

	private OnSQLiteSelectResultListener mOnResultListener = null;

	public void setOnResultListener(OnSQLiteSelectResultListener listener) {
		mOnResultListener = listener;
		if(mComplete){
			onResult();
		}
	}

	public enum SQLiteSelectType {
		String(0), StringArray(1), Cursor(2);

		private int mType = 0;

		SQLiteSelectType(int type) {
			mType = type;
		}

		public boolean equals(SQLiteSelectType type) {
			return this.mType == type.mType;
		}
	}

	private SQLiteSelectType mSelectType = SQLiteSelectType.String;
	
	public SQLiteSelectStatement() {
		super(SQLiteStatmentType.Select);
		setSQLiteSelectType(SQLiteSelectType.String);
	}
	
	public SQLiteSelectStatement(SQLiteSelectType type) {
		super(SQLiteStatmentType.Select);
		setSQLiteSelectType(type);
	}

	public SQLiteSelectStatement(String query) {
		super(SQLiteStatmentType.Select, query);
	}

	public SQLiteSelectStatement(String query, String... arguments) {
		super(SQLiteStatmentType.Select, arguments);
	}

	public SQLiteSelectStatement(SQLiteSelectType type, String query,
			String... arguments) {
		super(SQLiteStatmentType.Select, query, arguments);
		setSQLiteSelectType(type);
	}

	public void setSQLiteSelectType(SQLiteSelectType type) {
		mComplete = false;
		mSelectType = type;
	}
	
	public boolean returnResult(Cursor c) {
		if (mSelectType.equals(SQLiteSelectType.String)) {
			String result = null;
			if (!c.isClosed()) {
				if (c.getColumnCount() != 0) {
					if (c.moveToFirst()) {
						result = c.getString(0);
					}
				}
			}
			c.close();
			onStringResult(result);
			return false;
		} else if (mSelectType.equals(SQLiteSelectType.StringArray)) {
			List<String> result = new ArrayList<String>();
			if (!c.isClosed()) {
				if (c.getColumnCount() != 0) {
					if (c.moveToFirst()) {
						do {
							result.add(c.getString(0));
						} while (c.moveToNext());
					}
				}
			}
			c.close();
			onStringArrayResult(result.toArray(new String[0]));
			return false;
		} else if (mSelectType.equals(SQLiteSelectType.Cursor)) {
			return onCursorResult(c);
		}
		return true;
	}
	
	@Override
	public void setQuery(String query){
		mComplete = false;
		super.setQuery(query);
	}
	
	@Override
	public String getQuery(){
		return super.getQuery();
	}

	@Override
	public void setArguments(String... arguments) {
		mComplete = false;
		super.setArguments(arguments);
	}

	@Override
	public String[] getArguments() {
		return super.getArguments();
	}
	
	public SQLiteSelectStatement execute(SQLiteDatabase db) {
		mComplete = false;
		db.beginTransaction();
		Cursor c = db.rawQuery(this.getQuery(), this.getArguments());
		boolean close = this.returnResult(c);
		if (close) {
			c.close();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		return this;
	}
}
