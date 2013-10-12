package nz.smartlemon.DatabaseHelper.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nz.smartlemon.DatabaseHelper.Types.SQLiteConstants;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLiteForeignKey {
	public String ColumnName() default SQLiteConstants.DEFAULT_STRING_FOREIGN_OBJECT_ID;
}
