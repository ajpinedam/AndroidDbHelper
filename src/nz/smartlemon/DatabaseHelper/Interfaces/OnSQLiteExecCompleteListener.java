package nz.smartlemon.DatabaseHelper.Interfaces;

import nz.smartlemon.DatabaseHelper.Types.SQLiteExecStatement;

public interface OnSQLiteExecCompleteListener {

	public abstract void onComplete(SQLiteExecStatement statement);
}
