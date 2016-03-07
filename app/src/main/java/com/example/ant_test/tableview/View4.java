package com.example.ant_test.tableview;

import com.example.ant_test.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class View4 extends AbsPart{
	Paint paint = new Paint();

	public View4(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public View4(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public View4(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void init() {
		this.mMaxX = 600;
		this.mMaxY = 1000;
		super.init();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		paint.setTextSize(20);
		paint.setColor(getResources().getColor(android.R.color.black));
		for (int i = 20; i < 1200; i += 20) {
			String s = "";
			for (int j = 0; j < 50; j++) {
				s += i;
			}
			canvas.drawText(s, 0, i, paint);
		}
		super.onDraw(canvas);
	}

	@Override
	public void computeScroll() {
		Log.d("view4", "computeScroll");
		super.computeScroll();
	}

	@Override
	public boolean canVerticalSlide() {
		return true;
	}

	@Override
	public boolean canHorizontalSlide() {
		return true;
	}

	
	
}
