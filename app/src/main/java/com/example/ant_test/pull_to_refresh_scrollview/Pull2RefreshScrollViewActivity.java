package com.example.ant_test.pull_to_refresh_scrollview;

import com.example.ant_test.R;
import com.example.ant_test.pull_to_refresh_scrollview.Pull2RefreshScrollView.OnRefreshListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * @Title: Pull2RefreshScrollViewActivity.java
 * @Package com.example.ant_test.pull_to_refresh_scrollview
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-4-8 下午5:52:46
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class Pull2RefreshScrollViewActivity extends Activity implements OnRefreshListener{
	private Pull2RefreshScrollView p2rSv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pull_to_refresh_scrollview);
		p2rSv = (Pull2RefreshScrollView) findViewById(R.id.pull_to_refresh_scrollview_id);
		p2rSv.setonRefreshListener(this);
	}

	@Override
	public void onRefresh() {
		Log.d(">>>>>>>", "onRefresh");
		handler.sendEmptyMessageDelayed(0, 2000);
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			p2rSv.onRefreshComplete();
		}
		
	};
	
}
