package com.example.utils;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.example.model.Note;
import com.example.receiver.AlarmReceiver;
import com.example.service.AlarmService;

/**
 * helper class that help generate unique id for notification
 * 
 * @author tuanda
 * 
 */
public class NotificationHelper {

	private AlarmManager alarmManager;
	private final static AtomicInteger c = new AtomicInteger(0);
	private Context mContext;
	public static NotificationHelper sInstance;

	private NotificationHelper(Context context) {
		mContext = context;
		alarmManager = (AlarmManager) mContext
				.getSystemService(Context.ALARM_SERVICE);
	}

	public static synchronized NotificationHelper getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new NotificationHelper(context);
		}
		return sInstance;
	}

	public static int getID() {
		return c.incrementAndGet();
	}

	/**
	 * schedule notification message that notify user that note's time is
	 * happening,on
	 * 
	 */
	public void schedule(Note note) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(Calendar.SECOND, 0);
		//int minute = rightNow.get(Calendar.MINUTE);
		Calendar scheduleTime  = Calendar.getInstance();
		scheduleTime.setTimeInMillis(note.getAlarmTime());
		if (scheduleTime.compareTo(rightNow)>=0) {
			Intent myIntent = new Intent(mContext, AlarmReceiver.class);
			Log.e("updatelogic","schedule noteid "+note.getId());
			myIntent.setData(Uri.parse("timer:" + note.getId()));
			myIntent.putExtra(AlarmService.EXTRA_NOTE_ID, note.getId());
			myIntent.putExtra(AlarmService.EXTRA_TITLE, note.getTitle());
			myIntent.putExtra(AlarmService.EXTRA_NOTE, note.getNoteDetail());
			PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
					0, myIntent, PendingIntent.FLAG_CANCEL_CURRENT);
			alarmManager.set(AlarmManager.RTC, note.getAlarmTime(), pendingIntent);
		}
	}
}
