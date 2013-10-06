package nz.smartlemon.DatabaseHelper;

public class DatabaseSchema {
	public int Version = 1;
	public String Name = "Database.db";
	public android.content.Context Context = null;
	public android.database.sqlite.SQLiteDatabase.CursorFactory CursorFactory = null;
	public String[] OnCreateScrips = new String[0];
	public DatabaseSchemaUpgrade[] OnUpgradeScripts = new DatabaseSchemaUpgrade[0];
	public DatabaseLocation Location = DatabaseLocation.ApplicationData;
	public String Directory = null;
	
	public enum DatabaseLocation {
		Directory(0), ApplicationData(1);

		private int mLocationType = 0;

		DatabaseLocation(int locationType) {
			mLocationType = locationType;
		}

		public boolean equals(DatabaseLocation location) {
			return this.mLocationType == location.mLocationType;
		}
	}
}
