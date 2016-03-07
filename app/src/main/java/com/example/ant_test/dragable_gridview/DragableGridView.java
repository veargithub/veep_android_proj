package com.example.ant_test.dragable_gridview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class DragableGridView extends GridView{
	/** this is the view being dragged */
	private View mDraggedView;
	
	/** this is the view may be, but not necessary swapped*/
	private View mSwappedView;
	
	/** mDragedView's mirror, created by WindowManager */
	private View mMirrorView1;
	
	/** mSwapedView's mirror, created by WindowManager */
	private View mMirrorView2;
	
	private WindowManager mWindowManager;
	
	/** mDragedView's LayoutParams*/
	private WindowManager.LayoutParams mWlp1;
	
	/** mSwapedView's LayoutParams*/
	private WindowManager.LayoutParams mWlp2;
	
	/** the status's height*/
	private int mStatusHeight;
	
	/** mDragedView's index of this gridview*/
	private int mDraggedPosition = -1;
	
	/** mSwapedView's index of this gridview*/
	private int mSwappedPosition = -1;

	private int mPoint2ViewLeft;
	
	private int mPoint2ViewTop;

	private boolean isDragable = false;
	
	private final int LONG_CLICK_INTERVAL = 500;
	
	private final int WHAT_LONG_CLICK = 1;
	
	private OnSwapListener mOnSwapListener;
	
	public void setOnSwapListener(OnSwapListener listener) {
		this.mOnSwapListener = listener;
	}
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case WHAT_LONG_CLICK:
				isDragable = true;
				if (mDraggedView != null) {
					mDraggedView.setVisibility(View.INVISIBLE);
					int[] location = getAbsoluteCoordinate(mDraggedView);
					mDraggedView.getLocationOnScreen(location);
					mMirrorView1 = createMirror(mDraggedView, mWlp1, location[0], location[1] - mStatusHeight);
				}
				break;
			}
		}
		
	};

	public DragableGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DragableGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DragableGridView(Context context) {
		super(context);
		init();
	}
	
	public void init() {
		mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		mStatusHeight = getStatusHeight(getContext());
		mWlp1 = new WindowManager.LayoutParams();
		mWlp2 = new WindowManager.LayoutParams(); 
		initWLP(mWlp1);
		initWLP(mWlp2);
	}
	
	private void initWLP(WindowManager.LayoutParams wlp) {
		wlp.format = PixelFormat.TRANSLUCENT;
		wlp.gravity = Gravity.TOP | Gravity.LEFT;
		wlp.alpha = 0.50f;
		wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;  
		wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;  
		wlp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
	}

	/**
	 * get the status bar's height of the device
	 * @param context
	 * @return
	 */
	private int getStatusHeight(Context context){  
        int statusHeight = 0;  
        Rect localRect = new Rect();
        ((Activity)getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);  
        statusHeight = localRect.top;  
        if (0 == statusHeight) {  
            Class<?> localClass;
            try {  
                localClass = Class.forName("com.android.internal.R$dimen");  
                Object localObject = localClass.newInstance();  
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());  
                statusHeight = context.getResources().getDimensionPixelSize(i5);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }   
        }  
        return statusHeight;  
    }
	
	/**
	 * get the target view's absolute coordinate
	 * @param view target view
	 * @return
	 */
	private int[] getAbsoluteCoordinate(View view) {
		int[] coor = new int[2];
		view.getLocationOnScreen(coor);
		return coor;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int x = (int)ev.getX();
			int y = (int)ev.getY();
			mDraggedPosition = pointToPosition(x, y);
			if (mDraggedPosition == AdapterView.INVALID_POSITION) break;
			mHandler.sendEmptyMessageDelayed(WHAT_LONG_CLICK, LONG_CLICK_INTERVAL);
			mDraggedView = this.getChildAt(mDraggedPosition - this.getFirstVisiblePosition());
			mPoint2ViewLeft = x - mDraggedView.getLeft();
			mPoint2ViewTop = y - mDraggedView.getTop();
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			mHandler.removeMessages(WHAT_LONG_CLICK);
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isDragable && this.mMirrorView1 != null) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_MOVE:
				int x1 = (int)ev.getRawX();
				int y1 = (int)ev.getRawY();
				int x2 = (int)ev.getX();
				int y2 = (int)ev.getY();
				onItemDragged(x1, y1, x2, y2);
				break;
			case MotionEvent.ACTION_UP:
				isDragable = false;
				onStopDrag();
				break;
			}
			return true;
		} else {
			return super.onTouchEvent(ev);
		}
	}
	/**
	 * create a mirror of some view with parameters 
	 * @param prototype the original view will be mirrored
	 * @param wlp the mirror's LayoutParams object
	 * @param x the mirror's x-coordination
	 * @param y the mirror's y-coordination relative the screen except status-bar
	 * @return mirror view
	 */
	private View createMirror(View prototype, WindowManager.LayoutParams wlp, int x, int y) {
		if (prototype == null) return null;
		prototype.setDrawingCacheEnabled(true);
		Bitmap bitmap = prototype.getDrawingCache();
		wlp.x = x;
		wlp.y = y;
		Drawable drawable = new BitmapDrawable(getResources(), bitmap);
		View mirror = new ImageView(getContext());
		mirror.setBackgroundDrawable(drawable);
		mWindowManager.addView(mirror, wlp);
		return mirror;
	}
	
	private void onStopDrag() {
		if (mDraggedPosition != -1 && mSwappedPosition != -1 && this.mOnSwapListener != null) {
			this.mOnSwapListener.onSwap(mDraggedPosition, mSwappedPosition);
		}
		recoverDraggedView();
		recoverSwappedView();
	}
	
	/**
	 * recover the dragged view and it's mirror's status
	 */
	private void recoverDraggedView() {
		if (mDraggedView != null) mDraggedView.setVisibility(View.VISIBLE);
		if (mMirrorView1 != null) {
			mWindowManager.removeView(mMirrorView1);
			mMirrorView1 = null;
		}
		mDraggedPosition = -1;
	}
	
	/**
	 * recover the swapped view and it's mirror's status
	 */
	private void recoverSwappedView() {
		if (mSwappedView != null) mSwappedView.setVisibility(View.VISIBLE);
		if (mMirrorView2 != null) {
			mWindowManager.removeView(mMirrorView2);
			mMirrorView2 = null;
		}
		mSwappedPosition = -1;
	}
	
	private void onItemDragged(int x1, int y1, int x2, int y2) {
		if (mMirrorView1 != null) {
			int left = x1 - mPoint2ViewLeft;
			int top = y1 - mPoint2ViewTop - mStatusHeight;
			mWlp1.x = left;
			mWlp1.y = top;
			mWindowManager.updateViewLayout(mMirrorView1, mWlp1);
			onItemSwapped(x2, y2);
		}
	}
	
	private void onItemSwapped(int x, int y) {
		int newPosition = pointToPosition(x, y);
		Log.d(">>>>>>>>", "newPosition:" + newPosition);
		if (newPosition != AdapterView.INVALID_POSITION && newPosition != mDraggedPosition && newPosition != mSwappedPosition) {
			recoverSwappedView();
			mSwappedPosition = newPosition;
			this.mSwappedView = this.getChildAt(mSwappedPosition - this.getFirstVisiblePosition());
			int[] location = this.getAbsoluteCoordinate(this.mSwappedView);
			mSwappedView.setVisibility(View.INVISIBLE);
			mMirrorView2 = this.createMirror(mSwappedView, mWlp2, location[0], location[1] - mStatusHeight);
		} else {
			
		}
	}
	
	public interface OnSwapListener {
		public void onSwap(int draggedPosition, int swappedPosition);
	}
}
