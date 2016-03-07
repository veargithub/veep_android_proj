package com.example.ant_test.opengles;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.opengl.GLSurfaceView;

/**
 * @Title: OpenglESFooActivity.java
 * @Package com.example.ant_test.opengles
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-4-18 下午5:18:51
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class OpenglESFooActivity extends Activity{
	private static final String TAG =" OpenglESFooActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		GLSurfaceView view = new GLSurfaceView(this);
		view.setRenderer(new OpenglESFooRenderer());
		setContentView(view);
	}
	
	
}
