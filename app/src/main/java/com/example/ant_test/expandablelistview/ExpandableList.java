package com.example.ant_test.expandablelistview;

import com.example.ant_test.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandableList extends Activity{
	AEListAdapter adapter2;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.expandable_list_main);
		adapter2 = new AEListAdapter(this);
		String[] groups = new String[] {"linux", "windows", "info", "histroy"};
		String[][] childs = new String[][] {
				{"linux1", "linux1", "linux1", "linux1","linux1","linux1","linux1","linux1","linux1","linux1"},
				{"windows1","windows1","windows1","windows1","windows1","windows1","windows1","windows1","windows1"},
				{"info1","info1","info1","info1","info1","info1","info1","info1","info1","info1","info1"},
				{"history","history","history","history","history","history","history","history","history"}
		};
		adapter2.setGroups(groups);
		adapter2.setChilds(childs);
		AEListView expandableListView = (AEListView) findViewById(R.id.list);
		expandableListView.setAdapter(adapter2);
		
		
		//设置item点击的监听器
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				Toast.makeText(
						ExpandableList.this,
						"你点击了" + adapter2.getChild(groupPosition, childPosition),
						Toast.LENGTH_SHORT).show();
				String[] groups = new String[] {"linux2", "windows2", "info2", "histroy2"};
				adapter2.setGroups(groups);
				adapter2.notifyDataSetChanged();

				return false;
			}
		});
	}


}
