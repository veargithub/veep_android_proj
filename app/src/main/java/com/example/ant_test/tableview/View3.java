package com.example.ant_test.tableview;

import com.example.ant_test.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class View3 extends AbsPart{
	Paint paint = new Paint();

	public View3(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public View3(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public View3(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		paint.setTextSize(20);
		paint.setColor(getResources().getColor(android.R.color.black));
		for (int i = 20; i < 1200; i += 20) {
			canvas.drawText(i + "", 0, i, paint);
		}
		
		super.onDraw(canvas);
	}

	@Override
	public boolean canVerticalSlide() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canHorizontalSlide() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
