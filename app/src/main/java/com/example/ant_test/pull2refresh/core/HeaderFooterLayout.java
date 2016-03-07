package com.example.ant_test.pull2refresh.core;

import com.example.ant_test.R;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * @Title: HeaderFooterLayout.java
 * @Package com.example.ant_test.pull2refresh.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-25 下午4:24:36
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public abstract class HeaderFooterLayout extends FrameLayout{
	static final String LOG_TAG = "Pull2Refresh-LoadingLayout";

	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

	private FrameLayout mInnerLayout;

	protected final ImageView mHeaderImage;
	protected final ProgressBar mHeaderProgress;

	private boolean mUseIntrinsicAnimation;//是否使用view自带的动画

	private final TextView mHeaderText;
	private final TextView mSubHeaderText;

	protected final Pull2RefreshMode mMode;
	//protected final Orientation mScrollDirection;

	private String mPullLabel;//下拉刷新。。。
	private String mRefreshingLabel;//正在刷新。。。
	private String mReleaseLabel;//放开刷新。。。
	
	public HeaderFooterLayout(Context context, final Pull2RefreshMode mode, TypedArray attrs) {
		super(context);
		mMode = mode;
		LayoutInflater.from(context).inflate(R.layout.pull_2_refresh_header, this);
		mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
		mHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_text);
		mHeaderProgress = (ProgressBar) mInnerLayout.findViewById(R.id.pull_to_refresh_progress);
		mSubHeaderText = (TextView) mInnerLayout.findViewById(R.id.pull_to_refresh_sub_text);
		mHeaderImage = (ImageView) mInnerLayout.findViewById(R.id.pull_to_refresh_image);

		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInnerLayout.getLayoutParams();
		
		switch (mode) {//设定header上的所有文字信息
			case MODE_END :
			case MODE_MANUAL :
				lp.gravity = Gravity.TOP;
				mPullLabel = context.getString(R.string.pull_to_refresh_from_bottom_pull_label);
				mRefreshingLabel = context.getString(R.string.pull_to_refresh_from_bottom_refreshing_label);
				mReleaseLabel = context.getString(R.string.pull_to_refresh_from_bottom_release_label);
				break;
			case MODE_START:
			default:
				lp.gravity = Gravity.BOTTOM;
				mPullLabel = context.getString(R.string.pull_to_refresh_pull_label);
				mRefreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_label);
				mReleaseLabel = context.getString(R.string.pull_to_refresh_release_label);
				break;
		}

		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {//设定背景
			Drawable background = attrs.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
			if (null != background) {
				this.setBackgroundDrawable(background);
			}
		}

		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance)) {//设定主标题的文字style
			TypedValue styleID = new TypedValue();
			attrs.getValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance, styleID);
			setTextAppearance(styleID.data);
		}
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance)) {//设定副标题的文字style
			TypedValue styleID = new TypedValue();
			attrs.getValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance, styleID);
			setSubTextAppearance(styleID.data);
		}

		//文字颜色需要另外单独设置
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
			ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor);
			if (null != colors) {
				setTextColor(colors);
			}
		}
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderSubTextColor)) {
			ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderSubTextColor);
			if (null != colors) {
				setSubTextColor(colors);
			}
		}

		//图片(箭头或者转圈)
		Drawable imageDrawable = null;
		if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
			imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawable);
		}

		// 分别设置图片
