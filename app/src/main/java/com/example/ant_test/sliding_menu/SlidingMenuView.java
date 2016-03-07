package com.example.ant_test.sliding_menu;

import com.example.ant_test.sliding_menu.impl.SlidingMenuTransformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Title: SlidingMenuView.java
 * @Package com.example.ant_test.sliding_menu
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-8-22 下午2:50:26
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class SlidingMenuView extends ViewGroup{
	private final String TAG = "SlidingMenu_Menu";
	private View mContent;
	private SlidingMenuAbovePanel panel;
	
	
	public SlidingMenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SlidingMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SlidingMenuView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		this.setWillNotDraw(false);
		this.setDrawingCacheEnabled(true);
	}

	private int widthOffset;//这个view的右边到父view右边的间距,换句话说就是，当这个view显示的时候，上面那个view会显示多少

	public int getWidthOffset() {
		return widthOffset;
	}

	public void setWidthOffset(int widthOffset) {
		this.widthOffset = widthOffset;
		requestLayout();//当view确定自身已经不再适合现有的区域时，该view本身调用这个方法要求parent view重新调用他的onMeasure onLayout来对重新设置自己位置
	}
	
	private float leftOffsetScale = 0.5f;//此view左边默认冲出左屏幕的距离的scale
	
	public void setLeftOffsetScale(float leftOffsetScale) {
		this.leftOffsetScale = leftOffsetScale;
	}

	public void setPanel(SlidingMenuAbovePanel panel) {
		this.panel = panel;
	}
	
	private SlidingMenuTransformer mTransformer;
	
	public void setTransformer(SlidingMenuTransformer transformer) {
		this.mTransformer = transformer;
	}
	
	
	private float alphaFrom = 0.75f;

	public void setAlphaFrom(float alphaFrom) {
		this.alphaFrom = alphaFrom;
	}
	
	private float alphaTo = 1.0f;

	public void setAlphaTo(float alphaTo) {
		this.alphaTo = alphaTo;
	}
	
	private float scaleFrom = 0.5f;
	
	private float scaleTo = 0.75f;

	/**
	 * 设置这个view的内容
	 * @param view
	 */
	public void setContent(View view) {//先删除原有的，再添加
		if (this.getChildCount() > 0) {
			this.removeAllViews();
		}
		this.mContent = view;
		addView(mContent);
	}
	
	private boolean slidingEnabled = true;//本控件是否能滑动
	
	public void setSlidingEnabled(boolean b) {
		this.slidingEnabled = b;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = this.getMeasuredWidth();
		if (mContent != null) {
			int contentHeightMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, width - widthOffset);
			mContent.measure(contentHeightMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (mContent != null) {
			mContent.layout(0, 0, r - l - widthOffset, b - t);
		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (mTransformer != null) {
			canvas.save();
			float alpha = (float) (panel.getOpenPercents() * (alphaTo - alphaFrom) + alphaFrom);
			Log.d(TAG, "dispatchdraw, scale:" + alpha + ", percent:" + panel.getOpenPercents());
			if (alpha > 0.99f) alpha = 1f;//解决scale会无限接近1但小于1的情况
			float scale = (float) panel.getOpenPercents() * (scaleTo - scaleFrom) + scaleFrom;
			if (scale > 0.99f) scale = 1f;
			mTransformer.transform(this, canvas, scale, alpha);
			super.dispatchDraw(canvas);
			canvas.restore();
		} else {
			super.dispatchDraw(canvas);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.d(TAG, "onDraw");
	}

	/**
	 * panel向右最远能滑到哪
	 * @return
	 */
	public int getPanelLeftBound() {
		return this.getLeft() - getMenuWidth();
	}
	/**
	 * panel向左最远能滑到哪
	 * @return
	 */
	public int getPanelRightBound() {
		return this.getLeft();
	}
	/**
	 * 当panel滑动的时候，带动这个menu一起滑动
	 * @param x panel的scrollX
	 * @param y panel的scrollY
	 */
	public void scrollToWithPanel(int x, int y) {//TODO 明天再来看看
		if (this.mContent.getWidth() == 0) return;
		float ratio = 1 - (float)Math.abs(x) / getMenuWidth();
		//Log.d(TAG, "scrollToWithPanel, ratio:" + ratio);
		float mInitScrollX = (leftOffsetScale * getMenuWidth());
		int xOffset =  (int)(mInitScrollX * ratio);
		this.scrollTo(xOffset, y);
	}

	/**
	 * 获取菜单的真实宽度(减去右边的offset后的宽度)
	 * @return
	 */
	public int getMenuWidth() {
		return this.mContent.getWidth();
	}
}
