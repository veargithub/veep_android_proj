package com.example.ant_test.sliding_menu.impl;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;

/**
 * @Title: SlidingMenuTransformerBuilder.java
 * @Package com.example.ant_test.sliding_menu.impl
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-4 下午2:38:11
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class SlidingMenuTransformerBuilder {
	private static final String TAG = "SlidingMenuTransformerBuilder";
	public static final SlidingMenuTransformer CommonMenuTransformer() {
		return new SlidingMenuTransformer() {

			@Override
			public void transform(View view, Canvas canvas, float scale, float alpha) {
				canvas.scale(scale, scale, view.getWidth()/2, view.getHeight()/2);
				view.setAlpha(alpha);
				Log.d(TAG, "menu's height of canvas:" + canvas.getHeight() + " " + view.getHeight());
			}
			
		};
	}
	
	public static final SlidingMenuTransformer CommonPanelTransformer() {
		return new SlidingMenuTransformer() {

			@Override
			public void transform(View view, Canvas canvas, float scale, float alpha) {
				canvas.scale(scale, scale, 
						/*canvas.getWidth()/2,*/
						0,
						view.getHeight()/2);//坑爹:canvas的height居然和view的height不一样
				Log.d(TAG, "panel's height of canvas:" + canvas.getHeight() + " " + view.getHeight());
			}
			
		};
	}

}
