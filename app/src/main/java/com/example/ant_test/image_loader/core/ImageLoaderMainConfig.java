package com.example.ant_test.image_loader.core;

import java.util.concurrent.Executor;
import com.example.ant_test.image_loader.cache.disk.IDiskCache;
import com.example.ant_test.image_loader.cache.disk.naming.IFileNameGenerator;
import com.example.ant_test.image_loader.cache.memory.IMemoryCache;
import com.example.ant_test.image_loader.decoder.IImageDecoder;
import com.example.ant_test.image_loader.downloader.IImageDownloader;
import com.example.ant_test.image_loader.helper.ImageSize;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * @Title: ILConfig.java
 * @Package com.example.ant_test.image_loader.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-10-10 上午10:42:22
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ImageLoaderMainConfig {
	
	final Resources resources;
	
	final Executor taskExecutor;//任务executor
	final Executor taskExecutorForCachedImages;//缓存图片的任务executor
	final boolean customExecutor;//是否使用了自己的executor
	final boolean customExecutorForCachedImages;//是否使用了自己的executor
	
	final int threadPoolSize;//线程池数量
	
	final IMemoryCache<String, Bitmap> memoryCache;//内存缓存
	final IDiskCache diskCache;//磁盘缓存
	final IImageDownloader downloader;//图片下载器
	final IImageDecoder decoder;//图片解码器
	final DisplayImageOptions defaultDisplayImageOptions;//显示图片的options
	
	private ImageLoaderMainConfig(final Builder builder) {
		resources = builder.context.getResources();

		taskExecutor = builder.taskExecutor;
		taskExecutorForCachedImages = builder.taskExecutorForCachedImages;
		threadPoolSize = builder.threadPoolSize;
	
		diskCache = builder.diskCache;
		memoryCache = builder.memoryCache;
		
		downloader = builder.downloader;
		decoder = builder.decoder;
		defaultDisplayImageOptions = builder.defaultDisplayImageOptions;

		customExecutor = builder.customExecutor;
		customExecutorForCachedImages = builder.customExecutorForCachedImages;
	}
	
	ImageSize getMaxImageSize() {
		DisplayMetrics displayMetrics = resources.getDisplayMetrics();
		return new ImageSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
	}
	
	public static final class Builder {
		public static final int DEFAULT_THREAD_POOL_SIZE = 3;
		
		private Context context;
		
		private Executor taskExecutor = null;
		private Executor taskExecutorForCachedImages = null;
		private boolean customExecutor = false;
		private boolean customExecutorForCachedImages = false;
		
		private int threadPoolSize = DEFAULT_THREAD_POOL_SIZE;
		
		private int memoryCacheSize = 0;
		private long diskCacheSize = 0;
		private int diskCacheFileCount = 0;

		private IMemoryCache<String, Bitmap> memoryCache = null;
		private IDiskCache diskCache = null;
		private IFileNameGenerator diskCacheFileNameGenerator = null;
		private IImageDownloader downloader = null;
		private IImageDecoder decoder = null;
		private DisplayImageOptions defaultDisplayImageOptions = null;
		
		public Builder(Context context) {
			this.context = context.getApplicationContext();
		}
		
		public Builder taskExecutor(Executor executor) {
			if (this.taskExecutor != null) {
				Log.w("", "overlap!!!");
			}
			this.taskExecutor = executor;
			return this;
		}
		
		public Builder taskExecutorForCachedImages(Executor executorForCachedImages) {
			if (this.taskExecutorForCachedImages != null) {
				Log.w("", "overlap!!!");
			}

			this.taskExecutorForCachedImages = executorForCachedImages;
			return this;
		}
		
		public Builder threadPoolSize(int threadPoolSize) {
			if (this.threadPoolSize != DEFAULT_THREAD_POOL_SIZE) {
				Log.w("", "overlap!!!");
			}

			this.threadPoolSize = threadPoolSize;
			return this;
		}
		
		public Builder memoryCacheSize(int memoryCacheSize) {
			if (memoryCacheSize <= 0) throw new IllegalArgumentException("memoryCacheSize must be a positive number");

			if (memoryCache != null) {
				Log.w("", "overlap!!!");
			}

			this.memoryCacheSize = memoryCacheSize;
			return this;
		}
		
		/**
		 * 使用虚拟机最大内存的某百分比
		 * @param availableMemoryPercent 百分比[1,99]
		 * @return
		 */
		public Builder memoryCacheSizePercentage(int availableMemoryPercent) {
			if (availableMemoryPercent <= 0 || availableMemoryPercent >= 100) {
				throw new IllegalArgumentException("availableMemoryPercent must be in range (0 < % < 100)");
			}

			if (memoryCache != null) {
				Log.w("", "overlap!!!");
			}

			long availableMemory = Runtime.getRuntime().maxMemory();
			memoryCacheSize = (int) (availableMemory * (availableMemoryPercent / 100f));
			return this;
		}
		
		public Builder memoryCache(IMemoryCache<String, Bitmap> memoryCache) {
			if (memoryCacheSize != 0) {
				Log.w("", "overlap!!!");
			}

			this.memoryCache = memoryCache;
			return this;
		}
		
		public Builder diskCacheSize(int maxCacheSize) {
			if (maxCacheSize <= 0) throw new IllegalArgumentException("maxCacheSize must be a positive number");

			if (diskCache != null) {
				Log.w("", "overlap!!!");
			}

			this.diskCacheSize = maxCacheSize;
			return this;
		}
		
		
		
		public Builder diskCacheFileNameGenerator(IFileNameGenerator fileNameGenerator) {
			if (diskCache != null) {
				Log.w("", "overlap!!!");
			}

			this.diskCacheFileNameGenerator = fileNameGenerator;
			return this;
		}
		
		public Builder diskCache(IDiskCache diskCache) {
			
			if (diskCache != null) {
				Log.w("", "overlap!!!");
			}

			this.diskCache = diskCache;
			return this;
		}
		
		public Builder imageDownloader(IImageDownloader imageDownloader) {
			this.downloader = imageDownloader;
			return this;
		}
		
		public Builder imageDecoder(IImageDecoder imageDecoder) {
			this.decoder = imageDecoder;
			return this;
		}
		
		public Builder defaultDisplayImageOptions(DisplayImageOptions defaultDisplayImageOptions) {
			this.defaultDisplayImageOptions = defaultDisplayImageOptions;
			return this;
		}
		
		public ImageLoaderMainConfig build() {
			initEmptyFieldsWithDefaultValues();
			return new ImageLoaderMainConfig(this);
		}
		
		private void initEmptyFieldsWithDefaultValues() {
			if (taskExecutor == null) {
				taskExecutor = DefaultConfigurationFactory.createExecutor(threadPoolSize);
			} else {
				customExecutor = true;
			}
			if (taskExecutorForCachedImages == null) {
				taskExecutorForCachedImages = DefaultConfigurationFactory.createExecutor(threadPoolSize);
			} else {
				customExecutorForCachedImages = true;
			}
			if (diskCache == null) {
				if (diskCacheFileNameGenerator == null) {
					diskCacheFileNameGenerator = DefaultConfigurationFactory.createFileNameGenerator();
				}
				diskCache = DefaultConfigurationFactory
						.createDiskCache(context, diskCacheFileNameGenerator, diskCacheSize, diskCacheFileCount);
			}
			if (memoryCache == null) {
				memoryCache = DefaultConfigurationFactory.createMemoryCache(memoryCacheSize);
			}
			
			if (downloader == null) {
				downloader = DefaultConfigurationFactory.createImageDownloader(context);
			}
			if (decoder == null) {
				decoder = DefaultConfigurationFactory.createImageDecoder();
			}
			if (defaultDisplayImageOptions == null) {
				defaultDisplayImageOptions = DisplayImageOptions.createSimple();
			}
		}
	}

}
