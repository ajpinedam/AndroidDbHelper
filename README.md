DbHelper for android

Creating an instance of DbHelper:

	:::java
		public class MainActivity extends Activity{
		
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
	
Once the database is loaded you can start executing queries:

	:::java
		public class MainActivity extends Activity{
		
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
