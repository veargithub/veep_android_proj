package com.example.ant_test.point_tips;

import android.app.Activity;
import android.os.Bundle;

/**
 * @Title: PointTipsActivity.java
 * @Package com.example.ant_test.point_tips
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-28 下午2:38:00
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class PointTipsActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PointTips pt = new PointTips(this);
		pt.setBackgroundColor(0x66000000);
		setContentView(pt);
		pt.setPointSize(5);
		pt.setCurrent(1);
	}
	
}
