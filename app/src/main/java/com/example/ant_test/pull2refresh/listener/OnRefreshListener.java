package com.example.ant_test.pull2refresh.listener;

import com.example.ant_test.pull2refresh.core.Pull2RefreshBase;
import android.view.View;

/**
 * 不区分上拉和下拉的简单回调
 */
public interface OnRefreshListener<V extends View> {
	
	public void onRefresh(final Pull2RefreshBase<V> refreshView);
	
}


