package com.example.ant_test.image_loader.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.example.ant_test.image_loader.decoder.IImageDecoder;
import com.example.ant_test.image_loader.decoder.ImageDecoderInfo;
import com.example.ant_test.image_loader.downloader.IImageDownloader;
import com.example.ant_test.image_loader.downloader.IImageDownloader.Scheme;
import com.example.ant_test.image_loader.helper.FailReason;
import com.example.ant_test.image_loader.helper.FailReason.FailType;
import com.example.ant_test.image_loader.helper.ImageSize;
import com.example.ant_test.image_loader.helper.LoadedFrom;
import com.example.ant_test.image_loader.helper.ViewScaleType;
import com.example.ant_test.image_loader.image_wrapper.IImageWrapper;
import com.example.ant_test.image_loader.linstener.ImageLoadingListener;
import com.example.ant_test.image_loader.linstener.ImageLoadingProgressListener;
import com.example.ant_test.image_loader.util.IoUtils;

/**
 * @Title: LoadAndDisplayImageTask.java
 * @Package com.example.ant_test.image_loader.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-10-16 下午5:28:51
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class LoadAndDisplayImageTask implements Runnable, IoUtils.CopyListener{

	private final ImageLoaderEngine engine;//下载代理类
	private final ImageLoadingInfo imageLoadingInfo;//对一些下载信息的封装
	private final Handler handler;
	private final IImageDecoder decoder;
	private final ImageLoaderMainConfig configuration;//下载的配置信息
	private final IImageDownloader downloader;//下载器
	
	final String uri;//这个uri来自于ImageLoadingInfo，但由于用的太过频繁，所以单独抽出来方便使用
	private final String memoryCacheKey;//这个是保存在内存缓存map中的key
	final IImageWrapper wrapper;//对于imageview的封装
	private final ImageSize targetSize;
	final DisplayImageOptions options;
	final ImageLoadingListener listener;//回调
	final ImageLoadingProgressListener progressListener;//进度回调
	
	private LoadedFrom loadedFrom = LoadedFrom.NETWORK;
	private final boolean syncLoading;
	
	public LoadAndDisplayImageTask(ImageLoaderEngine engine, ImageLoadingInfo imageLoadingInfo, Handler handler) {
		this.engine = engine;
		this.imageLoadingInfo = imageLoadingInfo;
		this.handler = handler;

		configuration = engine.configuration;
		downloader = configuration.downloader;
		
		decoder = configuration.decoder;
		uri = imageLoadingInfo.uri;
		memoryCacheKey = imageLoadingInfo.memoryCacheKey;
		wrapper = imageLoadingInfo.imageWrapper;
		targetSize = imageLoadingInfo.imageSize;
		options = imageLoadingInfo.options;
		listener = imageLoadingInfo.listener;
		progressListener = imageLoadingInfo.progressListener;
		syncLoading = options.isSyncLoading();
	}

	@Override
	public void run() {
		if (waitIfPaused()) return;//如果当前线程在wait的过程中被意外中断则直接返回
		ReentrantLock loadFromUriLock = imageLoadingInfo.loadFromUriLock;
		Log.d("", "start display image task:" + memoryCacheKey);
		if (loadFromUriLock.isLocked()) {//如果被锁，说明这个uri的图片正在被下载
			Log.d("", "waiting for image loading:" + memoryCacheKey);
		}
		loadFromUriLock.lock();//防止2个相同uri的图片同时被下载，浪费资源
		Bitmap bmp;
		try {
			checkTaskNotActual();/** 在#waitIfPaused已经调用过了，这里再调用是因为在2次调用之间，情况可能发生了变化*/
			bmp = configuration.memoryCache.get(memoryCacheKey);//首先从内存缓存中尝试获取图片
			if (bmp == null || bmp.isRecycled()) {//如果在内存里没找到
				bmp = tryLoadBitmap();//加载图片
				if (bmp == null) return; //回调在tryLoadBitmap中已经被调用过
				checkTaskNotActual();//检查。。
				checkTaskInterrupted();//检查。。
				configuration.memoryCache.put(memoryCacheKey, bmp);//把图片缓存在内存里
			} else {//在内存中找到了这张图片
				loadedFrom = LoadedFrom.MEMORY;
				Log.d("", "load image from memory");
			}
		
		} catch (TaskCancelledException e) {//任务被取消
			fireCancelEvent();
			return;
		} finally {
			loadFromUriLock.unlock();//解锁
		}
		DisplayBitmapTask displayBitmapTask = new DisplayBitmapTask(bmp, imageLoadingInfo, engine, loadedFrom);
		runTask(displayBitmapTask, syncLoading, handler, engine);
		
	}
	
	/** @return <b>true</b> - 任务该被中断; <b>false</b> - 否则 */
	private boolean waitIfPaused() {
		AtomicBoolean pause = engine.getPause();
		if (pause.get()) {
			synchronized (engine.getPauseLock()) {
				if (pause.get()) {
					Log.d("", "wait for resume:" + memoryCacheKey);
					try {
						engine.getPauseLock().wait();
					} catch (InterruptedException e) {
						Log.e("", "waitIfPaused interrupted:" + memoryCacheKey);
						return true;
					}
					Log.d("", "resume after pause:" + memoryCacheKey);
				}
			}
		}
		return isTaskNotActual();
	}
	
	/**
	 * 加载图片，可能是从本地，也可能是网络
	 * @throws TaskCancelledException
	 */
	private Bitmap tryLoadBitmap() throws TaskCancelledException {
		Bitmap bitmap = null;
		try {
			File imageFile = configuration.diskCache.get(uri);//尝试从磁盘缓存中获取图片
			if (imageFile != null && imageFile.exists()) {//如果图片在磁盘中存在
				Log.d("", "load image from disk");
				loadedFrom = LoadedFrom.DISC;//设置图片来源
				checkTaskNotActual();//再次检查。。。TODO
				bitmap = loadAndDecodeImage(Scheme.FILE.wrap(imageFile.getAbsolutePath()));//从本地加载并压缩图片
			}
			if (bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {//本地加载失败或者图片已损坏
				Log.d("", "load image from network");
				loadedFrom = LoadedFrom.NETWORK;//设置图片来源
				if (saveImageOnDisk(downloadImage(uri))) {//如果成功地下载并保存图片到本地
					//TODO resize and save, maybe using byte array will be better
					imageFile = configuration.diskCache.get(uri);//尝试从磁盘缓存中获取图片
					if (imageFile != null && imageFile.exists()) {//如果图片在磁盘中存在
						checkTaskNotActual();//例行检查。。。
						bitmap = loadAndDecodeImage(Scheme.FILE.wrap(imageFile.getAbsolutePath()));//再次从本地加载并压缩图片
					}
				}
				if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
					fireFailEvent(FailType.DECODING_ERROR, null);
				}
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
			fireFailEvent(FailType.NETWORK_DENIED, null);
		} catch (TaskCancelledException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			//Log.e("", e.getMessage());
			fireFailEvent(FailType.IO_ERROR, e);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			//Log.e("", e.getMessage());
			fireFailEvent(FailType.OUT_OF_MEMORY, e);
		} catch (Throwable e) {
			e.printStackTrace();
			//Log.e("", e.getMessage());
			fireFailEvent(FailType.UNKNOWN, e);
		}
		return bitmap;
	}
	/**
	 * 从本地加载图片，然后压缩返回
	 * @param imageUri file:///xxx/yyy/zzz
	 * @throws IOException
	 */
	private Bitmap loadAndDecodeImage(String imageUri) throws IOException {
		ViewScaleType viewScaleType = wrapper.getScaleType();
		ImageDecoderInfo decodingInfo = new ImageDecoderInfo(imageUri,/* getDownloader(),*/ downloadImage(imageUri), targetSize, viewScaleType, options.getSampleType());
		return decoder.decode(decodingInfo);
	}
	/**
	 * 根据uri选择从本地加载图片或者从网络下载图片
	 * @param imageUri
	 * @throws IOException
	 */
	private InputStream downloadImage(String imageUri) throws IOException{
		return getDownloader().getStream(imageUri);
	}
	/**
	 * 将下载下来的图片保存到本地
	 * @param is 
	 * @throws IOException
	 */
	private boolean saveImageOnDisk(InputStream is) throws IOException{
		return configuration.diskCache.save(uri, is, this);
	}
	
	/** @return <b>true</b> - if loading should be continued; <b>false</b> - if loading should be interrupted */
	private boolean fireProgressEvent(final int current, final int total) {
		if (isTaskInterrupted() || isTaskNotActual()) return false;
		if (progressListener != null) {
			Runnable r = new Runnable() {
				@Override
				public void run() {
					progressListener.onProgressUpdate(uri, wrapper.getWrappedView(), current, total);
				}
			};
			runTask(r, false, handler, engine);
		}
		return true;
	}
	
	private void fireFailEvent(final FailType failType, final Throwable failCause) {
		if (isTaskInterrupted() || isTaskNotActual()) return;
		Runnable r = new Runnable() {
			@Override
			public void run() {
				if (options.shouldShowImageOnFail()) {
					wrapper.setImageDrawable(options.getImageOnFail(configuration.resources));
				}
				listener.onLoadingFailed(uri, wrapper.getWrappedView(), new FailReason(failType, failCause));
			}
		};
		runTask(r, false, handler, engine);
	}
	
	private void fireCancelEvent() {
		if (isTaskInterrupted()) return;
		Runnable r = new Runnable() {
			@Override
			public void run() {
				listener.onLoadingCancelled(uri, wrapper.getWrappedView());
			}
		};
		runTask(r, false, handler, engine);
	}
	
	private IImageDownloader getDownloader() {
		return this.downloader;
	}
	
	/**
	 * @throws TaskCancelledException 如果该task现在无法执行
	 */
	private void checkTaskNotActual() throws TaskCancelledException {
		checkViewCollected();
		checkViewReused();
	}
	
	/**
	 * 该task现在是否处在不能调用的状态
	 * @return true - task无法执行，false - task可以被执行
	 */
	private boolean isTaskNotActual() {
		return isViewCollected() || isViewReused();
	}
	
	/** @throws TaskCancelledException 如果view被回收 */
	private void checkViewCollected() throws TaskCancelledException {
		if (isViewCollected()) {
			throw new TaskCancelledException();
		}
	}

	/** @return <b>true</b> - 如果view被GC回收了; <b>false</b> - 否则 */
	private boolean isViewCollected() {
		if (wrapper.isGC()) {
			Log.d("", "isViewCollected:" + memoryCacheKey);
			return true;
		}
		return false;
	}

	/** @throws TaskCancelledException 如果view被用来显示其他的图片 */
	private void checkViewReused() throws TaskCancelledException {
		if (isViewReused()) {
			throw new TaskCancelledException();
		}
	}

	/** @return <b>true</b> - 如果view被用来显示其他的图片; <b>false</b> - 否则 */
	private boolean isViewReused() {
		String currentCacheKey = engine.getLoadingUriForView(wrapper);
		boolean imageAwareWasReused = !memoryCacheKey.equals(currentCacheKey);
		if (imageAwareWasReused) {
			Log.d("", "isViewReused:" + memoryCacheKey);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onBytesCopied(int current, int total) {
		return fireProgressEvent(current, total);
	}
	
	/** @throws TaskCancelledException 如果线程被中断了*/
	private void checkTaskInterrupted() throws TaskCancelledException {
		if (isTaskInterrupted()) {
			throw new TaskCancelledException();
		}
	}

	/** @return <b>true</b> - 如果线程被中断; <b>false</b> - 否则 */
	private boolean isTaskInterrupted() {
		if (Thread.interrupted()) {
			Log.d("", "isTaskInterrupted:" + memoryCacheKey);
			return true;
		}
		return false;
	}
	
	String getLoadingUri() {
		return uri;
	}
	
	/**
	 * 处理一个runnable
	 * @param r runnable
	 * @param sync 是否同步，如果是，则直接在当前线程run
	 * @param handler 用来把runnable post到主线程
	 * @param engine 把runnable放到分发任务的线程池中执行，如果线程池阻塞中，则这个任务也会被阻塞
	 */
	static void runTask(Runnable r, boolean sync, Handler handler, ImageLoaderEngine engine) {
		if (sync) {
			r.run();
		} else if (handler == null) {
			engine.fireCallback(r);
		} else {
			handler.post(r);
		}
	}
	
	class TaskCancelledException extends Exception {
		private static final long serialVersionUID = -2566901938808122698L;
	}
}
