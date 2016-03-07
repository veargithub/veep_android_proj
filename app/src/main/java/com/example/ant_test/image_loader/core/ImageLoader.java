package com.example.ant_test.image_loader.core;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.example.ant_test.image_loader.cache.disk.IDiskCache;
import com.example.ant_test.image_loader.cache.memory.IMemoryCache;
import com.example.ant_test.image_loader.helper.ImageSize;
import com.example.ant_test.image_loader.helper.LoadedFrom;
import com.example.ant_test.image_loader.helper.ViewScaleType;
import com.example.ant_test.image_loader.image_wrapper.IImageWrapper;
import com.example.ant_test.image_loader.image_wrapper.ImageNoViewWrapper;
import com.example.ant_test.image_loader.image_wrapper.ImageViewWrapper;
import com.example.ant_test.image_loader.linstener.ImageLoadingListener;
import com.example.ant_test.image_loader.linstener.ImageLoadingProgressListener;
import com.example.ant_test.image_loader.linstener.SimpleImageLoadingListener;
import com.example.ant_test.image_loader.util.ImageSizeUtil;

/**
 * image loader 的单例管理类，在调用这个类的其他方法之前，必须要先调用init方法
 */
public class ImageLoader {

	private ImageLoaderMainConfig configuration;//主配置
	private ImageLoaderEngine engine;//主引擎
	private final ImageLoadingListener emptyListener = new SimpleImageLoadingListener();//默认的监听器，表示不做任何监听
	private volatile static ImageLoader instance;//volatile在一定程度下可以节约一定的内存
	
	/**
	 * @return 单例
	 */
	public static ImageLoader getInstance() {
		if (instance == null) {
			synchronized (ImageLoader.class) {
				if (instance == null) {
					instance = new ImageLoader();
				}
			}
		}
		return instance;
	}
	
	private ImageLoader() {}
	
	/**
	 * 初始化主配置，如果已经被初始化过，则此次初始化失败
	 * @param configuration
	 */
	public synchronized void init(ImageLoaderMainConfig configuration) {
		if (configuration == null) {
			throw new IllegalArgumentException("main configuration must not be null");
		}
		if (this.configuration == null) {
			Log.d("", "init main config");
			engine = new ImageLoaderEngine(configuration);
			this.configuration = configuration;
		} else {
			Log.w("", "main config has been initialized, init failed");
		}
	}
	
	/**
	 * Image Loader是否被初始化过
	 */
	public boolean isInited() {
		return configuration != null;
	}
	/**
	 * 见 {@link #displayImage(String, IImageWrapper, DisplayImageOptions, ImageLoadingListener, ImageLoadingProgressListener)}
	 */
	public void displayImage(String uri, IImageWrapper imageWrapper) {
		displayImage(uri, imageWrapper, null, null, null);
	}
	/**
	 * 见 {@link #displayImage(String, IImageWrapper, DisplayImageOptions, ImageLoadingListener, ImageLoadingProgressListener)}
	 */
	public void displayImage(String uri, IImageWrapper imageWrapper, ImageLoadingListener listener) {
		displayImage(uri, imageWrapper, null, listener, null);
	}
	
