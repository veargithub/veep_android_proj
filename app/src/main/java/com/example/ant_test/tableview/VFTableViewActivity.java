package com.example.ant_test.tableview;

import java.util.ArrayList;
import java.util.List;

import com.example.ant_test.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;


public class VFTableViewActivity extends Activity{
	private static final int COLUMN = 4;
	private String[] headers;
	private List<String[]> data;
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		//setContentView(R.layout.activity_scroll_view);
		//VFTableView view = new VFTableView(this);
		ViewGroupTest view = new ViewGroupTest(this);
		ViewGroup.LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		view.setLayoutParams(lp);
		view.setBackgroundResource(R.color.gray);		
		setContentView(view);
//		view.post(new Runnable() {
//
//			@Override
//			public void run() {
//				Log.d("", "this.height:" + view.getHeight());
//				
//			}
//			
//		});
	}
	
	private void setData() {
		headers = new String[]{"header0", "header1", "header2", "header3"};
		data = new ArrayList<String[]>();
		for (int i = 0; i < 20; i++) {
			String[] ss = new String[4];
			for (int j = 0; j < COLUMN; j++) {
				ss[j] = i * COLUMN + j + "";
			}
			data.add(ss);
		}
	}
}
