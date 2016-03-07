package com.example.ant_test.image_loader.image_wrapper;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import com.example.ant_test.image_loader.helper.ImageSize;
import com.example.ant_test.image_loader.helper.ViewScaleType;


/**
 * @Title: ImageNoViewWrapper.java
 * @Package com.example.ant_test.image_loader.image_wrapper
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-29 上午11:29:32
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ImageNoViewWrapper implements IImageWrapper{

	protected final String imageUri;
	protected final ImageSize imageSize;
	protected final ViewScaleType scaleType;
	
	public ImageNoViewWrapper(ImageSize imageSize, ViewScaleType scaleType) {
		this(null, imageSize, scaleType);
	}

	public ImageNoViewWrapper(String imageUri, ImageSize imageSize, ViewScaleType scaleType) {
		//TODO 设定个默认的imageSize和scaleType或许会比较好
		if (imageSize == null) throw new IllegalArgumentException("imageSize must not be null");
		if (scaleType == null) throw new IllegalArgumentException("scaleType must not be null");

		this.imageUri = imageUri;
		this.imageSize = imageSize;
		this.scaleType = scaleType;
	}
	
	@Override
	public int getWidth() {
		return imageSize.getWidth();
	}

	@Override
	public int getHeight() {
		return imageSize.getHeight();
	}

	@Override
	public ViewScaleType getScaleType() {
		return scaleType;
	}

	@Override
	public View getWrappedView() {
		return null;
	}

	@Override
	public boolean isGC() {
		return false;
	}

	@Override
	public int getId() {
		return TextUtils.isEmpty(imageUri) ? super.hashCode() : imageUri.hashCode();
	}

	@Override
	public boolean setImageDrawable(Drawable drawable) {
		return true;
	}

	@Override
	public boolean setImageBitmap(Bitmap bitmap) {
		return true;
	}

}
