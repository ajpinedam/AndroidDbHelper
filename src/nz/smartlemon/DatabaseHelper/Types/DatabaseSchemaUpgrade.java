package nz.smartlemon.DatabaseHelper.Types;

public class DatabaseSchemaUpgrade {
	public int UpgradeVersion = 0;
	public String[] OnUpgradeScripts = new String[0];
	public boolean CreatePost = false;
}
