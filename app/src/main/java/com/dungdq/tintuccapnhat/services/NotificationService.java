package com.dungdq.tintuccapnhat.services;

import java.util.Calendar;

import com.dungdq.tintuccapnhat.receiver.NotificationReceiver;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

public class NotificationService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Context mContext = this.getApplicationContext();
		mContext.registerReceiver(new NotificationReceiver(), new IntentFilter(
				""));

		// boolean alarmUp =
		// (PendingIntent.getBroadcast(this.getApplicationContext(), 0,
		// new Intent(this.getApplicationContext(), NotificationReceiver.class),
		// PendingIntent.FLAG_NO_CREATE) != null);

//		SharedPreferences pref = getSharedPreferences("set_alarm", 0);
//		SharedPreferences.Editor editor = pref.edit();

		Intent myIntent = new Intent(this.getApplicationContext(),
				NotificationReceiver.class);
		PendingIntent pendingIntent;
		boolean alarmUp;

		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		Calendar now = Calendar.getInstance();

//		if (!pref.getBoolean("active", false)) {
//			Log.e("aaaaaaaaaaaaaaa", "aaaaaaaaaaaaaaaaaaaaaa");

			// Morning
			now.set(Calendar.HOUR_OF_DAY, 8);
			now.set(Calendar.MINUTE, 00);
			now.set(Calendar.SECOND, 0);
			myIntent.putExtra("status", 0);
			alarmUp = (PendingIntent.getBroadcast(this.getApplicationContext(), 0, 
			        myIntent, PendingIntent.FLAG_NO_CREATE) != null);
			pendingIntent = PendingIntent.getBroadcast(
					this.getApplicationContext(), 0, myIntent, 0);
			Log.e("alarmUp morning", alarmUp + "");
			if (!alarmUp) {
				Log.e("set alarm morning", "true");
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						now.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						pendingIntent);
			}

			// Afternoon
			now.set(Calendar.HOUR_OF_DAY, 13);
			now.set(Calendar.MINUTE, 00);
			now.set(Calendar.SECOND, 0);
			myIntent.putExtra("status", 1);
			alarmUp = (PendingIntent.getBroadcast(this.getApplicationContext(), 1, 
			        myIntent, PendingIntent.FLAG_NO_CREATE) != null);
			pendingIntent = PendingIntent.getBroadcast(
					this.getApplicationContext(), 1, myIntent, 0);
			Log.e("alarmUp afternoon", alarmUp + "");
			if (!alarmUp) {
				Log.e("set alarm afternoon", "true");
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						now.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						pendingIntent);
			}

			// Evening
			now.set(Calendar.HOUR_OF_DAY, 22);
			now.set(Calendar.MINUTE, 00);
			now.set(Calendar.SECOND, 0);
			myIntent.putExtra("status", 2);
			alarmUp = (PendingIntent.getBroadcast(this.getApplicationContext(), 2, 
			        myIntent, PendingIntent.FLAG_NO_CREATE) != null);
			pendingIntent = PendingIntent.getBroadcast(
					this.getApplicationContext(), 2, myIntent, 0);
			Log.e("alarmUp evening", alarmUp + "");
			if (!alarmUp) {
				Log.e("set alarm evening", "true");
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						now.getTimeInMillis(), AlarmManager.INTERVAL_DAY,
						pendingIntent);
			}

//			editor.putBoolean("active", true);
//			editor.commit();
//		}

		return super.onStartCommand(intent, flags, startId);
	}
}
