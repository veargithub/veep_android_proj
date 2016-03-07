package com.example.ant_test.camera;

import com.example.ant_test.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * @Title: CameraTestActivity.java
 * @Package com.example.ant_test.camera
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-24 下午5:03:08
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class CameraTestActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ImageTestView iv = new ImageTestView(this);
		iv.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		iv.setBackgroundResource(R.drawable.ic_launcher);
		this.setContentView(iv);
	}
	
}
