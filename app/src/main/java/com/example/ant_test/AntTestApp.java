package com.example.ant_test;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.ant_test.crash_handler.VCrashHandler;
import com.example.ant_test.receiver.receiver1.ConnectivityReceiver;
import com.example.ant_test.show_dialog_on_screen_locked.ShowDialogOnScreenLockedReceiver;

import cn.jpush.android.api.JPushInterface;

public class AntTestApp extends Application{
	private com.example.ant_test.receiver.receiver1.ConnectivityReceiver cr1;
	private com.example.ant_test.receiver.receiver2.ConnectivityReceiver cr2;
	private ShowDialogOnScreenLockedReceiver showDialogOnScreenLockedReceiver;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(">>>>>>", "application oncreate!!!");
		cr1 = new ConnectivityReceiver();
		cr2 = new com.example.ant_test.receiver.receiver2.ConnectivityReceiver();
		
		IntentFilter filter = new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
		registerReceiver(cr1, filter);
		registerReceiver(cr2, filter);
		
		showDialogOnScreenLockedReceiver = new ShowDialogOnScreenLockedReceiver();
		filter = new IntentFilter(ShowDialogOnScreenLockedReceiver.RECEIVER_ACTION);
		registerReceiver(showDialogOnScreenLockedReceiver, filter);
		//sendShowDialogOnscreenLockedBroadcast();
        VCrashHandler.getInstance().init(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
	}

    /**
     * 待机的时候弹出全屏dialog
     */
	private void sendShowDialogOnscreenLockedBroadcast() {
		new Thread(){
			public void run() {
				try {
					sleep(5000);
					Intent intent = new Intent(ShowDialogOnScreenLockedReceiver.RECEIVER_ACTION);
					AntTestApp.this.sendBroadcast(intent);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}
	
}
