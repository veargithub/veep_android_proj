package com.example.ant_test.tableview;

import com.example.ant_test.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class View1 extends AbsPart{
	Paint paint = new Paint();

	public View1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public View1(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public View1(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		Log.d(">>>>>>>>>>>", "View1 onDraw" + this.getMeasuredWidth() + " " + this.getMeasuredHeight());
		paint.setTextSize(20);
		paint.setTextAlign(Align.LEFT);
		paint.setColor(getResources().getColor(android.R.color.black));
		int h = this.getMeasuredHeight() / 2;
		canvas.drawText("111111111111111111111111111111", 0, h, paint);
		super.onDraw(canvas);
	}

	@Override
	public boolean canVerticalSlide() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canHorizontalSlide() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
