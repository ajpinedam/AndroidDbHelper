DbHelper for android

Creating an instance of DbHelper:

```java
public class MainActivity extends Activity  
	implements DbHelper.OnDatabaseLoadedListener{

	private DatabaseSchema mSchema = null;
	private DbHelper mDbHelper = null;
	
	@Override
	public void onCreate(Bundle b){
		super.onCreate(b);
		mSchema = new DatabaseSchema();
		mSchema.Version = 1;
		mSchema.Context = this.getApplicationContext();
		mSchema.Location = DatabaseSchema.DatabaseLocation.ApplicationData;
		mSchema.Name = "Test.db";
		mSchema.CursorFactory = null;
		mSchema.OnCreateScrips = new String[]{
				"CREATE TABLE IF NOT EXISTS test1 (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)",
				"CREATE TABLE IF NOT EXISTS test2 (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)"
		};
		boolean async = true;
		DbHelper.OnDatabaseLoadedListener listener = this;
		mDbHelper = new DbHelper(mSchema, async, listener);
	}
}
```
	
Once the database is loaded you can start executing queries:

```java
public class MainActivity extends Activity  
	implements DbHelper.OnDatabaseLoadedListener, 
				SQLiteInsertStatement.OnResultListener, 
				SQLiteSelectStatement.OnResultListener {

	...
	
	@Override
	public void onDatabaseLoaded() {
		Log.d("Database", "Database loaded");
		
		SQLiteInsertStatement insertStatement = new SQLiteInsertStatement();
		
		ContentValues values = new ContentValues();
		values.put("name", "Test name");
		insertStatement.setContentValues(values);
		insertStatement.setTable("test");
		insertStatement.setOnResultListener(this);
		mDbHelper.Execute(insertStatement);
	}

	@Override
	public void onDatabaseError(String error) {
		Log.e("Database", error);
	}

	@Override
	public void onInsertResult(SQLiteInsertStatement statement, long insertedId) {
		Log.d("Database", "Inserted: " + insertedId);
		
		SQLiteSelectStatement selectStatement = new SQLiteSelectStatement();
		selectStatement.setQuery("SELECT name FROM test");
		selectStatement.setOnResultListener(this);
		mDbHelper.Execute(selectStatement);
	}

	@Override
	public void onStringResult(SQLiteSelectStatement statement, String result) {
		Log.d("Database", "Selected: " + result);
	}

	@Override
	public void onStringArrayResult(SQLiteSelectStatement statement,
			String[] result) {
	}

	@Override
	public boolean onCursorResult(SQLiteSelectStatement statement, Cursor result) {
		return true;
	}
}
```

Remember to open and close the database when you aren't using it using:

```java
//This will open a new database object 
//if there isn't already one there
mDbHelper.open(); 

//This will close the database object
//and stop any transactions if needed
mDbHelper.close();
```

If you wish to use the database object yourself you can use

```java
//This will always get a Writable database
//as you can both read and write from it
//If their is one still open it will use
//that
mDbHelper.getDatabase();
```
		
To create a database use a DatabaseSchema object, this will provide the details needed
to create the DbHelper:

```java
DatabaseSchema schema = new DatabaseSchema();

//This needs to be above 1
schema.Version = 1;

//The name of the database
schema.Name = "Test.db";

//You can specify whether you want the database to be stored
//in a specific directory or with the applications data
//schema.Location = DatabaseSchema.DatabaseLocation.Directory;
schema.Location = DatabaseSchema.DatabaseLocation.ApplicationData;

//When you specify schema.Location as directory set the directory
//Make sure you have access to this
//Also make sure you have write to external storage enabled
//schema.Directory = "/sdcard/TestDatabase/";

//When you have schema.Location as ApplicationData you
//can set the context of the database
schema.Context = this.getApplicationContext();

//The CursorFactory for the database
//null for default
schema.CursorFactory = null;

//The scripts to run when the database is created
schema.OnCreateScrips = new String[]{
	"CREATE TABLE IF NOT EXISTS test (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)"
};

//The scrips to run on each upgrade, the ones that are run are 
//set by changing the version number.

DatabaseSchemaUpgrade upgrade = new DatabaseSchemaUpgrade();
//This will only be run when the old version of the database is under 2
//and the new version is equal to or over 2
upgrade.UpgradeVersion = 2;

//The scripts to run when this upgrade is run
upgrade.OnUpgradeScripts = new String[]{
	"DROP TABLE test"
};

//Flag to say whether onCreate should be called post to running these scripts
//This is good in the case of drop table scripts.
upgrade.CreatePost = true;

//Set the upgrade scripts to run
mSchema.OnUpgradeScripts = new DatabaseSchemaUpgrade[]{upgrade};
```
		
You can change whether to run the scripts in an asynchronous way or a synchronous way

```java
//tells us whether the database is running in an asynchronous way or not
mDbHelper.isAsync();//This is the same as mDbHelper.isAsynchronous();

//Default true, sets whether the database should run in an
//asynchronous way
mDbHelper.setAsync(true);//This is the same as mDbHelper.setAsynchronous(boolean async);
```
		
You can specify different things when creating the database helper

```java
//Default constructor 
DbHelper dbHelper = new DbHelper(DatabaseSchema schema);

//Specify a on database load listener
DbHelper dbHelper = new DbHelper(DatabaseSchema schema, DbHelper.OnDatabaseLoadedListener listener);

//Specify a on database load listener and whether the database is asynchronous
DbHelper dbHelper = new DbHelper(DatabaseSchema schema, boolean async, DbHelper.OnDatabaseLoadedListener listener);
```