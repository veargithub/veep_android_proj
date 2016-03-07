package com.example.ant_test.image_loader.core;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;
import com.example.ant_test.image_loader.image_wrapper.IImageWrapper;

/**
 * 下载引擎负责执行@link LoadAndDisplayImageTask
 */
public class ImageLoaderEngine {

	final ImageLoaderMainConfig configuration;//下载主配置
	
	private Executor taskExecutor;//下载用
	private Executor taskExecutorForCachedImages;//从缓存中读取用
	private Executor taskDistributor;//分发用，决定任务去往哪个executor
	
	private final Map<Integer, String> cacheKeysForImageWrappers = Collections
			.synchronizedMap(new HashMap<Integer, String>());//key对应的是wrapper的id，value对应的是这个wrapper将要加载的图片uri
	private final Map<String, ReentrantLock> uriLocks = new WeakHashMap<String, ReentrantLock>();//key对应的是图片的uri，value对应的是同步锁
	/*  关于上面2个map：可能会出现一种情况，一个页面有2个imageview同时加载同一张图片，那么在cacheKeysForImageWrappers中，会有2条记录
	  	，因为他们的id是不同的，但uriLocks里只有一条记录，因为他们的uri是相同的 */
	
	private final AtomicBoolean paused = new AtomicBoolean(false);//如果paused为true，则下载线程会wait，且如果paused被设置为false，这些线程会被唤醒
	private final Object pauseLock = new Object();//paused属性所对应的锁
	
	ImageLoaderEngine(ImageLoaderMainConfig configuration) {
		this.configuration = configuration;

		taskExecutor = configuration.taskExecutor;
		taskExecutorForCachedImages = configuration.taskExecutorForCachedImages;

		taskDistributor = DefaultConfigurationFactory.createTaskDistributor();
	}
	
	void submit(final LoadAndDisplayImageTask task) {
		taskDistributor.execute(new Runnable() {
			@Override
			public void run() {
				File image = configuration.diskCache.get(task.getLoadingUri());//根据uri从disk中获取图片，可能为null
				boolean isImageCachedOnDisk = image != null && image.exists();
				initExecutorsIfNeed();
				if (isImageCachedOnDisk) {
					taskExecutorForCachedImages.execute(task);
				} else {
					taskExecutor.execute(task);
				}
			}
		});
	}
	
	private void initExecutorsIfNeed() {
		if (((ExecutorService) taskExecutor).isShutdown()) {
			taskExecutor = createTaskExecutor();
		}
		if (((ExecutorService) taskExecutorForCachedImages).isShutdown()) {
			taskExecutorForCachedImages = createTaskExecutor();
		}
	}
	/**
	 * 根据configuration创建executor
	 */
	private Executor createTaskExecutor() {
		return DefaultConfigurationFactory.createExecutor(configuration.threadPoolSize);
	}
	/**
	 * 在缓存中寻找uri
	 */
	String getLoadingUriForView(IImageWrapper wrapper) {
		return cacheKeysForImageWrappers.get(wrapper.getId());
	}
	/**
	 * 向缓存中插入一条记录
	 * 这个方法在task进入线程池之前被调用，并且在这个task被执行的时候会调用{@link #getLoadingUriForView(IImageWrapper)}
	 * 来检查，这段时间内是否存在相同的wrapper但不同的memoryCacheKey被add进了cache，如果是，则该task取消
	 * 这样就解决了listview在滑动的过程中，imageview后加载的图片会覆盖掉之前的
	 */
	void prepareDisplayTaskFor(IImageWrapper wrapper, String memoryCacheKey) {
		cacheKeysForImageWrappers.put(wrapper.getId(), memoryCacheKey);
	}
	/**
	 * 向缓存中移除一条记录(这样做就相当于间接移除一个任务,任务会执行，但没有真正去加载图片)
	 */
	void cancelDisplayTaskFor(IImageWrapper imageAware) {
		cacheKeysForImageWrappers.remove(imageAware.getId());
	}
	/**
	 * 暂停所有后台加载线程
	 */
	void pause() {
		paused.set(true);
	}
	/**
	 * 使暂停的线程恢复
	 */
	void resume() {
		paused.set(false);
		synchronized (pauseLock) {
			pauseLock.notifyAll();
		}
	}
	/**
	 * 停止所有后台线程
	 */
	void stop() {
		((ExecutorService) taskExecutor).shutdownNow();
		((ExecutorService) taskExecutorForCachedImages).shutdownNow();
		cacheKeysForImageWrappers.clear();
		uriLocks.clear();
	}
	/**
	 * 直接在后台执行一个任务
	 */
	void fireCallback(Runnable r) {
		taskDistributor.execute(r);
	}
	/**
	 * 获取uri对应的锁(一开始这锁是不存在的，会new一个)
	 */
	ReentrantLock getLockForUri(String uri) {
		ReentrantLock lock = uriLocks.get(uri);
		if (lock == null) {
			lock = new ReentrantLock();
			uriLocks.put(uri, lock);
		}
		return lock;
	}
	
	AtomicBoolean getPause() {
		return paused;
	}

	Object getPauseLock() {
		return pauseLock;
	}
}
