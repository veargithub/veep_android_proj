package com.example.ant_test.touchEventConveyance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.ant_test.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class TouchEventConveyanceActivity extends Activity{
	private ListView lv;
	private SimpleAdapter adapter;
	private List<HashMap<String, String>> data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touch_event_conveyance);
		lv = (ListView)findViewById(R.id.lv1);
		data = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 20; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("text1", i * 100 + "");
			data.add(hm);
		}
		adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_1, 
				new String[]{"text1"}, new int[]{android.R.id.text1});
		lv.setAdapter(adapter);
		lv.setOnTouchListener(new OnTouchListener() {
		    // Setting on Touch Listener for handling the touch inside ScrollView
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		    	// Disallow the touch request for parent scroll on touch of child view
		    	Log.d(">>>>>>", "onTouch");
		    	v.getParent().requestDisallowInterceptTouchEvent(true);
			    return false;
		    }
			
		});
		lv.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				Log.d(">>>>>>", "onScroll");
				
			}
		});
	}
	
}
