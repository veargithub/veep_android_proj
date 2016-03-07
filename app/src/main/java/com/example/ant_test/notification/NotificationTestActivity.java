package com.example.ant_test.notification;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NotificationTestActivity extends Activity{
	String packageName = ""; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		packageName = this.getPackageName();
//		Intent intent = new Intent(this, NotificationTestService.class);
//		startService(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		//isApplicationShowing(packageName, this);
//		new Thread () {
//			public void run() {
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				isApplicationShowing(packageName, NotificationTestActivity.this);
//			}
//		}.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		//isApplicationShowing(packageName, this);
//		new Thread () {
//			public void run() {
//				try {
//					Thread.sleep(500);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				isApplicationShowing(packageName, NotificationTestActivity.this);
//			}
//		}.start();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		isApplicationShowing(packageName, NotificationTestActivity.this);
	}
	
	public static boolean isApplicationShowing(String packageName, Context context) {
		boolean result = false;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo>appProcesses = am.getRunningAppProcesses();
		if (appProcesses != null) {
			for (RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
				//Log.d(">>>>>", "processName:" + runningAppProcessInfo.processName);
				if (runningAppProcessInfo.processName.equals(packageName)) {
					int status = runningAppProcessInfo.importance;
					Log.d(">>>>>", "status:" + status);
					if (status == RunningAppProcessInfo.IMPORTANCE_VISIBLE || status == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
						result = true;
					}
				}
			}
		}
		return result;
	}
}
