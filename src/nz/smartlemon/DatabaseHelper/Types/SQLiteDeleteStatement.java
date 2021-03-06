package nz.smartlemon.DatabaseHelper.Types;

import nz.smartlemon.DatabaseHelper.Interfaces.OnSQLiteDeleteResultListener;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDeleteStatement extends SQLiteStatement {

	private void onDeleteResult(int deletedCount) {
		if (mOnResultListener != null) {
			mOnResultListener.onDeleteResult(this, deletedCount);
		}
	}

	private OnSQLiteDeleteResultListener mOnResultListener = null;

	public void setOnResultListener(OnSQLiteDeleteResultListener listener) {
		mOnResultListener = listener;
	}

	public SQLiteDeleteStatement() {
		super(SQLiteStatmentType.Delete);
	}

	public SQLiteDeleteStatement(String table) {
		super(SQLiteStatmentType.Delete);
		this.setTable(table);
	}

	public SQLiteDeleteStatement(String table, ContentValues contentValues) {
		super(SQLiteStatmentType.Delete, table, contentValues);
	}

	public SQLiteDeleteStatement(String table, String whereClause) {
		super(SQLiteStatmentType.Delete);
		this.setTable(table);
		this.setWhereClause(whereClause);
	}

	public SQLiteDeleteStatement(String table, String whereClause,
			String[] arguments) {
		super(SQLiteStatmentType.Delete);
		this.setTable(table);
		this.setWhereClause(whereClause);
		this.setArguments(arguments);
	}

	public SQLiteDeleteStatement(String table, ContentValues contentValues,
			String whereClause) {
		super(SQLiteStatmentType.Delete, table, contentValues, whereClause);
	}

	public SQLiteDeleteStatement(String table, ContentValues contentValues,
			String whereClause, String[] arguments) {
		super(SQLiteStatmentType.Delete, table, contentValues, whereClause,
				arguments);
	}

	public void returnResult(int deletedCount) {
		onDeleteResult(deletedCount);
	}
	
	@Override
	public void setTable(String table) {
		super.setTable(table);
	}

	@Override
	public String getTable() {
		return super.getTable();
	}

	@Override
	public void setWhereClause(String whereClause) {
		super.setWhereClause(whereClause);
	}

	@Override
	public String getWhereClause() {
		return super.getWhereClause();
	}

	@Override
	public void setArguments(String... arguments) {
		super.setArguments(arguments);
	}

	@Override
	public String[] getArguments() {
		return super.getArguments();
	}
	
	public void execute(SQLiteDatabase db) {
		db.beginTransaction();
		int deletedCount = db.delete(this.getTable(), this.getWhereClause(),
				this.getArguments());
		this.returnResult(deletedCount);
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}
