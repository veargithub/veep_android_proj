package com.example.ant_test.textview_with_circle;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

public class TextViewWithCircle extends TextView{
	private final float DEFAULT_RATIO = 0.4f;
	private float ratio;
	private Bitmap cornerMark;
	private int cw;
	private int ch;
	private Rect rect;

	public TextViewWithCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public TextViewWithCircle(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TextViewWithCircle(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);	
		if (this.cornerMark != null) {
			canvas.drawBitmap(this.cornerMark, null, rect, null);
		}
//		Log.d("<<<", "getPaddingLeft:" + this.getPaddingLeft());
	}
	
	public void setCornerMark(int res) {
		this.cornerMark = BitmapFactory.decodeResource(getResources(), res);
		this.setGravity(Gravity.RIGHT);
	}
	
	public void setRatio(float f) {
		this.ratio = f;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = this.getMeasuredWidth();
		int height = this.getMeasuredHeight();
		Log.d("<<<", "onMeasure:" + width + " " + height);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if (this.cw > 0 && this.ch > 0) {
			return;
		}
		int width = this.getMeasuredWidth();
		int height = this.getMeasuredHeight();
		Log.d("<<<", "onLayout:" + width + " " + height);
		if (this.cornerMark != null) {
			int cw = this.cornerMark.getWidth();
			int ch = this.cornerMark.getHeight();

			float cp = (float)cw / (float)ch;
			float ratio = this.ratio > 0 ? this.ratio : DEFAULT_RATIO;
			if (width > height) {
				if (cw > ch) {
					cw = (int)(height * ratio);
					ch = (int) (cw / cp);
				} else {
					ch = (int)(height * ratio);
					cw = (int)(ch * cp);
				}
			} else {
				if (cw > ch) {
					cw = (int)(width * ratio);
					ch = (int) (cw / cp);
				} else {
					ch = (int)(width * ratio);
					cw = (int)(ch * cp);
				}
			}
			Log.d("<<<", cw + " " + ch);
			if (rect == null || this.cw != cw || this.ch != ch) {
				this.cw = cw;
				this.ch = ch;
				rect = new Rect(width - cw, 0, width, ch);
				this.setPadding(0, ch / 2, cw / 2, 0);
			}
		}
	}
}
