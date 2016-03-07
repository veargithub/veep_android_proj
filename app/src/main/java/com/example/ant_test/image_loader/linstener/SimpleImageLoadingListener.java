package com.example.ant_test.image_loader.linstener;

import com.example.ant_test.image_loader.helper.FailReason;

import android.graphics.Bitmap;
import android.view.View;

/**
 * 一个空的listener，表示不做任何监听
 */
public class SimpleImageLoadingListener implements ImageLoadingListener {
	@Override
	public void onLoadingStarted(String imageUri, View view) {
		// Empty implementation
	}

	@Override
	public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
		// Empty implementation
	}

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		// Empty implementation
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
		// Empty implementation
	}
}
