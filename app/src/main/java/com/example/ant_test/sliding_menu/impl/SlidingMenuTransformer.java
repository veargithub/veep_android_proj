package com.example.ant_test.sliding_menu.impl;

import android.graphics.Canvas;
import android.view.View;

/**
 * @Title: SlidingMenuTransformer.java
 * @Package com.example.ant_test.sliding_menu.impl
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-4 下午2:12:07
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public interface SlidingMenuTransformer {
	public void transform(View view, Canvas canvas, float scale, float alpha);

}
