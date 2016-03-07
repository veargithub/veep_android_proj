package com.example.ant_test.vertical_horizontal_scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @Title: VHScrollView.java
 * @Package com.example.ant_test.vertical_horizontal_scrollview
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-8-6 下午4:14:18
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class VHScrollView extends ViewGroup{
	private static final String TAG = "VHScrollView";
	private Scroller mScroller;
	private int mScrollX;
	private int mScrollY;
	
	private int mTouchSlop;//最小滑动距离，仅当超过这个距离，才被看做是一次有效滑动
    private int mMinimumVelocity;//最小初速度，如果速度小于这个值，则不做惯性滑动
    private int mMaximumVelocity;//最大初速度，如果速度大于这个值，则用这个值替换

	public VHScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public VHScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public VHScrollView(Context context) {
		super(context);
		init();
	}

	private void init() {
		mScroller = new Scroller(this.getContext());
		final ViewConfiguration configuration = ViewConfiguration.get(this.getContext());
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        Log.d(TAG, "init:" + mTouchSlop + " " + mMinimumVelocity + " " + mMaximumVelocity);
	}
	
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		View child = this.getChildAt(0);
		measureChild(child, widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.d(TAG, "onlayout:" + l + " " + t + " " + r + " " + b);
		View child = this.getChildAt(0);
		int childWidth = child.getMeasuredWidth();
		int childHeight = child.getMeasuredHeight();
		Log.d(TAG, "childWidth:" + childWidth + ", childHeight:" + childHeight);
		child.layout(l, t, childWidth, childHeight);
		
	}

	@Override
    public void computeScroll() {
		Log.d(TAG, "computeScroll");
        if (mScroller.computeScrollOffset()) {
        	Log.d(TAG, "computeScrollOffset:" + "mScrollX:" + mScrollX + ", mScrollY:" + mScrollY);
            int oldX = mScrollX;
            int oldY = mScrollY;
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();

            
            if (x != oldX || y != oldY) {
//                mScrollX = x;
//                mScrollY = y;
                onScrollChanged(x, y, oldX, oldY);
            }
           
            scrollTo(x, y);
            
        }
    }
	
	private boolean mIsBeingDragged = false;//是否开始滑动
	private float mLastMotionY;//最后一次滑动的y的坐标
	private float mLastMotionX;
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
            	final float x = ev.getX();
                final float y = ev.getY();
                final int yDiff = (int) Math.abs(y - mLastMotionY);
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                if (yDiff > mTouchSlop || xDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                    mLastMotionX = x;
                    mLastMotionY = y;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
            	final float x = ev.getX();
                final float y = ev.getY();
                if (!inChild((int) x, (int) y)) {
                    mIsBeingDragged = false;
                    break;
                }
                mLastMotionX = x;
                mLastMotionY = y;
              
                mIsBeingDragged = !mScroller.isFinished();
                break;
            }

            case MotionEvent.ACTION_CANCEL://TODO 是否要去掉
            case MotionEvent.ACTION_UP://TODO 看看是否要去掉
                mIsBeingDragged = false;//这里不能设置为false，不然ontouchevent里就不执行了
                break;
            
        }
        Log.d(">>>>", "vh, " + action + mIsBeingDragged);
        return mIsBeingDragged;
	}
	
	private VelocityTracker mVelocityTracker;
	
	@Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
            return false;
        }

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(ev);

        final int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
            	final float x = ev.getX();
                final float y = ev.getY();
                if (!(mIsBeingDragged = inChild((int) ev.getX(), (int) y))) {
                    return false;
                }
                
                /*
                 * If being flinged and user touches, stop the fling. isFinished
                 * will be false if being flinged.
                 */
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                // Remember where the motion event started
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            }
            case MotionEvent.ACTION_MOVE:
                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                	final float x = ev.getX();
                    final float y = ev.getY();
                    final int deltaY = (int) (mLastMotionY - y);
                    final int deltaX = (int) (mLastMotionX - x);
                    mLastMotionX = x;
                    mLastMotionY = y;

                    scrollBy(deltaX, deltaY);
                    Log.d(TAG, "deltaY:" + deltaY);
                }
                break;
            case MotionEvent.ACTION_UP: 
                if (mIsBeingDragged) {
                    final VelocityTracker velocityTracker = mVelocityTracker;
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                    int initialXVelocity = (int) velocityTracker.getXVelocity();
                    int initialYVelocity = (int) velocityTracker.getYVelocity();
                    Log.d(TAG, "initialVelocity:" + initialYVelocity);
                    if (getChildCount() > 0) {
                    	if (Math.abs(initialYVelocity) > mMinimumVelocity || Math.abs(initialXVelocity) > mMinimumVelocity) {
                    		fling(-initialXVelocity, -initialYVelocity);
                    	}
                    }

                  
                    mIsBeingDragged = false;

                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged && getChildCount() > 0) {
                   
                    mIsBeingDragged = false;
                    if (mVelocityTracker != null) {
                        mVelocityTracker.recycle();
                        mVelocityTracker = null;
                    }
                }
                break;
            
        }
        return true;
    }
	
	/**
	 * 因为父view和子view是存在margin和padding的，如果手滑到padding和margin上，则此次操作作废
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean inChild(int x, int y) {
        if (getChildCount() > 0) {
            final int scrollY = mScrollY;
            final View child = getChildAt(0);
            return !(y < child.getTop() - scrollY
                    || y >= child.getBottom() - scrollY
                    || x < child.getLeft()
                    || x >= child.getRight());
        }
        return false;
    }

	public void fling(int velocityX, int velocityY) {
        if (getChildCount() > 0) {
            int height = getHeight();
            int bottom = getChildAt(0).getHeight();
            int width = getWidth();
            int right = getChildAt(0).getWidth();
            Log.d(TAG, "fling:" + "height:" + height + ", bottom:" + bottom + ", x:" 
            + getScrollX() + ", y:" + getScrollY() + ", vX:" + velocityX + ", vY:" + velocityY);
            //注：fling的minY必须小于maxY，mScroller.fling(0, 0, 0, -5000, 0, 0, -1000, 0)这样
            //是可以的，但没有意义，因为scroller很少会有y为负的情况
            mScroller.fling(getScrollX(), getScrollY(), velocityX, velocityY, 
            		0, Math.max(0, right - width), 0,  Math.max(0, bottom - height));
    
//            final boolean movingDown = velocityY > 0;
//    
//            View newFocused =
//                    findFocusableViewInMyBounds(movingDown, mScroller.getFinalY(), findFocus());
//            if (newFocused == null) {
//                newFocused = this;
//            }
//    
//            if (newFocused != findFocus()
//                    && newFocused.requestFocus(movingDown ? View.FOCUS_DOWN : View.FOCUS_UP)) {
//                mScrollViewMovedFocus = true;
//                mScrollViewMovedFocus = false;
//            }
    
            invalidate();
        }
    }
	
	@Override
	public void scrollTo(int x, int y) {
	        // we rely on the fact the View.scrollBy calls scrollTo.
		Log.d(TAG, "scrollToY:" + y + ", mScrollY:" + mScrollY);
	    if (getChildCount() > 0) {
	        View child = getChildAt(0);
	        x = clamp(x, getWidth(), child.getWidth());
	        y = clamp(y, getHeight(), child.getHeight());
	        if (x != mScrollX || y != mScrollY) {
	        	mScrollX = x;
	        	mScrollY = y;
	        	Log.d(TAG, "super.scrollTo()");
	        	super.scrollTo(x, y);
	        }
	    }
	}
	/**
	 * 这个方法的作用是锁定child，不让child的任何一部分滑到屏幕外面去，算法比较古怪
	 * @param n
	 * @param my
	 * @param child
	 * @return
	 */
	private int clamp(int n, int my, int child) {
	    if (my >= child || n < 0) {
	    		/* my >= child is this case:
	    		 *                    |--------------- me ---------------|
	             *     |------ child ------|
	             * or
	             *     |--------------- me ---------------|
	             *            |------ child ------|
	             * or
	             *     |--------------- me ---------------|
	             *                                  |------ child ------|
	             *
	             * n < 0 is this case:
	             *     |------ me ------|
	             *                    |-------- child --------|
	             *     |-- mScrollX --|
	             */
	        return 0;
	    }
	    if ((my+n) > child) {
	            /* this case:
	             *                    |------ me ------|
	             *     |------ child ------|
	             *     |-- mScrollX --|
	             */
	        return child-my;
	    }
	    return n;
	}
	
	public void test() {
		Log.d(TAG, "test:" + "mScrollX:" + mScrollX + ", mScrollY:" + mScrollY);
		//mScroller.startScroll(mScrollX, mScrollY, -500, -500, 2000);
		//mScroller.fling(0, 0, 0, 5000, 0, 0, 0, 1000);
		//invalidate();
	}
	
}
