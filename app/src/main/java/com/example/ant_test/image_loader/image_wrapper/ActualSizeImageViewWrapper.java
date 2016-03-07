package com.example.ant_test.image_loader.image_wrapper;

import com.example.ant_test.image_loader.helper.ImageSize;

import android.widget.ImageView;

/**
 * @Title: ActualSizeImageViewWrapper.java
 * @Package com.example.ant_test.image_loader.image_wrapper
 * @Description: 对于已经知道宽高的imageview的封装
 * @author Chenxiao
 * @date 2014-9-29 下午1:24:01
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ActualSizeImageViewWrapper extends ImageViewWrapper{
	
	protected final ImageSize imageSize;

	public ActualSizeImageViewWrapper(ImageView view, ImageSize imageSize) {
		super(view);
		this.imageSize = imageSize;
	}
	
	public ActualSizeImageViewWrapper(ImageView view, int width, int height) {
		super(view);
		this.imageSize = new ImageSize(width, height);
	}

	@Override
	public int getWidth() {
		if (imageSize.getWidth() < 0) return 0;
		return imageSize.getWidth();
	}

	@Override
	public int getHeight() {
		if (imageSize.getHeight() < 0) return 0;
		return imageSize.getHeight();
	}	
}
