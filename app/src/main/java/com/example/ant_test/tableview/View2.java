package com.example.ant_test.tableview;

import com.example.ant_test.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

public class View2 extends AbsPart{
	Paint paint = new Paint();
	

	public View2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public View2(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public View2(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		this.mMinX = 0;
		this.mMaxX = 600;
		this.mMinY = 0;
		this.mMaxY = 0;
	}
	
	@Override
	public void computeScroll() {
		int x = this.mScroller.getCurrX();
		Log.d("view2", x + "");
//		this.scrollTo(x, 0);
	}
	

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int w = MeasureSpec.getSize(widthMeasureSpec);
		int h = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
		Log.d("View2", "View2 onMeasure" + w + " " + h);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(">>>>>>>>>>>", "View2 onDraw" + this.getMeasuredWidth() + " " + this.getMeasuredHeight());
		paint.setTextSize(20);
		paint.setColor(getResources().getColor(android.R.color.white));
		int h = this.getMeasuredHeight() / 2;
		canvas.drawText("q w e r t y u i o p " +
				"a s d f g h j k l z x c v b n m ,", 0, h, paint);
		super.onDraw(canvas);
	}

	@Override
	public boolean canVerticalSlide() {
		return false;
	}

	@Override
	public boolean canHorizontalSlide() {
		return true;
	}

	
}
