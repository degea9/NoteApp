package com.example.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Context;
import android.util.DisplayMetrics;

/**a helper class 
 * @author tuanda
 *
 */
public class CommonUtil {
	public static int getScreenWidth(Context context) {
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		return display.widthPixels;
	}

	public static int getScreenHeight(Context context) {
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		return display.heightPixels;
	}

	public static String getDateFromMiliseconds(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		String month = String.format(Locale.getDefault(), "%02d",
				cal.get(Calendar.MONTH) + 1);
		String day = String.format(Locale.getDefault(), "%02d",
				cal.get(Calendar.DAY_OF_MONTH));
		String hour = String.format(Locale.getDefault(), "%02d",
				cal.get(Calendar.HOUR));
		String minute = String.format(Locale.getDefault(), "%02d",
				cal.get(Calendar.MINUTE));

		return day + "/" + month + " " + hour + ":" + minute;
	}

	public static String getDateFromCal(String pattern, Calendar cal) {
		DateFormat df = new SimpleDateFormat(pattern, Locale.getDefault());
		return df.format(cal.getTime());
	}

	public static String getDateFromMiliseconds(String pattern, long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return getDateFromCal(pattern, cal);
	}

}
