package nz.smartlemon.DatabaseHelper.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nz.smartlemon.DatabaseHelper.Types.SQLiteConstants;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLitePrimaryKey {
	public String ColumnName() default SQLiteConstants.DEFAULT_STRING_PRIMARY_KEY_COLUMN_NAME;
	public boolean AutoIncrement() default SQLiteConstants.DEFAULT_BOOLEAN_AUTO_INCREMENT;
}
