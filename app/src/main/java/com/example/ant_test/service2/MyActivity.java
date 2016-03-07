package com.example.ant_test.service2;

import com.example.ant_test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @Title: MyActivity.java
 * @Package com.example.ant_test.service2
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-3-27 下午1:19:36
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class MyActivity extends Activity implements OnClickListener{
	private Button btn1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_activity);
		btn1 = (Button)findViewById(R.id.btn1);
		btn1.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, MyService.class);
		startService(intent);
		
	}
	
}
