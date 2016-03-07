package com.example.ant_test.targetAPI;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

/**
 * @Title: TargetApiTest.java
 * @Package com.example.ant_test.targetAPI
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-5-19 上午9:31:14
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class TargetApiTest extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Button button = new Button(this);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		button.setLayoutParams(lp);
		button.setText("start activity");
		this.setContentView(button);
		button.setOnClickListener(this);
	}
	
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, TargetApiTest2.class); 
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out); 
		//finish();
		
	}
	
}
