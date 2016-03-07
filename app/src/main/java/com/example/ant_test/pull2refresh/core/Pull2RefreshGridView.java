package com.example.ant_test.pull2refresh.core;



import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;



/**
 * @Title: Pull2RefreshGridView.java
 * @Package com.example.ant_test.pull2refresh.core
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2015-1-14 下午4:33:18
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class Pull2RefreshGridView extends Pull2RefreshAdapterViewBase<GridView>{

	public Pull2RefreshGridView(Context context) {
		super(context);
	}

	public Pull2RefreshGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Pull2RefreshGridView(Context context, Pull2RefreshMode mode) {
		super(context, mode);
	}




	@Override
	protected final GridView createRefreshableView(Context context, AttributeSet attrs) {
		final GridView gv;
		
			gv = new InternalGridView(context, attrs);
		

		// Use Generated ID (from res/values/ids.xml)
		//gv.setId(R.id.gridview);
		return gv;
	}

	class InternalGridView extends GridView {

		public InternalGridView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			Pull2RefreshGridView.this.setEmptyView(emptyView);
		}

	
	}

	
}
