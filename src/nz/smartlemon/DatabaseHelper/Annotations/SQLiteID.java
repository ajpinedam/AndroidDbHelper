package nz.smartlemon.DatabaseHelper.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nz.smartlemon.DatabaseHelper.Types.SQLiteConstants;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLiteID {
	public String ColumnName() default SQLiteConstants.DEFAULT_STRING_ID;
	public boolean AutoIncrement() default SQLiteConstants.DEFAULT_BOOLEAN_AUTO_INCREMENT;
	boolean PrimaryKey() default SQLiteConstants.DEFAULT_BOOLEAN_PRIMARY_KEY;
}
