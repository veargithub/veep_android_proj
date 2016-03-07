package com.example.ant_test.show_dialog_on_screen_locked;

import com.example.ant_test.R;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Title: ShowDialogOnScreenLockedActivity.java
 * @Package com.example.ant_test.show_dialog_on_screen_locked
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2015-1-28 下午3:47:51
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ShowDialogOnScreenLockedActivity extends Activity{
	
	private LinearLayout ll1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(">>>>", "ShowDialogOnScreenLockedActivity:onCreate");
		final Window win = getWindow();  
	    win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED  
	            | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD  
	            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON  
	            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON); 
		
		super.onCreate(savedInstanceState);
//		TextView tv = new TextView(this);
//		tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
//				, LinearLayout.LayoutParams.FILL_PARENT));
//		tv.setGravity(Gravity.CENTER);
//		tv.setText("hello world!!!");
//		tv.setBackgroundColor(0xFF3c3c3c);
		setContentView(R.layout.activity_show_dialog_on_screen_locked);
		ll1 = (LinearLayout) findViewById(R.id.ll1);
		WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
		ll1.setBackgroundDrawable(wallpaperManager.getDrawable());
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Log.d(">>>>", "onNewIntent");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		final Window win = getWindow();
		win.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED  
	            | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD  
	            | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON  
	            | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}
}
