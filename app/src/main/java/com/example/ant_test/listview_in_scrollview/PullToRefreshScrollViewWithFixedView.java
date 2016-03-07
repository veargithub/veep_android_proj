package com.example.ant_test.listview_in_scrollview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.OverscrollHelper;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

/**
 * Created by 3020mt on 2015/12/18.
 */
public class PullToRefreshScrollViewWithFixedView extends PullToRefreshScrollView{
    public PullToRefreshScrollViewWithFixedView(Context context) {
        super(context);
    }

    public PullToRefreshScrollViewWithFixedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullToRefreshScrollViewWithFixedView(Context context, Mode mode) {
        super(context, mode);
    }

    public PullToRefreshScrollViewWithFixedView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }

    ScrollViewWithFixedView scrollView;

    @Override
    protected ScrollView createRefreshableView(Context context, AttributeSet attrs) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            scrollView = new InternalScrollViewSDK9(context, attrs);
        } else {
            scrollView = new ScrollViewWithFixedView(context, attrs);
        }

        scrollView.setId(com.handmark.pulltorefresh.library.R.id.scrollview);
        return scrollView;
    }

    public void setViews(View v1, View v2) {
        scrollView.setViews(v1, v2);
    }

    @TargetApi(9)
    final class InternalScrollViewSDK9 extends ScrollViewWithFixedView {

        public InternalScrollViewSDK9(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX,
                                       int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

            final boolean returnValue = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX,
                    scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);

            // Does all of the hard work...
            OverscrollHelper.overScrollBy(PullToRefreshScrollViewWithFixedView.this, deltaX, scrollX, deltaY, scrollY,
                    getScrollRange(), isTouchEvent);

            return returnValue;
        }

        /**
         * Taken from the AOSP ScrollView source
         */
        private int getScrollRange() {
            int scrollRange = 0;
            if (getChildCount() > 0) {
                View child = getChildAt(0);
                scrollRange = Math.max(0, child.getHeight() - (getHeight() - getPaddingBottom() - getPaddingTop()));
            }
            return scrollRange;
        }
    }
}
