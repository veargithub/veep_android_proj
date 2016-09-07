package com.example.ant_test.swipe_layout.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.example.ant_test.R;
import com.example.ant_test.swipe_layout.library.MaterialRefreshLayout;
import com.example.ant_test.swipe_layout.library.MaterialRefreshListener;

/**
 * Created by Administrator on 2015/9/10.
 */
public class ScrollViewActivity extends Activity implements View.OnClickListener {
    private MaterialRefreshLayout materialRefreshLayout;
    private TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setText("我多年不见的同学突然找我，我多年不见的同学突然找我，我多年不见的同学突然找我,我多年不见的同学突然找我，我多年不见的同学突然找我，我多年不见的同学突然找我,");
        tv1.setText("我多年不见的同学突然找我，我多年不见的同学突然找我，我多年不见的同学突然找我");
        tv1.post(new Runnable() {
            @Override
            public void run() {
                Layout layout = tv1.getLayout();
                int lines = layout.getLineCount();
                if ( lines > 0 && layout.getEllipsisCount(lines - 1) > 0) {
                    Log.d(">>>>", "ellipsized");
                } else {
                    Log.d(">>>>", "not ellipsized");
                }
            }
        });
        materialRefreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialRefreshLayout.finishRefresh();

                    }
                }, 3000);
                materialRefreshLayout.finishRefreshLoadMore();

            }

            @Override
            public void onfinish() {
                Toast.makeText(ScrollViewActivity.this, "finish", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {
                Toast.makeText(ScrollViewActivity.this, "load more", Toast.LENGTH_LONG).show();

                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialRefreshLayout.finishRefreshLoadMore();

                    }
                }, 2000);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
//            case R.id.simple:
//                startActivity(new Intent(this,SimpleActivity.class));
//                break;
//
//            case R.id.wave:
//                startActivity(new Intent(this,WaveActivity.class));
//                break;
//
//            case R.id.rv:
//                Intent intent = new Intent(this,LoadMoreActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.lv:
//                Intent intent2 = new Intent(this,AutoRefreshActivity.class);
//                startActivity(intent2);
//                break;
//
//
//            case R.id.sun:
//                startActivity(new Intent(this,SunActivity.class));
//                break;
//
//            case R.id.overLay:
//                startActivity(new Intent(this,OverLayActivity.class));
//                break;

        }
    }
}
