package com.example.ant_test;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.example.ant_test.R;

public class FragmentTestActivity extends FragmentActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fragment);
	}
	

}