	/**
	 * 页面最终都是调用这个方法来显示一张图片的
	 * @param uri http://xxx.xxx 或者 file:///mnt/sdcard/xxx/xxx
	 * @param imageWrapper 对于imageview的封装
	 * @param options 显示图片的options
	 * @param listener 加载一张图片的listener
	 * @param progressListener 加载图片的进度
	 */
	public void displayImage(String uri, IImageWrapper imageWrapper, DisplayImageOptions options,
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		checkConfiguration();//检查配置是否被初始化
		if (imageWrapper == null) {//没有imageview， 那图片显示在哪呢
			throw new IllegalArgumentException("image wrapper can not be null");
		}
		if (listener == null) {//没有linstener则自动给个空的
			listener = emptyListener;
		}
		if (options == null) {//如果显示图片的options为空，则给一个默认的
			options = configuration.defaultDisplayImageOptions;
		}
		if (TextUtils.isEmpty(uri)) {//如果uri为空
			engine.cancelDisplayTaskFor(imageWrapper);//在任务列表中删除这个任务，其实此时该任务还未进任务列表，
			//但有可能这个wrapper对应着其他的uri的任务还在列表里，这里顺便删掉，即使不删，后面也会做处理，问题不大。
			listener.onLoadingStarted(uri, imageWrapper.getWrappedView());
			if (options.shouldShowImageForEmptyUri()) {//设置uri为空应该显示的图片
				imageWrapper.setImageDrawable(options.getImageForEmptyUri(configuration.resources));
			} else {
				imageWrapper.setImageDrawable(null);
			}
			listener.onLoadingComplete(uri, imageWrapper.getWrappedView(), null);
			return;
		}
		ImageSize targetSize = ImageSizeUtil.defineTargetSizeForView(imageWrapper, configuration.getMaxImageSize());
		String memoryCacheKey = uri; //MemoryCacheUtils.generateKey(uri, targetSize);
		engine.prepareDisplayTaskFor(imageWrapper, memoryCacheKey);//把任务放入任务队列
		
		listener.onLoadingStarted(uri, imageWrapper.getWrappedView());
		Bitmap bmp = configuration.memoryCache.get(memoryCacheKey);//尝试从内存缓存里查找图片
		if (bmp != null && !bmp.isRecycled()) {//在内存中找到了
			Log.d("", "load image from memory!!");
			options.getDisplayer().display(bmp, imageWrapper, LoadedFrom.MEMORY);//直接显示图片
			listener.onLoadingComplete(uri, imageWrapper.getWrappedView(), bmp);//回调
		} else {//如果内存中没有该图片
			if (options.shouldShowImageOnLoading()) {//在加载的过程中是否要显示一张默认的图片
				imageWrapper.setImageDrawable(options.getImageOnLoading(configuration.resources));
			} else if (options.isResetViewBeforeLoading()) {//在加载之前是否要先清除掉原来的图片
				imageWrapper.setImageDrawable(null);
			}
			ImageLoadingInfo imageLoadingInfo = new ImageLoadingInfo(uri, memoryCacheKey, imageWrapper, targetSize, 
					options, listener, progressListener, engine.getLockForUri(uri));
			LoadAndDisplayImageTask displayTask = new LoadAndDisplayImageTask(engine, imageLoadingInfo,
					defineHandler(options));
			if (options.isSyncLoading()) {//如果在主线程中加载。。。
				displayTask.run();
			} else {
				engine.submit(displayTask);
			}
		}
	}
	/**
	 * 见 {@link #displayImage(String, IImageWrapper, DisplayImageOptions, ImageLoadingListener, ImageLoadingProgressListener)}
	 */
	public void displayImage(String uri, ImageView imageView) {
		displayImage(uri, new ImageViewWrapper(imageView), null, null, null);
	}
	/**
	 * 见 {@link #displayImage(String, IImageWrapper, DisplayImageOptions, ImageLoadingListener, ImageLoadingProgressListener)}
	 */
	public void displayImage(String uri, ImageView imageView, DisplayImageOptions options,
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		displayImage(uri, new ImageViewWrapper(imageView), options, listener, progressListener);
	}
	/**
	 * 加载一张图片，但并不立即显示
	 * @param uri http://xxx.xxx 或者 file:///mnt/sdcard/xxx/xxx
	 * @param targetImageSize 压缩的时候要用到
	 * @param options 显示图片的options
	 * @param listener 回调
	 * @param progressListener 进度listener
	 */
	public void loadImage(String uri, ImageSize targetImageSize, DisplayImageOptions options,
			ImageLoadingListener listener, ImageLoadingProgressListener progressListener) {
		checkConfiguration();
		if (targetImageSize == null) {
			targetImageSize = configuration.getMaxImageSize();
		}
		if (options == null) {
			options = configuration.defaultDisplayImageOptions;
		}

		ImageNoViewWrapper imageAware = new ImageNoViewWrapper(uri, targetImageSize, ViewScaleType.CROP);
		displayImage(uri, imageAware, options, listener, progressListener);
	}
	
	/**
	 * 检查config是否被初始化过, 如果没有初始化过，则无法加载图片，
	 * 见{@link #displayImage(String, IImageWrapper, DisplayImageOptions, ImageLoadingListener, ImageLoadingProgressListener)}
	 */
	private void checkConfiguration() {
		if (configuration == null) {
			throw new IllegalStateException("main config has been not initialized");
		}
	}
	/**
	 * 获取内存缓存
	 */
	public IMemoryCache<String, Bitmap> getMemoryCache() {
		checkConfiguration();
		return configuration.memoryCache;
	}
	/**
	 * 清除内存缓存
	 */
	public void clearMemoryCache() {
		checkConfiguration();
		configuration.memoryCache.clear();
	}
	/**
	 * 获取磁盘缓存
	 */
	public IDiskCache getDiskCache() {
		checkConfiguration();
		return configuration.diskCache;
	}
	/**
	 * 清除磁盘缓存
	 */
	public void clearDiskCache() {
		checkConfiguration();
		configuration.diskCache.clearCache();
	}
	/**
	 * 获取image wrapper已经在下载任务队列里的uri
	 */
	public String getLoadingUriForView(IImageWrapper imageWrapper) {
		return engine.getLoadingUriForView(imageWrapper);
	}

	/**
	 * 获取image view已经在下载任务队列里的uri
	 */
	public String getLoadingUriForView(ImageView imageView) {
		return engine.getLoadingUriForView(new ImageViewWrapper(imageView));
	}
	/**
	 * 取消image wrapper 对应的下载任务
	 */
	public void cancelDisplayTask(IImageWrapper imageWrapper) {
		engine.cancelDisplayTaskFor(imageWrapper);
	}
	/**
	 * 取消image view 对应的下载任务
	 */
	public void cancelDisplayTask(ImageView imageView) {
		engine.cancelDisplayTaskFor(new ImageViewWrapper(imageView));
	}
	
	/**
	 * 暂停所有任务，正在运行的任务不受影响
	 */
	public void pause() {
		engine.pause();
	}

	/** 恢复所有任务 */
	public void resume() {
		engine.resume();
	}
	/**
	 * 停止所有正在执行或等待执行的任务
	 */
	public void stop() {
		engine.stop();
	}
	/**
	 * 除了调用了stop外，还会释放一些资源，此外，依然可以通过init方法重新启动imageloader
	 */
	public void destroy() {
		if (configuration != null) Log.d("", "destroy image loader");
		stop();
		if (configuration != null) configuration.diskCache.close();
		engine = null;
		configuration = null;
	}
	/**
	 * @param options
	 * 生成一个默认的handler，用来当图片下载完成后把显示图片的runnable post到主线程中
	 */
	private static Handler defineHandler(DisplayImageOptions options) {
		Handler handler = options.getHandler();
		if (options.isSyncLoading()) {
			handler = null;
		} else if (handler == null && Looper.myLooper() == Looper.getMainLooper()) {
			handler = new Handler();
		}
		return handler;
	}
}
