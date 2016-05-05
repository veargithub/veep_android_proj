package com.example.ant_test.toolbar;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ant_test.R;

/**
 * Created by 3020mt on 2016/4/25.
 */
public class ToolBarActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_48_24);

        getSupportActionBar().setTitle("我的toolbar");//title必须要用getSupportActionBar来设置，但subtitle缺不需要
        toolbar.setTitleTextAppearance(this, R.style.myToolbarTitleStyle);

        toolbar.setSubtitle("我的sub toolbar");
        toolbar.setSubtitleTextAppearance(this, R.style.myToolbarSubTitleStyle);
        //toolbar.inflateMenu(R.menu.base_toolbar_menu);



        toolbar.setOnMenuItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fund_detail, menu);
        MenuItem favoriteMenuItem = toolbar.getMenu().findItem(R.id.menu_fund_detail);
        if (favoriteMenuItem != null) {
            favoriteMenuItem.setIcon(getResources().getDrawable(R.drawable.fund_add));
            favoriteMenuItem.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fund_detail:
                //如果比较之前不使用setIcon()设置一遍，则这个equal必定为false
                if (item.getIcon().getConstantState().equals(getResources().getDrawable(R.drawable.fund_add).getConstantState())) {
                    Log.d(">>>>", "fund_add");
                    item.setIcon(getResources().getDrawable(R.drawable.fund_added2));
                } else {
                    Log.d(">>>>", "fund_add2");
                    item.setIcon(getResources().getDrawable(R.drawable.fund_add));
                }
                Toast.makeText(this, "add favorite", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
