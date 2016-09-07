package com.example.ant_test.swiperefresh;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.ant_test.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 3020mt on 2016/5/5.
 */
public class SwipeRefreshActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {

    private ListView lv1;
    private SwipeRefreshLayout swipeRefreshLayout;
    List<Map<String, String>> data;
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_refresh);

        lv1 = (ListView) findViewById(R.id.list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        data = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 20; i++) {
            HashMap hm = new HashMap();
            hm.put("name", i + "");
            data.add(hm);
        }
        adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_1, new String[] {"name"}, new int[] {android.R.id.text1});
        lv1.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.transparent_white);
        swipeRefreshLayout.setSize(0);//只有2种，0->large,1->default
        swipeRefreshLayout.setProgressViewOffset(true, 100, 200);//三个参数的意思分别是，下拉的时候progress是否scale，progress的起点以及终点

    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("name", "test");
                data.add(hm);
                adapter.notifyDataSetChanged();

            }
        }, 2000);
    }
}
