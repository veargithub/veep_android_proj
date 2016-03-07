package com.example.ant_test.intentFlag;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.ant_test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class IntentFlagActivity extends Activity implements OnClickListener{
	private static final String TAG = IntentFlagActivity.class.getSimpleName();
	private TextView tv1;
	private Button btn1;
	private ImageView iv1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_flag);
		Log.d(TAG, "oncreate");
		tv1 = (TextView) findViewById(R.id.tv1);
		btn1 = (Button) findViewById(R.id.btn1);
		iv1 = (ImageView) findViewById(R.id.iv1);
		tv1.setText(TAG);
		btn1.setOnClickListener(this);
		
		String str = "{" +
			  "\"startupAd\": {" +
			    "\"style\": \"\"," +
			    "\"imageUrlAndroid\": \"http://00f3c1b4.sohunewsclientpic.imgcdn.sohucs.com/img8/wb/smccloud/product_pic/2014/04/11/1397214180179.jpg\"," +
			    "\"imageUrlIos\": \"http://00f3c1b4.sohunewsclientpic.imgcdn.sohucs.com/img8/wb/smccloud/product_pic/2014/04/11/1397214243832.jpg\"," +
			    "\"id\": 1," +
			    "\"mainTitle\": \"\"," +
			    "\"monitorShowUrl\": \"\"," +
			    "\"beginTime\": 1396285261," +
			    "\"expiredTime\": 1401555661," +
			    "\"showTime\": \"3.5\"" +
			  "}," +
			  "\"pullrefreshAd\": {" +
			    "\"id\": 1," +
			    "\"beginTime\": 1396285261," +
			    "\"expiredTime\": 1401555661," +
			    "\"imageUrlIos\": \"https://app.yinxiang.com/shard/s6/sh/fd501b86-66c4-46a7-b201-e69d80dea6d3/e0dba67b583b862d9cef374fae8768ea/res/1a784f03-380a-4e31-9b83-39aeb19398c5/ios640x360.png?resizeSmall&width=832\"," +
			    "\"imageUrlAndroid\": \"https://app.yinxiang.com/shard/s6/sh/fd501b86-66c4-46a7-b201-e69d80dea6d3/e0dba67b583b862d9cef374fae8768ea/res/53b470bb-dbd2-4d30-b759-16dd7cfcf040/android720x292.png?resizeSmall&width=832\"" +
			  "}" +
			"}";
		
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(str);
			JSONObject startupAd = jsonObject.optJSONObject("startupAd");
			JSONObject pullrefreshAd = jsonObject.optJSONObject("pullrefreshAd");
			String startupAdStyle = startupAd.optString("style");
			String startupAdImageUrlAndroid = startupAd.optString("imageUrlAndroid");
			String startupAdId = startupAd.optString("id");
			String startupAdMainTitle = startupAd.optString("mainTitle");
			String startupAdMonitorShowUrl = startupAd.optString("monitorShowUrl");
			long startupAdBeginTime = startupAd.optLong("beginTime") * 1000;
			long startupAdExpiredTime = startupAd.optLong("expiredTime") * 1000;
			String startupAdShowTime = startupAd.optString("showTime");
			
			Date date1 = new Date(startupAdBeginTime);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String d1 = sdf.format(date1);
			
			int pullrefreshAdId = pullrefreshAd.optInt("id");
			long pullrefreshAdBeginTime = pullrefreshAd.optLong("beginTime") * 1000;
			long pullrefreshAdExpiredTime = pullrefreshAd.optLong("expiredTime") * 1000;
			String pullrefreshAdImageUrlAndroid = pullrefreshAd.optString("imageUrlAndroid");
			
			Date now = new Date();
			
			Log.d(">>>>>>>", now.getTime() + " " + d1);
			Log.d(">>>>>>>", startupAdBeginTime + " " + sdf.format(now));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onresume");
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) Log.d(TAG, "bundle is null");
		else Log.d(TAG, "bundle is not null");
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		iv1.setImageResource(R.drawable.home_page_back_unclickable);
		Intent intent = new Intent(this, ActivityA.class);
		Bundle bundle = new Bundle();
		intent.putExtras(bundle);
		this.startActivity(intent);
	}
	
}
