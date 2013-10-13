package nz.smartlemon.DatabaseHelper.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nz.smartlemon.DatabaseHelper.Types.SQLiteConstants;
import nz.smartlemon.DatabaseHelper.Types.SQLiteType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLiteColumn {
	/**
	 * The name of the column, this is defaulted to SQLiteConstants.DEFAULT_STRING_COLUMN_NAME 
	 */
	public String ColumnName() default SQLiteConstants.DEFAULT_STRING_COLUMN_NAME;
	/**
	 * The column type
	 */
	public SQLiteType ColumnType() default SQLiteType.DEFAULT;
	/**
	 * Specifies whether the default value is set;
	 */
	public boolean HasDefaultValue() default false;
	/**
	 * The default value of the column
	 */
	public String DefaultValue() default SQLiteConstants.DEFAULT_STRING;
	/**
	 * The constraints on the column
	 */
	public String Constraints() default "";
	/**
	 * Specifies whether to auto increment the column (Column type needs to be SQLiteType.INTEGER for this)
	 */
	boolean AutoIncrement() default SQLiteConstants.DEFAULT_BOOLEAN_COLUMN_AUTO_INCREMENT;
	/**
	 * Specifies whether the column is a primary key
	 */
	public boolean PrimaryKey() default SQLiteConstants.DEFAULT_BOOLEAN_COLUMN_PRIMARY_KEY;
	/**
	 * Specifies whether the column is a integer primary key auto increment
	 */
	public boolean IntegerPrimaryKeyAutoIncrement() default SQLiteConstants.DEFAULT_BOOLEAN_COLUMN_INTEGER_PRIMARY_KEY_AUTO_INCREMENT;
	/**
	 * Specifies whether the column is a integer primary key
	 */
	public boolean IntegerPrimaryKey() default SQLiteConstants.DEFAULT_BOOLEAN_COLUMN_INTEGER_PRIMARY_KEY;
}
