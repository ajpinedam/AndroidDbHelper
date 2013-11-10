package nz.smartlemon.DatabaseHelper.Types;

import nz.smartlemon.DatabaseHelper.Interfaces.OnSQLiteUpdateResultListener;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteUpdateStatement extends SQLiteStatement {

	private void onUpdateResult(int updatedCount) {
		if (mOnResultListener != null) {
			mOnResultListener.onUpdateResult(this, updatedCount);
		}
	}

	private OnSQLiteUpdateResultListener mOnResultListener = null;

	public void setOnResultListener(OnSQLiteUpdateResultListener listener) {
		mOnResultListener = listener;
	}

	public SQLiteUpdateStatement() {
		super(SQLiteStatmentType.Update);
	}

	public SQLiteUpdateStatement(String table) {
		super(SQLiteStatmentType.Update);
		this.setTable(table);
	}

	public SQLiteUpdateStatement(String table, ContentValues contentValues) {
		super(SQLiteStatmentType.Update, table, contentValues);
	}

	public SQLiteUpdateStatement(String table, String whereClause) {
		super(SQLiteStatmentType.Update);
		this.setTable(table);
		this.setWhereClause(whereClause);
	}

	public SQLiteUpdateStatement(String table, String whereClause,
			String[] arguments) {
		super(SQLiteStatmentType.Update);
		this.setTable(table);
		this.setWhereClause(whereClause);
		this.setArguments(arguments);
	}

	public SQLiteUpdateStatement(String table, ContentValues contentValues,
			String whereClause) {
		super(SQLiteStatmentType.Update, table, contentValues, whereClause);
	}

	public SQLiteUpdateStatement(String table, ContentValues contentValues,
			String whereClause, String[] arguments) {
		super(SQLiteStatmentType.Update, table, contentValues, whereClause,
				arguments);
	}

	public void returnResult(int updatedCount) {
		onUpdateResult(updatedCount);
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
	public void setContentValues(ContentValues values) {
		super.setContentValues(values);
	}

	@Override
	public ContentValues getContentValues() {
		return super.getContentValues();
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
		int updatedCount = db.update(this.getTable(), this.getContentValues(),
				this.getWhereClause(), this.getArguments());
		this.returnResult(updatedCount);
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}
