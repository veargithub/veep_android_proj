package com.example.ant_test.pull2refresh.core;
/**
 * @Title: Pull2RefreshListLoadMode.java
 * @Package com.example.ant_test.pull2refresh.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2015-1-26 上午10:23:57
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public enum Pull2RefreshStyle {
	
	PULL(0x1),//下拉刷新
	
	AUTO(0x2),//自动刷新
	
	EVENT(0x4);//由事件触发刷新
	
	private int intValue;
	
	Pull2RefreshStyle(int value) {
		this.intValue = value;
	}

	public int getIntValue() {
		return intValue;
	}
	
	public static Pull2RefreshStyle convertIntToValue(int value) {
		for (Pull2RefreshStyle style : Pull2RefreshStyle.values()) {
			if (style.getIntValue() == value) {
				return style;
			}
		}
		return getDefault();
	}
	
	public static Pull2RefreshStyle getDefault() {
		return PULL;
	}
	
	public boolean isPull() {
		return intValue == PULL.getIntValue();
	}
	
	public boolean isAuto() {
		return intValue == AUTO.getIntValue();
	}
	
	public boolean isEvent() {
		return intValue == EVENT.getIntValue();
	}
}
