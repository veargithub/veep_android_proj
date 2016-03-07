package com.example.ant_test.pull2refresh.listener;

import android.view.View;
import com.example.ant_test.pull2refresh.core.Pull2RefreshBase;

/**
 * 区分上拉和下拉的回调
 */
public interface OnRefreshListener2<V extends View> {
	
	public void onPullDownToRefresh(final Pull2RefreshBase<V> refreshView);
	
	public void onPullUpToRefresh(final Pull2RefreshBase<V> refreshView);
	
}
