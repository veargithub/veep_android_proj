package com.example.ant_test.rounded_bitmap;

import com.example.ant_test.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * @Title: RoundedBitmapActivity.java
 * @Package com.example.ant_test.rounded_bitmap
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-30 下午1:44:48
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class RoundedBitmapActivity extends Activity{
	
	private ImageView iv;
	
	@Override
	public void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.activity_rounded_bitmap);
		iv = (ImageView) findViewById(R.id.iv1);
		Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
		RoundedDrawable rd = new RoundedDrawable(bitmap, 20, 10);
		iv.setImageDrawable(rd);
	}

}
