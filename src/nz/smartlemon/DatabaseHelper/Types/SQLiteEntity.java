package nz.smartlemon.DatabaseHelper.Types;

import java.lang.reflect.Field;

import nz.smartlemon.DatabaseHelper.Annotations.SQLiteAutoIncrement;
import nz.smartlemon.DatabaseHelper.Annotations.SQLiteColumn;
import nz.smartlemon.DatabaseHelper.Annotations.SQLiteForeignKey;
import nz.smartlemon.DatabaseHelper.Annotations.SQLiteID;
import nz.smartlemon.DatabaseHelper.Annotations.SQLiteIntegerPrimaryKey;
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
		
		/*
		String tableName = getTableName();
		SQLiteSelectStatement statement = new SQLiteSelectStatement();
		statement.setQuery("SELECT 1 FROM " + tableName);
		 */
		
		String create = getCreateScript();
		create = create + "";
	}

	private void populate() {

	}

	/**
	 * @return An instance of the entity class in question, the class of this
	 *         needs to match the class given in the contructor
	 */
	public abstract Object getInstance();

	public void update() {
		//ContentValues values = this.getContentValues();
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

	public ContentValues getContentValues() {
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
		StringBuilder end = new StringBuilder();
		for (Field field : fields) {
			ColumnDefinition c = getColumnDefinition(field);
			if(c == null){
				continue;
			}
			StringBuilder s = new StringBuilder();
			s.append(c.ColumnName);
			s.append(" ");
			if(c.ColumnType == null || c.ColumnType.equals(SQLiteType.DEFAULT)){
				s.append(SQLiteConstants.DEFAULT_STRING_COLUMN_TYPE);
			}else{
				s.append(c.ColumnType.toString());
			}
			s.append(" ");
			s.append(c.ColumnConstraints);
			if(c.DefaultValue != null){
				if(!c.ColumnConstraints.toUpperCase().contains("DEFAULT(")){
					if(c.ColumnType != null && c.ColumnType.equals(SQLiteType.INTEGER)){
						if(c.DefaultValue != null && c.DefaultValue.length() > 0){
							s.append(" DEFAULT(");
							for(char a : c.DefaultValue.toCharArray()){
								try{
									Integer i = Integer.parseInt(String.valueOf(a));
									if(i != null){
										s.append(String.valueOf(i));
									}
								}catch(NumberFormatException e){
									continue;
								}
							}
							s.append(")");
						}
					}else if(c.ColumnType != null && (c.ColumnType.equals(SQLiteType.NUMERIC) || c.ColumnType.equals(SQLiteType.REAL))){
						if(c.DefaultValue != null && c.DefaultValue.length() > 0){
							StringBuilder numeric = new StringBuilder();
							for(char a : c.DefaultValue.toCharArray()){
								if(a == '.'){
									if(!numeric.toString().contains(".")){
										numeric.append(".");
									}
								}else{
									try{
										Integer i = Integer.parseInt(String.valueOf(c));
										if(i != null){
											s.append(String.valueOf(i));
										}
									}catch(NumberFormatException e){
										continue;
									}
								}
							}
							String n = numeric.toString();
							if(n.length() > 0){
								if(n != "."){
									if(n.toCharArray()[n.length() - 1] != '.'){
										n += "0";
									}
									if(n.toCharArray()[0] != '.'){
										n = "0" + n;
									}
									s.append("DEFAULT(");
									s.append(n);
									s.append(")");
								}
							};
						}
					}else{
						c.ColumnConstraints += " DEFAULT('" + c.DefaultValue + "')";
					}
				}
			}
			if(c.AppendToEndOfTableDef != null){
				if(end.length() > 0){
					end.append(", ");				
				}
				end.append(c.AppendToEndOfTableDef);
			}
			if(s.length() > 0){
				if(sb.length() > 0){
					sb.append(", ");
				}
				sb.append(replaceFieldName(field, s.toString()));
			}
		}
		if(sb.length() == 0){
			throw new EntityException("No columns found for " + table + ", please implement at least one column");
		}
		while(table.contains(SQLiteConstants.STRING_CLASS_NAME)){
			table = table.replace(SQLiteConstants.STRING_CLASS_NAME, mClass.getName());
		}
		String create = "CREATE TABLE IF NOT EXISTS " + table + "(" + sb.toString() + (end.length() == 0 ? "" : ", " + end.toString()) + ")";
		while(create.contains(SQLiteConstants.STRING_TABLE_NAME)){
			create = create.replace(SQLiteConstants.STRING_TABLE_NAME, table);
		}
		return singleSpaces(create);
	}
	
	private static String singleSpaces(String s){
		s = s.trim();
		while(s.contains("  ")){
			s = s.replace("  ", " ");
		}
		return s;
	}
	
	private static String replaceFieldName(Field field, String sb){
		while(sb.contains(SQLiteConstants.STRING_FIELD_NAME)){
			sb.replace(SQLiteConstants.STRING_FIELD_NAME, field.getName());
		}
		return sb;
	}

	private ColumnDefinition getColumnDefinition(Field field) {
		SQLiteColumn column = field.getAnnotation(SQLiteColumn.class);
		SQLiteIntegerPrimaryKey key = field
				.getAnnotation(SQLiteIntegerPrimaryKey.class);
		SQLitePrimaryKey pkey = field.getAnnotation(SQLitePrimaryKey.class);
		SQLiteForeignKey fkey = field.getAnnotation(SQLiteForeignKey.class);
		SQLiteID id = field.getAnnotation(SQLiteID.class);
		SQLiteAutoIncrement inc = field
				.getAnnotation(SQLiteAutoIncrement.class);

		if (column == null && key == null && pkey == null && fkey == null
				&& id == null && inc == null) {
			return null;
		}

		ColumnDefinition c = new ColumnDefinition();

		if (column != null) {
			c.ColumnName = column.ColumnName();
			c.ColumnType = column.ColumnType();
			if (column.HasDefaultValue()) {
				c.DefaultValue = column.DefaultValue();
			}
			c.ColumnConstraints = column.Constraints();
			if (column.IntegerPrimaryKey()) {
				if (c.ColumnType.equals(SQLiteType.DEFAULT)
						|| !c.ColumnType.equals(SQLiteType.INTEGER)) {
					throw new EntityException(
							"SQLiteColumn.IntegerPrimaryKey is set to true but the SQLiteColumn.ColumnType is not set to SQLiteType.INTEGER or SQLiteType.DEFAULT");
				} else {
					c.ColumnType = SQLiteType.INTEGER;
				}
				if (!c.ColumnConstraints.toUpperCase().contains("PRIMARY KEY")) {
					c.ColumnConstraints += " PRIMARY KEY";
				}
			}
			if (column.IntegerPrimaryKeyAutoIncrement()) {
				if (c.ColumnType.equals(SQLiteType.DEFAULT)
						|| !c.ColumnType.equals(SQLiteType.INTEGER)) {
					throw new EntityException(
							"SQLiteColumn.IntegerPrimaryKey is set to true but the SQLiteColumn.ColumnType is not set to SQLiteType.INTEGER or SQLiteType.DEFAULT");
				} else {
					c.ColumnType = SQLiteType.INTEGER;
				}
				if (!c.ColumnConstraints.toUpperCase().contains("PRIMARY KEY")) {
					c.ColumnConstraints += " PRIMARY KEY";
				}
				if (!c.ColumnConstraints.toUpperCase()
						.contains("AUTOINCREMENT")) {
					c.ColumnConstraints += " AUTOINCREMENT";
				}
			}
			if (column.PrimaryKey()) {
				if (!c.ColumnConstraints.toUpperCase().contains("PRIMARY KEY")) {
					c.ColumnConstraints += " PRIMARY KEY";
				}
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

			if (column != null) {
				if (column.AutoIncrement()) {
					throw new EntityException(
							"SQLiteForeignKey cannot be present with SQLiteColumn.AutoIncrement");
				}
				if (column.IntegerPrimaryKey()) {
					throw new EntityException(
							"SQLiteForeignKey cannot be present with SQLiteColumn.IntegerPrimaryKey");
				}
				if (column.PrimaryKey()) {
					throw new EntityException(
							"SQLiteForeignKey cannot be present with SQLiteColumn.PrimaryKey");
				}
				if (column.IntegerPrimaryKeyAutoIncrement()) {
					throw new EntityException(
							"SQLiteForeignKey cannot be present with SQLiteColumn.IntegerPrimaryKeyAutoIncrement");
				}
				if (c.ColumnConstraints.toUpperCase().contains("PRIMARY KEY")) {
					throw new EntityException(
							"SQLiteForeignKey cannot be present when SQLiteColumn.Constraints contains 'PRIMARY KEY'");
				}
				if (c.ColumnConstraints.toUpperCase()
						.contains("AUTO INCREMENT")) {
					throw new EntityException(
							"SQLiteForeignKey cannot be present when SQLiteColumn.Constraints contains 'AUTO INCREMENT'");
				}
			}
			String ftable = "";
			String fcolumn = "";
			Class<?> cls = field.getType();
			if (cls.getSuperclass() != null) {
				while (cls.getSuperclass() != null) {
					Class<?> cls2 = cls.getSuperclass();
					if (cls2 == null) {
						break;
					}
					if (cls2.equals(SQLiteEntity.class)) {
						break;
					} else {
						cls = cls2;
					}
				}
				if (cls.getSuperclass() == null
						|| !cls.getSuperclass().equals(SQLiteEntity.class)) {
					throw new EntityException(
							"SQLiteForeignKey can only be used with a class extending SQLiteEntity!");
				}
			} else {
				throw new EntityException(
						"SQLiteForeignKey can only be used with a class extending SQLiteEntity!");
			}
			SQLiteTable t = cls.getAnnotation(SQLiteTable.class);
			if (t != null) {
				ftable = t.TableName();
			} else {
				ftable = cls.getName();
			}
			if (fkey.ForeignColumnName().equals(
					SQLiteConstants.DEFAULT_STRING_ID)) {
				for (Field f : cls.getFields()) {
					SQLiteID i = f.getAnnotation(SQLiteID.class);
					SQLiteIntegerPrimaryKey k = f
							.getAnnotation(SQLiteIntegerPrimaryKey.class);
					SQLiteColumn a = f.getAnnotation(SQLiteColumn.class);
					if (i != null) {
						if (!i.ColumnName().equals(
								SQLiteConstants.DEFAULT_STRING_ID)) {
							fcolumn = i.ColumnName();
							break;
						}
					}
					if (k != null && fcolumn == null) {
						if (!k.ColumnName()
								.equals(SQLiteConstants.DEFAULT_STRING_PRIMARY_KEY_COLUMN_NAME)) {
							fcolumn = k.ColumnName();
							break;
						}
					}
					if (a != null && fcolumn == null) {
						if (!a.ColumnName().equals(
								SQLiteConstants.DEFAULT_STRING_COLUMN_NAME)) {
							if (a.IntegerPrimaryKey()
									|| a.IntegerPrimaryKeyAutoIncrement()
									|| a.PrimaryKey()
									|| a.Constraints().toUpperCase()
											.contains("PRIMARY KEY")) {
								fcolumn = a.ColumnName();
								break;
							}
						}
					}
				}
				if (fcolumn == null) {
					for (Field f : cls.getFields()) {
						SQLiteID i = f.getAnnotation(SQLiteID.class);
						SQLiteIntegerPrimaryKey k = f
								.getAnnotation(SQLiteIntegerPrimaryKey.class);
						SQLiteColumn a = f.getAnnotation(SQLiteColumn.class);
						if (i != null && fcolumn == null) {
							fcolumn = i.ColumnName();
							break;
						} else if (k != null && fcolumn == null) {
							fcolumn = k.ColumnName();
							break;
						} else if (a != null) {
							if (a.IntegerPrimaryKey()
									|| a.IntegerPrimaryKeyAutoIncrement()
									|| a.PrimaryKey()
									|| a.Constraints().toUpperCase()
											.contains("PRIMARY KEY")) {
								fcolumn = a.ColumnName();
								break;
							}
						}
					}

				}
				if (fcolumn == null) {
					throw new EntityException(
							"No ID column can be found for the table '"
									+ ftable + "'");
				}
			} else {
				fcolumn = fkey.ForeignColumnName();
			}
			if (c.ColumnName == null) {
				if (fkey.ColumnName() != null) {
					c.ColumnName = fkey.ColumnName();
				}
			}
			c.AppendToEndOfTableDef = " FORIEN KEY(" + c.ColumnName
					+ ") REFERENCES " + ftable + "(" + fcolumn + ")";
			return c;
		}

		if (key != null) {
			if (c.ColumnName == null) {
				c.ColumnName = key.ColumnName();
			}
			if (column != null
					&& (!c.ColumnType.equals(SQLiteType.DEFAULT) || !c.ColumnType
							.equals(SQLiteType.INTEGER))) {
				throw new EntityException(
						"The column SQLiteIntegerPrimaryKey should have the base type SQLiteType.INTEGER");
			}
			if (c.ColumnConstraints == null
					|| c.ColumnConstraints.length() == 0) {
				c.ColumnConstraints = " PRIMARY KEY";
			} else if (!c.ColumnConstraints.toUpperCase().contains(
					"PRIMARY KEY")) {
				c.ColumnConstraints += " PRIMARY KEY";
			}
		}

		if (id != null) {
			if (c.ColumnName == null) {
				c.ColumnName = id.ColumnName();
			}
			if (column != null
					&& (!c.ColumnType.equals(SQLiteType.DEFAULT) || !c.ColumnType
							.equals(SQLiteType.INTEGER))) {
				throw new EntityException(
						"The column SQLiteID should have the base type SQLiteType.INTEGER");
			}
			c.ColumnType = SQLiteType.INTEGER;
			if (c.ColumnConstraints == null
					|| c.ColumnConstraints.length() == 0) {
				c.ColumnConstraints = "";
				if (id.PrimaryKey()) {
					c.ColumnConstraints += " PRIMARY KEY";
				}
				if (id.AutoIncrement()) {
					c.ColumnConstraints += " AUTO INCREMENT";
				}
			} else {
				if (id.PrimaryKey()) {
					if (!c.ColumnConstraints.toUpperCase().contains(
							"PRIMARY KEY")) {
						c.ColumnConstraints += " PRIMARY KEY";
					}
				}
				if (id.AutoIncrement()) {
					if (!c.ColumnConstraints.toUpperCase().contains(
							"AUTO INCREMENT")) {
						c.ColumnConstraints += " AUTO INCREMENT";
					}
				}
			}
		}
		if (pkey != null) {
			if (c.ColumnName != null) {
				c.ColumnName = pkey.ColumnName();
			}
			if (c.ColumnConstraints == null
					|| c.ColumnConstraints.length() == 0) {
				c.ColumnConstraints = "";
			}
			if (!c.ColumnConstraints.toUpperCase().contains("PRIMARY KEY")) {
				c.ColumnConstraints += " PRIMARY KEY";
			}
			if (pkey.AutoIncrement()
					&& !c.ColumnConstraints.toUpperCase().contains(
							"AUTOINCREMENT")) {
				c.ColumnConstraints += " AUTOINCREMENT";
			}
		}
		return c;
	}

	private class ColumnDefinition {
		public String ColumnName = null;
		public SQLiteType ColumnType = SQLiteType.DEFAULT;
		public String DefaultValue = null;
		public String ColumnConstraints = null;
		public String AppendToEndOfTableDef = null;
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

	/*
	private boolean getTableAsync() {
		SQLiteTable table = getTable();
		if (table == null) {
			return SQLiteConstants.DEFAULT_BOOLEAN_TABLE_ASYNC;
		} else {
			return table.Async();
		}
	}*/

	private Field[] getFieldInfo() {
		return mClass.getDeclaredFields();
	}
}
