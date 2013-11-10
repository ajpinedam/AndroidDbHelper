package nz.smartlemon.DatabaseHelper.Extra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import nz.smartlemon.DatabaseHelper.DbHelper;
import nz.smartlemon.DatabaseHelper.Types.DatabaseLocation;
import nz.smartlemon.DatabaseHelper.Types.DatabaseSchema;
import nz.smartlemon.DatabaseHelper.Types.SQLiteEntity;
import nz.smartlemon.DatabaseHelper.Types.SQLiteEntityStore;
import android.os.Environment;

public class ExtraDatabaseHelpers {
	
	//Not working yet
	public static boolean CopyDatabase(DbHelper dbHelper, String backupDBPath) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			
			if (sd.canWrite()) {
				String currentDBPath = null;
				DatabaseSchema s = dbHelper.getSchema();
				if(s == null || s.Name == null || s.Name.length() == 0){
					return false;
				}
				if(s.Location.equals(DatabaseLocation.ApplicationData)){
					currentDBPath = "//data//"
							+ s.Context.getPackageName()
							+ "//databases//";
				}else if(s.Location.equals(DatabaseLocation.Directory)){
					currentDBPath = s.Directory + "//" + s.Name;
				}
				
				if(currentDBPath == null || currentDBPath.length() == 0){
					return false;
				}
				
				//Log.d("Path", backupDBPath);
				
				File backupDir = new File(backupDBPath);
				
				File currentDBDir = new File(data.toString() + currentDBPath);
					
				if (!backupDir.exists()) {
					if (!backupDir.mkdirs()) {
						//Log.d("Path", "Can not make directory");
						return false;
					}
				} else {
					//Log.d("Path", "Directory exists");
				}
				
				//String[] list = currentDBDir.list();
				if(!currentDBDir.canRead()){
					return false;
				}
				
				File currentDB = new File(currentDBDir, s.Name);

				File backupDB = new File(backupDir, s.Name);

				FileInputStream fis = new FileInputStream(currentDB);
				FileOutputStream fos = new FileOutputStream(backupDB);
				FileChannel src = fis.getChannel();
				FileChannel dst = fos.getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				fis.close();
				fos.close();
				return true;
			}
			//Log.e("DbCopy", "Can not write to sdcard");
			return false;
		} catch (Exception e) {
			//e.printStackTrace();
			return false;
		}
	}
}
