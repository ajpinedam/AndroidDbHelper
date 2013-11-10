package nz.smartlemon.DatabaseHelper.Interfaces;

import nz.smartlemon.DatabaseHelper.Types.SQLiteInsertStatement;

public interface OnSQLiteInsertResultListener {
	public abstract void onInsertResult(SQLiteInsertStatement statement, long insertedId);
}
