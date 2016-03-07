package com.example.ant_test.sliding_menu;

import com.example.ant_test.sliding_menu.impl.SlidingMenuTransformer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

/**
 * @Title: SlidingMenuLayout.java
 * @Package com.example.ant_test.sliding_menu
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-8-22 下午2:45:41
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class SlidingMenuLayout extends RelativeLayout{
	private SlidingMenuView menu;//下面的view
	private SlidingMenuAbovePanel panel;//上面的view

	public SlidingMenuLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SlidingMenuLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SlidingMenuLayout(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		//上下2个view都是match_parent,所以用一个layout param 就够了
		LayoutParams menuAndPanelParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		menu = new SlidingMenuView(context);
		panel = new SlidingMenuAbovePanel(context);
		menu.setPanel(panel);
		panel.setMenu(menu);
		
		this.addView(menu, menuAndPanelParams);
		this.addView(panel, menuAndPanelParams);//把上下2个view先后add进来，先下面的，再上面的
		
	}
	
	public void setPanel(int resId) {
		setPanel(LayoutInflater.from(getContext()).inflate(resId, null));
	}
	
	public void setPanel(View view) {
		if (view == null) {
			throw new IllegalArgumentException("panel's view of the sliding menu must not be null");
		}
		panel.setContent(view);
	}
	
	public void setMenu(int resId) {
		setMenu(LayoutInflater.from(getContext()).inflate(resId, null));
	}
	
	public void setMenu(View view) {
		if (view == null) {
			throw new IllegalArgumentException("menu's view of the sliding menu must not be null");
		}
		menu.setContent(view);
	}
	
	public void setMenuLeftOffsetScale(float f) {
		menu.setLeftOffsetScale(f);
	}
	/**
	 * 设置这个控件是否可滑动
	 * @param b 如果为false则这个控件无法滑动，但也不消费这次touch
	 */
	public void enableSliding(boolean b1, boolean b2) {
		panel.setSlidingEnabled(b1);
		menu.setSlidingEnabled(b2);
	}
	
	public void setMenuWidthOffset(int offset) {
		menu.setWidthOffset(offset);
	}
	
	public void setMenuWidthOffsetRes(int resId) {
		menu.setWidthOffset(getContext().getResources().getDimensionPixelOffset(resId));
	}

	public void setMenuTransFormer(SlidingMenuTransformer transformer) {
		menu.setTransformer(transformer);
	}
	
	public void setPanelTransFormer(SlidingMenuTransformer transformer) {
		panel.setTransformer(transformer);
	}
}
