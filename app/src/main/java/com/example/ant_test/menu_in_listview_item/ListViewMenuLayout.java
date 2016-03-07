package com.example.ant_test.menu_in_listview_item;

import android.content.Context;
import android.os.StrictMode;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ant_test.R;
import com.example.ant_test.tableview.AbsPart;
import com.example.veep.vcomm.log.Vlog;

/**
 * Created by 3020mt on 2016/2/19.
 * listview侧滑删除
 */
public class ListViewMenuLayout extends FrameLayout {
    static final String TAG = "ListViewMenuLayout";
    View tipsView;
    View contentView;

    View menuView;

    ScrollerCompat scrollerCompat;

    LinearLayout containerLayout;

    public ListViewMenuLayout(Context context) {
        super(context);
        init();
    }

    public ListViewMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ListViewMenuLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        Vlog.getLog4j(TAG).debug("INIT");
        scrollerCompat = ScrollerCompat.create(getContext(), new DecelerateInterpolator(2.0F));

//        containerLayout = new LinearLayout(getContext());
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 350);
//        containerLayout.setLayoutParams(lp);
//        containerLayout.setGravity(LinearLayout.HORIZONTAL);
//        containerLayout.setBackgroundResource(R.color.blue);
//        this.addView(containerLayout);

//        TextView tv = new TextView(getContext());
//        LinearLayout.LayoutParams lptv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        tv.setLayoutParams(lptv);
//        containerLayout.addView(tv);
//        tv.setText("111");

        View v3 = new View(getContext());
        ViewGroup.LayoutParams lp3 = new ViewGroup.LayoutParams(50, 350);
        v3.setLayoutParams(lp3);
        v3.setBackgroundResource(R.color.yellow);
        this.addView(v3);
        tipsView = v3;

        View v4 = new View(getContext());
        ViewGroup.LayoutParams lp4 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 350);
        v4.setLayoutParams(lp4);
        v4.setBackgroundResource(R.color.green);
        this.addView(v4);
        contentView = v4;

        Button v2 = new Button(getContext());
        ViewGroup.LayoutParams lp2 = new ViewGroup.LayoutParams(100, 350);
        v2.setLayoutParams(lp2);
        v2.setBackgroundResource(R.color.red);
        this.addView(v2);
        menuView = v2;

        contentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "contentView", Toast.LENGTH_SHORT).show();
            }
        });

        menuView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "menuView", Toast.LENGTH_SHORT).show();
            }
        });

    }

//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int width = this.getMeasuredWidth();
//        LinearLayout.LayoutParams lp4 = new LinearLayout.LayoutParams(width, 350);
//        contentView.setLayoutParams(lp4);
//        super.onLayout(changed, l, t, r, b);
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int tipsViewWidth = tipsView.getMeasuredWidth();
        int tipsViewHeight = tipsView.getMeasuredHeight();
        tipsView.layout(0, 0, tipsViewWidth, tipsViewHeight);

        int contentViewWidth = contentView.getMeasuredWidth();
        int contentViewHeight = contentView.getMeasuredHeight();
        contentView.layout(tipsViewWidth, 0 , contentViewWidth + tipsViewWidth, contentViewHeight);

        int menuViewWidth = menuView.getMeasuredWidth();
        int menuViewHeight = menuView.getMeasuredHeight();
        menuView.layout(contentViewWidth + tipsViewWidth, 0, contentViewWidth + tipsViewWidth + menuViewWidth, menuViewHeight);

        if (tipsView != null) {
            mCurrX = 0;
        }
    }

    float mLastX, mLastY;
    int mCurrX = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Vlog.getLog4j(TAG).debug("onTouchEvent");
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                finishScrolling();
                mLastX = x;
                mLastY = y;
                break ;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int)(mLastX - x);
                if (mIsBeingDragged) {
                    requestParentDisallowInterceptTouchEvent(true);
                    if (deltaX < 0) {//向右
                        if (mCurrX + deltaX < 0) {//// TODO: 2016/2/22
                            deltaX = -mCurrX;
                        }
                        mCurrX = mCurrX + deltaX;
                    } else {//向左
                        if (mCurrX + deltaX > tipsView.getWidth() + menuView.getWidth()) {
                            deltaX = tipsView.getWidth() + menuView.getWidth() - mCurrX;
                        }
                        mCurrX = mCurrX + deltaX;
                    }
                }
                this.scrollTo(mCurrX, 0);
                mLastX = x ;
                mLastY = y;
                break ;
        }
        return true ;
    }

    boolean mIsBeingDragged = false;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
//            Vlog.getLog4j(TAG).debug("mIsBeingDragged");
            return true;
        }
        float x = ev.getX();
        float y = ev.getY();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                int deltaX = (int)Math.abs(mLastX - x);
                int deltaY = (int)Math.abs(mLastY - y);
                if (deltaX >= 5 && deltaX > deltaY) {// TODO: 2016/2/22
                    mIsBeingDragged = true;
                    mLastX = x;
                    mLastY = y;
                    requestParentDisallowInterceptTouchEvent(true);
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                finishScrolling();
                mLastX = x;
                mLastY = y;
                mIsBeingDragged = false;
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                break;

        }
        return mIsBeingDragged;
    }


    @Override
    public void computeScroll() {
        if (scrollerCompat.computeScrollOffset()) {
            this.scrollTo(scrollerCompat.getCurrX(), scrollerCompat.getCurrY());
        }
    }

    public void finishScrolling() {
        if (!scrollerCompat.isFinished()) {
            scrollerCompat.abortAnimation();
            this.scrollTo(scrollerCompat.getFinalX(), scrollerCompat.getFinalY());
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        mCurrX = x;
    }

    public void reset() {
        int offset = 0;
        if (tipsView != null) {
            offset = tipsView.getWidth();
        }
        this.scrollTo(offset, 0);
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }
}
