package nz.smartlemon.DatabaseHelper.Interfaces;

import nz.smartlemon.DatabaseHelper.Types.SQLiteDeleteStatement;

public interface OnSQLiteDeleteResultListener {
	public abstract void onDeleteResult(SQLiteDeleteStatement statement,
			int deletedCount);
}
