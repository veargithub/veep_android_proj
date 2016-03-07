package com.example.ant_test.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.ant_test.R;

import android.content.Context;

import android.os.Handler;

import android.util.AttributeSet;
import android.util.Log;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;

import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import com.example.ant_test.view.PullToRefreshCallBack;

public class PullListView extends ListView implements OnScrollListener{
	private static final String TAG = "PullListView";
	private static final int RATIO = 2;
	private LayoutInflater inflater;
	private LinearLayout headView;
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	private int headContentWidth;
	private int headContentHeight;
	public boolean isRefreshable;
	private IPullListView pullListViewImpl;
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	private int TOP_STATUS;
	private final int DONE = 0;
	private final int RELEASE_TO_REFRESH = 1;
	public void setPullListViewImpl(IPullListView impl) {
		this.pullListViewImpl = impl;
	}
	public PullListView(Context context) {
		super(context);
		init(context);
	}

	public PullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private RelativeLayout mRefreshView2;
	private ProgressBar mRefreshViewProgress2;

	private void init(Context context) {
		inflater = LayoutInflater.from(context);
		headView = (LinearLayout) inflater.inflate(R.layout.item_refresh_head, null);
		mRefreshView2 = (RelativeLayout) inflater.inflate(R.layout.pull_to_refresh_footer, this, false);
		mRefreshViewProgress2 = (ProgressBar) mRefreshView2.findViewById(R.id.pull_to_refresh_progress);
		mRefreshViewProgress2.setVisibility(View.VISIBLE);
		arrowImageView = (ImageView) headView.findViewById(R.id.head_arrowImageView);
		arrowImageView.setMinimumWidth(70);
		arrowImageView.setMinimumHeight(50);
		progressBar = (ProgressBar) headView.findViewById(R.id.head_progressBar);
		tipsTextview = (TextView) headView.findViewById(R.id.head_tipsTextView);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.head_lastUpdatedTextView);
		//measureView(headView);
		headView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		headContentHeight = headView.getMeasuredHeight();
		headContentWidth = headView.getMeasuredWidth();
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		headView.invalidate();
		Log.v("size", "width:" + headContentWidth + " height:" + headContentHeight);
		addHeaderView(headView, null, false);
		isRefreshable = true;
		this.setOnScrollListener(this);
		animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		
		animation.setDuration(250);
		animation.setFillAfter(true);
		reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
		TOP_STATUS = DONE;
	}

	private int mLastMotionY;
	/** 记录开始下拉刷新的时候的Y值（在一次下拉刷新的过程中，这个值只被记录一次） */
	private int topStart;
	/** 标志是否开始了一次下拉刷新*/
	private boolean topStartRecord = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mLastMotionY = (int) event.getY();
				break;
			case MotionEvent.ACTION_UP:
//				Log.d(TAG, headView.getPaddingTop() + " " + this.getFirstVisiblePosition() + " " +  this.getChildAt(0).getTop() + " " + this.getLastVisiblePosition() 
//						+ " " + this.getChildCount() + " " + this.getCount() + " " + this.getChildAt(this.getChildCount() - 1).getBottom() + " " + this.getChildAt(this.getChildCount() - 1).getTop() 
//						+ " " + this.getChildAt(this.getChildCount() - 1).getHeight() + " " + this.getChildAt(this.getChildCount() - 1) + " " + this.computeVerticalScrollOffset());
				topStartRecord = true;
				if (this.getFirstVisiblePosition() == 0) {
					if (headView.getPaddingTop() >= 0) {//下拉刷新这个view完全暴露出来
						headView.setPadding(0, 0, 0, 0);//滑到正好露出这个view的位置
						if (this.pullListViewImpl != null) {//回调
							isRefreshable = false;
							this.pullListViewImpl.onTopRefresh();
							Log.d(TAG, "onTopRefresh");
						}
					} else {
						resetHeader();//重置header
					}
				}
				break;
			case MotionEvent.ACTION_MOVE:
				int currentY = (int) event.getY();
				int diffY = currentY - mLastMotionY;//记录向上或向下滑动了多少距离
				mLastMotionY = currentY;
				if (this.getFirstVisiblePosition() == 0) {//说明listview的第一个item完全暴露出来了
					if (!topStartRecord) {//开启了一次下拉刷新的过程
						topStart = currentY;
						topStartRecord = true;
					}
					Log.d(TAG, "currentY" + currentY + " topStart" + topStart + " headContentHeight:" + headContentHeight);
					if (currentY - topStart >= headContentHeight) {//如果下拉的距离大于等于header的高度（说明这时如果松开就要触发事件了）
						if (TOP_STATUS == DONE) {
							arrowImageView.clearAnimation();
							arrowImageView.startAnimation(animation);
							tipsTextview.setText("松开刷新");
							TOP_STATUS = RELEASE_TO_REFRESH;
						}
					} else {//说明下拉刷新的距离小于header的高度（这时如果松开，只要还原header就行了）
						if (TOP_STATUS == RELEASE_TO_REFRESH) {
							arrowImageView.clearAnimation();
							arrowImageView.startAnimation(reverseAnimation);
							tipsTextview.setText("下拉刷新");
							TOP_STATUS = DONE;
						}
					}
					headView.setPadding(0, headView.getPaddingTop() + diffY, 0, 0);//刷新header
				} else { 
					if (getFooterViewsCount() == 0 && diffY < 0) {
						addFooterView(mRefreshView2);
					}
				}
				Log.d(TAG, "getFooterViewsCount:" + getFooterViewsCount());
				break;
			}
		}
		return super.onTouchEvent(event);
	}
	public void resetHeader() {
		headView.setPadding(0, -headContentHeight, 0, 0);
		this.scrollTo(0, 0);
		arrowImageView.clearAnimation();
		arrowImageView.setImageResource(R.drawable.arrow_down);
		tipsTextview.setText("下拉刷新");
		TOP_STATUS = DONE;
	}
	public void resetFooter() {
		removeFooterView(mRefreshView2);
	}
	public void setAdapter(BaseAdapter adapter) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String date=format.format(new Date());
		lastUpdatedTextView.setText("date:" + date);
		super.setAdapter(adapter);
	}
	private void initHeadView() {
		LinearLayout text = new LinearLayout(getContext());
		text.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		text.setOrientation(LinearLayout.VERTICAL);
		TextView tips = new TextView(getContext());
		tips.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		tips.setGravity(Gravity.CENTER);
		TextView latestUpdate = new TextView(getContext());
		latestUpdate.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		latestUpdate.setGravity(Gravity.CENTER);
		text.addView(tips);
		text.addView(latestUpdate);
		RelativeLayout image = new RelativeLayout(getContext());
		image.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) image.getLayoutParams();
		rlp.addRule(RelativeLayout.LEFT_OF, text.getId());
		ImageView arrow = new ImageView(getContext());
		ProgressBar pb = new ProgressBar(getContext());
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			Log.d(TAG, "SCROLL_STATE_IDLE " + this.getFirstVisiblePosition() + " " + this.getChildCount() + " " + this.getCount());
			if (this.getFirstVisiblePosition() + this.getChildCount() >= this.getCount()) {
				if (this.pullListViewImpl != null) {
					isRefreshable = false;
					this.pullListViewImpl.onFootRefresh();
					
				}
			}
		}
	}
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		Log.d(TAG, "firstVisibleItem:" + firstVisibleItem);
	}

	public interface IPullListView {
		public void onTopRefresh();
		public void onFootRefresh();
	}
}

