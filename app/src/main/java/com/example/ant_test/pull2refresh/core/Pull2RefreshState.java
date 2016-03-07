package com.example.ant_test.pull2refresh.core;

/**
 * @Title: Pull2RefreshState.java
 * @Package com.example.ant_test.pull2refresh.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-12-4 下午1:39:34
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public enum Pull2RefreshState {
	/**
	 * 默认状态，即，如果不属于以下任何状态则属于这个状态
	 */
	RESET(0x0),

	/**
	 * 下拉刷新状态
	 */
	PULL_TO_REFRESH(0x1),

	/**
	 * 下拉已足够，释放就刷新的状态
	 */
	RELEASE_TO_REFRESH(0x2),

	/**
	 * 正在刷新状态
	 */
	REFRESHING(0x8),

	/**
	 * 手动刷新状态,指只能通过代码来刷新，而且不能通过触摸，滑动，点击等事件，（调用setRefresh()）
	 */
	MANUAL_REFRESHING(0x9),

	/**
	 * 处于over scrolling状态（不太明白）
	 */
	OVERSCROLLING(0x10);

	static Pull2RefreshState convertIntToValue(final int stateInt) {
		for (Pull2RefreshState value : Pull2RefreshState.values()) {
			if (stateInt == value.getIntValue()) {
				return value;
			}
		}
		return RESET;
	}

	private int mIntValue;

	Pull2RefreshState(int intValue) {
		mIntValue = intValue;
	}

	int getIntValue() {
		return mIntValue;
	}
}
