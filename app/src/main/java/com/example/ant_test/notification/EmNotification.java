package com.example.ant_test.notification;

import com.example.ant_test.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class EmNotification {
	private Context context;
	private NotificationManager notificationManager;
	private static int i;
	
	public EmNotification(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
    }
	
	public void notify(int icon, String message, String className) {
        Notification notification = new Notification();
        notification.icon = icon;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.when = System.currentTimeMillis();
        notification.tickerText = message;
        Class classType;
        try {
        	classType = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return;
		}
        Intent intent = new Intent(context, classType);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
        		intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, "test", message, contentIntent);
        notificationManager.notify(i++, notification);
    }
	
	public void notifyTest(int id) {
		Notification notification = new Notification();
		notification.icon = R.drawable.ic_launcher;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS;
        notification.when = System.currentTimeMillis();
        notification.tickerText = id + "";
        
        Intent intent = new Intent(context, NotificationTestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
        		new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(context, "test", id + "", contentIntent);
        notificationManager.notify(id, notification);
	}
}
