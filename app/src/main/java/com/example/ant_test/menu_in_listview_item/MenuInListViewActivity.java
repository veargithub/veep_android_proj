package com.example.ant_test.menu_in_listview_item;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.ant_test.R;

/**
 * Created by 3020mt on 2016/2/18.
 */
public class MenuInListViewActivity extends Activity implements View.OnClickListener {

    private ListViewMenuLayout listViewMenuLayout;
    private Button btn1, btn2;
    private ListView lv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_in_listview);
//        listViewMenuLayout = (ListViewMenuLayout) findViewById(R.id.lvml1);
//        btn1 = (Button) findViewById(R.id.btn1);
//        btn1.setOnClickListener(this);
//        btn2 = (Button) findViewById(R.id.btn2);
//        btn2.setOnClickListener(this);

        lv1 = (ListView) findViewById(R.id.lv1);
        lv1.setAdapter(new MyAdapter());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                listViewMenuLayout.scrollerCompat.startScroll(0, 0, 300, 0, 2000);
                listViewMenuLayout.invalidate();
                break;
            case R.id.btn2:
                listViewMenuLayout.finishScrolling();
                break;
        }

    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListViewMenuLayout view;
            if (convertView == null) {
                view = new ListViewMenuLayout(MenuInListViewActivity.this);
            } else {
                view = (ListViewMenuLayout) convertView;
            }
            //view.reset();
            return view;
        }
    }
}
