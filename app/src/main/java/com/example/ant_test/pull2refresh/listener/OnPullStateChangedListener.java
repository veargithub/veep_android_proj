package com.example.ant_test.pull2refresh.listener;

import com.example.ant_test.pull2refresh.core.Pull2RefreshBase;
import com.example.ant_test.pull2refresh.core.Pull2RefreshMode;
import com.example.ant_test.pull2refresh.core.Pull2RefreshState;
import android.view.View;

/**
 * 当下拉刷新状态改变时用户自定义的回调函数
 */
public interface OnPullStateChangedListener<V extends View> {
	/**
	 * 
	 * @param refreshView 除去header和footer的中间的那个view
	 * @param state 要成为什么状态
	 * @param direction 上拉or下拉
	 */
	public void onChange(final Pull2RefreshBase<V> refreshView, Pull2RefreshState state, Pull2RefreshMode direction);
}
