package com.example.ant_test.log_to_file;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ant_test.R;
import com.example.veep.vcomm.log.Vlog;
import com.example.util.ExternalUtil;

/**
 * @Title: LogToFileActivity.java
 * @Package com.example.ant_test.log_to_file
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-3 下午3:14:59
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class LogToFileActivity extends Activity{
    private final String TAG = LogToFileActivity.this.getClass().getSimpleName();
	private boolean isRun;
    private Vlog.EasyLog elog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_with_one_textview);
		TextView tv = (TextView) findViewById(R.id.tv1);
		tv.setText(ExternalUtil.isExternalStorageAvailable() + "");
		isRun = true;
        elog = Vlog.getLog4j("/veep2/test/", "test" , TAG);
		new Thread() {
			public void run() {
				while (isRun) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	//				elog = Vlog.getLog4j("LogToFileTest");
	//				elog.d("test");

	//				log4j = LoggerFile.getLog4j("test!!");
	//				log4j.info("1234");
					for (int i = 0; i < 10; i++) {
                        elog.info("" + i);
					}
				}
			}
		}.start();
		
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isRun = false;
	}

	

}
