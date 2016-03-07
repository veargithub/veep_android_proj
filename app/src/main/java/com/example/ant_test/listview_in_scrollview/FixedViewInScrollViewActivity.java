package com.example.ant_test.listview_in_scrollview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.ant_test.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by 3020mt on 2015/12/18.
 * scrollview 中有个固定的浮层
 */
public class FixedViewInScrollViewActivity extends Activity  {

    private TextView tvFour, tvCopy;
    private PullToRefreshScrollViewWithFixedView sv1;
    private View[] tabs;
    private int[] tabIds = {R.id.llTab0, R.id.llTab1, R.id.llTab2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fixed_view_in_sv);
        tvFour = (TextView)findViewById(R.id.tvFour);
        tvCopy = (TextView)findViewById(R.id.tvCopy);
        tabs = new View[tabIds.length];
        for (int i = 0; i < tabIds.length; i++) {
            tabs[i] = findViewById(tabIds[i]);
            if (i == 0) tabs[i].setVisibility(View.VISIBLE);
            else tabs[i].setVisibility(View.GONE);
        }

        sv1 = (PullToRefreshScrollViewWithFixedView) findViewById(R.id.sv1);
        sv1.setViews(tvFour, tvCopy);
        sv1.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                sv1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sv1.onRefreshComplete();
                        int current = 0;
                        for (int i = 0; i < tabs.length; i++) {
                            if (tabs[i].getVisibility() == View.VISIBLE) {
                                current = i;
                                break;
                            }
                        }
                        int next = (current + 1) % tabs.length;
                        for (int i = 0; i < tabs.length; i++) {
                            if (i == next) tabs[i].setVisibility(View.VISIBLE);
                            else tabs[i].setVisibility(View.GONE);
                        }
                    }
                }, 1000);

            }
        });

    }



}
