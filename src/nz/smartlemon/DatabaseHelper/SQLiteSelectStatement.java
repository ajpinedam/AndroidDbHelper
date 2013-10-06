package nz.smartlemon.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteSelectStatement extends SQLiteStatement {

	private void onStringResult(String result) {
		if (mOnResultListener != null) {
			mOnResultListener.onStringResult(this, result);
		}
	}

	private void onStringArrayResult(String[] result) {
		if (mOnResultListener != null) {
			mOnResultListener.onStringArrayResult(this, result);
		}
	}

	private boolean onCursorResult(Cursor result) {
		if (mOnResultListener != null) {
			return mOnResultListener.onCursorResult(this, result);
		}
		return true;
	}

	public interface OnResultListener {
		public abstract void onStringResult(SQLiteSelectStatement statement,
				String result);

		public abstract void onStringArrayResult(
				SQLiteSelectStatement statement, String[] result);

		public abstract boolean onCursorResult(SQLiteSelectStatement statement,
				Cursor result);
	}

	private OnResultListener mOnResultListener = null;

	public void setOnResultListener(OnResultListener listener) {
		mOnResultListener = listener;
	}

	public enum SQLiteSelectType {
		String(0), StringArray(1), Cursor(2);

		private int mType = 0;

		SQLiteSelectType(int type) {
			mType = type;
		}

		public boolean equals(SQLiteSelectType type) {
			return this.mType == type.mType;
		}
	}

	private SQLiteSelectType mSelectType = SQLiteSelectType.String;

	public SQLiteSelectStatement(SQLiteSelectType type) {
		super(SQLiteStatmentType.Select);
		setSQLiteSelectType(type);
	}

	public SQLiteSelectStatement(String query) {
		super(SQLiteStatmentType.Select, query);
	}

	public SQLiteSelectStatement(String query, String... arguments) {
		super(SQLiteStatmentType.Select, arguments);
	}

	public SQLiteSelectStatement(SQLiteSelectType type, String query,
			String... arguments) {
		super(SQLiteStatmentType.Select, query, arguments);
		setSQLiteSelectType(type);
	}

	public void setSQLiteSelectType(SQLiteSelectType type) {
		mSelectType = type;
	}

	public boolean returnResult(Cursor c) {
		if (mSelectType.equals(SQLiteSelectType.String)) {
			String result = null;
			if (!c.isClosed()) {
				if (c.getColumnCount() != 0) {
					if (c.moveToFirst()) {
						result = c.getString(0);
					}
				}
			}
			c.close();
			onStringResult(result);
			return false;
		} else if (mSelectType.equals(SQLiteSelectType.StringArray)) {
			List<String> result = new ArrayList<String>();
			if (!c.isClosed()) {
				if (c.getColumnCount() != 0) {
					if (c.moveToFirst()) {
						do {
							result.add(c.getString(0));
						} while (c.moveToNext());
					}
				}
			}
			c.close();
			onStringArrayResult(result.toArray(new String[0]));
			return false;
		} else if (mSelectType.equals(SQLiteSelectType.Cursor)) {
			return onCursorResult(c);
		}
		return true;
	}

	public void execute(SQLiteDatabase db) {
		db.beginTransaction();
		Cursor c = db.rawQuery(this.getQuery(), this.getArguments());
		boolean close = this.returnResult(c);
		if (close) {
			c.close();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
	}
}
