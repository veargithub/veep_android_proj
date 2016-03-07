package com.example.ant_test.receiver.receiver2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver {
	private ConnectivityManager connectivityManager;
	private NetworkInfo info;
    private final String TAG = "CR2";

    @Override
    public void onReceive(Context context, Intent intent) {
    	String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Log.d(TAG, "onReceive ConnectivityManager.CONNECTIVITY_ACTION");
            connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            info = connectivityManager.getActiveNetworkInfo();
            if(info != null && info.isAvailable()) { 	
                String name = info.getTypeName().toLowerCase();
                Log.d(TAG, "network is available ,the name：" + name);
            } else {
            	Log.d(TAG, "no network available!!");
            }
        }
    }
}
