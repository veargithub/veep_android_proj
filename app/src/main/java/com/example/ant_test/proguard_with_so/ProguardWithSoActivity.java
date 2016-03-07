package com.example.ant_test.proguard_with_so;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.example.hellojni.HelloJni;
/**
 * @Title: ProguardWithSoActivity.java
 * @Package com.example.ant_test.proguard_with_so
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-10-22 下午3:37:28
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ProguardWithSoActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        HelloJni helloJni = new HelloJni();
		Log.d(">>>>", helloJni.stringFromJNI() + " test");
	}


	

}
