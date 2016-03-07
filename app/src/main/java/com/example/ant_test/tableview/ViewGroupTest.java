package com.example.ant_test.tableview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.Scroller;

import com.example.ant_test.R;
import com.example.ant_test.tableview.AbsPart.Orientation;

public class ViewGroupTest extends ViewGroup{
	private static final String TAG = "ViewGroupTest";
	private AbsPart tl;
	private AbsPart tr;
	private AbsPart bl;
	private AbsPart br;

	public ViewGroupTest(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ViewGroupTest(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ViewGroupTest(Context context) {
		super(context);
		init();
	}

	private void init() {
		mScroller = new Scroller(getContext());
		
		View1 v1 = new View1(getContext());
		ViewGroup.LayoutParams lp1 = new LayoutParams(100, 50);
		v1.setLayoutParams(lp1);
		v1.setBackgroundResource(R.color.blue_clicked);
		addView(v1, Position.TOP_LEFT);
		
		View2 v2 = new View2(getContext());
		ViewGroup.LayoutParams lp2 = new LayoutParams(LayoutParams.FILL_PARENT, 50);
		v2.setLayoutParams(lp2);
		v2.setBackgroundResource(R.color.red);
		addView(v2, Position.TOP_RIGHT);
		
		View3 v3 = new View3(getContext());
		ViewGroup.LayoutParams lp3 = new LayoutParams(100, LayoutParams.FILL_PARENT);
		v3.setLayoutParams(lp3);
		v3.setBackgroundResource(R.color.yellow);
		addView(v3, Position.BOTTOM_LEFT);
		
		View4 v4 = new View4(getContext());
		ViewGroup.LayoutParams lp4 = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		v4.setLayoutParams(lp4);
		v4.setBackgroundResource(R.color.green);
		addView(v4, Position.BOTTOM_RIGHT);
		
		v2.setScroller(mScroller);
		v3.setScroller(mScroller);
		v4.setScroller(mScroller);
		
		
	}
	
	public void addView(AbsPart child, Position P) {
		addView(child);
		switch (P) {
		case TOP_LEFT:this.tl = child;break;
		case TOP_RIGHT:this.tr = child;break;
		case BOTTOM_LEFT:this.bl = child;break;
		case BOTTOM_RIGHT:this.br = child;break;
		default:throw new IllegalArgumentException("illegal position");
		}
	}
	
	int w;
	int h;
	int tlW;
	int tlH;
	int trW;
	int trH;
	int blW;
	int blH;
	int brW;
	int brH;
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		//Log.d("<><><>", "widthMode:" + widthMode + " widthSize:" + widthSize + " heightMode:" + heightMode + " heightSize:" + heightSize);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		w = this.getMeasuredWidth();
		h = this.getMeasuredHeight();
		
		this.measureChild(tl, w, h);
		tlW = tl.getMeasuredWidth();
		tlH = tl.getMeasuredHeight();
		
		this.measureChild(tr, MeasureSpec.makeMeasureSpec(w - tlW, MeasureSpec.EXACTLY), tlH);
		trW = tr.getMeasuredWidth();
		trH = tr.getMeasuredHeight();
		
		this.measureChild(bl, tlW, MeasureSpec.makeMeasureSpec(h - tlH, MeasureSpec.EXACTLY));
		blW = bl.getMeasuredWidth();
		blH = bl.getMeasuredHeight();
		
		this.measureChild(br, MeasureSpec.makeMeasureSpec(trW, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(blH, MeasureSpec.EXACTLY));
		brW = br.getMeasuredWidth();
		brH = br.getMeasuredHeight();
//		Log.d("<><><>", tr.getMeasuredWidth() + " " + tr.getMeasuredHeight());
		
//		trW = w - tlW;
//		trH = tlH;
//		blW = tlW;
//		blH = h - tlH;
//		brW = trW;
//		brH = blH;
		
//		tlW = tl.getMeasuredWidth();
//		tlH = tl.getMeasuredHeight();
//		trW = tr.getMeasuredWidth();
//		trH = tr.getMeasuredHeight();
//		blW = bl.getMeasuredWidth();
//		blH = bl.getMeasuredHeight();
//		brW = br.getMeasuredWidth();
//		brH = br.getMeasuredHeight();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		Log.d("measure", tlW + " " + tlH + " " + trW + " " + blH);
		setChildFrame(tl, 0, 0, tlW, tlH);
		setChildFrame(tr, tlW, 0, trW, trH);
		setChildFrame(bl, 0, tlH, blW, blH);
		setChildFrame(br, blW, trH, brW, brH);
//		int w  = MeasureSpec.makeMeasureSpec(380, MeasureSpec.EXACTLY);
//		getChildMeasureSpec(w, 0, -1);
	}

	private void setChildFrame(View child, int left, int top, int width, int height) {        
        child.layout(left, top, left + width, top + height);
    }
	
	enum Position {
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT
	}
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	private float mLastX;
	private float mLastY;
	private int mCurrX;
	private int mCurrY;
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
			
			if (Math.abs(deltaX) < 30 && Math.abs(deltaY) < 30) {
				break;
			}
			
			Log.d(TAG, "mCurrX = " + mCurrX + " deltaX = " + deltaX + " x = " + x + " y = " + y + " mLastX = " + mLastX + " mLastY = " + mLastY );
			Orientation o;
			if (Math.abs(deltaX) > Math.abs(deltaY)) {
				if (deltaX < 0) {//向右
					if (mCurrX + deltaX < 0) {
						deltaX = -mCurrX;
					}
					mCurrX = mCurrX + deltaX;
				} else {//向左
					
					mCurrX = mCurrX + deltaX;
				}
				o = Orientation.HORIZONTAL;
			} else {
				if (deltaY < 0) {//向下
					if (mCurrY + deltaY < 0) {
						deltaY = -mCurrY;
					}
					mCurrY += deltaY;
				} else {//向上
					
					mCurrY += deltaY;
				}
				o = Orientation.VERTICAL;
			}
			
			this.tr.scrollBy(deltaX, deltaY, o);
			this.br.scrollBy(deltaX, deltaY, o);
			this.bl.scrollBy(deltaX, deltaY, o);
			
//			mScroller.startScroll((int)mLastX, (int)mLastY, -deltaX, -deltaY);
			
			mLastX = x ;
			mLastY = y;
			break ;
		case MotionEvent.ACTION_UP:
//			final VelocityTracker velocityTracker = mVelocityTracker  ;
//			velocityTracker.computeCurrentVelocity(1000, 4000);
//			
//			int velocityX = (int) velocityTracker.getXVelocity() ;
//			
//			Log.e(TAG , "---velocityX---" + velocityX + " x = " + mScroller.getCurrX() + " y = " + mScroller.getCurrY());
//			
//			//mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), dx, dy);
//			
//			if (mVelocityTracker != null) {
//				mVelocityTracker.recycle();
//				mVelocityTracker = null;
//			}
			
		    break;
		case MotionEvent.ACTION_CANCEL:
			
			break;
		}
//		invalidate();
		return true ;
	}

	@Override
	protected void onFinishInflate() {
		Log.d(TAG, "this.height:" + this.getHeight());
	}
	
	
}
