package com.example.ant_test.wheel;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.ant_test.R;
import com.example.ant_test.wheel.wheelCustomView.TestWheelAdapter;
import com.example.ant_test.wheel.wheelCustomView.WheelView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 3020mt on 2016/4/13.
 */
public class WheelActivity extends Activity{

    PopupWindow popWindowChoosePlace;
    View popViewChoosePlace;
    WheelView mProvince;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);
        ll = (LinearLayout) findViewById(R.id.ll1);
        popWindowChoosePlace = new PopupWindow(this);
        popViewChoosePlace = LayoutInflater.from(this).inflate(R.layout.layout_wheel, null);
        popWindowChoosePlace.setContentView(popViewChoosePlace);
        popWindowChoosePlace.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popWindowChoosePlace.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popWindowChoosePlace.setFocusable(true);
        popWindowChoosePlace.setBackgroundDrawable(new ColorDrawable(0x50000000));
        popWindowChoosePlace.setOutsideTouchable(true);

        List list = new ArrayList();
        for (int i = 0; i < 20; i++) {
            list.add(i + "");
        }
        mProvince = (WheelView) popViewChoosePlace.findViewById(R.id.periodWheel);
        mProvince.setViewAdapter(new TestWheelAdapter(this, 0, list));
        mProvince.setVisibleItems(5);
        mProvince.setWheelBackground(R.color.white);
        mProvince.setCurrentItem(0);
        //mProvince.setWheelForeground(R.drawable.line2);
        ll.post(new Runnable() {
            @Override
            public void run() {
                popWindowChoosePlace.setAnimationStyle(R.style.AnimBottom);
                popWindowChoosePlace.showAtLocation(ll, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

    }
}
