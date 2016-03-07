package com.example.ant_test.pull2refresh.activity;

import com.example.ant_test.pull2refresh.extra.BounceListView;

import android.app.Activity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * @Title: BounceListViewActivity.java
 * @Package com.example.ant_test.pull2refresh.activity
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2015-1-20 上午10:43:38
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class BounceListViewActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBounceContentView();
	}
	
	private void setBounceContentView() {
		BounceListView blv = new BounceListView(this);
		blv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
		String[] data = new String[30];  
        for (int i = 0; i < data.length; i++) {  
            data[i] = "天天记录 " + i;  
        }  
          
        ArrayAdapter<String> arrayAdapter =   
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, data);  
          
        blv.setAdapter(arrayAdapter);
        this.setContentView(blv);
	}
}
