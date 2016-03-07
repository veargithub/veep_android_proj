package com.example.ant_test.pull2refresh.core;

import com.example.ant_test.pull2refresh.listener.OnLastItemVisibleListener;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @Title: Pull2RefreshAdapterViewBase.java
 * @Package com.example.ant_test.pull2refresh.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-12-16 下午1:22:59
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public abstract class Pull2RefreshAdapterViewBase<T extends AbsListView> 
		extends Pull2RefreshBase<T> implements OnScrollListener{

	private boolean mLastItemVisible;
	private OnScrollListener mOnScrollListener;
	private OnLastItemVisibleListener mOnLastItemVisibleListener;
	private View mEmptyView;

	//private IndicatorLayout mIndicatorIvTop;
	//private IndicatorLayout mIndicatorIvBottom;

	//private boolean mShowIndicator;
	private boolean mScrollEmptyView = true;
	
	public Pull2RefreshAdapterViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		mRefreshableView.setOnScrollListener(this);
	}

	public Pull2RefreshAdapterViewBase(Context context, Pull2RefreshMode mode) {
		super(context, mode);
		mRefreshableView.setOnScrollListener(this);
	}

	public Pull2RefreshAdapterViewBase(Context context) {
		super(context);
		mRefreshableView.setOnScrollListener(this);
	}
	
	public void setAdapter(ListAdapter adapter) {
		((AdapterView<ListAdapter>) mRefreshableView).setAdapter(adapter);
	}
	
	public final void setEmptyView(View newEmptyView) {
		FrameLayout refreshableViewWrapper = getRefreshableViewWrapper();
		if (null != newEmptyView) {
			newEmptyView.setClickable(true);
			ViewParent newEmptyViewParent = newEmptyView.getParent();
			if (null != newEmptyViewParent && newEmptyViewParent instanceof ViewGroup) {
				((ViewGroup) newEmptyViewParent).removeView(newEmptyView);
			}
//			FrameLayout.LayoutParams lp = convertEmptyViewLayoutParams(newEmptyView.getLayoutParams());
//			if (null != lp) {
//				refreshableViewWrapper.addView(newEmptyView, lp);
//			} else {
				refreshableViewWrapper.addView(newEmptyView);
//			}
		}

//		if (mRefreshableView instanceof EmptyViewMethodAccessor) {
//			((EmptyViewMethodAccessor) mRefreshableView).setEmptyViewInternal(newEmptyView);
//		} else {
//			mRefreshableView.setEmptyView(newEmptyView);
//		}
		mEmptyView = newEmptyView;
	}
	
	public final void removeEmptyView() {
		if (mEmptyView != null) {
			FrameLayout refreshableViewWrapper = getRefreshableViewWrapper();
			refreshableViewWrapper.removeView(mEmptyView);
		}
	}
	
	@Override
	protected boolean isReadyForPullStart() {
		return isFirstItemVisible();
	}

	@Override
	protected boolean isReadyForPullEnd() {
		return isLastItemVisible();
	}
	
	private boolean isFirstItemVisible() {
		final Adapter adapter = mRefreshableView.getAdapter();
		if (null == adapter || adapter.isEmpty()) {
			Log.d(LOG_TAG, "isFirstItemVisible. Empty View.");
			return true;
		} else {
			if (mRefreshableView.getFirstVisiblePosition() <= 1) {
				final View firstVisibleChild = mRefreshableView.getChildAt(0);
				if (firstVisibleChild != null) {
					Log.d(LOG_TAG, "firstVisibleChild:" + firstVisibleChild.getTop() + ", scrollY:" + mRefreshableView.getScrollY());
					return firstVisibleChild.getTop() >= mRefreshableView.getTop() && mRefreshableView.getScrollY() <= 0;
				}
			}
		}
		return false;
	}
	
	private boolean isLastItemVisible() {
		final Adapter adapter = mRefreshableView.getAdapter();
		if (null == adapter || adapter.isEmpty()) {
			Log.d(LOG_TAG, "isLastItemVisible. Empty View.");
			return true;
		} else {
			final int lastItemPosition = mRefreshableView.getCount() - 1;
			final int lastVisiblePosition = mRefreshableView.getLastVisiblePosition();
			final int firstVisblePostion = mRefreshableView.getFirstVisiblePosition();
			final int childCount = mRefreshableView.getChildCount();
			Log.d(LOG_TAG, "firstVisblePostion:" + firstVisblePostion + " Last Visible Pos: "
					+ lastVisiblePosition + ", Last Item Position: " + lastItemPosition 
					+ ", childCount:" + childCount);
			if (lastVisiblePosition >= lastItemPosition - 1 && childCount + firstVisblePostion >= mRefreshableView.getCount()) {
				final int childIndex = lastVisiblePosition - mRefreshableView.getFirstVisiblePosition();
				final View lastVisibleChild = mRefreshableView.getChildAt(childIndex);
				if (lastVisibleChild != null) {
					return lastVisibleChild.getBottom() <= mRefreshableView.getBottom();
				}
			}
		}
		return false;
	}
	
	@Override
	public final void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount,
			final int totalItemCount) {

		Log.d(LOG_TAG, "First Visible: " + firstVisibleItem + ". Visible Count: " + visibleItemCount
					+ ". Total Items:" + totalItemCount);
		
		/**
		 * Set whether the Last Item is Visible. lastVisibleItemIndex is a
		 * zero-based index, so we minus one totalItemCount to check
		 */
		if (null != mOnLastItemVisibleListener) {
			mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
		}
		
		

		// If we're showing the indicator, check positions...
//		if (getShowIndicatorInternal()) {
//			updateIndicatorViewsVisibility();
//		}

		// Finally call OnScrollListener if we have one
		if (null != mOnScrollListener) {
			mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	@Override
	public final void onScrollStateChanged(final AbsListView view, final int state) {
		Log.d(LOG_TAG, "onScrollStateChanged:" + state + ", isLastItemVisible():" + isLastItemVisible());
		/**
		 * Check that the scrolling has stopped, and that the last item is
		 * visible.
		 */
		if (state == OnScrollListener.SCROLL_STATE_IDLE) {
			if (isLastItemVisible()) {
				if (null != mOnLastItemVisibleListener) {
					mOnLastItemVisibleListener.onLastItemVisible();
				}
				//TODO auto refreshing to get more data
				if (this.getEndStyle().isAuto()) {//自动上拉加载更多
					autoPullUpRefresh();
				}
			} else if (isFirstItemVisible()) {
				if (this.getStartStyle().isAuto()) {//自动下拉刷新
					autoPullDownRefresh();
				}
			}
		}

		if (null != mOnScrollListener) {
			mOnScrollListener.onScrollStateChanged(view, state);
		}
	}
	
	/** 自动(用代码)下拉刷新*/
	public void autoPullDownRefresh() {
		if (getMode().showHeaderLoadingLayout()  && !isRefreshing()) {
			this.post(new Runnable() {
				public void run() {
					setCurrentMode(Pull2RefreshMode.MODE_START);
					getRefreshableView().setSelection(0);
					setState(Pull2RefreshState.REFRESHING, false);
					int height = getHeaderSize();
					if (height <= 0) {
						height = getMeasuredHeaderSize();
					}
					Log.d(">>>>", "header height:" + height);
					setHeaderScroll(-height);
					
				}
			});
		}
	}
	
	/** 自动(用代码)上拉加载更多 */
	public void autoPullUpRefresh() {
		if (getMode().showHeaderLoadingLayout() && !isRefreshing()) {//TODO !refreshing()
			this.post(new Runnable() {
				public void run() {
					setCurrentMode(Pull2RefreshMode.MODE_END);
					if (getRefreshableView().getAdapter() != null) {
						int size = getRefreshableView().getAdapter().getCount();
						getRefreshableView().setSelection(size - 1);
					}
					setState(Pull2RefreshState.REFRESHING, false);
					int height = getFooterSize();
					if (height <= 0) {
						height = getMeasuredFooterSize();
					}
					Log.d(">>>>", "footer height:" + height);
					setHeaderScroll(height);
				}
			});
		}
	}
	
	public void setOnItemClickListener(OnItemClickListener listener) {
		mRefreshableView.setOnItemClickListener(listener);
	}

	public final void setOnLastItemVisibleListener(OnLastItemVisibleListener listener) {
		mOnLastItemVisibleListener = listener;
	}

	public final void setOnScrollListener(OnScrollListener listener) {
		mOnScrollListener = listener;
	}
}
