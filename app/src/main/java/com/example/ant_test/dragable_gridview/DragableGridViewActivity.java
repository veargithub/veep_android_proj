package com.example.ant_test.dragable_gridview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.ant_test.R;
import com.example.ant_test.dragable_gridview.DragableGridView.OnSwapListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;

public class DragableGridViewActivity extends Activity implements OnSwapListener{
	private List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	private DragableGridView dgv;
	private SimpleAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dragable_gridview);
		dgv = (DragableGridView)findViewById(R.id.gv1);
		
		for (int i = 0; i < 30; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("item_image",R.drawable.ic_launcher);
			item.put("item_text", i + "");
			data.add(item);
		}
		
		adapter = new SimpleAdapter(this, data,
				R.layout.gridview_item, new String[] { "item_image", "item_text" },
				new int[] { R.id.item_image, R.id.item_text });
		dgv.setAdapter(adapter);
		dgv.setOnSwapListener(this);

	}

	@Override
	public void onSwap(int draggedPosition, int swappedPosition) {
		Log.d(">>>>>>>>>", "draggedPosition:" + draggedPosition + ", swappedPosition:" 
				+ swappedPosition);
		HashMap<String, Object> drag = this.data.get(draggedPosition);
		HashMap<String, Object> swap = this.data.get(swappedPosition);
		this.data.remove(draggedPosition);
		this.data.add(draggedPosition, swap);
		this.data.remove(swappedPosition);
		this.data.add(swappedPosition, drag);
		adapter.notifyDataSetChanged();
	}
	
}
