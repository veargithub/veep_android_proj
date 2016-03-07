package com.example.ant_test.intentFlag;

import com.example.ant_test.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityB extends IntentFlagBaseActivity implements OnClickListener{
	private static final String TAG = ActivityB.class.getSimpleName();
	private TextView tv1;
	private Button btn1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intent_flag);
		//AL.add(this);
		Log.d(TAG, "oncreate");
		tv1 = (TextView) findViewById(R.id.tv1);
		btn1 = (Button) findViewById(R.id.btn1);
		
		tv1.setText(TAG);
		btn1.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onresume");
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) Log.d(TAG, "bundle is null");
		else Log.d(TAG, "bundle is not null");
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, ActivityC.class);
		Bundle bundle = new Bundle();
		intent.putExtras(bundle);
		this.startActivity(intent);
	}
}
