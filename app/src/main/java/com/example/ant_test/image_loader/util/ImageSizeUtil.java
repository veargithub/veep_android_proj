package com.example.ant_test.image_loader.util;

import com.example.ant_test.image_loader.helper.ImageSize;
import com.example.ant_test.image_loader.helper.ViewScaleType;
import com.example.ant_test.image_loader.image_wrapper.IImageWrapper;


/**
 * @Title: ImageSizeUtil.java
 * @Package com.example.ant_test.image_loader.util
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-28 下午2:40:20
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ImageSizeUtil {
	
	/**
	 * 
	 * @param srcSize 原图的尺寸
	 * @param targetSize imageview的尺寸
	 * @param viewScaleType 缩放类型
	 * @param powerOf2Scale 是否只缩放2的n方的倍数
	 * @return
	 */
	public static int computeImageSampleSize(ImageSize srcSize, ImageSize targetSize, ViewScaleType viewScaleType,
			boolean powerOf2Scale) {
		final int srcWidth = srcSize.getWidth();
		final int srcHeight = srcSize.getHeight();
		final int targetWidth = targetSize.getWidth();
		final int targetHeight = targetSize.getHeight();

		int scale = 1;

		switch (viewScaleType) {
			case FIT_INSIDE:
				if (powerOf2Scale) {
					final int halfWidth = srcWidth / 2;
					final int halfHeight = srcHeight / 2;
					while ((halfWidth / scale) > targetWidth || (halfHeight / scale) > targetHeight) { // ||
						scale *= 2;
					}
				} else {
					scale = Math.max(srcWidth / targetWidth, srcHeight / targetHeight); // max
				}
				break;
			case CROP:
				if (powerOf2Scale) {
					final int halfWidth = srcWidth / 2;
					final int halfHeight = srcHeight / 2;
					while ((halfWidth / scale) > targetWidth && (halfHeight / scale) > targetHeight) { // &&
						scale *= 2;
					}
				} else {
					scale = Math.min(srcWidth / targetWidth, srcHeight / targetHeight); // min
				}
				break;
		}

		if (scale < 1) {
			scale = 1;
		}
		

		return scale;
	}
	
	/**
	 * 获取imageview的宽和高，如果获取不了，则返回设备屏幕的宽和高
	 * @param imageWrapper
	 * @param maxImageSize
	 * @return
	 */
	public static ImageSize defineTargetSizeForView(IImageWrapper imageWrapper, ImageSize maxImageSize) {
		int width = imageWrapper.getWidth();
		if (width <= 0) width = maxImageSize.getWidth();

		int height = imageWrapper.getHeight();
		if (height <= 0) height = maxImageSize.getHeight();

		return new ImageSize(width, height);
	}
}
