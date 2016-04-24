package com.openarena.model.receivers;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;

import com.openarena.R;
import com.openarena.controllers.DBManager;
import com.openarena.model.objects.Fixture;
import com.openarena.util.Const;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationBroadcastReceiver extends BroadcastReceiver {

	public static final String TAG = "NotificationBroadcastReceiver";
	private static final String FIXTURE = "fixture";

	@Override
	public void onReceive(Context context, Intent intent) {
		PowerManager powerManager = (PowerManager) context.getSystemService(
				Context.POWER_SERVICE
		);
		PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, TAG
		);
		wakeLock.acquire();
		Fixture fixture = intent.getParcelableExtra(FIXTURE);
		if (fixture != null) {
			Notification notification = new NotificationCompat.Builder(context)
					.setAutoCancel(true)
					.setSmallIcon(R.mipmap.ic_launcher)
					.setTicker(context.getString(R.string.notification_fixture_ticker))
					.setContentTitle(String.format(context.getString(
							R.string.notification_fixture_title),
							new SimpleDateFormat("HH:mm", Locale.getDefault())
									.format(new Date(fixture.getDate())))
					)
					.setContentText(String.format(
							context.getString(R.string.notification_fixture_text),
							fixture.getHomeTeamName(), fixture.getAwayTeamName())
					)
					.setColor(ContextCompat.getColor(context, R.color.primary))
					.setLights(ContextCompat.getColor(context, R.color.primary), 200, 5000)
					.build();
			NotificationManagerCompat.from(context).notify(fixture.getID(), notification);
			fixture.setChange();
			DBManager.setFixture(fixture);
		}

		wakeLock.release();
	}

	public void setNotifiedFixture(Context context, Fixture fixture, boolean canNotified) {
		if (fixture != null) {
			Intent intent = new Intent(Const.ACTION_NOTIFICATION_BROADCAST);
			intent.putExtra(FIXTURE, fixture);
			if (canNotified) {
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						context, Const.REQUEST_CODE_NOTIFIED_FIXTURE, intent,
						PendingIntent.FLAG_CANCEL_CURRENT
				);
				setAlarm(context, fixture.getDate() - 1000 * 60 * 60, pendingIntent);
			}
			else {
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						context, Const.REQUEST_CODE_NOTIFIED_FIXTURE, intent,
						PendingIntent.FLAG_CANCEL_CURRENT
				);
				cancelAlarm(context, pendingIntent);
			}
		}
	}

	private void setAlarm(Context context, long time, PendingIntent pendingIntent){
		AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
	}

	private void cancelAlarm(Context context, PendingIntent pendingIntent) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
	}

}
