package com.example.ant_test.show_image_in_sdcard;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.util.ImageUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class NativeImageLoader {
	private LruCache<String, Bitmap> mMemoryCache;
	private static NativeImageLoader mInstance = new NativeImageLoader();
	private ExecutorService mImageThreadPool = Executors.newFixedThreadPool(1);
	
	
	
	private NativeImageLoader(){
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 4;
		mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
			}
		};
	}
	
	public static NativeImageLoader getInstance(){
		return mInstance;
	}
	
	public Bitmap loadNativeImage(final String path, final NativeImageCallBack mCallBack){
		return this.loadNativeImage(path, null, mCallBack);
	}
	
	public Bitmap loadNativeImage(final String path, final Point mPoint, final NativeImageCallBack mCallBack){
	
		Bitmap bitmap = getBitmapFromMemCache(path);
		
		final Handler mHander = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				mCallBack.onImageLoader((Bitmap)msg.obj, path);
			}
			
		};
		
		if(bitmap == null){
			mImageThreadPool.execute(new Runnable() {
				
				@Override
				public void run() {
					Bitmap mBitmap = decodeThumbBitmapForFile(path, mPoint == null ? 0: mPoint.x, mPoint == null ? 0: mPoint.y);
					Message msg = mHander.obtainMessage();
					msg.obj = mBitmap;
					mHander.sendMessage(msg);
					addBitmapToMemoryCache(path, mBitmap);
				}
			});
		}
		return bitmap;
	}
	
	public void loadImage(final String path, final View view, final int width, final int height) {
		new Thread() {
			private Bitmap bitmap = getBitmapFromMemCache(path);
			
			public void run() {
				if (bitmap == null) {
					Log.d(">>>>", "pic no found in cache");
					bitmap = ImageUtil.loadFromExternal(path, width, height);
				}
				final ImageView iv = (ImageView)view;
				if (bitmap != null && iv != null) {
					iv.post(new Runnable() {
						@Override
						public void run() {
							iv.setImageBitmap(bitmap);
							addBitmapToMemoryCache(path, bitmap);
						}
					});
				}
			}
		}.start();
	}

	private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null && bitmap != null) {
			mMemoryCache.put(key, bitmap);
		}
	}

	private Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}
	
	private Bitmap decodeThumbBitmapForFile(String path, int viewWidth, int viewHeight){
		BitmapFactory.Options options = new BitmapFactory.Options();
		
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = computeScale(options, viewWidth, viewHeight);
		
		options.inJustDecodeBounds = false;
		
		return BitmapFactory.decodeFile(path, options);
	}
	
	private int computeScale(BitmapFactory.Options options, int viewWidth, int viewHeight){
		int inSampleSize = 1;
		if(viewWidth == 0 || viewWidth == 0){
			return inSampleSize;
		}
		int bitmapWidth = options.outWidth;
		int bitmapHeight = options.outHeight;
		
		if(bitmapWidth > viewWidth || bitmapHeight > viewWidth){
			int widthScale = Math.round((float) bitmapWidth / (float) viewWidth);
			int heightScale = Math.round((float) bitmapHeight / (float) viewWidth);
			
			inSampleSize = widthScale < heightScale ? widthScale : heightScale;
		}
		return inSampleSize;
	}
	
	public interface NativeImageCallBack{
		public void onImageLoader(Bitmap bitmap, String path);
	}
}
