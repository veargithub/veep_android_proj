package com.example.ant_test.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * @Title: TestReceive.java
 * @Package com.example.ant_test.broadcast
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-7 上午10:26:59
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class TestReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(BroadcastTestService.FOO_ACTION)) {
			Log.d(">>>>", "packageName:" + context.getPackageName() + " received " + BroadcastTestService.FOO_ACTION);
		}
		
	}

}
