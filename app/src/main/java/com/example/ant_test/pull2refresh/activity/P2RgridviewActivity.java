package com.example.ant_test.pull2refresh.activity;

import java.util.Arrays;
import java.util.LinkedList;

import com.example.ant_test.R;
import com.example.ant_test.pull2refresh.core.Pull2RefreshBase;
import com.example.ant_test.pull2refresh.core.Pull2RefreshGridView;
import com.example.ant_test.pull2refresh.listener.OnRefreshListener2;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;



/**
 * @Title: P2RgridviewActivity.java
 * @Package com.example.ant_test.pull2refresh.activity
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2015-1-14 下午4:37:48
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class P2RgridviewActivity extends Activity{

	static final int MENU_SET_MODE = 0;

	private LinkedList<String> mListItems;
	private Pull2RefreshGridView mPullRefreshGridView;
	private GridView mGridView;
	private ArrayAdapter<String> mAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_p2r_gridview);

		mPullRefreshGridView = (Pull2RefreshGridView) findViewById(R.id.p2rgv);
		mGridView = mPullRefreshGridView.getRefreshableView();

		// Set a listener to be invoked when the list should be refreshed.
		mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {

			@Override
			public void onPullDownToRefresh(Pull2RefreshBase<GridView> refreshView) {
				Toast.makeText(P2RgridviewActivity.this, "Pull Down!", Toast.LENGTH_SHORT).show();
				new GetDataTask().execute();
			}

			@Override
			public void onPullUpToRefresh(Pull2RefreshBase<GridView> refreshView) {
				Toast.makeText(P2RgridviewActivity.this, "Pull Up!", Toast.LENGTH_SHORT).show();
				new GetDataTask().execute();
			}

		});

		mListItems = new LinkedList<String>();

		TextView tv = new TextView(this);
		tv.setGravity(Gravity.CENTER);
		tv.setText("Empty View, Pull Down/Up to Add Items");
		mPullRefreshGridView.setEmptyView(tv);

		mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
		mGridView.setAdapter(mAdapter);
		
		mPullRefreshGridView.autoPullDownRefresh();
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			return mStrings;
		}

		@Override
		protected void onPostExecute(String[] result) {
			mListItems.addFirst("Added after refresh...");
			mListItems.addAll(Arrays.asList(result));
			if (mListItems.size() > 0) {
				mPullRefreshGridView.removeEmptyView();
			}
			mAdapter.notifyDataSetChanged();
			
			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshGridView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

	

	

	private String[] mStrings = { "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
			"Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale", "Aisy Cendre",
			"Allgauer Emmentaler" };
}
