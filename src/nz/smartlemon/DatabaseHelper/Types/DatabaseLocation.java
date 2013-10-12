package nz.smartlemon.DatabaseHelper.Types;


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
