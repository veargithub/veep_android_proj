package com.example.ant_test.sliding_menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.ant_test.R;
import com.example.ant_test.sliding_menu.impl.SlidingMenuTransformerBuilder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * @Title: SlidingMenuActivity.java
 * @Package com.example.ant_test.sliding_menu
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-8-22 下午1:08:22
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class SlidingMenuActivity extends Activity{
	private SlidingMenuLayout sml;
	private ListView listview;
	private List<HashMap<String, String>> data;
	private SimpleAdapter adapter;
	private TextView textview1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sml = new SlidingMenuLayout(this);
		sml.setMenuWidthOffset(200);
		sml.setMenu(R.layout.activity_with_one_textview);
		sml.setPanel(R.layout.sliding_menu_panel);//TODO problem of sequence
		sml.setMenuTransFormer(SlidingMenuTransformerBuilder.CommonMenuTransformer());
		sml.setPanelTransFormer(SlidingMenuTransformerBuilder.CommonPanelTransformer());
		setContentView(sml);
		listview = (ListView) findViewById(R.id.lv1);
		data = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < 20; i++) {
			HashMap<String, String> hm = new HashMap<String, String>();
			hm.put("text1", "simple" + i);
			data.add(hm);
		}
		adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_1, 
				new String[] {"text1"}, new int[] {android.R.id.text1});
		listview.setAdapter(adapter);
		textview1 = (TextView) findViewById(R.id.tv1);
		textview1.setText("12346578913456798");
	}
	
}
