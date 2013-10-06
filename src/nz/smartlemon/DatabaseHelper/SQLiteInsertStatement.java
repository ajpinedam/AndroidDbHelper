package nz.smartlemon.DatabaseHelper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteInsertStatement extends SQLiteStatement {

	private void onInsertResult(long insertedId){
		if(mOnResultListener != null){
			mOnResultListener.onInsertResult(this, insertedId);
		}
	}
	
	public interface OnResultListener{
		public abstract void onInsertResult(SQLiteInsertStatement statement, long insertedId);
	}
	
	private OnResultListener mOnResultListener = null;
	
	public void setOnResultListener(OnResultListener listener){
		mOnResultListener = listener;
	}
	
	public SQLiteInsertStatement() {
		super(SQLiteStatmentType.Insert);
	}
	
	public SQLiteInsertStatement(String table) {
		super(SQLiteStatmentType.Insert);
		this.setTable(table);
	}
	
	public SQLiteInsertStatement(String table, ContentValues contentValues) {
		super(SQLiteStatmentType.Insert, table, contentValues);
	}

	public SQLiteInsertStatement(String table, String whereClause) {
		super(SQLiteStatmentType.Insert);
		this.setTable(table);
		this.setWhereClause(whereClause);
	}
	
	public SQLiteInsertStatement(String table, String whereClause, String[] arguments) {
		super(SQLiteStatmentType.Insert);
		this.setTable(table);
		this.setWhereClause(whereClause);
		this.setArguments(arguments);
	}
	
	public SQLiteInsertStatement(String table, ContentValues contentValues, String whereClause) {
		super(SQLiteStatmentType.Insert, table, contentValues, whereClause);
	}
	
	public SQLiteInsertStatement(String table, ContentValues contentValues, String whereClause, String[] arguments) {
		super(SQLiteStatmentType.Insert, table, contentValues, whereClause, arguments);
	}
	
	public void returnResult(long insertedId){
		onInsertResult(insertedId);
	}

	public void execute(SQLiteDatabase db){
		db.beginTransaction();
		long insertedId = db.insert(this.getTable(), this.getNullColumnHack(), this.getContentValues());
		this.returnResult(insertedId);
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}
