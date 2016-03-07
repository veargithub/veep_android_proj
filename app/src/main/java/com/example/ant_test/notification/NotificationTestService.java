package com.example.ant_test.notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class NotificationTestService extends Service{
	private EmNotification notification;
	private static int sId = 0;

	@Override
	public void onCreate() {
		super.onCreate();
		notification = new EmNotification(this);
		
		new Thread() {
			public void run() {
				for (int i = 0; i < 20; i++) {
					Log.d("notification_id", sId + "");
					notification.notifyTest(sId++);
					try {
						Thread.sleep(1 * 60 * 1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}



	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
