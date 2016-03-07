package com.example.ant_test.vertical_horizontal_scrollview;

import com.example.ant_test.R;

import android.app.Activity;
import android.os.Bundle;

/**
 * @Title: VHScrollViewActivity.java
 * @Package com.example.ant_test.vertical_horizontal_scrollview
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-8-6 下午4:00:55
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class VHScrollViewActivity extends Activity{
	private VHScrollView vhsView;
	private TestView view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vhsView = new VHScrollView(this);
		view = new TestView(this);
		vhsView.addView(view);
//		setContentView(R.layout.only_one_scrollview);
		setContentView(vhsView);
		vhsView.postDelayed(
				new Runnable() {

					@Override
					public void run() {
						vhsView.test();
					}
			
		}, 1000);
	}
	
}
