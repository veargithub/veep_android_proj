package com.example.ant_test.pull2refresh.activity;

import com.example.ant_test.R;
import com.example.ant_test.pull2refresh.core.Pull2RefreshBase;
import com.example.ant_test.pull2refresh.core.Pull2RefreshMode;
import com.example.ant_test.pull2refresh.core.Pull2RefreshScrollView;
import com.example.ant_test.pull2refresh.extra.BounceListView;
import com.example.ant_test.pull2refresh.listener.OnRefreshListener;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;

/**
 * @Title: P2RscrollviewActivity.java
 * @Package com.example.ant_test.pull2refresh.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-12-9 下午5:40:12
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class P2RscrollviewActivity extends Activity implements OnClickListener, OnRefreshListener<ScrollView>{
	private Pull2RefreshScrollView sv;
	private Button btnBoth;
	private Button btnStart;
	private Button btnEnd;
	private Button btnNone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_p2r_scrollview);
		sv = (Pull2RefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		sv.setOnRefreshListener(this);
		btnBoth = (Button) findViewById(R.id.both);   btnBoth.setOnClickListener(this);
		btnStart = (Button) findViewById(R.id.start); btnStart.setOnClickListener(this);
		btnEnd = (Button) findViewById(R.id.end);     btnEnd.setOnClickListener(this);
		btnNone = (Button) findViewById(R.id.none);   btnNone.setOnClickListener(this);
		Log.d(">>>>", "child size:" + sv.getChildCount());
		
//		setBounceContentView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.both:
		default:
			sv.setMode(Pull2RefreshMode.MODE_BOTH);
			break;
		case R.id.start:
			sv.setMode(Pull2RefreshMode.MODE_START);
			break;
		case R.id.end:
			sv.setMode(Pull2RefreshMode.MODE_END);
			break;
		case R.id.none:
			sv.setMode(Pull2RefreshMode.MODE_DISABLE);
			break;
		}
		
	}

	@Override
	public void onRefresh(Pull2RefreshBase<ScrollView> refreshView) {
		new MyTask().execute();
	}

	private class MyTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			sv.onRefreshComplete();
			super.onPostExecute(result);
		}
	}
	
	private void setBounceContentView() {
		BounceListView blv = new BounceListView(this);
		blv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		String[] data = new String[30];  
        for (int i = 0; i < data.length; i++) {  
            data[i] = "天天记录 " + i;  
        }  
          
        ArrayAdapter<String> arrayAdapter =   
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, data);  
          
        blv.setAdapter(arrayAdapter);
        this.setContentView(blv);
	}
}
