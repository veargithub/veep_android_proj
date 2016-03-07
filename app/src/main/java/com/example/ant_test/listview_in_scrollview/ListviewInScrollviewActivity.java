package com.example.ant_test.listview_in_scrollview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ant_test.R;

import java.util.LinkedList;

/**
 * Created by chx7078 on 2015/5/11.
 */
public class ListviewInScrollviewActivity extends Activity {

    private ListView lv;
    private ArrayAdapter<String> mAdapter;
    private LinkedList<String> mListItems;
    private ScrollViewWithFixedView sv;
    private TextView tv1;
    private TextView tv2;
    private boolean disallowInterrupt = false;
//    private LinearLayout ll1;
//    private LinearLayout ll2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_in_scrollview);
//        ll1 = (LinearLayout)findViewById(R.id.ll1);
//        ll2 = (LinearLayout)findViewById(R.id.ll2);
        lv = (ListView)findViewById(R.id.listView);
        sv = (ScrollViewWithFixedView)findViewById(R.id.sv);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        sv.post(new Runnable() {
            @Override
            public void run() {
                sv.scrollTo(0, 0);
            }
        });

        mListItems = new LinkedList<String>();
        for (int i = 0; i < 20; i++) {
            mListItems.add(i + "");
        }
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
        lv.setAdapter(mAdapter);

        lv.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(">>>>", "disallowInterrupt:" + disallowInterrupt);
                if (disallowInterrupt) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return false;
            }
        });

        sv.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        // 这一步很重要，使得上面的购买布局和下面的购买布局重合

                    }
                });

//        setListViewHeightBasedOnChildren(lv);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}
