package com.example.ant_test.picshift;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @Title: BannerViewPagerAdapter.java
 * @Package com.example.ant_test.picshift
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-29 上午11:18:59
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class BannerPagerAdapter extends PagerAdapter{
	private List<ImageView> imageList;
	
	public BannerPagerAdapter(Context context, int[] res) {
		if (context != null && res != null) {
			imageList = new ArrayList<ImageView>();
			for (int i = 0; i < res.length; i++) {
				ImageView iv = new ImageView(context);
				iv.setImageDrawable(context.getResources().getDrawable(res[i]));
				imageList.add(iv);
			}
		}
	}
	
	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		if (imageList != null && imageList.size() > 0) {
			container.removeView(imageList.get(position % imageList.size()));
		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (imageList != null && imageList.size() > 0) {
			int size = imageList.size();
			View view = this.imageList.get(position % size);
			container.addView(view);
			return view;
		}
		return null;
	}
	
	public int getImageSize() {
		if (imageList != null) {
			return imageList.size();
		} 
		return 0;
	}
}
