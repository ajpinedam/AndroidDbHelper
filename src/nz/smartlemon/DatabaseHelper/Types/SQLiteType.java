package nz.smartlemon.DatabaseHelper.Types;

public enum SQLiteType {
	//Base types
	TEXT(null, "TEXT"), INTEGER(null, "INTEGER"), REAL(null, "REAL"), BLOB(null, "BLOB"), NUMERIC(null, "NUMERIC"), NULL(null, "NULL"),
	//Default
	DEFAULT(null, "#_DEFAULT_TYPE"),
	//Secondary types as defined by http://www.sqlite.org/datatype3.html
	INT(INTEGER), TINYINT(INTEGER), SMALLINT(INTEGER), MEDIUMINT(INTEGER),
	BIGINT(INTEGER), UNSIGNED_BIG_INT(INTEGER), INT2(INTEGER), INT8(INTEGER),
	CHARACTER(TEXT), VARCHAR(TEXT), VARYING_CHARACTER(TEXT), NCHAR(TEXT),
	NATIVE_CHARACTER(TEXT), NVARCHAR(TEXT), CLOB(TEXT),
	DOUBLE(REAL), DOUBLE_PRECISION(REAL), FLOAT(REAL),
	DECIMAL(NUMERIC), BOOLEAN(NUMERIC), DATE(NUMERIC),
	DATETIME(NUMERIC);
	
	private SQLiteType mBaseType = null;
	private String mName = null;
	
	SQLiteType(SQLiteType baseType, String name) {
		mBaseType=baseType;
		mName = name;
	}
	
	SQLiteType(SQLiteType baseType) {
		mBaseType=baseType;
		mName = null;
	}
	
	public boolean equals(SQLiteType type){
		return this.getBaseSQLiteType().toString().equals(type.getBaseSQLiteType().toString());
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
	
	/**
	 * Returns the base type's SQLite string representation
	 */
	@Override
	public String toString(){
		SQLiteType type = this.getBaseSQLiteType();
		return type.mName;
	}
}
