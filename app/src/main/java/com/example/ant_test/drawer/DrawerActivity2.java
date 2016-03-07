package com.example.ant_test.drawer;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import com.example.ant_test.R;

/**
 * Created by Administrator on 2015/3/3.
 */
public class DrawerActivity2 extends Activity {

    private Button bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_test);
        bottom = (Button) findViewById(R.id.bottom1);
        TranslateAnimation ta = new TranslateAnimation(0, 0, 0, 300);
        ta.setDuration(2000);
        ta.setFillAfter(true);
        bottom.startAnimation(ta);
    }
}
