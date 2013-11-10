package nz.smartlemon.DatabaseHelper.Interfaces;

import nz.smartlemon.DatabaseHelper.Types.SQLiteSelectStatement;
import android.database.Cursor;

public interface OnSQLiteSelectResultListener {
	public abstract void onStringResult(SQLiteSelectStatement statement,
			String result);

	public abstract void onStringArrayResult(
			SQLiteSelectStatement statement, String[] result);

	public abstract boolean onCursorResult(SQLiteSelectStatement statement,
			Cursor result);
}
