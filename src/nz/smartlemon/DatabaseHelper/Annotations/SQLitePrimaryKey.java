package nz.smartlemon.DatabaseHelper.Annotations;

import nz.smartlemon.DatabaseHelper.Types.SQLiteConstants;

public @interface SQLitePrimaryKey {
	public String ColumnName() default SQLiteConstants.DEFAULT_STRING_PRIMARY_KEY_COLUMN_NAME;
	public boolean AutoIncrement() default SQLiteConstants.DEFAULT_BOOLEAN_COLUMN_AUTO_INCREMENT;
}
