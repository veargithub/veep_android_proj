package com.example.ant_test.pull2refresh.core;

import android.content.Context;
import android.content.res.TypedArray;



/**
 * @Title: AnimationStyle.java
 * @Package com.example.ant_test.pull2refresh.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-12-2 下午3:37:11
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public enum AnimationStyle {

	ROTATE,

	FLIP;

	public static AnimationStyle getDefault() {
		return ROTATE;
	}

	static AnimationStyle convertIntToValue(int modeInt) {
		switch (modeInt) {
			case 0x0:
			default:
				return ROTATE;
			case 0x1:
				return FLIP;
		}
	}

	public HeaderFooterLayout createLoadingLayout(Context context, Pull2RefreshMode mode, TypedArray attrs) {
		switch (this) {
			case ROTATE:
			default:
				return new RotateHeaderFooterLayout(context, mode, attrs);
			case FLIP://TODO
				//return new FlipLoadingLayout(context, mode, scrollDirection, attrs);
				return null;
		}
	}
}
