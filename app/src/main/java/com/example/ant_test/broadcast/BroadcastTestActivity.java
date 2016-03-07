package com.example.ant_test.broadcast;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * @Title: BoardcastActivity.java
 * @Package com.example.ant_test.boardcast
 * @Description: 这个例子说明了一个广播如何被本进程和其他进程接收的
 * @author Chenxiao
 * @date 2014-7-7 上午9:43:10
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class BroadcastTestActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Intent service = new Intent(this, BroadcastTestService.class);
		this.startService(service);
	}

	TestReceiver receiver;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BroadcastTestService.FOO_ACTION);
		receiver = new TestReceiver();
		this.registerReceiver(receiver, filter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.unregisterReceiver(receiver);
	}

	

}
