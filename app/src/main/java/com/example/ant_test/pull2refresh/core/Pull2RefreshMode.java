package com.example.ant_test.pull2refresh.core;

/**
 * @Title: Pull2RefreshMode.java
 * @Package com.example.ant_test.pull2refresh.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-28 下午3:20:50
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public enum Pull2RefreshMode {
	
	MODE_DISABLE(0x0),
	
	MODE_START(0x1),
	
	MODE_END(0x2),
	
	MODE_BOTH(0x3/*MODE_START.getIntValue() | MODE_END.getIntValue()*/),
	
	//MODE_MANUAL_START(0x4/*MODE_START.getIntValue() | 4*/),
	MODE_MANUAL(0x4);
	//MODE_MANUAL_END(0x5/*MODE_END.getIntValue() | 8*/),
	
	//MODE_MANUAL_BOTH(0x6/*MODE_MANUAL_START.getIntValue() | MODE_MANUAL_END.getIntValue()*/);

	private int mIntValue;
	
	Pull2RefreshMode(int modeInt) {
		mIntValue = modeInt;
	}
	
	static Pull2RefreshMode convertIntToValue(final int modeInt) {
		for (Pull2RefreshMode value : Pull2RefreshMode.values()) {
			if (modeInt == value.getIntValue()) {
				return value;
			}
		}
		return getDefault();
	}

	static Pull2RefreshMode getDefault() {
		return MODE_START;
	}
	
	int getIntValue() {
		return mIntValue;
	}
	
	public boolean showHeaderLoadingLayout() {
		return this == MODE_START || this == MODE_BOTH /*|| this == MODE_MANUAL_START || this == MODE_MANUAL_BOTH */;
	}


	public boolean showFooterLoadingLayout() {
		return this == MODE_END || this == MODE_BOTH || this == MODE_MANUAL /*this == //MODE_MANUAL_END || this == MODE_MANUAL_BOTH */;
	}
	
	boolean permitsPullToRefresh() {//这个refreshview是否可滑
		return !(this == MODE_DISABLE || this == MODE_MANUAL);
	}
}
