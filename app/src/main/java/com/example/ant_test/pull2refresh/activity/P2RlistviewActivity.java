package com.example.ant_test.pull2refresh.activity;

import java.util.LinkedList;

import org.apache.log4j.lf5.viewer.configure.MRUFileManager;

import com.example.ant_test.R;
import com.example.ant_test.pull2refresh.core.Pull2RefreshBase;
import com.example.ant_test.pull2refresh.core.Pull2RefreshListView;
import com.example.ant_test.pull2refresh.core.Pull2RefreshMode;
import com.example.ant_test.pull2refresh.core.Pull2RefreshOrientation;
import com.example.ant_test.pull2refresh.core.Pull2RefreshState;
import com.example.ant_test.pull2refresh.listener.OnRefreshListener;
import com.example.ant_test.pull2refresh.listener.OnRefreshListener2;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @Title: P2RlistviewActivity.java
 * @Package com.example.ant_test.pull2refresh.activity
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-12-16 下午3:21:12
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class P2RlistviewActivity extends Activity implements OnRefreshListener2<ListView>{
	private static final int TOTAL_SIZE = 71;
	private static final int SIZE = 2;
	private static final int INITIAL_SIZE = 15;
	private static final int MAX_RESERVER_SIZE = 40;
	private int startIndex = 0;
	private int lastIndex = 0;
	private LinkedList<String> mListItems;
	private Pull2RefreshListView mPullRefreshListView;
	private ArrayAdapter<String> mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_p2r_listview);
		mPullRefreshListView = (Pull2RefreshListView) findViewById(R.id.p2rlv22);
		initData();
		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
		mPullRefreshListView.setAdapter(mAdapter);
		mPullRefreshListView.setOnRefreshListener(this);
		//mPullRefreshListView.autoPullUpRefresh();
		
//		addEmptyView();
		
		mPullRefreshListView.postDelayed(new Runnable() {

			@Override
			public void run() {
				//mPullRefreshListView.setRefreshing(false);
				//mPullRefreshListView.scrollTo(0, -200);
				
			}
			
		}, 0);


	}

	@Override
	public void onPullDownToRefresh(Pull2RefreshBase<ListView> refreshView) {
		Log.d(">>>>", "onPullDownToRefresh");
		MyTask task = new MyTask();
		task.execute(Pull2RefreshOrientation.DOWN);
	}

	@Override
	public void onPullUpToRefresh(Pull2RefreshBase<ListView> refreshView) {
		Log.d(">>>>", "onPullUpToRefresh");
		MyTask task = new MyTask();
		task.execute(Pull2RefreshOrientation.UP);
	}
	
	private void initData() {
		mListItems = new LinkedList<String>();
		for (int i = 0; i < SIZE; i++, lastIndex++) {
			mListItems.add(lastIndex + "");
		}
	}
	/** add an empty view when there is no data*/
	private void addEmptyView() {
		if (mListItems != null) {
			mListItems.clear();
		}
		TextView tv = new TextView(this);
		tv.setGravity(Gravity.CENTER);
		tv.setText("Empty View, Pull Down/Up to Add Items");
		mPullRefreshListView.setEmptyView(tv);
	}
	
	private class MyTask extends AsyncTask<Pull2RefreshOrientation, Void, Void> {
		
		private Pull2RefreshOrientation orientation;
	
		@Override
		protected Void doInBackground(Pull2RefreshOrientation... params) {
			if (params != null && params.length > 0) {
				orientation = params[0];
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			requestData(orientation);
			mAdapter.notifyDataSetChanged();
			if (!mAdapter.isEmpty()) {
				mPullRefreshListView.removeEmptyView();
			}
			mPullRefreshListView.onRefreshComplete(orientation);
			
//			int fvp = mPullRefreshListView.getRefreshableView().getFirstVisiblePosition();
//			View view = mPullRefreshListView.getRefreshableView().getChildAt(0);
//			Log.d(">>>>", fvp + " " + view.getTop());
//			mPullRefreshListView.getRefreshableView().setSelectionFromTop(fvp + 1, view.getTop());
			//mPullRefreshListView.scrollBy(0, mPullRefreshListView.getFooter().getHeight());

		}
		
		private void requestData(Pull2RefreshOrientation orientation) {
			if (orientation == Pull2RefreshOrientation.UP) {
				for (int i = 0;  i < SIZE && lastIndex < TOTAL_SIZE; i++, lastIndex++) {
					mListItems.add(lastIndex + "");
				}
			}
		}
	}
}
