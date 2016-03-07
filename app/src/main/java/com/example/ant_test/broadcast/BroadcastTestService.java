package com.example.ant_test.broadcast;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
/**
 * @Title: BoardcastService.java
 * @Package com.example.ant_test.boardcast
 * @Description: 一个service， 每过几秒发一个广播
 * @author Chenxiao
 * @date 2014-7-7 上午10:07:52
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class BroadcastTestService extends Service{
	public static final String FOO_ACTION = "com.example.ant_test.boardcast.foo";
	private Foo foo;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(">>>>", "BroadcastTestService oncreate");
		foo = new Foo();
		new Thread(foo).start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(">>>>", "BroadcastTestService onDestroy");
		foo.isRun = false;
	}
	
	class Foo implements Runnable {
		boolean isRun = true;
		@Override
		public void run() {
			while (isRun) {
				Intent intent = new Intent(FOO_ACTION);
				BroadcastTestService.this.sendBroadcast(intent);
				Log.d(">>>>", "BroadcastTestService send broadcast");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Log.d(">>>>", "exit foo");
		}
		
	};

}
