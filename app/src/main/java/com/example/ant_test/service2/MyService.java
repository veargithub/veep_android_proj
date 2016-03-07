package com.example.ant_test.service2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * @Title: MyService.java
 * @Package com.example.ant_test.service2
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-3-27 下午1:19:22
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class MyService extends Service{
	
	

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("MyService", this.getPackageName());
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
