package com.example.ant_test.listview_in_scrollview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

/**
 * 自定义scrollview，实现滚动回调
 * @author andy
 *
 */
public class ScrollViewWithFixedView extends ScrollView{

    private View fixedView, copyView;
	
	public ScrollViewWithFixedView(Context context) {
        this(context, null);  
    }  
      
    public ScrollViewWithFixedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);  
    }  
	
	public ScrollViewWithFixedView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
        if (fixedView == null || copyView == null) return;
        Log.d(">>>>", fixedView.getTop() + "|" + getScrollY());
        if (getScrollY() >= fixedView.getTop()) {
            copyView.setVisibility(View.VISIBLE);
        } else {
            copyView.setVisibility(View.GONE);
        }
	}

    public void setViews(View fixedView, View copyView) {
        this.fixedView = fixedView;
        this.copyView = copyView;
    }
}
