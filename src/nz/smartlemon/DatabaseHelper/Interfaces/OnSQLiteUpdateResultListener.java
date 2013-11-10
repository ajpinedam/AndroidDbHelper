package nz.smartlemon.DatabaseHelper.Interfaces;

import nz.smartlemon.DatabaseHelper.Types.SQLiteUpdateStatement;

public interface OnSQLiteUpdateResultListener {

	public abstract void onUpdateResult(SQLiteUpdateStatement statement,
			int updatedCount);
}
