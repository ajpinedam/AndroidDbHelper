package nz.smartlemon.DatabaseHelper.Types;

public class SQLiteConstants {
	public static final String STRING_CLASS_NAME = "#_classname";
	public static final String STRING_TABLE_NAME = "#_tablename";
	public static final String STRING_FIELD_NAME = "#_fieldname";

	public static final boolean DEFAULT_BOOLEAN_ID_PRIMARY_KEY = true;
	public static final boolean DEFAULT_BOOLEAN_ID_AUTO_INCREMENT = true;
	public static final boolean DEFAULT_BOOLEAN_COLUMN_PRIMARY_KEY = false;
	public static final boolean DEFAULT_BOOLEAN_COLUMN_AUTO_INCREMENT = false;
	public static final boolean DEFAULT_BOOLEAN_COLUMN_INTEGER_PRIMARY_KEY = false;
	public static final boolean DEFAULT_BOOLEAN_COLUMN_INTEGER_PRIMARY_KEY_AUTO_INCREMENT = false;
	public static final boolean DEFAULT_BOOLEAN_TABLE_ASYNC = true;

	public static final String DEFAULT_STRING = "[defaultstring]";
	public static final String DEFAULT_STRING_COLUMN_TYPE = "TEXT";
	public static final String DEFAULT_STRING_COLUMN_NAME = SQLiteConstants.STRING_FIELD_NAME;
	public static final String DEFAULT_STRING_PRIMARY_KEY_COLUMN_NAME = SQLiteConstants.STRING_TABLE_NAME
			+ "_ID";
	public static final String DEFAULT_STRING_TABLE_NAME = SQLiteConstants.STRING_CLASS_NAME;
	public static final String DEFAULT_STRING_ID = SQLiteConstants.DEFAULT_STRING_PRIMARY_KEY_COLUMN_NAME;
	public static final String DEFAULT_STRING_FOREIGN_OBJECT_ID = STRING_FIELD_NAME
			+ "_ID";
	
}
