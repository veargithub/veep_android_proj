package com.example.ant_test.textview_with_circle;


import com.example.ant_test.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;

public class TextViewWithCircleActivity extends Activity{
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		this.setContentView(R.layout.activity_textview_with_circle);
		TextViewWithCircle tv = (TextViewWithCircle) findViewById(R.id.text);
		tv.setCornerMark(R.drawable.tab_unread_bg);
		tv.setText("1234567");
	}
}
