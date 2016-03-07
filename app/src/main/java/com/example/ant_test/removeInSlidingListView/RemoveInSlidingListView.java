package com.example.ant_test.removeInSlidingListView;

import android.R.color;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Scroller;

public class RemoveInSlidingListView extends ListView{
	private final float REMOVE_RATIO = 0.4f;
	private Scroller mScroller;
	private int mTouchSlop;
	private int width;
	private int x;
	private int y;
	private int position;
	private boolean isSliding = false;
	private View mSelectedView;

	public RemoveInSlidingListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RemoveInSlidingListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RemoveInSlidingListView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		mScroller = new Scroller(getContext());
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				return super.dispatchTouchEvent(event);
			}
			x = (int)event.getX();
			y = (int)event.getY();
			position = pointToPosition(x, y);
			if (position == AdapterView.INVALID_POSITION) {
				break;
			}
			mSelectedView = getChildAt(position - this.getFirstVisiblePosition());
			break;
		case MotionEvent.ACTION_MOVE:
			if (Math.abs(event.getX() - x) > mTouchSlop && Math.abs(event.getY() - y) < mTouchSlop) {
				isSliding = true;
			}
			break;
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isSliding) {
			int curX = (int)ev.getX();
			switch(ev.getAction()) {
			case MotionEvent.ACTION_MOVE:
				int deltaX = x - curX;
				x = curX;
				mSelectedView.scrollBy(deltaX, 0);
				computeTransparency(curX);
				break;
			case MotionEvent.ACTION_UP:
				isSliding = false;
				move();
				break;
			}
			return true;
		} else {
			return super.onTouchEvent(ev);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		this.width = this.getMeasuredWidth();
	}
	
	@Override
	public void computeScroll() {
		if (this.mSelectedView != null) {
			if (this.mScroller.computeScrollOffset()) {
				int x = this.mScroller.getCurrX();
				int y = this.mScroller.getCurrY();
				this.mSelectedView.scrollTo(x, y);
				computeTransparency(x);
				postInvalidate();
				if (this.mScroller.isFinished() && this.mScroller.getCurrX() != 0) {
					this.mSelectedView.scrollTo(0, 0);
					if (this.listener != null) {
						this.listener.onRemove(position);
					}
				}
			}
		}
	}

	public void computeTransparency(int x) {
		int ratio = (int)(((float)(width - Math.abs(x)) / (float)width) * 100);
		Log.d(">>>>>", "ratio:" + ratio);
		if (mSelectedView != null) {
			if (mSelectedView.getBackground() == null) {
				mSelectedView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
			}
			mSelectedView.getBackground().setAlpha(ratio);
		}
	}
	
	public void move() {
		if (mSelectedView != null) {
			int x = mSelectedView.getScrollX();
			int delta = 0;
			int duration = 0;
			if (x < this.width * REMOVE_RATIO && x > -this.width * REMOVE_RATIO) {
				delta = -x;
				duration = Math.abs(x);
			} else if (x > 0) {
				delta = this.width -x;
				duration = Math.abs(delta) * 2;
			} else {
				delta = -(this.width + x);
				duration = Math.abs(delta) * 2;
			}
			mScroller.startScroll(x, 0, delta, 0, duration);
			postInvalidate();
		}
	}
	
	public enum Orientation {
		LEFT, RIGHT
	}
	
	private OnRemoveListener listener;
	public void setOnRemoveListener(OnRemoveListener listener) {
		this.listener = listener;
	} 
	public interface OnRemoveListener {
		public void onRemove(int position);
	}
}
