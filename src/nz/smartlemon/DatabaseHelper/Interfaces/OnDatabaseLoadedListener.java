package nz.smartlemon.DatabaseHelper.Interfaces;

public interface OnDatabaseLoadedListener {

	public abstract void onDatabaseLoaded();

	public abstract void onDatabaseError(String error);
}
