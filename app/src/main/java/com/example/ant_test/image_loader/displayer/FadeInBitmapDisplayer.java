package com.example.ant_test.image_loader.displayer;

import android.graphics.Bitmap;
import android.os.DeadObjectException;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.DecelerateInterpolator;

import com.example.ant_test.image_loader.helper.LoadedFrom;
import com.example.ant_test.image_loader.image_wrapper.IImageWrapper;

/**
 * 以fadein的方式显示图片
 */
public class FadeInBitmapDisplayer implements IBitmapDisplayer{
	
	private final boolean doFromNetWork;
	private final boolean doFromMomery;
	private final boolean doFromDisk;
	
	private final int durationMillis;
	
	public FadeInBitmapDisplayer(int durationMillis) {
		this(durationMillis, true, true, true);
	}

	public FadeInBitmapDisplayer(int durationMillis, boolean doFromNetWork, boolean doFromMomery,
			boolean doFromDisk) {
		this.doFromNetWork = doFromNetWork;
		this.doFromMomery = doFromMomery;
		this.doFromDisk = doFromDisk;
		this.durationMillis = durationMillis;
	}

	@Override
	public void display(Bitmap bitmap, IImageWrapper wrapper, LoadedFrom from) {
		if ((doFromNetWork && from == LoadedFrom.NETWORK) ||
				(doFromMomery && from == LoadedFrom.MEMORY) ||
				(doFromDisk && from == LoadedFrom.DISC)) {
			animate(wrapper.getWrappedView(), durationMillis);
		}
		
	}

	public static final void animate(View view, int durationMillis) {
		AlphaAnimation aa = new AlphaAnimation(0, 1);
		aa.setDuration(durationMillis);
		aa.setInterpolator(new DecelerateInterpolator());
		view.startAnimation(aa);
	}
}
