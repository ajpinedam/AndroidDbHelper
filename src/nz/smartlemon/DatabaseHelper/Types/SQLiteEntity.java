package nz.smartlemon.DatabaseHelper.Types;

import java.lang.reflect.Field;

import nz.smartlemon.DatabaseHelper.Annotations.SQLiteAutoIncrement;
import nz.smartlemon.DatabaseHelper.Annotations.SQLiteColumn;
import nz.smartlemon.DatabaseHelper.Annotations.SQLiteForeignKey;
import nz.smartlemon.DatabaseHelper.Annotations.SQLiteID;
import nz.smartlemon.DatabaseHelper.Annotations.SQLitePrimaryKey;
import nz.smartlemon.DatabaseHelper.Annotations.SQLiteTable;
import android.content.ContentValues;

public abstract class SQLiteEntity {

	private Long mBaseID = null;

	private Class<? extends SQLiteEntity> mClass = null;

	protected SQLiteEntity(Class<? extends SQLiteEntity> cls, Long id) {
		this(cls);
		mBaseID = id;
		if (mBaseID != null) {
			populate();
		}
	}

	protected SQLiteEntity(Class<? extends SQLiteEntity> cls) {
		mClass = cls;

		String tableName = getTableName();
		SQLiteSelectStatement statement = new SQLiteSelectStatement();
		statement.setQuery("SELECT 1 FROM " + tableName);

	}

	private void populate() {

	}

	/**
	 * @return An instance of the entity class in question, the class of this
	 *         needs to match the class given in the contructor
	 */
	public abstract Object getInstance();

	public void update() {
		ContentValues values = this.getContentValues();
	}

	/**
	 * Deletes the Object from the SQLiteDatabase
	 */
	public void delete() {
		// Long id = getID();

	}

	/**
	 * Inserts the Object into the SQLiteDatabase
	 */
	public void insert() {
		// ContentValues values = this.getContentValues();
	}

	private ContentValues getContentValues() {
		Object instance = getInstance();
		if (!instance.getClass().equals(mClass)) {
			throw new EntityException("The instance given for the table \""
					+ getTableName() + "\" does not match the class given.");
		}
		ContentValues values = new ContentValues();

		return values;
	}

	/**
	 * @return The id of the object
	 */
	public Long getID() {
		return mBaseID;
	}

	private String getCreateScript() {
		String table = getTableName();
		Field[] fields = getFieldInfo();

		StringBuilder sb = new StringBuilder();

		for (Field field : fields) {

		}
		return null;
	}

	private String getColumnDefinition(Field field) {
		StringBuilder sb = new StringBuilder();
		SQLiteColumn column = field.getAnnotation(SQLiteColumn.class);
		SQLitePrimaryKey key = field.getAnnotation(SQLitePrimaryKey.class);
		SQLiteForeignKey fkey = field.getAnnotation(SQLiteForeignKey.class);
		SQLiteID id = field.getAnnotation(SQLiteID.class);
		SQLiteAutoIncrement inc = field
				.getAnnotation(SQLiteAutoIncrement.class);

		

		String columnname = null;
		SQLiteType type = SQLiteType.DEFAULT;
		String constraints = null;

		String defaultvalue = null;

		if (column != null) {
			columnname = column.ColumnName();
			type = column.ColumnType();
			if (column.HasDefaultValue()) {
				defaultvalue = column.DefaultValue();
			}
			constraints += column.Constraints();
		}

		if (key != null) {
			if (columnname == null) {
				columnname = key.ColumnName();
			}
			if (constraints == null || constraints.length() == 0) {
				constraints = " PRIMARY KEY";
			} else if (!constraints.contains("PRIMARY KEY")) {
				constraints += " PRIMARY KEY";
			}
		}
		
		if (fkey != null) {
			if (key != null) {
				throw new EntityException(
						"SQLiteForeignKey cannot be present with SQLitePrimaryKey");
			}
			if (id != null) {
				throw new EntityException(
						"SQLiteForeignKey cannot be present with SQLiteID");
			}
			if (inc != null) {
				throw new EntityException(
						"SQLiteForeignKey cannot be present with SQLiteAutoIncrement");
			}
			
			//Return here as the others don't matter
			return null;
		}

		if (id != null) {
			if (columnname == null) {
				columnname = id.ColumnName();
			}
			if (column != null
					&& !type.getBaseSQLiteType().getBaseSQLiteType()
							.equals(SQLiteType.INTEGER)) {
				throw new EntityException(
						"The column SQLiteID should have the base type SQLiteType.INTEGER");
			}
			type = SQLiteType.INTEGER;
			if (constraints == null || constraints.length() == 0) {
				constraints = "";
				if (id.PrimaryKey()) {
					constraints += " PRIMARY KEY";
				}
				if (id.AutoIncrement()) {
					constraints += " AUTO INCREMENT";
				}
			} else {
				if (id.PrimaryKey()) {
					if (!constraints.contains("PRIMARY KEY")) {
						constraints += " PRIMARY KEY";
					}
				}
				if (id.AutoIncrement()) {
					if (!constraints.contains("AUTO INCREMENT")) {
						constraints += " AUTO INCREMENT";
					}
				}
			}
		}
		
		type.toString();

		if (fkey != null) {

		}

		return sb.toString();
	}

	private SQLiteTable getTable() {
		return mClass.getAnnotation(SQLiteTable.class);
	}

	private String getTableName() {
		SQLiteTable table = getTable();
		if (table == null) {
			return mClass.getName();
		} else {
			return table.TableName();
		}
	}

	private boolean getTableAsync() {
		SQLiteTable table = getTable();
		if (table == null) {
			return SQLiteConstants.DEFAULT_BOOLEAN_TABLE_ASYNC;
		} else {
			return table.Async();
		}
	}

	private Field[] getFieldInfo() {
		return mClass.getFields();
	}
}
