package com.example.ant_test.running_activities;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.util.Log;

public class RunningActivity extends Activity{
	private static final String TAG = "RunningActivity";
	@Override
	public void onPause() {
		super.onPause();
		boolean result = CheckIsCurrentTask(this);
		boolean result2 = isScreenLocked(this);
		Log.d(TAG, result + " " + result2);
	}
	
	private boolean CheckIsCurrentTask(Context context) {
		if (isScreenLocked(context)) {
			return false;
		}
		ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> a = mActivityManager.getRunningTasks(1);
		if (!context.getPackageName().equals(a.get(0).baseActivity.getPackageName())) {
			return false;
		}
		return true;
	}
	
	private final boolean isScreenLocked(Context context) {
        android.app.KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(context.KEYGUARD_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }
}
