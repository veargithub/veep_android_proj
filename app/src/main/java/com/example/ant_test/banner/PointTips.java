package com.example.ant_test.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.ant_test.R;

public class PointTips extends LinearLayout{
	private int resPointNormal;//default point's resource id
	private int resPointSelected;//default selected point's resource id
	private int pointNumber;//the count of point
	private int current;//the current selected point's index
	private View[] pointViews;
	private int pointHeight;//point's diameter
	private int pointMargin;//the margin of points

    public PointTips(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}

	public PointTips(Context context) {
		super(context);
		init(null);
	}
	
	private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PointTips);
            if (a.hasValue(R.styleable.PointTips_pointNormal)) {
                resPointNormal = a.getResourceId(R.styleable.PointTips_pointNormal, 0);
            }
            if (a.hasValue(R.styleable.PointTips_pointSelected)) {
                resPointSelected = a.getResourceId(R.styleable.PointTips_pointSelected, 0);
            }
            if (a.hasValue(R.styleable.PointTips_pointHeight)) {
                pointHeight = a.getDimensionPixelSize(R.styleable.PointTips_pointHeight, 0);
            }
            if (a.hasValue(R.styleable.PointTips_pointMargin)) {
                pointMargin = a.getDimensionPixelSize(R.styleable.PointTips_pointMargin, 0);
            }
            if (a.hasValue(R.styleable.PointTips_pointNumber)) {
                pointNumber = a.getInteger(R.styleable.PointTips_pointNumber, 0);
            }
            current = 0;
//            resPointNormal = R.drawable.point_normal;
//            resPointSelected = R.drawable.point_selected;
            setPointNumber(pointNumber);
            a.recycle();
        }
	}
	/**
	 * 设置总共有多少个圆圈
	 * @param number
	 */
	public void setPointNumber(int number) {
		if (number < 0) return;
		this.pointNumber = number;
		this.removeAllViews();
		pointViews = new View[number];
		for (int i = 0; i < number; i++) {
			View view = new View(this.getContext());
			view.setBackgroundResource(resPointNormal);
			LayoutParams lp = new LayoutParams(pointHeight, pointHeight);
			lp.leftMargin = pointMargin;
			view.setLayoutParams(lp);
			pointViews[i] = view;
			this.addView(view);
		}
        setCurrent(current);
	}

    public int getPointNumber() {
        return pointNumber;
    }
	/**
	 * set which point is selected
	 * @param index
	 */
	public void setCurrent(int index) {
		if (index >= pointNumber || index < 0) return;
        if (current >= pointViews.length || current < 0) return;
		pointViews[current].setBackgroundResource(resPointNormal);
		current = index;
		pointViews[current].setBackgroundResource(resPointSelected);
	}

    public int getCurrent() {
        return current;
    }

	public void setResPointNormal(int resPointNormal) {
		this.resPointNormal = resPointNormal;
	}

	public void setResPointSelected(int resPointSelected) {
		this.resPointSelected = resPointSelected;
	}

	public void setPointHeight(int pointHeight) {
		this.pointHeight = pointHeight;
	}

	public void setPointMargin(int pointMargin) {
		this.pointMargin = pointMargin;
	}

}
