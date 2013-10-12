package nz.smartlemon.DatabaseHelper.Types;

public class DatabaseSchema {
	public int Version = 1;
	public String Name = "Database.db";
	public android.content.Context Context = null;
	public android.database.sqlite.SQLiteDatabase.CursorFactory CursorFactory = null;
	public String[] OnCreateScrips = new String[0];
	public DatabaseSchemaUpgrade[] OnUpgradeScripts = new DatabaseSchemaUpgrade[0];
	public DatabaseLocation Location = DatabaseLocation.ApplicationData;
	public String Directory = null;
}
