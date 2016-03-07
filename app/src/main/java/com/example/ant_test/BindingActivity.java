package com.example.ant_test;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.example.ant_test.service.LocalService;
import com.example.ant_test.service.LocalService.LocalBinder;
import com.example.ant_test.service.RemoteService;
import com.example.ant_test.service.aidl.IPMListener;
import com.example.ant_test.service.aidl.IPMService;
import com.example.ant_test.service.bean.EmPushMessage;

public class BindingActivity extends Activity implements OnClickListener {
	private static final String TAG = "BindingActivity";
	IPMService rService;
    TextView tv;
    
    Handler h = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			tv.setText("get message from service");
			super.handleMessage(msg);
		}
    	
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.tv1);
        tv.setOnClickListener(this);
        Log.d(TAG, "oncreate!!");
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent2 = new Intent(this, RemoteService.class);
        boolean result2 = bindService(intent2, rConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bind result:" + " " + result2);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "Unbind from the service");
       
        unbindService(rConnection);
    }
  
    private ServiceConnection rConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			rService = IPMService.Stub.asInterface(service);
			
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			rService = null;
			
		}
    	
    };

	@Override
	public void onClick(View v) {
		Log.d("service", "service!!!");
		try {
			rService.setListener(listener);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	IPMListener listener = new  IPMListener.Stub() {
		
		@Override
		public void onReceiveMessage(EmPushMessage message) throws RemoteException {
			Log.d(TAG, message.toString());
			h.sendEmptyMessage(0);
		}
	};
}
