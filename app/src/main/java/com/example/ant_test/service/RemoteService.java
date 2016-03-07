package com.example.ant_test.service;

import com.example.ant_test.service.aidl.IPMListener;
import com.example.ant_test.service.aidl.IPMService;
import com.example.ant_test.service.bean.EmPushMessage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class RemoteService extends Service{
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final IPMService.Stub mBinder = new IPMService.Stub() {

		@Override
		public void setListener(IPMListener listener) throws RemoteException {
			EmPushMessage message = new EmPushMessage("8151013691868748&&1&&1,600680&&股价达到9.53元，上涨达到您设置的9.00元。&&&&20130815145408");
			listener.onReceiveMessage(message);
			Log.d("BindingActivity", "callback:" + message.toString());
		}       
	};

}
