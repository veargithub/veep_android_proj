package com.example.ant_test.multireceiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * @Title: ReceiverTest1.java
 * @Package com.example.ant_test.multireceiver
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-4-11 下午7:18:52
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ReceiverTest1 extends BroadcastReceiver {
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
    private final String TAG = "ReceiverTest#1";

    @Override
    public void onReceive(Context context, Intent intent) {
    	String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            
            connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connectivityManager.getActiveNetworkInfo();
            if(info != null && info.isAvailable()) { 	
                String name = info.getTypeName().toLowerCase();
               
                ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    			List<RunningTaskInfo> a = mActivityManager.getRunningTasks(1);
    			
               // this.notifyNetworkStatus(true, name);
            } else {
            	
            	//this.notifyNetworkStatus(false, "");
            }
        }
    }
}
