package nz.smartlemon.DatabaseHelper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteUpdateStatement extends SQLiteStatement {

	private void onUpdateResult(int updatedCount) {
		if (mOnResultListener != null) {
			mOnResultListener.onUpdateResult(this, updatedCount);
		}
	}

	public interface OnResultListener {
		public abstract void onUpdateResult(SQLiteUpdateStatement statement,
				int updatedCount);
	}

	private OnResultListener mOnResultListener = null;

	public void setOnResultListener(OnResultListener listener) {
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

	public void execute(SQLiteDatabase db) {
		db.beginTransaction();
		int updatedCount = db.update(this.getTable(), this.getContentValues(),
				this.getWhereClause(), this.getArguments());
		this.returnResult(updatedCount);
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}
