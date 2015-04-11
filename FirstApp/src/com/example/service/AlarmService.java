package com.example.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.firstapp.MainActivity;
import com.example.firstapp.NoteDetailActivity;
import com.example.firstapp.R;

/**
 * a service that notify a notification to the user ,use {@link}
 * {@link IntentService} to destroy the service after the notification was sent
 * 
 * @author tuanda
 * 
 */
public class AlarmService extends IntentService {
	public static final String EXTRA_TITLE = "TITLE";
	public static final String EXTRA_NOTE = "NOTE";
	public static final String EXTRA_NOTE_ID = "NOTE_ID";
	private NotificationManager alarmNotificationManager;

	public AlarmService() {
		super("alarm service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		sendNotification(intent.getLongExtra((EXTRA_NOTE_ID), -1),
				intent.getStringExtra(EXTRA_TITLE),
				intent.getStringExtra(EXTRA_NOTE));
	}

	private void sendNotification(long noteId, String title, String note) {
		alarmNotificationManager = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent noteDetailIntent = new Intent(this, NoteDetailActivity.class);
		noteDetailIntent.putExtra(MainActivity.EXTRA_NOTE_ID,
				Long.valueOf(noteId));
		
		//need to set dummy action to prevent extras to be dropped.
		//read more at http://stackoverflow.com/questions/3168484/pendingintent-works-correctly-for-the-first-notification-but-incorrectly-for-the
		noteDetailIntent.setAction(""+noteId);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				noteDetailIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
				this).setContentTitle(title)
				.setSmallIcon(R.drawable.ic_launcher)
				.setStyle(new NotificationCompat.BigTextStyle().bigText(note))
				.setContentText(note).setAutoCancel(true);
		alamNotificationBuilder.setContentIntent(contentIntent);
		Log.e("updatelogic","sendNotification " + noteId);
		alarmNotificationManager.notify((int)noteId,
				alamNotificationBuilder.build());
	}

}
