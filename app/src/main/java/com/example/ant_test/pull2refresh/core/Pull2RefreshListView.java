package com.example.ant_test.pull2refresh.core;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * @Title: Pull2RefreshListView.java
 * @Package com.example.ant_test.pull2refresh.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-12-16 下午3:07:41
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class Pull2RefreshListView extends Pull2RefreshAdapterViewBase<ListView>{

	public Pull2RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Pull2RefreshListView(Context context, Pull2RefreshMode mode) {
		super(context, mode);
	}

	public Pull2RefreshListView(Context context) {
		super(context);
	}

	@Override
	protected ListView createRefreshableView(Context context, AttributeSet attrs) {
		ListView lv = createListView(context, attrs);
		lv.setId(android.R.id.list);
		return lv;
	}
	
	protected ListView createListView(Context context, AttributeSet attrs) {
		final ListView lv;
		if (VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD) {
			lv = new InternalListViewSDK9(context, attrs);
		} else {
			lv = new InternalListView(context, attrs);
		}
		return lv;
	}
	
	protected class InternalListView extends ListView {

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		protected void dispatchDraw(Canvas canvas) {
			try {
				super.dispatchDraw(canvas);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}

		@Override
		public boolean dispatchTouchEvent(MotionEvent ev) {
			try {
				return super.dispatchTouchEvent(ev);
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	@TargetApi(9)
	final class InternalListViewSDK9 extends InternalListView {

		public InternalListViewSDK9(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		//使得listview具有阻尼效果
		@Override
		protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
				int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

			final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
					scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
			Log.d("OverscrollHelper", "overScrollBy>>>>deltaY:" + deltaY);
			// Does all of the hard work...
			OverscrollHelper.overScrollBy(Pull2RefreshListView.this, deltaX, scrollX, deltaY, scrollY, isTouchEvent);

			return returnValue;
		}
	}

	
	/**
	 * 因为Listview如果上拉加载更多，需要至少显示1条新的数据告诉用户加载成功，所以listview要向上滑动一段距离，
	 * 因此重写了Pull2RefreshBase的onRefreshComplete方法
	 * @param orientation
	 */
	public void onRefreshComplete(final Pull2RefreshOrientation orientation) {
		if (isRefreshing()) {
			setState(Pull2RefreshState.RESET);
			//if (orientation == Pull2RefreshOrientation.UP) {//如果上拉加载更多
			if (getCurrentMode() == Pull2RefreshMode.MODE_END || getCurrentMode() == Pull2RefreshMode.MODE_MANUAL) {
				final ListView listview = getRefreshableView();
				if (listview != null) {
					final View firstView = listview.getChildAt(0);
					final int firstVisiblePosition = listview.getFirstVisiblePosition();
					if (firstView != null) {
						listview.setSelectionFromTop(firstVisiblePosition + 1, firstView.getTop());//用scrollBy或scrollTo会有些问题
					}
				}
			}
		}
		
	}
	
}
