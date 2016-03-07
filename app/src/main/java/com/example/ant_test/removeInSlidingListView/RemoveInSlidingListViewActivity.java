package com.example.ant_test.removeInSlidingListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.ant_test.R;
import com.example.ant_test.removeInSlidingListView.RemoveInSlidingListView.OnRemoveListener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SimpleAdapter;

public class RemoveInSlidingListViewActivity extends Activity implements OnRemoveListener{
	private List<Map<String, String>> data;
	private SimpleAdapter adapter;
	private RemoveInSlidingListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_remove_in_sliding_listview);
		lv = (RemoveInSlidingListView)findViewById(R.id.lv1);
		data = new ArrayList<Map<String, String>>();
		for (int i = 0; i < 25; i++) {
        	Map<String, String> map = new HashMap<String, String>();
        	map.put("test1", i + "");
	       	map.put("test2", i * 10 + "");
	       	map.put("test3", i * 100 + "");
	       	map.put("test4", i * 1000 + "");
        	data.add(map);
        }
        adapter = new SimpleAdapter(this, data, R.layout.common_stocktable_view, 
        		new String[] {"test1", "test2", "test3", "test4"}, 
        		new int[] {R.id.val1, R.id.val2, R.id.val3, R.id.val4});
        lv.setAdapter(adapter);
        lv.setOnRemoveListener(this);
	}

	@Override
	public void onRemove(int position) {
		data.remove(position);
		adapter.notifyDataSetChanged();
	}
	
}
