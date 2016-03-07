package com.example.ant_test.interpolator;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.example.veep.vcomm.log.Logger;

import java.text.DecimalFormat;

/**
 * Created by chx7078 on 2015/4/15.
 */
public class InterpolatorActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
        AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();
        AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();
        DecimalFormat df = new DecimalFormat("#.##");
        int x = 0;
        for (float f = 0f; f <= 1f; f += 0.05f) {
            float f1 = decelerateInterpolator.getInterpolation(f);
            float f2 = accelerateInterpolator.getInterpolation(f);
            float f3 = accelerateDecelerateInterpolator.getInterpolation(f);
            sb1.append(df.format(f1)).append(",");
            sb2.append(df.format(f2)).append(",");
            x += (int)(f3 * 50f + 25f);
            sb3.append(df.format((f3) * 50f + 25f)).append(",");
        }
        Logger.d("x:" + x);
        Logger.d("decelerateInterpolator:" + sb1.toString());//0,0.19,0.36,0.51,0.64,0.75,0.84,0.91,0.96,0.99
        Logger.d("accelerateInterpolator:" + sb2.toString());//0,0.01,0.04,0.09,0.16,0.25,0.36,0.49,0.64,0.81,
        Logger.d("accelerateDecelerateInterpolator:" + sb3.toString());//0,0.02,0.1,0.21,0.35,0.5,0.65,0.79,0.9,0.98,

        //sb.delete(0, sb.length());//也可以sb.setLength(0)

    }
}
