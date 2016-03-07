package com.example.ant_test.gradient_area;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by chx7078 on 2015/7/28.
 */
public class GradientAreaActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new GradientAreaView(this));
    }
}
