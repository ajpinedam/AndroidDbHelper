package nz.smartlemon.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataHelpers {
	
	public static String DateNow() {
		return convertDate(Calendar.getInstance());
	}

	public static String convertDate(Calendar date) {
		return convertDate(date.getTime());
	}

	public static String convertDate(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSZ",
				Locale.ENGLISH);
		return df.format(date);
	}

	public static Date convertDate(String date) {
		if (date == null)
			return null;
		Date r = null;
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSSZ",
				Locale.getDefault());
		try {
			r = df.parse(date);
		} catch (ParseException e) {
			df = new SimpleDateFormat("dd-MM-yyyy HH:mmZ", Locale.getDefault());
			try {
				r = df.parse(date);
			} catch (ParseException a) {
				e.printStackTrace();
			}
		}
		return r;
	}

	public static String convertBoolean(boolean value) {
		return value ? "1" : "0";
	}

	public static boolean convertBoolean(String value) {
		if(value == null)
			return false;
		return value.equals("1");
	}

}
