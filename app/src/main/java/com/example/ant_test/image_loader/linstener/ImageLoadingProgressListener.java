package com.example.ant_test.image_loader.linstener;

import android.view.View;

/**
 * 对于下载图片进度的监听
 */
public interface ImageLoadingProgressListener {

	/**
	 * 刷新下载进度用到的类
	 * @param imageUri 
	 * @param view
	 * @param current 下载了多少字节
	 * @param total 总共有多少字节
	 */
	void onProgressUpdate(String imageUri, View view, int current, int total);
	
}
