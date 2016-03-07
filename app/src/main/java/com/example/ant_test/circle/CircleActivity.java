package com.example.ant_test.circle;

import android.app.Activity;
import android.os.Bundle;

import com.example.ant_test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chx7078 on 2015/7/22.
 */
public class CircleActivity extends Activity{
    private YgbCircleView circleView;
    private YgbSemiCircleView semiCircleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        circleView = (YgbCircleView) findViewById(R.id.cv1);
        semiCircleView = (YgbSemiCircleView) findViewById(R.id.scv1);
        CircleViewData data1 = new CircleViewData(50d, R.color.asset1);
        CircleViewData data2 = new CircleViewData(220d, R.color.asset2);
        CircleViewData data3 = new CircleViewData(100d, R.color.asset3);
        CircleViewData data4 = new CircleViewData(150d, R.color.asset4);
        List<CircleViewData> data = new ArrayList<CircleViewData>();
        data.add(data1);data.add(data2); data.add(data3); data.add(data4);
        circleView.setData(data);
        CircleViewData data5 = new CircleViewData(50d, R.color.asset1);
        CircleViewData data6 = new CircleViewData(220d, R.color.asset2);
        CircleViewData data7 = new CircleViewData(100d, R.color.asset3);
        CircleViewData data8 = new CircleViewData(150d, R.color.asset4);
        List<CircleViewData> data_ = new ArrayList<CircleViewData>();
        data_.add(data5);data_.add(data6); data_.add(data7); data_.add(data8);
        semiCircleView.setBgColor(getResources().getColor(R.color.white));
        semiCircleView.setData(data_);
    }
}
