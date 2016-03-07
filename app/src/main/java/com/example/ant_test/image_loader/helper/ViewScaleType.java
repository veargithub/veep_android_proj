package com.example.ant_test.image_loader.helper;

import android.widget.ImageView;

/**
 * @Title: ViewScaleType.java
 * @Package com.example.ant_test.image_loader
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-18 下午5:12:08
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public enum ViewScaleType {
	CROP,
	
	FIT_INSIDE;
	
	public static final ViewScaleType getScaleType(ImageView imageView) {
		switch (imageView.getScaleType()) {
		case FIT_CENTER:
		case FIT_XY:
		case FIT_START:
		case FIT_END:
		case CENTER_INSIDE:
			return FIT_INSIDE;
		case MATRIX:
		case CENTER:
		case CENTER_CROP:
		default:
			return CROP;
	}
	}
}
