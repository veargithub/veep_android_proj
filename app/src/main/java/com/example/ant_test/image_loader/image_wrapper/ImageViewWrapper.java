package com.example.ant_test.image_loader.image_wrapper;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.ant_test.image_loader.helper.ViewScaleType;

/**
 * @Title: ImageViewWrapper.java
 * @Package com.example.ant_test.image_loader.image_wrapper
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-29 上午10:41:28
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ImageViewWrapper implements IImageWrapper{

	protected Reference<ImageView> viewRef;
	
	public ImageViewWrapper(ImageView view) {
		if (view == null) {
			throw new IllegalArgumentException("view can not be null");
		}
		this.viewRef = new WeakReference<ImageView>(view);
	}

	@Override
	public int getWidth() {
		ImageView iv = viewRef.get();
		int width = 0;
		if (iv != null) {
			width = iv.getWidth();
		}
		if (width < 0) width = 0;
		return width;
	}

	@Override
	public int getHeight() {
		ImageView iv = viewRef.get();
		int height = 0;
		if (iv != null) {
			height = iv.getHeight();
		}
		if (height < 0) height = 0;
		return height;
	}

	@Override
	public ViewScaleType getScaleType() {
		return ViewScaleType.CROP;
	}

	@Override
	public View getWrappedView() {
		return this.viewRef.get();
	}

	@Override
	public boolean isGC() {
		return getWrappedView() == null;
	}

	@Override
	public int getId() {
		ImageView iv = (ImageView)getWrappedView();
		return iv == null ? super.hashCode() : iv.hashCode();
	}

	@Override
	public boolean setImageDrawable(Drawable drawable) {
		if (Looper.myLooper() == Looper.getMainLooper()) {
			ImageView iv = (ImageView) getWrappedView();
			if (iv != null) {
				iv.setImageDrawable(drawable);
				if (drawable instanceof AnimationDrawable) {
					((AnimationDrawable) drawable).start();
				}
			}
			return true;
		} else {
			Log.d("", "can not set image, it's not in main thread");
		}
		return false;
	}

	@Override
	public boolean setImageBitmap(Bitmap bitmap) {
		if (Looper.myLooper() == Looper.getMainLooper()) {
			ImageView iv = (ImageView) getWrappedView();
			if (iv != null) {
				iv.setImageBitmap(bitmap);
			}
			return true;
		} else {
			Log.d("", "can not set image, it's not in main thread");
		}
		return false;
	}

}
