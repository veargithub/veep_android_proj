package com.example.ant_test.image_loader.displayer;

import android.graphics.Bitmap;

import com.example.ant_test.image_loader.helper.LoadedFrom;
import com.example.ant_test.image_loader.image_wrapper.IImageWrapper;

/**
 * @Title: SimpleBitmapDisplayer.java
 * @Package com.example.ant_test.image_loader.displayer
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-29 下午1:54:45
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class SimpleBitmapDisplayer implements IBitmapDisplayer{

	@Override
	public void display(Bitmap bitmap, IImageWrapper wrapper, LoadedFrom from) {
		wrapper.setImageBitmap(bitmap);
		
	}

}
