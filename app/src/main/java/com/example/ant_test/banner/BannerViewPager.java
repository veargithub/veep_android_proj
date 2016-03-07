package com.example.ant_test.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

public class BannerViewPager extends ViewPager{

	private BannerViewPagerScroller mScroller;

	public BannerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BannerViewPager(Context context) {
		super(context);
		init();
	}
	
	private void init() {
//		mScroller = new ViewPagerScroller(context);
		try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewpager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);
            mScroller = new BannerViewPagerScroller(getContext());
            scroller.set(this, mScroller);
        } catch (Exception e) {
        	Log.d(">>>>", "reflect failed");
        }
	}

	public void setDuration(int millsec) {
		if (mScroller != null) {
			mScroller.setDuration(millsec);
		}
	}
	/**
	 * @param arg0 adapter
	 */

	public void setAdapter(BannerPagerAdapter arg0) {
		super.setAdapter(arg0);
		if (arg0.getImageSize() != 0) {
			this.setCurrentItem(arg0.getCount() / 2 - arg0.getCount() / 2 % arg0.getImageSize());
		}
	}

	class BannerViewPagerScroller extends Scroller {
		private int mDuration = 1000;
		
		public BannerViewPagerScroller(Context context, Interpolator interpolator) {
			super(context, interpolator);
		}

		public BannerViewPagerScroller(Context context) {
			super(context);
		}

		@Override
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	        super.startScroll(startX, startY, dx, dy, mDuration);
	    }

	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy) {
	        super.startScroll(startX, startY, dx, dy, mDuration);
	    }
	    
	    public void setDuration(int duration) {
	    	this.mDuration = duration;
	    }
	}
}
