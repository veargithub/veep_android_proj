package com.example.ant_test.show_dialog_on_screen_locked;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

/**
 * @Title: ShowDialogOnScreenLockedReceiver.java
 * @Package com.example.ant_test.show_dialog_on_screen_locked
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2015-1-28 下午4:16:44
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ShowDialogOnScreenLockedReceiver extends BroadcastReceiver{

	public static final String RECEIVER_ACTION = "com.example.ant_test.show_dialog_on_screen_locked";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(RECEIVER_ACTION)) {
			Log.d(">>>>", "receive broadcast: " + RECEIVER_ACTION);
			KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);  
	        if (km.inKeyguardRestrictedInputMode()) {  
	            Intent alarmIntent = new Intent(context, ShowDialogOnScreenLockedActivity.class);  
	            alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
	            context.startActivity(alarmIntent);  
	        }
//			go(context);
		}
		
	}
	
	private void go(Context context) {
	    KeyguardManager km = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("IN");
        kl.disableKeyguard();
       
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl=pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "My_App");
        wl.acquire();
       
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        
        adb.setMessage("Testing");
        adb.setCancelable(false);
        adb.setNeutralButton("Close",new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        kl.reenableKeyguard();
                }
        });
        AlertDialog ad = adb.create();
        ad.getWindow().setType(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        ad.show();
       
        wl.release();

	}
 
}
