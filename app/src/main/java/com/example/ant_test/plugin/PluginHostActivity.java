package com.example.ant_test.plugin;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ant_test.plugin.core.PluginInfo;
import com.example.ant_test.plugin.core.PluginManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chx7078 on 2015/9/24.
 * this is a host activity to start another activity in plugin
 */
public class PluginHostActivity extends Activity implements AdapterView.OnItemClickListener {
//    TextView tv1;
    String output = "";
    ListView lv1;
    ArrayAdapter<String> adapter;
    List<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lv1 = new ListView(this);
        setContentView(lv1);
//        tv1 = (TextView) findViewById(R.id.tv1);
        init();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        lv1.setAdapter(adapter);
        lv1.setOnItemClickListener(this);
    }

    private void init() {
        data = new ArrayList();
        String pluginFolder = Environment.getExternalStorageDirectory() + File.separator + "veep_plugin";
        File file = new File(pluginFolder);
        File[] plugins = file.listFiles();
        if (plugins == null || plugins.length == 0) {
            output = "no plugins found";
        } else {
            for (File plugin : plugins) {
                PluginInfo pluginInfo = PluginManager.getInstance(this).loadPlugin(plugin.getAbsolutePath());
                output += pluginInfo + "\n";
                data.add(pluginInfo.packageName);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String packageName = adapter.getItem(position);
        PluginInfo pluginInfo = PluginManager.getInstance(this).getPlugin(packageName);
        Toast.makeText(this, packageName, Toast.LENGTH_SHORT).show();
    }
}
