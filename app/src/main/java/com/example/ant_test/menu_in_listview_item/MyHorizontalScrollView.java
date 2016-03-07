package com.example.ant_test.menu_in_listview_item;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewParent;
import android.widget.HorizontalScrollView;

/**
 * Created by 3020mt on 2016/2/23.
 */
public class MyHorizontalScrollView extends HorizontalScrollView{
    private static final String TAG = "MyHorizontalScrollView";

    private static final int INVALID_POINTER = -1;
    private boolean mIsBeingDragged = false;
    private int mActivePointerId = INVALID_POINTER;
    private int mLastMotionX;
    private int mTouchSlop = 30;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    break;
                }

                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + activePointerId
                            + " in onInterceptTouchEvent");
                    break;
                }

                final int x = (int) ev.getX(pointerIndex);
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                if (xDiff > mTouchSlop) {
                    mIsBeingDragged = true;
                    mLastMotionX = x;
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                final int x = (int) ev.getX();
//                if (!inChild((int) x, (int) ev.getY())) {
//                    mIsBeingDragged = false;
//                    recycleVelocityTracker();
//                    break;
//                }
                mLastMotionX = x;
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = ev.getActionIndex();
                mLastMotionX = (int) ev.getX(index);
                mActivePointerId = ev.getPointerId(index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        final int action = ev.getAction();

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                if (getChildCount() == 0) {
                    return false;
                }

                mLastMotionX = (int) ev.getX();
                mActivePointerId = ev.getPointerId(0);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                final int activePointerIndex = ev.findPointerIndex(mActivePointerId);
                if (activePointerIndex == -1) {
                    Log.e(TAG, "Invalid pointerId=" + mActivePointerId + " in onTouchEvent");
                    break;
                }

                final int x = (int) ev.getX(activePointerIndex);
                int deltaX = mLastMotionX - x;
                if (!mIsBeingDragged && Math.abs(deltaX) > mTouchSlop) {
                    final ViewParent parent = getParent();
                    mIsBeingDragged = true;
                    if (deltaX > 0) {
                        deltaX -= mTouchSlop;
                    } else {
                        deltaX += mTouchSlop;
                    }
                }
                if (mIsBeingDragged) {
                    mLastMotionX = x;
                    scrollBy(deltaX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsBeingDragged) {
                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsBeingDragged && getChildCount() > 0) {
                    mActivePointerId = INVALID_POINTER;
                    mIsBeingDragged = false;
                }
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
        }
        return true;
    }
}
