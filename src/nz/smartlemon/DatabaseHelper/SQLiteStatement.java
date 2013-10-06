package nz.smartlemon.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;

class SQLiteStatement {

	public enum SQLiteStatmentType {
		Select(0), Update(1), Insert(2), Delete(3), Exec(5);

		private int mType = 0;

		SQLiteStatmentType(int type) {
			mType = type;
		}

		public boolean equals(SQLiteStatmentType type) {
			return this.mType == type.mType;
		}
	}

	protected SQLiteStatmentType mType = SQLiteStatmentType.Exec;

	protected String[] mArguments = new String[0];
	protected String mQuery = null;
	protected String mTable = null;
	protected ContentValues mContentValues = new ContentValues();
	protected UUID mQueryID = null;
	protected String mWhereClause = null;
	protected String mNullColumnHack = null;

	public SQLiteStatement(SQLiteStatmentType type) {
		mType = type;
		mQueryID = UUID.randomUUID();
	}

	public SQLiteStatement(SQLiteStatmentType type, String query) {
		this(type);
		this.setQuery(query);
	}

	public SQLiteStatement(SQLiteStatmentType type, String table,
			ContentValues contentValues) {
		this(type);
		this.setTable(table);
		this.setContentValues(contentValues);
	}

	public SQLiteStatement(SQLiteStatmentType type, String table,
			ContentValues contentValues, String whereClause) {
		this(type);
		this.setTable(table);
		this.setContentValues(contentValues);
		this.setWhereClause(whereClause);
	}

	public SQLiteStatement(SQLiteStatmentType type, String table,
			ContentValues contentValues, String whereClause, String[] arguments) {
		this(type, arguments);
		this.setTable(table);
		this.setContentValues(contentValues);
		this.setWhereClause(whereClause);
	}

	public SQLiteStatement(SQLiteStatmentType type, ContentValues contentValues) {
		this(type);
		this.setContentValues(contentValues);
	}

	public SQLiteStatement(SQLiteStatmentType type, String query,
			String[] arguments) {
		this(type, query);
		this.setArguments(arguments);
	}

	public SQLiteStatement(SQLiteStatmentType type, String[] arguments) {
		this(type, null, arguments);
	}
	
	public UUID getQueryID(){
		return mQueryID;
	}
	
	public void setNullColumnHack(String column) {
		mNullColumnHack = column;
	}

	public String getNullColumnHack() {
		return mNullColumnHack;
	}

	public void setWhereClause(String whereClause) {
		mWhereClause = whereClause;
	}

	public String getWhereClause() {
		return mWhereClause;
	}

	public void setContentValues(ContentValues contentValues) {
		if (contentValues != null) {
			mContentValues = contentValues;
		}
	}

	public ContentValues getContentValues() {
		return mContentValues;
	}

	public void setTable(String table) {
		mTable = table;
	}

	public String getTable() {
		return mTable;
	}

	public void setQuery(String query) {
		mQuery = query;
	}

	public String getQuery() {
		return mQuery;
	}

	public void setArguments(String... arguments) {
		if (arguments != null) {
			mArguments = arguments;
		}
	}

	public void appendArguments(String... arguments) {
		List<String> args = new ArrayList<String>();
		for (String s : mArguments) {
			args.add(s);
		}
		for (String s : arguments) {
			args.add(s);
		}
		mArguments = args.toArray(new String[0]);
	}

	public void setArgument(String argument) {
		mArguments = new String[] { argument };
	}

	public String getArgument() {
		if (mArguments == null || mArguments.length == 0) {
			return null;
		}
		return mArguments[0];
	}

	public String[] getArguments() {
		if (mArguments != null) {
			return mArguments;
		}
		return new String[0];
	}
}
