package nz.smartlemon.DatabaseHelper.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nz.smartlemon.DatabaseHelper.Types.SQLiteConstants;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLiteTable {
	public String TableName() default SQLiteConstants.DEFAULT_STRING_TABLE_NAME;
	boolean Async() default SQLiteConstants.DEFAULT_BOOLEAN_TABLE_ASYNC;
}
