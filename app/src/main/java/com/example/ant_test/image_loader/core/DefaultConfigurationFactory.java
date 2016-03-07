package com.example.ant_test.image_loader.core;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.ant_test.image_loader.cache.disk.IDiskCache;
import com.example.ant_test.image_loader.cache.disk.UnlimitedDiscCache;
import com.example.ant_test.image_loader.cache.disk.ext.LruDiscCache;
import com.example.ant_test.image_loader.cache.disk.naming.IFileNameGenerator;
import com.example.ant_test.image_loader.cache.disk.naming.Md5FileNameGenerator;
import com.example.ant_test.image_loader.cache.memory.IMemoryCache;
import com.example.ant_test.image_loader.cache.memory.LruMemoryCache;
import com.example.ant_test.image_loader.decoder.IImageDecoder;
import com.example.ant_test.image_loader.decoder.ImageDecoder;
import com.example.ant_test.image_loader.displayer.IBitmapDisplayer;
import com.example.ant_test.image_loader.displayer.SimpleBitmapDisplayer;
import com.example.ant_test.image_loader.downloader.BaseImageDownloader;
import com.example.ant_test.image_loader.downloader.IImageDownloader;
import com.example.ant_test.image_loader.util.StorageUtils;

/**
 * 为ImageLoaderMainConfig提供options
 */
public class DefaultConfigurationFactory {
	
	public static Executor createExecutor(int threadPoolSize) {
		return new ThreadPoolExecutor(threadPoolSize, threadPoolSize, 0L, TimeUnit.MILLISECONDS, 
				new LinkedBlockingDeque<Runnable>(), createThreadFactory("vil-pool-"));
	}
	
	public static Executor createTaskDistributor() {
		return Executors.newCachedThreadPool(createThreadFactory("vil-pool-d-"));
	}

	/**
	 * 
	 * @param context
	 * @param diskCacheFileNameGenerator 图片名生成器
	 * @param diskCacheSize cache的最大值（单位byte）
	 * @param diskCacheFileCount 文件的数量的最大值
	 * @return 
	 */
	public static IDiskCache createDiskCache(Context context, IFileNameGenerator diskCacheFileNameGenerator,
			long diskCacheSize, int diskCacheFileCount) {
		if (diskCacheSize > 0) {//有限空间的缓存
			File individualCacheDir = StorageUtils.getIndividualCacheDirectory(context);//获取缓存文件夹(不可能为null或者"")
			try {
				return new LruDiscCache(individualCacheDir, diskCacheSize, diskCacheFileCount, diskCacheFileNameGenerator);
			} catch (IOException e) {
				Log.e("", e.getMessage());
				e.printStackTrace();
			}
		}
		//如果创建LruDiscCache失败，则创建UnlimitedDiscCache
		File cacheDir = StorageUtils.getCacheDirectory(context);
		return new UnlimitedDiscCache(cacheDir, null, diskCacheFileNameGenerator);
	}
	
	/**
	 * 创建文件名生成器
	 */
	public static IFileNameGenerator createFileNameGenerator() {
		return new Md5FileNameGenerator();
	}
	
	/**
	 * 根据指定大小memoryCacheSize，创建memory缓存
	 */
	public static IMemoryCache<String, Bitmap> createMemoryCache(int memoryCacheSize) {
		if (memoryCacheSize == 0) {
			memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);
		}
		return new LruMemoryCache(memoryCacheSize);
	}
	
	/**
	 * 创建图片下载器
	 */
	public static IImageDownloader createImageDownloader(Context context) {
		return new BaseImageDownloader(context);
	}
	
	/**
	 * 创建图片解码器
	 */
	public static IImageDecoder createImageDecoder() {
		return new ImageDecoder();
	}
	
	/**
	 * 创建图片显示器
	 * TODO 试试不同的显示效果
	 */
	public static IBitmapDisplayer createBitmapDisplayer() {
		return new SimpleBitmapDisplayer();
	}
	
	private static ThreadFactory createThreadFactory(String threadNamePrefix) {
		return new DefaultThreadFactory(threadNamePrefix);
	}
	
	private static class DefaultThreadFactory implements ThreadFactory{

		private static AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;
		
		public DefaultThreadFactory(String namePrefix) {
			this.namePrefix = namePrefix;
		}

		@Override
		public Thread newThread(Runnable r) {
			return new Thread(r, this.namePrefix + threadNumber.getAndIncrement());
		}
		
	}
}
