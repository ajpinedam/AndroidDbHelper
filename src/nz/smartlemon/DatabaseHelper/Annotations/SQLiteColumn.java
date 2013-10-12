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
}
