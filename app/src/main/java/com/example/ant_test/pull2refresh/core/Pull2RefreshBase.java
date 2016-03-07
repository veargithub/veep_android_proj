package com.example.ant_test.pull2refresh.core;

import com.example.ant_test.R;
import com.example.ant_test.pull2refresh.listener.OnPullStateChangedListener;
import com.example.ant_test.pull2refresh.listener.OnRefreshListener;
import com.example.ant_test.pull2refresh.listener.OnRefreshListener2;
import com.example.ant_test.pull2refresh.listener.OnSmoothScrollFinishedListener;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * @Title: Pull2RefreshBase.java
 * @Package com.example.ant_test.pull2refresh.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-12-2 下午2:48:54
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public abstract class Pull2RefreshBase<T extends View> extends LinearLayout{
	private Pull2RefreshMode mMode = Pull2RefreshMode.getDefault();//模式：下拉刷新，上拉刷新，点击刷新等
	private Pull2RefreshMode mCurrentMode;//当前正在进行的模式，只有start和end两种
	private Pull2RefreshStyle mStartStyle = Pull2RefreshStyle.getDefault(), mEndStyle = Pull2RefreshStyle.getDefault();//刷新方式(下拉，自动，点击等)
	private int mTouchSlop;//判定一次滑动的最短移动距离
	private AnimationStyle mLoadingAnimationStyle = AnimationStyle.getDefault();//动画风格：旋转，翻转等
	T mRefreshableView;//下拉刷新view
	private FrameLayout mRefreshableViewWrapper;//用来放置 mRefreshableView 的framelayout
	private HeaderFooterLayout mHeaderLayout;
	private HeaderFooterLayout mFooterLayout;
	
	static final String LOG_TAG = "Pull2Refresh";
	static final float FRICTION = 2.0f;//手指滑动2px,view滑动1px
	static final boolean USE_HW_LAYERS = false;//是否开启硬件加速
	
	private boolean mIsBeingDragged = false;
	private boolean mScrollingWhileRefreshingEnabled = false;//“正在刷新”时，是否还允许滑动
	private boolean mLayoutVisibilityChangesEnabled = true;//如果header不出现在屏幕中，是否设置其为INVISIBLE
	private Pull2RefreshState mState = Pull2RefreshState.RESET;
	private float mLastMotionX, mLastMotionY;
	private float mInitialMotionX, mInitialMotionY;
	private boolean mFilterTouchEvents = true;//是否开启过滤，仅当diffY > diffX时，才看作是一次垂直方向上的有效滑动
	private SmoothScrollRunnable mCurrentSmoothScrollRunnable;//自动滑动的runnable
	private Interpolator mScrollAnimationInterpolator;//自动滑动的变速器
	
	private OnPullStateChangedListener<T> mOnPullStateChangedListener;//当状态改变时的回调
	private OnRefreshListener<T> mOnRefreshListener;
	private OnRefreshListener2<T> mOnRefreshListener2;
	
	public static final int SMOOTH_SCROLL_DURATION_MS = 200;//平滑运动的耗时
	public static final int SMOOTH_SCROLL_LONG_DURATION_MS = 325;
	
	private boolean mOverScrollEnabled = true;//是否允许over scroll
	
	public Pull2RefreshBase(Context context) {
		super(context);
		init(context, null);
	}

	public Pull2RefreshBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}
	
	public Pull2RefreshBase(Context context, Pull2RefreshMode mode) {
		super(context);
		mMode = mode;
		init(context, null);
	}
	
	private void init(Context context, AttributeSet attrs) {
		setOrientation(LinearLayout.VERTICAL);
		setGravity(Gravity.CENTER);
		ViewConfiguration config = ViewConfiguration.get(context);
		mTouchSlop = config.getScaledTouchSlop();
		//从xml加载各种属性
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefresh);
		if (a.hasValue(R.styleable.PullToRefresh_ptrMode)) {
			mMode = Pull2RefreshMode.convertIntToValue(a.getInteger(R.styleable.PullToRefresh_ptrMode, 0));
		}
		
		if (a.hasValue(R.styleable.PullToRefresh_ptrEndStyle)) {
			mEndStyle = Pull2RefreshStyle.convertIntToValue(a.getInteger(R.styleable.PullToRefresh_ptrEndStyle, 0));
		}
		
		if (a.hasValue(R.styleable.PullToRefresh_ptrStartStyle)) {
			mStartStyle = Pull2RefreshStyle.convertIntToValue(a.getInteger(R.styleable.PullToRefresh_ptrStartStyle, 0));
		}

		if (a.hasValue(R.styleable.PullToRefresh_ptrAnimationStyle)) {
			mLoadingAnimationStyle = AnimationStyle.convertIntToValue(a.getInteger(
					R.styleable.PullToRefresh_ptrAnimationStyle, 0));
		}
		//创建中间
		mRefreshableView = createRefreshableView(context, attrs);
		addRefreshableView(context, mRefreshableView);
		//创建头和尾
		mHeaderLayout = createLoadingLayout(context, Pull2RefreshMode.MODE_START, a);
		mFooterLayout = createLoadingLayout(context, Pull2RefreshMode.MODE_END, a);
		
		if (a.hasValue(R.styleable.PullToRefresh_ptrRefreshableViewBackground)) {//设置refreshview的background
			Drawable background = a.getDrawable(R.styleable.PullToRefresh_ptrRefreshableViewBackground);
			if (null != background) {
				mRefreshableView.setBackgroundDrawable(background);
			}
		}
		
		if (a.hasValue(R.styleable.PullToRefresh_ptrOverScroll)) {//设置是否具有overscroll效果
			mOverScrollEnabled = a.getBoolean(R.styleable.PullToRefresh_ptrOverScroll, true);
		}
		a.recycle();
		updateUIForMode();
	}
	
	private void addRefreshableView(Context context, T refreshableView) {
		mRefreshableViewWrapper = new FrameLayout(context);
		mRefreshableViewWrapper.addView(refreshableView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		addViewInternal(mRefreshableViewWrapper, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	protected final void addViewInternal(View child, int index, ViewGroup.LayoutParams params) {
		super.addView(child, index, params);
	}

	protected final void addViewInternal(View child, ViewGroup.LayoutParams params) {
		super.addView(child, -1, params);
	}
	
	@Override
	public void addView(View child, int index, ViewGroup.LayoutParams params) {
		
		Log.d(LOG_TAG, "addView: " + child.getClass().getSimpleName());
		

		final T refreshableView = getRefreshableView();

		if (refreshableView instanceof ViewGroup) {
			((ViewGroup) refreshableView).addView(child, index, params);
		} else {
			throw new UnsupportedOperationException("Refreshable View is not a ViewGroup so can't addView");
		}
	}
	
	public final T getRefreshableView() {
		return mRefreshableView;
	}
	
	public final HeaderFooterLayout getFooterLayout() {
		return mFooterLayout;
	}
	
	public final Pull2RefreshMode getCurrentMode() {
		return mCurrentMode;
	}
	
	protected HeaderFooterLayout createLoadingLayout(Context context, Pull2RefreshMode mode, TypedArray attrs) {
		HeaderFooterLayout layout = mLoadingAnimationStyle.createLoadingLayout(context, mode, attrs);
		layout.setVisibility(View.INVISIBLE);
		return layout;
	}
	
	protected void updateUIForMode() {
		final LinearLayout.LayoutParams lp = getLoadingLayoutLayoutParams();
		if (this == mHeaderLayout.getParent()) {
			removeView(mHeaderLayout);
		}
		if (mMode.showHeaderLoadingLayout()) {
			addViewInternal(mHeaderLayout, 0, lp);
		}
		if (this == mFooterLayout.getParent()) {
			removeView(mFooterLayout);
		}
		if (mMode.showFooterLoadingLayout()) {
			addViewInternal(mFooterLayout, lp);
		}
		refreshLoadingViewsSize();//隐藏header

		// If we're not using Mode.BOTH, set mCurrentMode to mMode, otherwise
		// set it to pull down
		mCurrentMode = (mMode != Pull2RefreshMode.MODE_BOTH) ? mMode : Pull2RefreshMode.MODE_START;//TODO 如果新增模式，这里需要相应修改
	}
	
	private LinearLayout.LayoutParams getLoadingLayoutLayoutParams() {
		return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		
	}
	
	/** 初始化padding以隐藏header和footer */
	protected final void refreshLoadingViewsSize() {
		final int maximumPullScroll = (int) (getMaximumPullScroll() * 1.2f);
		int pLeft = getPaddingLeft();
		int pTop = getPaddingTop();
		int pRight = getPaddingRight();
		int pBottom = getPaddingBottom();
		if (mMode.showHeaderLoadingLayout()) {
			mHeaderLayout.setHeight(maximumPullScroll);
			pTop = -maximumPullScroll;
		} else {
			pTop = 0;
		}
		if (mMode.showFooterLoadingLayout()) {
			mFooterLayout.setHeight(maximumPullScroll);
			pBottom = -maximumPullScroll;
		} else {
			pBottom = 0;
		}
		Log.d(LOG_TAG, String.format("Setting Padding. L: %d, T: %d, R: %d, B: %d", pLeft, pTop, pRight, pBottom));
		setPadding(pLeft, pTop, pRight, pBottom);
	}
	
	/** 该view最大能被拖动的距离 */
	private int getMaximumPullScroll() {
		return Math.round(getHeight() / FRICTION);
	}
	
	
	@Override
	public final boolean onInterceptTouchEvent(MotionEvent event) {

		if (!isPullToRefreshEnabled()) {//判断当前view是否可滑
			return false;
		}

		final int action = event.getAction();

		if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			mIsBeingDragged = false;
			return false;
		}

		if (action != MotionEvent.ACTION_DOWN && mIsBeingDragged) {
			return true;
		}

		switch (action) {
			case MotionEvent.ACTION_MOVE: {
				//如果“正在刷新”且mScrollingWhileRefreshingEnabled为true，截断所有事件
				if (!mScrollingWhileRefreshingEnabled && isRefreshing()) {
					return true;
				}

				if (isReadyForPull()) {
					final float y = event.getY(), x = event.getX();
					final float diff, oppositeDiff, absDiff;
					diff = y - mLastMotionY;//垂直方向移动的距离
					oppositeDiff = x - mLastMotionX;//水平方向移动的距离
					absDiff = Math.abs(diff);//垂直方向移动的距离的绝对值

					if (absDiff > mTouchSlop && (!mFilterTouchEvents || absDiff > Math.abs(oppositeDiff))) {
						if (mMode.showHeaderLoadingLayout() && diff >= 1f && isReadyForPullStart()) {//下拉
							mLastMotionY = y;
							mLastMotionX = x;
							mIsBeingDragged = true;
							if (mMode == Pull2RefreshMode.MODE_BOTH) {
								mCurrentMode = Pull2RefreshMode.MODE_START;
							}
						} else if (mMode.showFooterLoadingLayout() && diff <= -1f && isReadyForPullEnd()) {//上拉
							mLastMotionY = y;
							mLastMotionX = x;
							mIsBeingDragged = true;
							if (mMode == Pull2RefreshMode.MODE_BOTH) {
								mCurrentMode = Pull2RefreshMode.MODE_END;
							}
						}
					}
				}
				break;
			}
			case MotionEvent.ACTION_DOWN: {
				if (isReadyForPull()) {
					mLastMotionY = mInitialMotionY = event.getY();
					mLastMotionX = mInitialMotionX = event.getX();
					mIsBeingDragged = false;
				}
				break;
			}
		}
		return mIsBeingDragged;
	}
	
	@Override
	public final boolean onTouchEvent(MotionEvent event) {

		if (!isPullToRefreshEnabled()) {//判断当前view是否可滑
			return false;
		}

		//如果“正在刷新”且mScrollingWhileRefreshingEnabled为true，截断所有事件
		if (!mScrollingWhileRefreshingEnabled && isRefreshing()) {
			return true;
		}
		//如果手指按在边界上
		if (event.getAction() == MotionEvent.ACTION_DOWN && event.getEdgeFlags() != 0) {
			return false;
		}

		switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE: {
				if (mIsBeingDragged) {
					mLastMotionY = event.getY();
					mLastMotionX = event.getX();
					pullEvent();
					return true;
				}
				break;
			}

			case MotionEvent.ACTION_DOWN: {
				if (isReadyForPull()) {
					mLastMotionY = mInitialMotionY = event.getY();
					mLastMotionX = mInitialMotionX = event.getX();
					return true;
				}
				break;
			}

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP: {
				if (mIsBeingDragged) {
					mIsBeingDragged = false;
					//如果当前是“释放刷新”的状态且2个回调函数不都为null，则切换到“正在刷新”的状态
					if (mState == Pull2RefreshState.RELEASE_TO_REFRESH
							&& (null != mOnRefreshListener || null != mOnRefreshListener2)) {
						setState(Pull2RefreshState.REFRESHING, true);
						return true;
					}

					//如果当前已经是“正在刷新”状态，则直接滑动到合适的位置
					if (isRefreshing()) {
						smoothScrollTo(0);
						return true;
					}

					//如果不在“释放刷新”和“正在刷新”2个状态，则还原到初始状态
					setState(Pull2RefreshState.RESET);

					return true;
				}
				break;
			}
		}

		return false;
	}
	
	/** 当前view是否可滑动，如果为false，直接将事件往下传 */
	public final boolean isPullToRefreshEnabled() {
		return mMode.permitsPullToRefresh();
	}
	
	/** 当前是否在“正在刷新”状态中 */
	public final boolean isRefreshing() {
		return mState == Pull2RefreshState.REFRESHING || mState == Pull2RefreshState.MANUAL_REFRESHING;
	}
	
	/** 是否有必要拦截触摸事件 */
	private boolean isReadyForPull() {
		switch (mMode) {//值得注意的是，如果是手动刷新模式，则永远不会拦截
			case MODE_START:
				return isReadyForPullStart();
			case MODE_END:
				return isReadyForPullEnd();
			case MODE_BOTH:
				return isReadyForPullEnd() || isReadyForPullStart();
			default:
				return false;
		}
	}
	
	private void pullEvent() {
		final int newScrollValue;//view应该滑动到的位置
		final int itemDimension;//header的实际height
//		final float initialMotionValue, lastMotionValue;
//		initialMotionValue = mInitialMotionY;
//		lastMotionValue = mLastMotionY;
		final float diffMotionValue = mInitialMotionY - mLastMotionY;//如果下拉，则为负数，上拉为正数
		if (mCurrentMode == Pull2RefreshMode.MODE_END) {//上拉
			newScrollValue = Math.round(Math.max(diffMotionValue, 0) / FRICTION);
			itemDimension = getFooterSize();
		} else {//下拉
			newScrollValue = Math.round(Math.min(diffMotionValue, 0) / FRICTION);
			itemDimension = getHeaderSize();
		}

		setHeaderScroll(newScrollValue);//滑动到目标位置

		if (newScrollValue != 0 && !isRefreshing()) {
			float scale = Math.abs(newScrollValue) / (float) itemDimension;
			switch (mCurrentMode) {
				case MODE_END:
					mFooterLayout.onPull(scale);
					break;
				case MODE_START:
				default:
					mHeaderLayout.onPull(scale);
					break;
			}

			if (mState != Pull2RefreshState.PULL_TO_REFRESH && itemDimension >= Math.abs(newScrollValue)) {
				setState(Pull2RefreshState.PULL_TO_REFRESH);//设置文字“下拉刷新”
			} else if (mState == Pull2RefreshState.PULL_TO_REFRESH && itemDimension < Math.abs(newScrollValue)) {
				setState(Pull2RefreshState.RELEASE_TO_REFRESH);//设置文字“释放刷新”
			}
		}
	}
	
	/** 获取footer的高度 */
	protected final int getFooterSize() {
		return mFooterLayout.getContentSize();
	}
	/** 获取测量的header的高度*/
	protected int getMeasuredFooterSize() {
		this.getFooterLayout().measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		return this.getFooterLayout().getMeasuredHeight();
	}
	/** 获取header的高度 */
	protected final int getHeaderSize() {
		return mHeaderLayout.getContentSize();
	}
	/** 获取测量的header的高度*/
	protected int getMeasuredHeaderSize() {
		this.getHeaderLayout().measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		return this.getHeaderLayout().getMeasuredHeight();
	}
	/** 获取header的layout(不包括外面的wrapper)*/
	protected final HeaderFooterLayout getHeaderLayout() {
		return mHeaderLayout;
	}
	
	/**
	 * 根据触摸事件滑动该view
	 * @param value 如果是正数，则上拉，如果为负数，则下拉
	 */
	protected final void setHeaderScroll(int value) {
		Log.d(LOG_TAG, "setHeaderScroll: " + value + ", child size:" + this.getChildCount());
		final int maximumPullScroll = getMaximumPullScroll();//获取移动范围
		value = Math.min(maximumPullScroll, Math.max(-maximumPullScroll, value));//设置移动范围
		if (mLayoutVisibilityChangesEnabled) {
			if (value < 0) {
				mHeaderLayout.setVisibility(View.VISIBLE);
			} else if (value > 0) {
				mFooterLayout.setVisibility(View.VISIBLE);
			} else {
				mHeaderLayout.setVisibility(View.INVISIBLE);
				mFooterLayout.setVisibility(View.INVISIBLE);
			}
		}
		if (USE_HW_LAYERS) {//TODO 开启硬件加速
			
		}
		scrollTo(0, value);
	}
	
	/** 当状态改变时，设置header的新状态 */
	public final void setState(Pull2RefreshState state, final boolean... params) {
		mState = state;
		Log.d(LOG_TAG, "State: " + mState.name());
		switch (mState) {
			case RESET://回到初始状态
				onReset();
				break;
			case PULL_TO_REFRESH://下拉刷新
				onPullToRefresh();
				break;
			case RELEASE_TO_REFRESH://释放刷新
				onReleaseToRefresh();
				break;
			case REFRESHING://正在刷新
			case MANUAL_REFRESHING://手动刷新
				onRefreshing(params[0]);
				break;
			case OVERSCROLLING:
				// NO-OP
				break;
		}

		// Call OnPullEventListener
		if (null != mOnPullStateChangedListener) {
			mOnPullStateChangedListener.onChange(this, mState, mCurrentMode);
		}
	}
	
	public final Pull2RefreshState getState() {
		return mState;
	}
	
	protected void onReleaseToRefresh() {
		switch (mCurrentMode) {
			case MODE_END:
				mFooterLayout.releaseToRefresh();
				break;
			case MODE_START:
				mHeaderLayout.releaseToRefresh();
				break;
			default:
				// NO-OP
				break;
		}
	}
	
	protected void onReset() {
		mIsBeingDragged = false;
		mLayoutVisibilityChangesEnabled = true;
		mHeaderLayout.reset();
		mFooterLayout.reset();
		smoothScrollTo(0);
//		scrollTo(0, 0);//TODO modify
//		int height = getFooterSize();
//		Log.d(">>>>", "height:" + height);
//		setHeaderScroll(height);
	}
	
	protected void onPullToRefresh() {
		switch (mCurrentMode) {
			case MODE_END:
				mFooterLayout.pullToRefresh();
				break;
			case MODE_START:
				mHeaderLayout.pullToRefresh();
				break;
			default:
				// NO-OP
				break;
		}
	}
	
	protected void onRefreshing(final boolean doScroll) {
		if (mMode.showHeaderLoadingLayout()) {
			mHeaderLayout.refreshing();
		}
		if (mMode.showFooterLoadingLayout()) {
			mFooterLayout.refreshing();
		}

		if (doScroll) {
			// Call Refresh Listener when the Scroll has finished
			OnSmoothScrollFinishedListener listener = new OnSmoothScrollFinishedListener() {
				@Override
				public void onSmoothScrollFinished() {
					callRefreshListener();
				}
			};

			switch (mCurrentMode) {
				case MODE_MANUAL:
				case MODE_END:
					smoothScrollTo(getFooterSize(), listener);
					break;
				default:
				case MODE_START:
					smoothScrollTo(-getHeaderSize(), listener);
					break;
			}
		} else {
			// We're not scrolling, so just call Refresh Listener now
			callRefreshListener();
		}
	}
	
	public final void onRefreshComplete() {
		if (isRefreshing()) {
			setState(Pull2RefreshState.RESET);
		}
	}
	
	private void callRefreshListener() {
		if (null != mOnRefreshListener) {
			mOnRefreshListener.onRefresh(this);
		} else if (null != mOnRefreshListener2) {
			if (mCurrentMode == Pull2RefreshMode.MODE_START) {
				mOnRefreshListener2.onPullDownToRefresh(this);
			} else if (mCurrentMode == Pull2RefreshMode.MODE_END) {
				mOnRefreshListener2.onPullUpToRefresh(this);
			}
		}
	}
	
	protected final void smoothScrollTo(int scrollValue) {
		smoothScrollTo(scrollValue, SMOOTH_SCROLL_DURATION_MS);
	}
	
	private final void smoothScrollTo(int scrollValue, long duration) {
		smoothScrollTo(scrollValue, duration, 0, null);
	}
	
	protected final void smoothScrollTo(int scrollValue, OnSmoothScrollFinishedListener listener) {
		smoothScrollTo(scrollValue, SMOOTH_SCROLL_DURATION_MS, 0, listener);
	}
	
	private final void smoothScrollTo(int newScrollValue, long duration, long delayMillis,
			OnSmoothScrollFinishedListener listener) {
		if (null != mCurrentSmoothScrollRunnable) {
			mCurrentSmoothScrollRunnable.stop();
		}

		final int oldScrollValue = getScrollY();
		
		if (oldScrollValue != newScrollValue) {
			if (null == mScrollAnimationInterpolator) {
				// Default interpolator is a Decelerate Interpolator
				mScrollAnimationInterpolator = new DecelerateInterpolator();
			}
			mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration, listener);

			if (delayMillis > 0) {
				postDelayed(mCurrentSmoothScrollRunnable, delayMillis);
			} else {
				post(mCurrentSmoothScrollRunnable);
			}
		}
	}
	
	@Override
	protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
		Log.d(LOG_TAG, String.format("onSizeChanged. W: %d, H: %d", w, h));
		super.onSizeChanged(w, h, oldw, oldh);

		refreshLoadingViewsSize();//更新header的高度
		refreshRefreshableViewSize(w, h);//更新中间的view的高度

		/**
		 * As we're currently in a Layout Pass, we need to schedule another one
		 * to layout any changes we've made here
		 */
		post(new Runnable() {
			@Override
			public void run() {
				requestLayout();
			}
		});
	}
	
	protected final void refreshRefreshableViewSize(int width, int height) {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mRefreshableViewWrapper.getLayoutParams();
		if (lp.height != height) {
			lp.height = height;
			mRefreshableViewWrapper.requestLayout();
		}
	}
	
	public final void setRefreshing(boolean doScroll) {
		if (!isRefreshing()) {
			setState(Pull2RefreshState.MANUAL_REFRESHING, doScroll);
		}
	}
	
	public final void setOnRefreshListener(OnRefreshListener<T> listener) {
		mOnRefreshListener = listener;
		mOnRefreshListener2 = null;
	}

	public final void setOnRefreshListener(OnRefreshListener2<T> listener) {
		mOnRefreshListener2 = listener;
		mOnRefreshListener = null;
	}
	
	public final void setMode(Pull2RefreshMode mode) {
		if (mode != mMode) {
			Log.d(LOG_TAG, "Setting mode to: " + mode);
			mMode = mode;
			updateUIForMode();
		}
	}
	
	public final Pull2RefreshMode getMode() {
		return mMode;
	}
	
	public final void setStartStyle(Pull2RefreshStyle style) {
		if (style != null) {
			mStartStyle = style;
		}
	}
	
	public final Pull2RefreshStyle getStartStyle() {
		return mStartStyle;
	}
	
	public final void setEndStyle(Pull2RefreshStyle style) {
		if (style != null) {
			mEndStyle = style;
		}
	}
	
	public final Pull2RefreshStyle getEndStyle() {
		return mEndStyle;
	}
	
	/** mCurrentMode本不应该被外部改变，但因为要使listview能自动刷新，必须控制mCurrentMode来区别是上拉还是下拉*/
	protected void setCurrentMode(Pull2RefreshMode mode) {
		mCurrentMode = mode;
	}
	
	protected FrameLayout getRefreshableViewWrapper() {
		return mRefreshableViewWrapper;
	}
	
	/** OverScroll是否可用 */
	public final boolean isPullToRefreshOverScrollEnabled() {
		return VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD && mOverScrollEnabled
				&& OverscrollHelper.isAndroidOverScrollEnabled(mRefreshableView);
	}
	
	protected final void disableLoadingLayoutVisibilityChanges() {
		mLayoutVisibilityChangesEnabled = false;
	}

	
	/** 创建一个可下拉刷新的view，如listview，gridview等 */
	protected abstract T createRefreshableView(Context context, AttributeSet attrs);
	
	/** 是否能“上拉刷新” */
	protected abstract boolean isReadyForPullEnd();

	/** 是否能“下拉刷新” */
	protected abstract boolean isReadyForPullStart();
	
	final class SmoothScrollRunnable implements Runnable {
		private final Interpolator mInterpolator;
		private final int mScrollToY;
		private final int mScrollFromY;
		private final long mDuration;
		private OnSmoothScrollFinishedListener mListener;

		private boolean mContinueRunning = true;
		private long mStartTime = -1;
		private int mCurrentY = -1;

		public SmoothScrollRunnable(int fromY, int toY, long duration, OnSmoothScrollFinishedListener listener) {
			mScrollFromY = fromY;
			mScrollToY = toY;
			mInterpolator = mScrollAnimationInterpolator;
			mDuration = duration;
			mListener = listener;
		}

		@Override
		public void run() {

			/**
			 * Only set mStartTime if this is the first time we're starting,
			 * else actually calculate the Y delta
			 */
			if (mStartTime == -1) {
				mStartTime = System.currentTimeMillis();
			} else {

				/**
				 * We do do all calculations in long to reduce software float
				 * calculations. We use 1000 as it gives us good accuracy and
				 * small rounding errors
				 */
				long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;
				
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				final int deltaY = Math.round((mScrollFromY - mScrollToY)
						* mInterpolator.getInterpolation(normalizedTime / 1000f));
				mCurrentY = mScrollFromY - deltaY;
				Log.d(LOG_TAG, "normalizedTime:" + normalizedTime + ", mCurrentY:" + mCurrentY
						+ ", mScrollFromY:" + mScrollFromY + ", deltaY:" + deltaY);
				setHeaderScroll(mCurrentY);
			}

			// If we're not at the target Y, keep going...
			if (mContinueRunning && mScrollToY != mCurrentY) {
				postOnAnimation(Pull2RefreshBase.this, this);
			} else {
				if (null != mListener) {
					mListener.onSmoothScrollFinished();
				}
			}
		}

		public void stop() {
			mContinueRunning = false;
			removeCallbacks(this);
		}
		
		private void postOnAnimation(View view, Runnable runnable) {
			if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
				postOnAnimationOverSdk16(view, runnable);
			} else {
				view.postDelayed(runnable, 16);
			}
		}
		
		@TargetApi(16)
		private void postOnAnimationOverSdk16(View view, Runnable runnable) {
			view.postOnAnimation(runnable);
		}
	}
}
