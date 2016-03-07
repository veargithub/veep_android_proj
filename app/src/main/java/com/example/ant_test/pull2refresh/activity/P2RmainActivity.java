package com.example.ant_test.pull2refresh.activity;

import java.util.Arrays;

import com.example.ant_test.pull2refresh.extra.BounceListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @Title: P2RmainActivity.java
 * @Package com.example.ant_test.pull2refresh.activity
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2015-1-20 上午10:14:16
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class P2RmainActivity extends Activity implements OnItemClickListener{
	private ListView lv;
	private ArrayAdapter<String> adapter;
	private static final String[] MENU = {"bounce", "listview", "scrollview", "gridview"};
	private static final Class[] CLASSES = {BounceListViewActivity.class, P2RlistviewActivity.class,
		P2RscrollviewActivity.class, P2RgridviewActivity.class};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lv = new ListView(this);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, Arrays.asList(MENU));
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		this.setContentView(lv);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(this, CLASSES[position]);
		startActivity(intent);
		
	}

	
}
