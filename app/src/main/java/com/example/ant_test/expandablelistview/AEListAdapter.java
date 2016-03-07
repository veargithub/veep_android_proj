package com.example.ant_test.expandablelistview;

import com.example.ant_test.R;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AEListAdapter extends BaseExpandableListAdapter{
	private Context context;
	
	public AEListAdapter(Context context) {
		this.context = context;
	}

	private String[] groups;

	private String[][] childs;
	
	public void setGroups(String[] groups) {
		this.groups = groups;
	}
	
	public void setChilds(String[][] childs) {
		this.childs = childs;
	}
	
	//自己定义一个获得文字信息的方法
	TextView getTextView() {
		AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT, 64);
		TextView textView = new TextView(
				context);
		textView.setLayoutParams(lp);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setPadding(36, 0, 0, 0);
		textView.setTextSize(20);
		textView.setTextColor(Color.BLACK);
		return textView;
	}

	
	//重写ExpandableListAdapter中的各个方法
	@Override
	public int getGroupCount() {
		return groups.length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups[groupPosition];
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childs[groupPosition].length;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childs[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.group, parent, false);
		}
		TextView textView = (TextView)convertView.findViewById(R.id.tv1);
		textView.setText(getGroup(groupPosition).toString());
		ViewInfo vi = new ViewInfo(groupPosition, -1);
		convertView.setTag(vi);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LinearLayout ll = new LinearLayout(
				context);
		ll.setOrientation(0);
		

		TextView textView = getTextView();
		textView.setText(getChild(groupPosition, childPosition)
				.toString());
		ll.addView(textView);
		ViewInfo vi = new ViewInfo(groupPosition, childPosition);
		ll.setTag(vi);
		return ll;
	}

	@Override
	public boolean isChildSelectable(int groupPosition,
			int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	public View getFixedView(int first) {
		return null;//TODO
	}
	
	class ViewInfo {
		int groupPosition = -1;
		int childPosition = -1;
		
		public ViewInfo(int g, int c) {
			this.groupPosition = g;
			this.childPosition = c;
		}
	}
}
