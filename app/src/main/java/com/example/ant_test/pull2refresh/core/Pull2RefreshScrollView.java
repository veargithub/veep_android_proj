package com.example.ant_test.pull2refresh.core;


import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;



/**
 * @Title: Pull2RefreshScrollView.java
 * @Package com.example.ant_test.pull2refresh.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-12-9 下午4:58:54
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class Pull2RefreshScrollView extends Pull2RefreshBase<ScrollView>{

	public Pull2RefreshScrollView(Context context) {
		super(context);
	}

	public Pull2RefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Pull2RefreshScrollView(Context context, Pull2RefreshMode mode) {
		super(context, mode);
	}

//	public Pull2RefreshScrollView(Context context, Pull2RefreshMode mode, AnimationStyle style) {
//		super(context, mode, style);
//	}

	

	@Override
	protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {
		ScrollView scrollView;
//		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
//			scrollView = new InternalScrollViewSDK9(context, attrs);
//		} else {
			scrollView = new ScrollView(context, attrs);
//		}

		//scrollView.setId(R.id.scrollview);
		return scrollView;
	}

	@Override
	protected boolean isReadyForPullStart() {
		return mRefreshableView.getScrollY() == 0;
	}

	@Override
	protected boolean isReadyForPullEnd() {
		View scrollViewChild = mRefreshableView.getChildAt(0);
		if (null != scrollViewChild) {
			return mRefreshableView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
		}
		return false;
	}

//	@TargetApi(9)
//	final class InternalScrollViewSDK9 extends ScrollView {
//
//		public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
//			super(context, attrs);
//		}
//
//		@Override
//		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
//				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
//
//			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
//					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
//
//			// Does all of the hard work...
//			OverscrollHelper.overScrollBy(PullToRefreshScrollView.this, deltaX, scrollX, deltaY, scrollY,
//					getScrollRange(), isTouchEvent);
//
//			return returnValue;
//		}
//
//		/**
//		 * Taken from the AOSP ScrollView source
//		 */
//		private int getScrollRange() {
//			int scrollRange = 0;
//			if (getChildCount() > 0) {
//				View child = getChildAt(0);
//				scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
//			}
//			return scrollRange;
//		}
//	}
}
