package nz.smartlemon.DatabaseHelper.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import nz.smartlemon.DatabaseHelper.Types.SQLiteType;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SQLiteColumn {
	public String ColumnName();
	public SQLiteType ColumnType() default SQLiteType.TEXT;
	public boolean IsDefaultValue() default false;
	public String DefaultTextValue() default "";
	public int DefaultIntValue() default 0;
	public boolean DefaultBooleanValue() default false;
	public double DefaultDoubleValue() default 0.0d;
	public float DefaultFloatValue() default 0.0f;
	public long DefaultLongValue() default 0L;
	public byte DefaultByteValue() default 0;
	public short DefualtShortValue() default 0;
	public char DefaultCharValue() default '\u0000';
}
