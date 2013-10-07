package nz.smartlemon.DatabaseHelper.Types;

public enum SQLiteType {
	//Base types
	TEXT, INTEGER, REAL, BLOB, NUMERIC, NULL,
	//Secondary types as defined by http://www.sqlite.org/datatype3.html
	INT(INTEGER), TINYINT(INTEGER), SMALLINT(INTEGER), MEDIUMINT(INTEGER),
	BIGINT(INTEGER), UNSIGNED_BIG_INT(INTEGER), INT2(INTEGER), INT8(INTEGER),
	CHARACTER(TEXT), VARCHAR(TEXT), VARYING_CHARACTER(TEXT), NCHAR(TEXT),
	NATIVE_CHARACTER(TEXT), NVARCHAR(TEXT), CLOB(TEXT),
	DOUBLE(REAL), DOUBLE_PRECISION(REAL), FLOAT(REAL),
	DECIMAL(NUMERIC), BOOLEAN(NUMERIC), DATE(NUMERIC),
	DATETIME(NUMERIC);

	private SQLiteType mBaseType = null;
	
	SQLiteType() {
		this(null);
	}
	
	SQLiteType(SQLiteType baseType) {
		mBaseType=baseType;
	}
	
	public SQLiteType getBaseSQLiteType(){
		SQLiteType lastType = this;
		boolean con = false;
		do{
			if(lastType.mBaseType != null){
				lastType = lastType.mBaseType;
			}else{
				con = false;
			}
		}while(con);
		return lastType;
	}
}