//		switch (mode) {
//			case PULL_FROM_START:
//			default:
//				if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableStart)) {
//					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableStart);
//				} else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableTop)) {
//					Utils.warnDeprecation("ptrDrawableTop", "ptrDrawableStart");
//					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableTop);
//				}
//				break;
//
//			case PULL_FROM_END:
//				if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd)) {
//					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd);
//				} else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableBottom)) {
//					Utils.warnDeprecation("ptrDrawableBottom", "ptrDrawableEnd");
//					imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableBottom);
//				}
//				break;
//		}

		if (null == imageDrawable) {//如果xml里没有配置图片,则使用默认的图片
			imageDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
		}

		setLoadingDrawable(imageDrawable);//设置图片

		reset();
	}
	
	private void setTextAppearance(int value) {
		if (null != mHeaderText) {
			mHeaderText.setTextAppearance(getContext(), value);
		}
		if (null != mSubHeaderText) {
			mSubHeaderText.setTextAppearance(getContext(), value);
		}
	}
	
	private void setSubTextAppearance(int value) {
		if (null != mSubHeaderText) {
			mSubHeaderText.setTextAppearance(getContext(), value);
		}
	}
	
	private void setTextColor(ColorStateList color) {
		if (null != mHeaderText) {
			mHeaderText.setTextColor(color);
		}
		if (null != mSubHeaderText) {
			mSubHeaderText.setTextColor(color);
		}
	}
	
	private void setSubTextColor(ColorStateList color) {
		if (null != mSubHeaderText) {
			mSubHeaderText.setTextColor(color);
		}
	}
	
	public final void setLoadingDrawable(Drawable imageDrawable) {
		// Set Drawable
		mHeaderImage.setImageDrawable(imageDrawable);
		mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);

		// Now call the callback
		onLoadingDrawableSet(imageDrawable);
	}
	
	public final void reset() {
		if (null != mHeaderText) {
			mHeaderText.setText(mPullLabel);
		}
		mHeaderImage.setVisibility(View.VISIBLE);

		if (mUseIntrinsicAnimation) {
			((AnimationDrawable) mHeaderImage.getDrawable()).stop();
		} else {
			// Now call the callback
			resetImpl();
		}

		if (null != mSubHeaderText) {
			if (TextUtils.isEmpty(mSubHeaderText.getText())) {
				mSubHeaderText.setVisibility(View.GONE);
			} else {
				mSubHeaderText.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public final void hideAllViews() {
		if (View.VISIBLE == mHeaderText.getVisibility()) {
			mHeaderText.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mHeaderProgress.getVisibility()) {
			mHeaderProgress.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mHeaderImage.getVisibility()) {
			mHeaderImage.setVisibility(View.INVISIBLE);
		}
		if (View.VISIBLE == mSubHeaderText.getVisibility()) {
			mSubHeaderText.setVisibility(View.INVISIBLE);
		}
	}
	
	public final void setHeight(int height) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) getLayoutParams();
		lp.height = height;
		requestLayout();
	}
	
	public final int getContentSize() {
		return mInnerLayout.getHeight();
	}
	
	/**
	 * 下拉刷新时的回调函数
	 * @param scaleOfLayout 下拉的距离与header的高度的比值,如果使用drawable自带的动画效果,则mUseIntrinsicAnimation会为true,最终不会调用onPullImpl
	 */
	public final void onPull(float scaleOfLayout) {
		if (!mUseIntrinsicAnimation) {
			onPullImpl(scaleOfLayout);
		}
	}
	
	public final void pullToRefresh() {
		if (null != mHeaderText) {
			mHeaderText.setText(mPullLabel);
		}
		pullToRefreshImpl();
	}
	
	public final void releaseToRefresh() {
		if (null != mHeaderText) {
			mHeaderText.setText(mReleaseLabel);
		}
		releaseToRefreshImpl();
	}
	
	public final void refreshing() {
		if (null != mHeaderText) {
			mHeaderText.setText(mRefreshingLabel);
		}

		if (mUseIntrinsicAnimation) {
			((AnimationDrawable) mHeaderImage.getDrawable()).start();
		} else {
			refreshingImpl();
		}

		if (null != mSubHeaderText) {
			mSubHeaderText.setVisibility(View.GONE);
		}
	}
	
	protected abstract int getDefaultDrawableResId();
	
	protected abstract void refreshingImpl();
	
	protected abstract void resetImpl();
	
	protected abstract void onPullImpl(float scaleOfLayout);//scaleOfLayout应该是可见高度与总高度的比例
	
	protected abstract void onLoadingDrawableSet(Drawable imageDrawable);//加载图片时的回调
	
	protected abstract void pullToRefreshImpl();//由其他状态进入到“下拉刷新”状态
	
	protected abstract void releaseToRefreshImpl();//由其他状态进入到“释放刷新”状态
}
