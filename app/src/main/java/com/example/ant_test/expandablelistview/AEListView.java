package com.example.ant_test.expandablelistview;

import com.example.ant_test.expandablelistview.AEListAdapter.ViewInfo;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.AbsListView;

public class AEListView extends ExpandableListView {
	private View header;
	
	public AEListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public AEListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public AEListView(Context context) {
		super(context);
		init();
	}
	
	public void init() {
		setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				//System.out.println("first:" + firstVisibleItem + " visible:" + visibleItemCount + " total:" + totalItemCount);
				ExpandableListAdapter adapter = getExpandableListAdapter();
				if (adapter != null && adapter.getGroupCount() > 0) {
					View first = view.getChildAt(0);
					ViewInfo tag = (ViewInfo)first.getTag();
					header = adapter.getGroupView(tag.groupPosition, isGroupExpanded(tag.groupPosition), header, AEListView.this);
					//measureHeader();
					View next = null;
					int y = 0;
					if (tag.childPosition == -1) {
						if (!isGroupExpanded(tag.groupPosition)) {
							next = view.getChildAt(1);
						}
					} else {
						int count = adapter.getChildrenCount(tag.groupPosition);
						next = view.getChildAt(count - tag.childPosition); 
					}
					if (next != null) {
						int top = next.getTop();
						int height = header.getMeasuredHeight();
						if (top < height) {
							y = top - height;
						}
					}
					//System.out.println("header.getMeasuredWidth():" + header.getMeasuredWidth());
					header.layout(0, y, header.getMeasuredWidth(), header.getMeasuredHeight());
				} 
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		System.out.println("MeasureSpec:" + MeasureSpec.toString(widthMeasureSpec));
		//System.out.println("MeasureSpec:" + widthMeasureSpec + " " + MeasureSpec.getMode(widthMeasureSpec) + " " + MeasureSpec.getSize(widthMeasureSpec));
		if (header != null) {
			measureChild(header, widthMeasureSpec, heightMeasureSpec);
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (null != header) {
			drawChild(canvas, header, getDrawingTime());
		}
	}
 }
