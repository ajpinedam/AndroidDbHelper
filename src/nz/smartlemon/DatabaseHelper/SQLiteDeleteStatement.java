package nz.smartlemon.DatabaseHelper;

import nz.smartlemon.DatabaseHelper.SQLiteStatement.SQLiteStatmentType;
import nz.smartlemon.DatabaseHelper.SQLiteUpdateStatement.OnResultListener;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDeleteStatement extends SQLiteStatement {

	private void onDeleteResult(int deletedCount) {
		if (mOnResultListener != null) {
			mOnResultListener.onDeleteResult(this, deletedCount);
		}
	}

	public interface OnResultListener {
		public abstract void onDeleteResult(SQLiteDeleteStatement statement,
				int deletedCount);
	}

	private OnResultListener mOnResultListener = null;

	public void setOnResultListener(OnResultListener listener) {
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

	public void execute(SQLiteDatabase db) {
		db.beginTransaction();
		int deletedCount = db.delete(this.getTable(), this.getWhereClause(),
				this.getArguments());
		this.returnResult(deletedCount);
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}
