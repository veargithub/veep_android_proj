package com.example.ant_test.https;

import com.example.ant_test.R;
import com.example.ant_test.https.network.VRealHttpClient;
import com.example.ant_test.https.network.VhttpClient;
import com.example.ant_test.https.network.request.Https;
import com.example.ant_test.https.network.request.VhttpRequest;
import com.example.ant_test.https.network.response.VhttpResponse;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @Title: HttpsActivity.java
 * @Package com.example.ant_test.https
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-12 上午9:25:30
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class HttpsActivity extends Activity implements OnClickListener{

	private TextView[] tvArray;
	private Button[] btnArray;
	private int[] tvIds = {R.id.tv1, R.id.tv2, R.id.tv3};
	private int[] btnIds = {R.id.btn1, R.id.btn2, R.id.btn3};
	private String[] urls = {"https://tradeapi.1234567.com.cn:450/home/GetFriendlyWords", 
			"https://10.0.2.2:10002/veep/index.html", "https://10.0.2.2:12346/veep/index.html"};
	private int[] crtResId = {R.raw.trade_mobile1, R.raw.test2_for_emulator, R.raw.test2_for_emulator};
	private int[] crtResPwdID = {R.string.https_password1, R.string.https_password1, R.string.https_password1};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_https);
		tvArray = new TextView[tvIds.length];
		btnArray = new Button[btnIds.length];
		for (int i = 0; i < tvIds.length; i++) {
			tvArray[i] = (TextView) findViewById(tvIds[i]);
			btnArray[i] = (Button) findViewById(btnIds[i]);
			btnArray[i].setOnClickListener(this);
		}
		
		//SSLuseHttpClientTest.TEST(this);
	}

	@Override
	public void onClick(View v) {
		for (int i = 0; i < btnIds.length; i++) {
			if (v.getId() == btnIds[i]) {
				tvArray[i].setText("");
				sendRequest(i);
			}
		}
	}
	
	private void sendRequest(final int index) {
		final VhttpRequest request = new VhttpRequest(urls[index]);
		final Https https = new Https(this, crtResId[index], crtResPwdID[index], true, true);
		request.https = https;
		request.retryCount = 0;
		new Thread() {
			public void run() {
				//VhttpClient client = new VhttpClient();
				VRealHttpClient client = new VRealHttpClient();
				try {
					VhttpResponse response = (VhttpResponse)client.getResponse(request);
					Message msg = Message.obtain();
					msg.obj = response.content;
					msg.what = index;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			String content = (String)msg.obj;
			tvArray[msg.what].setText("[" + urls[msg.what] + "] " + content);
		}
		
	};
	
}
