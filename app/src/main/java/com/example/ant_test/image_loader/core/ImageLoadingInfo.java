package com.example.ant_test.image_loader.core;

import java.util.concurrent.locks.ReentrantLock;

import com.example.ant_test.image_loader.helper.ImageSize;
import com.example.ant_test.image_loader.image_wrapper.IImageWrapper;
import com.example.ant_test.image_loader.linstener.ImageLoadingListener;
import com.example.ant_test.image_loader.linstener.ImageLoadingProgressListener;


/**
 * load and display image task要用到的information
 */
public class ImageLoadingInfo {
	final String uri;//图片的uri
	final String memoryCacheKey;//存在内存cache中的key
	final IImageWrapper imageWrapper;//imageview
	final ImageSize imageSize;//图片的尺寸
	final DisplayImageOptions options;//显示图片的options
	final ImageLoadingListener listener;
	final ImageLoadingProgressListener progressListener;
	/**
	 * 这把锁加在图片的uri上，即如果2个imageview同时加载同一张图片，
	 * 一个线程在下载前上锁，另一个线程等待，下载完成后，唤醒等待的
	 * 线程，然后发现这张图片已经下载完成会直接从本地缓存中读取（如
	 * 果下载完后保存到缓存中的话）
	 */
	final ReentrantLock loadFromUriLock;
	
	public ImageLoadingInfo(String uri, String memoryCacheKey,
			IImageWrapper imageWrapper, ImageSize imageSize,
			DisplayImageOptions options, ImageLoadingListener listener,
			ImageLoadingProgressListener progressListener,
			ReentrantLock loadFromUriLock) {
		super();
		this.uri = uri;
		this.memoryCacheKey = memoryCacheKey;
		this.imageWrapper = imageWrapper;
		this.imageSize = imageSize;
		this.options = options;
		this.listener = listener;
		this.progressListener = progressListener;
		this.loadFromUriLock = loadFromUriLock;
	}
	
	
	
}
