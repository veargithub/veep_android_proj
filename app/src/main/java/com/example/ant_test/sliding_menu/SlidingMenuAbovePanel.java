package com.example.ant_test.sliding_menu;

import com.example.ant_test.sliding_menu.impl.SlidingMenuTransformer;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @Title: SlidingMenuAbovePanel.java
 * @Package com.example.ant_test.sliding_menu
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-8-22 下午2:51:12
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class SlidingMenuAbovePanel extends ViewGroup{
	private final String TAG = "SlidingMenu_Panel";
	private SlidingMenuView menu;
	private View content;
	protected VelocityTracker mVelocityTracker;
	private int mMinimumVelocity;
	protected int mMaximumVelocity;
	private int mTouchSlop;
	private Scroller mScroller;
	private final int MIN_DISTANCE_FOR_FLING = 25;
	private int mFlingDistance;
	private SlidingMenuTransformer transformer;

	public SlidingMenuAbovePanel(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SlidingMenuAbovePanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlidingMenuAbovePanel(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		setWillNotDraw(false);//使得ondraw被调用
		setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
		setFocusable(true);
		final Context context = getContext();
		mScroller = new Scroller(context);//TODO 加速器
		final ViewConfiguration configuration = ViewConfiguration.get(context);
		mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration) / 2;//最小滑动距离
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();//最小滑动初速度
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();//最大滑动初速度
		final float density = context.getResources().getDisplayMetrics().density;
		mFlingDistance = (int) (MIN_DISTANCE_FOR_FLING * density);//这个值是最小达到能惯性滑动的距离,即如果滑动距离小于这个值，不做惯性滑动
	}
	
	public void setMenu(SlidingMenuView menu) {
		this.menu = menu;
	}
	
	public void setTransformer(SlidingMenuTransformer transformer) {
		this.transformer = transformer;
	}

	/**
	 * 设置这个view的内容
	 * @param view
	 */
	public void setContent(View view) {//先删除原有的，再添加 
		if (this.getChildCount() > 0) {
			removeAllViews();
		}
		content = view;
		content.setOnTouchListener(new OnTouchListener() {//必须要这么搞一下，不然onInterceptTouchEvent只会走一次action_down事件
		    @Override
		    public boolean onTouch(View v, MotionEvent event) {
		        return true;
		    }
		});
		addView(content);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		content.layout(0, 0, r - l, b - t);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		content.measure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private float scaleFrom = 0.75f;

	public void setAlphaFrom(float scaleFrom) {
		this.scaleFrom = scaleFrom;
	}
	
	private float scaleTo = 1.0f;
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (this.transformer != null) {
			float scale = (1 - this.getOpenPercents()) * (scaleTo - scaleFrom) + scaleFrom;
			Log.d(TAG, "dispatchdraw, scale:" + scale + ", percent:" + this.getOpenPercents());
			canvas.save();
			transformer.transform(this, canvas, scale, 1f);
			super.dispatchDraw(canvas);
			canvas.restore();
		} else {
			super.dispatchDraw(canvas);
		}
	}

	private boolean slidingEnabled = true;//本控件是否能滑动
		
	public void setSlidingEnabled(boolean b) {
		this.slidingEnabled = b;
	}
	
	private float mLastMotionX;
	private float mLastMotionY;
	private boolean mIsBeingDragged = false;
	
	/**
	 * 
    You will receive the down event here.
    
    The down event will be handled either by a child of this view group, or given to your own 
    onTouchEvent() method to handle; this means you should implement onTouchEvent() to return true, 
    so you will continue to see the rest of the gesture (instead of looking for a parent view to handle it). 
    Also, by returning true from onTouchEvent(), you will not receive any following events in onInterceptTouchEvent()
    and all touch processing must happen in onTouchEvent() like normal.
    
    For as long as you return false from this function, each following event (up to and including the final up) 
    will be delivered first here and then to the target's onTouchEvent().
    
    If you return true from here, you will not receive any following events: the target view will receive 
    the same event but with the action ACTION_CANCEL, and all further events will be delivered to your 
    onTouchEvent() method and no longer appear here. 
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!slidingEnabled) {//如果本view没法滑动，直接把touch扔给子view
			return false;
		}
		int action = ev.getAction() & MotionEvent.ACTION_MASK;
		switch (action) {
		case MotionEvent.ACTION_MOVE :
			mIsBeingDragged = determineDrag(ev);
			break;
		case MotionEvent.ACTION_DOWN :
			completeScroll();
			mLastMotionX = ev.getX();
			mLastMotionY = ev.getY();
			mIsBeingDragged = false;
			break;
		
		case MotionEvent.ACTION_UP :
		case MotionEvent.ACTION_CANCEL :
			endDrag();
			break;
		}
		Log.d(TAG, "me, onInterceptTouchEvent, " + action + mIsBeingDragged);
		return mIsBeingDragged;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction() & MotionEvent.ACTION_MASK;
		Log.d(TAG, "me, onTouchEvent, " + action + mIsBeingDragged);
		if (!mIsBeingDragged) {
			return false;
		}
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		switch (action) {
		case MotionEvent.ACTION_DOWN ://似乎永远不会走这里
			//TODO 如果正在滑动，则停止
			break;
		case MotionEvent.ACTION_MOVE :
			if (mIsBeingDragged) {
				float x = event.getX();
				int deltaX = (int)(mLastMotionX - x);
				mLastMotionX = x;
				int oldScrollX = getScrollX();
				//Log.d(TAG, "getLeftBound:" + getLeftBound() + ", getRightBound:" + getRightBound());
				int scrollX = oldScrollX + deltaX;
				if (scrollX < getLeftBound()) {
					scrollX = getLeftBound();
				}
				if (scrollX > getRightBound()) {
					scrollX = getRightBound();
				}
				scrollTo(scrollX, getScrollY());
			}
			break;
		case MotionEvent.ACTION_UP :
			if (mIsBeingDragged) {
				float x = event.getX();
				int deltaX = (int)(mLastMotionX - x);
				mLastMotionX = x;
				int oldScrollX = getScrollX();
				int scrollX = oldScrollX + deltaX;
				final VelocityTracker tracker = this.mVelocityTracker;
				tracker.computeCurrentVelocity(1000, mMaximumVelocity);
				int initVelocity = (int)tracker.getXVelocity();
				int page = determinePage(initVelocity, scrollX);
				showPage(page, initVelocity);//TODO 下周来测试下
			}
			endDrag();
			break;
		}
		return true;
	}
	
	private boolean determineDrag(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		int absDeltaX = Math.abs((int)(x - mLastMotionX));
		int absDeltaY = Math.abs((int)(y - mLastMotionY));
		if (absDeltaY > absDeltaX) {
			return false;
		}
		if (absDeltaX < mTouchSlop) {
			return false;
		}
		mLastMotionX = x;
		mLastMotionY = y;
		return true;
	}
	
	private final int PAGE_BEHIND = 1;
	private final int PAGE_ABOVE = 2;
	/**
	 * 决定显示哪个页面
	 * @param v 速度，带方向，如果大于0说明向右滑，小于0向左
	 * @param nowScrollX scroller现在所处的位置（如果速度小于mMinimumVelocity， 则根据这个值来确定显示哪个页面）
	 * @return PAGE_BEHIND 显示menu， PAGE_ABOVE 显示panel 
	 */
	private int determinePage(int v, int nowScrollX) {
		if (Math.abs(v) > mMinimumVelocity) {//速度足够快
			if (v > 0) {//手势向右滑
				return PAGE_BEHIND;
			} else {//手势向左
				return PAGE_ABOVE;
			}
		} else {
			if (Math.abs(nowScrollX) >= this.getWidth() / 2) {//向右滑
				return PAGE_BEHIND;
			} else {//向左滑
				return PAGE_ABOVE;
			}
		}
	}

	private void endDrag() {
		mIsBeingDragged = false;
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}
	
	private void showPage(int page, int v) {
		if (page == PAGE_BEHIND) {//显示菜单
			int destX = getLeftBound();
			int destY = getScrollY();
			scrollTo(destX, destY, v);
		} else if (page == PAGE_ABOVE) {//显示主面板
			int destX = getRightBound();
			int destY = getScrollY();
			scrollTo(destX, destY, v);
		}
	}
	
	private void scrollTo(int x, int y, int v) {
		int curX = getScrollX();
		int curY = getScrollY();
		int dx = x - curX;
		int dy = y = curY;
		if (dx == 0 && dy == 0) {
			
		} else {
			int duration = 600;//TODO
			mScroller.startScroll(curX, curY, dx, dy, duration);
			invalidate();
		}
	}
	/** [0, 1] 整个menu显示了多少 */
	private float openPercents = 0;
	
	public float getOpenPercents() {
		return openPercents;
	}
	
	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		if (getMenuWidth() != 0) openPercents = Math.abs((float)x) / getMenuWidth();
		menu.scrollToWithPanel(x, y);
	}
	/**
	 * 获取menu的宽度
	 * @return
	 */
	private int getMenuWidth() {
		return this.menu.getMenuWidth();
	}
	
	@Override
	public void computeScroll() {
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				int oldX = getScrollX();
				int oldY = getScrollY();
				int x = mScroller.getCurrX();
				int y = mScroller.getCurrY();
				if (oldX != x || oldY != y) {
					scrollTo(x, y);
					//pageScrolled(x); TODO 回调
				}
				invalidate();
				return;
			}
		}
		//completeScroll(); TODO 保险措施
	}
	
	private void completeScroll() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
			int oldX = getScrollX();
			int oldY = getScrollY();
			int x = mScroller.getCurrX();
			int y = mScroller.getCurrY();
			if (oldX != x || oldY != y) {
				scrollTo(x, y);
			}
		}
	}

	private int getLeftBound() {
		return this.menu.getPanelLeftBound();
	}
	
	private int getRightBound() {
		return this.menu.getPanelRightBound();
	}
	
}
