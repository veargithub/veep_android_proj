package com.example.ant_test.bezier_curve;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

/**
 * Created by chx7078 on 2015/7/23.
 */
public class BezierActivity extends Activity{
//    BezierView bezierView;
    BezierView2 bezierView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        bezierView = new BezierView(this);
//        bezierView.setLayoutParams(new LinearLayout.LayoutParams(100, 200));
//
//        bezierView.start();
        bezierView2 = new BezierView2(this);
        setContentView(bezierView2);
        //bezierView2.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        bezierView.stop();
        //bezierView2.stop();
    }
}
