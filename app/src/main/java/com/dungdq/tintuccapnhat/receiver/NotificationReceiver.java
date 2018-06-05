package com.dungdq.tintuccapnhat.receiver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.dungdq.tintuccapnhat.MainActivity;
import com.dungdq.tintuccapnhat.R;
import com.dungdq.tintuccapnhat.utils.Const;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class NotificationReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		int timerStatus = intent.getIntExtra("status", 0);
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
		
		Calendar now = Calendar.getInstance();

	    int hour = now.get(Calendar.HOUR_OF_DAY);
	    int minute = now.get(Calendar.MINUTE);
	    
	    Log.e("Current time", now.getTime() + "");
	    
	    // Notification builder
	    intent = new Intent(context, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		builder.setAutoCancel(true);
		builder.setSmallIcon(R.drawable.ic_notification);
		builder.setLargeIcon(BitmapFactory.decodeResource(
				context.getResources(), R.drawable.ic_launcher));
//		builder.setWhen(System.currentTimeMillis());
		builder.setContentTitle(context.getString(R.string.app_name));
		builder.setTicker(context.getString(R.string.app_name));
		builder.setContentIntent(pendingIntent);
		builder.setDefaults(Notification.DEFAULT_ALL);

		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		
//		builder.setContentText(context.getString(R.string.notification_morning));
//		builder.setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.notification_morning)));
//
//		notificationManager.notify(0,
//				builder.build());

//	    try {
//			Date date = timeFormat.parse(hour + ":" + minute);
//			Date dateMorning = timeFormat.parse(Const.morning);
//			Date dateAfternoon = timeFormat.parse(Const.afternoon);
//			Date dateEvening = timeFormat.parse(Const.evening);
//			
			if (timerStatus == 0) {
				// Morning
				builder.setContentText(context.getString(R.string.notification_morning));
				builder.setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.notification_morning)));

				notificationManager.notify(0,
						builder.build());
			} else if (timerStatus == 1) {
				// Afternoon
				builder.setContentText(context.getString(R.string.notification_afternoon));
				builder.setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.notification_afternoon)));

				notificationManager.notify(0,
						builder.build());
			} else if (timerStatus == 2) {
				// Evening
				builder.setContentText(context.getString(R.string.notification_evening));
				builder.setStyle(new NotificationCompat.BigTextStyle().bigText(context.getString(R.string.notification_evening)));

				notificationManager.notify(0,
						builder.build());
			}
//			
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}

	}

}
