package com.example.ant_test.scheme;

import com.example.ant_test.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @Title: SchemeActivityA.java
 * @Package com.example.ant_test.scheme
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-2 下午4:48:01
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class SchemeActivityA extends Activity{
	private TextView tv1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_with_one_textview);
		tv1 = (TextView) findViewById(R.id.tv1);
		Intent intent = this.getIntent();
		if (intent != null) {
			String scheme = intent.getScheme();
			Uri uri = intent.getData();
			String data = uri.toString().replaceFirst(scheme + "\\:\\/\\/",  "");
            String a = uri.getQueryParameter("a");
			tv1.setText("SchemeActivityA, scheme:" + scheme + ", data:" + data + ", a=" + a);
		}
	}
	
	

}
