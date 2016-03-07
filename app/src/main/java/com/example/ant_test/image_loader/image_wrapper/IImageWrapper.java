package com.example.ant_test.image_loader.image_wrapper;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.example.ant_test.image_loader.helper.ViewScaleType;



/**
 * @Title: IImageWrapper.java
 * @Package com.example.ant_test.image_loader.image_wrapper
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-18 下午5:46:26
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public interface IImageWrapper {
	int getWidth();
	
	int getHeight();
	
	ViewScaleType getScaleType();
	
	View getWrappedView();
	
	boolean isGC();
	
	int getId();
	
	boolean setImageDrawable(Drawable drawable);
	
	boolean setImageBitmap(Bitmap bitmap);
}
