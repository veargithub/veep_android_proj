package com.example.ant_test.image_loader.core;

import com.example.ant_test.image_loader.displayer.IBitmapDisplayer;
import com.example.ant_test.image_loader.helper.LoadedFrom;
import com.example.ant_test.image_loader.image_wrapper.IImageWrapper;
import com.example.ant_test.image_loader.linstener.ImageLoadingListener;

import android.graphics.Bitmap;
import android.util.Log;



/**
 * 显示图片任务，必须在主线程中运行
 */
public class DisplayBitmapTask implements Runnable{

	private final Bitmap bitmap;
	private final String imageUri;
	private final IImageWrapper imageWrapper;
	private final String memoryCacheKey;
	private final IBitmapDisplayer displayer;
	private final ImageLoadingListener listener;
	private final ImageLoaderEngine engine;
	private final LoadedFrom loadedFrom;

	public DisplayBitmapTask(Bitmap bitmap, ImageLoadingInfo imageLoadingInfo, ImageLoaderEngine engine,
			LoadedFrom loadedFrom) {
		this.bitmap = bitmap;
		imageUri = imageLoadingInfo.uri;
		imageWrapper = imageLoadingInfo.imageWrapper;
		memoryCacheKey = imageLoadingInfo.memoryCacheKey;
		displayer = imageLoadingInfo.options.getDisplayer();
		listener = imageLoadingInfo.listener;
		this.engine = engine;
		this.loadedFrom = loadedFrom;
	}

	@Override
	public void run() {
		if (imageWrapper.isGC()) {
			Log.d("", memoryCacheKey + " is cancelled");
			listener.onLoadingCancelled(imageUri, imageWrapper.getWrappedView());
		} else if (isViewWasReused()) {
			Log.d("", memoryCacheKey + " is reused");
			listener.onLoadingCancelled(imageUri, imageWrapper.getWrappedView());
		} else {
			Log.d("",  "display " + memoryCacheKey + ", loadedFrom " + loadedFrom);
			displayer.display(bitmap, imageWrapper, loadedFrom);
			engine.cancelDisplayTaskFor(imageWrapper);
			listener.onLoadingComplete(imageUri, imageWrapper.getWrappedView(), bitmap);
		}
	}

	/** 该view是否去显示其他图片了 */
	private boolean isViewWasReused() {
		String currentCacheKey = engine.getLoadingUriForView(imageWrapper);
		return !memoryCacheKey.equals(currentCacheKey);
	}

}
