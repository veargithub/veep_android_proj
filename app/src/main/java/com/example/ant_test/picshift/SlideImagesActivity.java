package com.example.ant_test.picshift;

import java.util.ArrayList;
import java.util.List;

import com.example.ant_test.R;
import com.example.ant_test.point_tips.PointTips;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @Title: SlideImagesActivity.java
 * @Package com.example.ant_test.picshift
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-28 下午4:52:12
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class SlideImagesActivity extends Activity implements OnPageChangeListener{
	private static final int BANNER_INTERVAL = 3000;
	private static final int BANNER_DURATION = 1000;
	private BannerViewPager pager;
	private PointTips point;
	private BannerPagerAdapter adapter;
	private static final int HANDLER_SHOW_NEXT_IMAGE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_slide_images);
		pager = (BannerViewPager) findViewById(R.id.viewpager);
		int[] res = new int[] {R.drawable.ps1, R.drawable.ps2, R.drawable.ps3, R.drawable.ps4};
		adapter = new BannerPagerAdapter(this, res);
		pager.setDuration(BANNER_DURATION);
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(this);
		point = (PointTips) findViewById(R.id.pointTips);
		point.setPointSize(adapter.getImageSize());
		point.setCurrent(0);
		
		handler.sendEmptyMessageDelayed(HANDLER_SHOW_NEXT_IMAGE, BANNER_INTERVAL + BANNER_DURATION);
	}
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case HANDLER_SHOW_NEXT_IMAGE : 
				pager.setCurrentItem(pager.getCurrentItem() + 1);
				this.sendEmptyMessageDelayed(HANDLER_SHOW_NEXT_IMAGE, BANNER_INTERVAL + BANNER_DURATION);
				break;
			}
		}
		
	};

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageSelected(int arg0) {
		point.setCurrent(arg0 % adapter.getImageSize());
	}
	
	
}
