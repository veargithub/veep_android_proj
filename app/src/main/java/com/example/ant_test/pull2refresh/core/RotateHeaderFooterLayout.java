package com.example.ant_test.pull2refresh.core;

import com.example.ant_test.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView.ScaleType;

/**
 * @Title: RotateHeaderFooterLayout.java
 * @Package com.example.ant_test.pull2refresh.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-12-1 上午9:39:05
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class RotateHeaderFooterLayout extends HeaderFooterLayout{
	
	static final int ROTATION_ANIMATION_DURATION = 1200;//图片转一圈耗时

	private final Animation mRotateAnimation;
	private final Matrix mHeaderImageMatrix;

	private float mRotationPivotX, mRotationPivotY;

	private final boolean mRotateDrawableWhilePulling;//下拉的时候，图片是否要转

	public RotateHeaderFooterLayout(Context context, Pull2RefreshMode mode,
			TypedArray attrs) {
		super(context, mode, attrs);
		mRotateDrawableWhilePulling = attrs.getBoolean(R.styleable.PullToRefresh_ptrRotateDrawableWhilePulling, true);

		mHeaderImage.setScaleType(ScaleType.MATRIX);
		mHeaderImageMatrix = new Matrix();
		mHeaderImage.setImageMatrix(mHeaderImageMatrix);

		mRotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.default_ptr_rotate;
	}

	@Override
	protected void refreshingImpl() {
		mHeaderImage.startAnimation(mRotateAnimation);
	}

	@Override
	protected void resetImpl() {
		mHeaderImage.clearAnimation();
		resetImageRotation();
	}
	
	private void resetImageRotation() {
		if (null != mHeaderImageMatrix) {
			mHeaderImageMatrix.reset();
			mHeaderImage.setImageMatrix(mHeaderImageMatrix);
		}
	}
	
	protected void onPullImpl(float scaleOfLayout) {
		float angle;
		if (mRotateDrawableWhilePulling) {
			angle = scaleOfLayout * 90f;
		} else {
			angle = Math.max(0f, Math.min(180f, scaleOfLayout * 360f - 180f));
		}

		mHeaderImageMatrix.setRotate(angle, mRotationPivotX, mRotationPivotY);
		mHeaderImage.setImageMatrix(mHeaderImageMatrix);
	}

	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable) {
		if (null != imageDrawable) {
			mRotationPivotX = Math.round(imageDrawable.getIntrinsicWidth() / 2f);
			mRotationPivotY = Math.round(imageDrawable.getIntrinsicHeight() / 2f);
		}
	}

	@Override
	protected void pullToRefreshImpl() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void releaseToRefreshImpl() {
		// TODO Auto-generated method stub
		
	}
}
