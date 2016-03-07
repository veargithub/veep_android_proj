package com.example.ant_test.picshift;

import java.lang.reflect.Field;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * @Title: MyViewPager.java
 * @Package com.example.ant_test.picshift
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-29 上午9:13:50
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class BannerViewPager extends ViewPager{
	private BannerViewPagerScroller mScroller;

	public BannerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public BannerViewPager(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
//		mScroller = new ViewPagerScroller(context);
		try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = viewpager.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);
            mScroller = new BannerViewPagerScroller(getContext());
            scroller.set(this, mScroller);
            Log.d(">>>>", "reflect success");
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
