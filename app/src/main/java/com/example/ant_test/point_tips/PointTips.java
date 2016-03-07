package com.example.ant_test.point_tips;

import com.example.ant_test.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * @Title: PointTips.java
 * @Package com.example.ant_test.point_tips
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-28 下午2:23:23
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class PointTips extends LinearLayout{
	private int resPointNormal;//默认的圆圈的资源id
	private int resPointSelected;//选中的圆圈的资源id
	private int pointSize;//圆圈的总数
	private int current;//当前哪个圆圈被选中
	private View[] pointViews;
	private int pointHeight;//圆圈的直径
	private int pointMargin;//圆圈之间的margin

	public PointTips(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PointTips(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		resPointNormal = R.drawable.point_normal;
		resPointSelected = R.drawable.point_selected;
		pointSize = 0;
		current = 0;
		pointHeight = 20;
		pointMargin = 25;
	}
	/**
	 * 设置总共有多少个圆圈
	 * @param number
	 */
	public void setPointSize(int number) {
		if (number < 0) return;
		this.pointSize = number;
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
	}
	/**
	 * 设置当前哪个圆圈被选中
	 * @param index
	 */
	public void setCurrent(int index) {
		if (index > pointSize || index < 0) return;
		pointViews[current].setBackgroundResource(resPointNormal);
		current = index;
		pointViews[current].setBackgroundResource(resPointSelected);
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
