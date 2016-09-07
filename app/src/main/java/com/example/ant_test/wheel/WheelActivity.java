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
    WheelView mProvince, fund1, fund2;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wheel);
        ll = (LinearLayout) findViewById(R.id.ll1);
        popWindowChoosePlace = new PopupWindow(this);
        //popViewChoosePlace = LayoutInflater.from(this).inflate(R.layout.layout_wheel, null);
        popViewChoosePlace = LayoutInflater.from(this).inflate(R.layout.layout_two_wheels, null);
        popWindowChoosePlace.setContentView(popViewChoosePlace);
        popWindowChoosePlace.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popWindowChoosePlace.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popWindowChoosePlace.setFocusable(true);
        popWindowChoosePlace.setBackgroundDrawable(new ColorDrawable(0x50000000));
        popWindowChoosePlace.setOutsideTouchable(true);

        List list = new ArrayList();
        List list2 = new ArrayList();
        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0) {
                list.add(i + "啊啊啊啊不不不不不不不不不不不不不不不不不不不不不不不不不不不不吧呃呃呃呃呃呃鹅鹅鹅鹅鹅鹅饿");
                list2.add("list2>>>>>>>>>>" + i);
            } else {
                list.add(i + "aaaaaaaaaaaaaaaa");
                list2.add("list2>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + i);
            }
        }

//        mProvince = (WheelView) popViewChoosePlace.findViewById(R.id.periodWheel);
//        initWheelView(mProvince, list);
        //mProvince.setWheelForeground(R.drawable.line2);

        fund1 = (WheelView) popViewChoosePlace.findViewById(R.id.fundName1);
        initWheelView(fund1, list);
        fund1.setBackgroundColor(getResources().getColor(R.color.green));

        fund2 = (WheelView) popViewChoosePlace.findViewById(R.id.fundName2);
        initWheelView(fund2, list2);
        fund2.setBackgroundColor(getResources().getColor(R.color.red));

        ll.post(new Runnable() {
            @Override
            public void run() {
                popWindowChoosePlace.setAnimationStyle(R.style.AnimBottom);
                popWindowChoosePlace.showAtLocation(ll, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

    }

    private void initWheelView(WheelView wv, List list) {
        wv.setViewAdapter(new TestWheelAdapter(this, 0, list));
        wv.setVisibleItems(5);
        wv.setWheelBackground(R.color.white);
        wv.setCurrentItem(0);
        wv.setWheelForeground(R.drawable.bg_fund_cash_wheel);
    }
}
