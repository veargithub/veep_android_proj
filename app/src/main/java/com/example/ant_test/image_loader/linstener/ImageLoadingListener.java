package com.example.ant_test.image_loader.linstener;


import com.example.ant_test.image_loader.helper.FailReason;

import android.graphics.Bitmap;
import android.view.View;

/**
 * 对于整个图片加载过程的监听
 */
public interface ImageLoadingListener {
	
	/**
	 * 刚开始加载的时候调用
	 * @param imageUri
	 * @param view
	 */
	void onLoadingStarted(String imageUri, View view);
	
	/**
	 * 加载图片失败(可能发生在下载、解码、存储的任何一个过程中)
	 * @param imageUri
	 * @param view
	 * @param failReason
	 */
	void onLoadingFailed(String imageUri, View view, FailReason failReason);
	
	/**
	 * 当图片加载完成的时候
	 * @param imageUri
	 * @param view
	 * @param loadedImage
	 */
	void onLoadingComplete(String imageUri, View view, Bitmap loadedImage);
	
	/**
	 * 当取消加载图片的时候，一般发生在view另做他用的时候
	 * @param imageUri
	 * @param view
	 */
	void onLoadingCancelled(String imageUri, View view);

}
