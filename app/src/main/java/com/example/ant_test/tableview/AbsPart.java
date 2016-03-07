package com.example.ant_test.tableview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

public abstract class AbsPart extends View{
	protected Scroller mScroller;
	protected int mMaxX;
	protected int mMinX;
	protected int mMinY;
	protected int mMaxY;
	protected int mCurrentX;
	protected int mCurrentY;
	

	public AbsPart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public AbsPart(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AbsPart(Context context) {
		super(context);
		init();
	}
	public void init() {
		
	}
	public abstract boolean canVerticalSlide();
	public abstract boolean canHorizontalSlide();
	
	public void scrollBy(int x, int y, Orientation o) {
		Log.d("<><><>", this.mScroller.getCurrX() + " " + this.mScroller.getCurrY());
		if (o == Orientation.VERTICAL) {
			if (canVerticalSlide()) {
				this.scrollBy(0, y);
			}
		} else if (o == Orientation.HORIZONTAL) {
			if (canHorizontalSlide()) {
				this.scrollBy(x, 0);
			}
		}
	}

	enum Orientation {
		VERTICAL, HORIZONTAL
	}
	
	public void setScroller(Scroller scroller) {
		this.mScroller = scroller;
	}
}
