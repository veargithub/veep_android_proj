package com.example.ant_test.drawer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ant_test.R;

/**
 * Created by Administrator on 2015/2/26.
 */
public class DrawerActivity extends Activity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView lv1;
    private String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        final Vdrawer drawer = (Vdrawer) findViewById(R.id.drawer1);
        lv1 = (ListView) findViewById(R.id.lv1);
        data = new String[20];
        for (int i = 0; i < 20; i++) {
            data[i] = new String(i + "");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(this);
        drawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer.popDown();

            }
        }, 1000);

        drawer.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawer.popUp();

            }
        }, 5000);

//        drawer.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                drawer.popRight();
//
//            }
//        }, 9000);

//        final Button btnClose = (Button) findViewById(R.id.btn_go);
//        btnClose.setOnClickListener(this);


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("V_drawer", "onItemClick:" + position);
    }

    @Override
    public void onClick(View v) {
        Log.d("V_drawer", "onClick:");
    }


}
