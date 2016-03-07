package com.example.ant_test.tableview;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.Scroller;

public class VFTableView extends View{
	private static final String TAG = "VFTableView";
	private Paint mPaint;
	private Scroller mScroller;
	private int mMaxX;
	private int mMinX;
	private int mMaxY;
	private int mMinY;
	
	public VFTableView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public VFTableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public VFTableView(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		mPaint = new Paint();
		mScroller = new Scroller(this.getContext());
		mMinX = 0;
		mMinY = 0;
		mMaxX = 800;
		mMaxY = 1500;
	}

	private List<Unit> units;
	private String[] headers;
	private List<String[]> data;
	
	public void setHeader(String[] headers) {
		this.headers = headers;
	}
	
	public void setData(List<String[]> data) {
		this.data = data;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int borderLength = 50;
		for (int i = 0; i <= 1600; i += borderLength) {
			for (int j = borderLength; j <= 800; j += borderLength) {
				float textSize = i / borderLength * 5 + j / borderLength + 10;
				float squareLength = textSize * 3 / 4;
				mPaint.setColor(Color.BLACK);
				int left = i;
				float top = j - squareLength;
				float right = i + squareLength;
				int bottom = j;
				canvas.drawRect(left, top, right, bottom, mPaint);
				
				mPaint.setTextSize(textSize);
				mPaint.setColor(Color.RED);
				canvas.drawText("" + (int)textSize, i, j, mPaint);
			}
		}
//		Log.e(TAG, "maxX = " + mMaxX + " maxY = " + mMaxY);
	}

	@Override
	public void computeScroll() {
		Log.d(TAG, "computeScroll1");
		if (mScroller.computeScrollOffset()) {
			Log.d(TAG, "computeScroll2");
		} else {
//			scrollTo(mScroller.getFinalX(), mScroller.getFinalY());
		}
		
	}
	private VelocityTracker mVelocityTracker;
	private float mLastX;
	private float mLastY;
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		
		//手指位置地点
		float x = event.getX();
		float y = event.getY();
		
		
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			//如果屏幕的动画还没结束，你就按下了，我们就结束该动画
			if(mScroller != null){
				if(!mScroller.isFinished()){
					mScroller.abortAnimation();
				}
			}
			mLastX = x;
			mLastY = y;
			break ;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int)(mLastX - x);
			int deltaY = (int)(mLastY - y);
//			Log.e(TAG, "ACTION_MOVE" + this.getScrollX() + " " + this.getScrollY());
			//scrollBy(deltaX, deltaY);
//			mScroller.setFinalX((int)x);
//			mScroller.setFinalY((int)y);
			if (Math.abs(deltaX) < 16 && Math.abs(deltaY) < 16) {
				break;
			}
//			mScroller.abortAnimation();
//			Log.d(TAG, "curX:" + mScroller.getCurrX() + " curY:" + mScroller.getCurrY() + " deltaX:" + deltaX + " deltaY:" + deltaY);
//			mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), deltaX, deltaY);
			int finalX = mScroller.getCurrX() + deltaX;
			int finalY = mScroller.getCurrY() + deltaY;
			if (finalX < mMinX) finalX = mMinX;
			if (finalX > mMaxX) finalX = mMaxX;
			if (finalY < mMinY) finalY = mMinY;
			if (finalY > mMaxY) finalY = mMaxY;
			scrollTo(finalX, finalY);
			mScroller.setFinalX(finalX);
			mScroller.setFinalY(finalY);
			mLastX = x;
			mLastY = y;
			break ;
		case MotionEvent.ACTION_UP:
			final VelocityTracker velocityTracker = mVelocityTracker  ;
			velocityTracker.computeCurrentVelocity(1000, 4000);
			
			int velocityX = (int) velocityTracker.getXVelocity() ;
			
//			Log.e(TAG , "---velocityX---" + velocityX + " x = " + mScroller.getCurrX() + " y = " + mScroller.getCurrY());
			
			//mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), dx, dy);
			
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			
			
			
		    break;
		case MotionEvent.ACTION_CANCEL:
			
			break;
		}
//		invalidate();
		return true ;
	}
}
