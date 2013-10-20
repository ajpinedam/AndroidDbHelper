package nz.smartlemon.DatabaseHelper.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;

class SQLiteStatement {

	protected enum SQLiteStatmentType {
		Select(0), Update(1), Insert(2), Delete(3), Exec(5);

		private int mType = 0;

		SQLiteStatmentType(int type) {
			mType = type;
		}

		public boolean equals(SQLiteStatmentType type) {
			return this.mType == type.mType;
		}
	}

	private SQLiteStatmentType mType = SQLiteStatmentType.Exec;

	private String[] mArguments = new String[0];
	private String mQuery = null;
	private String mTable = null;
	private ContentValues mContentValues = new ContentValues();
	private UUID mQueryID = null;
	private String mWhereClause = null;
	private String mNullColumnHack = null;

	protected SQLiteStatement(SQLiteStatmentType type) {
		mType = type;
		mQueryID = UUID.randomUUID();
	}

	protected SQLiteStatement(SQLiteStatmentType type, String query) {
		this(type);
		this.setQuery(query);
	}

	protected SQLiteStatement(SQLiteStatmentType type, String table,
			ContentValues contentValues) {
		this(type);
		this.setTable(table);
		this.setContentValues(contentValues);
	}

	protected SQLiteStatement(SQLiteStatmentType type, String table,
			ContentValues contentValues, String whereClause) {
		this(type);
		this.setTable(table);
		this.setContentValues(contentValues);
		this.setWhereClause(whereClause);
	}

	protected SQLiteStatement(SQLiteStatmentType type, String table,
			ContentValues contentValues, String whereClause, String[] arguments) {
		this(type, arguments);
		this.setTable(table);
		this.setContentValues(contentValues);
		this.setWhereClause(whereClause);
	}

	protected SQLiteStatement(SQLiteStatmentType type, ContentValues contentValues) {
		this(type);
		this.setContentValues(contentValues);
	}

	protected SQLiteStatement(SQLiteStatmentType type, String query,
			String[] arguments) {
		this(type, query);
		this.setArguments(arguments);
	}

	protected SQLiteStatement(SQLiteStatmentType type, String[] arguments) {
		this(type, null, arguments);
	}
	
	public UUID getQueryID(){
		return mQueryID;
	}
	
	protected void setNullColumnHack(String column) {
		mNullColumnHack = column;
	}

	protected String getNullColumnHack() {
		return mNullColumnHack;
	}

	protected void setWhereClause(String whereClause) {
		mWhereClause = whereClause;
	}

	protected String getWhereClause() {
		return mWhereClause;
	}

	protected void setContentValues(ContentValues contentValues) {
		if (contentValues != null) {
			mContentValues = contentValues;
		}
	}

	protected ContentValues getContentValues() {
		return mContentValues;
	}

	protected void setTable(String table) {
		mTable = table;
	}

	protected String getTable() {
		return mTable;
	}

	protected void setQuery(String query) {
		mQuery = query;
	}

	protected String getQuery() {
		return mQuery;
	}

	protected void setArguments(String... arguments) {
		if (arguments != null) {
			mArguments = arguments;
		}
	}

	protected void appendArguments(String... arguments) {
		List<String> args = new ArrayList<String>();
		for (String s : mArguments) {
			args.add(s);
		}
		for (String s : arguments) {
			args.add(s);
		}
		mArguments = args.toArray(new String[0]);
	}

	protected void setArgument(String argument) {
		mArguments = new String[] { argument };
	}

	protected String getArgument() {
		if (mArguments == null || mArguments.length == 0) {
			return null;
		}
		return mArguments[0];
	}

	protected String[] getArguments() {
		if (mArguments != null) {
			return mArguments;
		}
		return new String[0];
	}
	
	public SQLiteStatmentType getBaseType(){
		return mType;
	}
}
