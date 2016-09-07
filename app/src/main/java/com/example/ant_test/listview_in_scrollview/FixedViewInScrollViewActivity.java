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
//import com.handmark.pulltorefresh.library.PullToRefreshBase;

/**
 * Created by 3020mt on 2015/12/18.
 * scrollview 中有个固定的浮层
 */
public class FixedViewInScrollViewActivity extends Activity  {

    private TextView tvFour, tvCopy;
    private ScrollViewWithFixedView sv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fixed_view_in_sv);
        tvFour = (TextView)findViewById(R.id.tvFour);
        tvCopy = (TextView)findViewById(R.id.tvCopy);
        sv1 = (ScrollViewWithFixedView) findViewById(R.id.sv1);
        sv1.setViews(tvFour, tvCopy);
    }
}
