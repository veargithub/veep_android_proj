package com.example.ant_test.image_loader.displayer;

import com.example.ant_test.image_loader.helper.LoadedFrom;
import com.example.ant_test.image_loader.image_wrapper.IImageWrapper;

import android.graphics.Bitmap;

/**
 * @Title: IImageDisplayer.java
 * @Package com.example.ant_test.image_loader.displayer
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-18 下午5:38:44
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public interface IBitmapDisplayer {
	void display(Bitmap bitmap, IImageWrapper wrapper, LoadedFrom from);
}
