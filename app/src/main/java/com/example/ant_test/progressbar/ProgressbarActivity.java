package com.example.ant_test.progressbar;

import com.example.ant_test.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ProgressbarActivity extends Activity{
	private Button btn_go = null;
	private NumberProgressBar myProgress = null;
	private Handler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_progressbar);
		findView();
        setParam();
        addListener();
        
        mHandler =  new Handler() {
			
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				myProgress.setNumber(msg.what);
			}
		};
	}
	
	private void findView(){
    	btn_go = (Button) findViewById(R.id.btn_go);
    	myProgress = (NumberProgressBar) findViewById(R.id.pgsBar);
    	myProgress.setMaxNumber(20);
    }
    
    private void setParam(){
    	btn_go.setText("开始");
    }
    private void addListener(){
    	btn_go.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {	
					@Override
					public void run() {
						// TODO Auto-generated method stub
						for(int i = 0; i <= 20; i++){
							mHandler.sendEmptyMessage(i);
							try {
								Thread.sleep(80);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
		});
    }
	
}
