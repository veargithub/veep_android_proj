package com.example.ant_test.scheme;

import com.example.ant_test.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * @Title: SchemeActivityA.java
 * @Package com.example.ant_test.scheme
 * @Description: 这个例子说明了如何通过scheme来跳转activity
 * @author Chenxiao
 * @date 2014-7-2 下午4:35:40
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class SchemeActivityMain extends Activity implements OnClickListener{
	private Button btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_with_one_button);
		btn = (Button) findViewById(R.id.btn1);
		btn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn1) {
			
			Uri uri = Uri.parse("in2://user/invite2?a=b");
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
		
	}
	
	

}
