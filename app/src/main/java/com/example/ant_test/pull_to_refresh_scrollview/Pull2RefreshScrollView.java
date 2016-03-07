package com.example.ant_test.pull_to_refresh_scrollview;

import java.util.Date;
import com.example.ant_test.R;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @Title: Pull2RefreshScrollView.java
 * @Package com.example.ant_test.pull_to_refresh_scrollview
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-4-8 下午5:51:47
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class Pull2RefreshScrollView extends ScrollView{
	private static final String TAG = "Pull2RefreshScrollView";
	private final static int RELEASE_To_REFRESH = 0;
	private final static int PULL_To_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
//	private final static int LOADING = 4;
	// 实际的padding的距离与界面上偏移距离的比例
	private final static int RATIO = 2;
	private LinearLayout headView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private int headContentHeight;
	//是否滚动
	protected boolean isRefreshable;
	private int state;
	private boolean isBack;
	private boolean canReturn;
	private boolean isRecored;
	private int startY;
	
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	private OnRefreshListener refreshListener;

	public Pull2RefreshScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public Pull2RefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public Pull2RefreshScrollView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		
	}
	
	private void addHead() {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		headView = (LinearLayout) inflater.inflate(R.layout.item_refresh_head, null);
		int childCount = this.getChildCount();
		if (childCount >= 1) {
			ViewGroup vg = (ViewGroup)this.getChildAt(0);
			vg.addView(headView, 0);
		} else {
			this.addView(headView);
		}
		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
		progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);
		measureView(headView);

		headContentHeight = headView.getMeasuredHeight();
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		
		state = DONE;
		isRefreshable = false;
		canReturn = false;
		
		animation = new RotateAnimation(0, 180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(250);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
	}
	
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		addHead();
		Log.d(TAG, "onFinishInflate");
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (getScrollY() == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != REFRESHING) {
					if (state == DONE) {
						// 什么都不做
					}
					if (state == PULL_To_REFRESH) {
						state = DONE;
						changeHeaderViewByState();
						// Log.i(TAG, "由下拉刷新状态，到done状态");
					}
					if (state == RELEASE_To_REFRESH) {
						state = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
						// Log.i(TAG, "由松开刷新状态，到done状态");
					}
				}
				isRecored = false;
				isBack = false;
	
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				Log.d(TAG, "tempY:" + tempY + " scrollY:" + getScrollY());
				if (!isRecored && getScrollY() == 0) {
					// Log.i(TAG, "在move时候记录下位置");
					isRecored = true;
					startY = tempY;
				}
	
				if (state != REFRESHING && isRecored) {
					// 可以松手去刷新了
					if (state == RELEASE_To_REFRESH) {
						canReturn = true;
	
						if (((tempY - startY) / RATIO < headContentHeight) && (tempY - startY) > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
							// Log.i(TAG, "由松开刷新状态转变到下拉刷新状态");
						}
						// 一下子推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
							// Log.i(TAG, "由松开刷新状态转变到done状态");
						} else {
							// 不用进行特别的操作，只用更新paddingTop的值就行了
						}
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (state == PULL_To_REFRESH) {
						canReturn = true;
	
						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - startY) / RATIO >= headContentHeight) {
							state = RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
							// Log.i(TAG, "由done或者下拉刷新状态转变到松开刷新");
						}
						// 上推到顶了
						else if (tempY - startY <= 0) {
							state = DONE;
							changeHeaderViewByState();
							// Log.i(TAG, "由DOne或者下拉刷新状态转变到done状态");
						}
					}
	
					// done状态下
					if (state == DONE) {
						if (tempY - startY > 0) {
							state = PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}
	
					// 更新headView的size
					if (state == PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);
	
					}
	
					// 更新headView的paddingTop
					if (state == RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
					}
					if (canReturn) {
						canReturn = false;
						return true;
					}
				}
				break;
			}
			return super.onTouchEvent(event);
		} else {
			return false;
		}
	}
	
	// 当状态改变时候，调用该方法，以更新界面
		private void changeHeaderViewByState() {
			switch (state) {
			case RELEASE_To_REFRESH:
				arrowImageView.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				tipsTextview.setVisibility(View.VISIBLE);
				lastUpdatedTextView.setVisibility(View.VISIBLE);

				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(animation);

				tipsTextview.setText("松开刷新");

				// Log.i(TAG, "当前状态，松开刷新");
				break;
			case PULL_To_REFRESH:
				progressBar.setVisibility(View.GONE);
				tipsTextview.setVisibility(View.VISIBLE);
				lastUpdatedTextView.setVisibility(View.VISIBLE);
				arrowImageView.clearAnimation();
				arrowImageView.setVisibility(View.VISIBLE);
				// 是由RELEASE_To_REFRESH状态转变来的
				if (isBack) {
					isBack = false;
					arrowImageView.clearAnimation();
					arrowImageView.startAnimation(reverseAnimation);

					tipsTextview.setText("下拉刷新");
				} else {
					tipsTextview.setText("下拉刷新");
				}
				// Log.i(TAG, "当前状态，下拉刷新");
				break;

			case REFRESHING:

				headView.setPadding(0, 0, 0, 0);

				progressBar.setVisibility(View.VISIBLE);
				arrowImageView.clearAnimation();
				arrowImageView.setVisibility(View.GONE);
				tipsTextview.setText("正在刷新...");
				lastUpdatedTextView.setVisibility(View.VISIBLE);
				// Log.i(TAG, "当前状态,正在刷新...");
				break;
			case DONE:
				headView.setPadding(0, -1 * headContentHeight, 0, 0);

				progressBar.setVisibility(View.GONE);
				arrowImageView.clearAnimation();
				arrowImageView.setImageResource(R.drawable.arrow_down);
				tipsTextview.setText("下拉刷新");
				lastUpdatedTextView.setVisibility(View.VISIBLE);

				// Log.i(TAG, "当前状态，done");
				break;
			}
		}
	
		public void setonRefreshListener(OnRefreshListener refreshListener) {
			this.refreshListener = refreshListener;
			isRefreshable = true;
		}

		public interface OnRefreshListener {
			public void onRefresh();
		}

		public void onRefreshComplete() {
			((Activity) getContext()).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					state = DONE;
					Date now = new Date();
					String time = now.getMonth() + "-" + now.getDay() + " " + now.getHours() + ":" + now.getMinutes();
					//time = time.substring(4, 6) + "-" + time.substring(6, 8) + " " + time.substring(9, 11) + ":" + time.substring(11, 13);
					lastUpdatedTextView.setText("最近更新:" + time);
					changeHeaderViewByState();
					invalidate();
					scrollTo(0, 0);
				}
			});
		}

		private void onRefresh() {
			if (refreshListener != null) {
				refreshListener.onRefresh();
			}
		}
}
