package com.example.ant_test.plugin.core;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.example.ant_test.plugin.impl.IActivityPlugin;
import com.example.ant_test.plugin.impl.IPluginAttach;

/**
 * Created by chx7078 on 2015/9/28.
 */
public class BaseProxyActivity extends Activity implements IPluginAttach{

    protected IActivityPlugin pluginActivity;
    private ProxyImpl proxy;

    @Override
    public void attach(IActivityPlugin pluginActivity) {
        this.pluginActivity = pluginActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        proxy.onCreate(getIntent());
    }

    @Override
    public void onStart() {
        pluginActivity.onStart();
        super.onStart();
    }

    @Override
    public void onRestart() {
        pluginActivity.onRestart();
        super.onRestart();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        pluginActivity.onActivityResult(requestCode, resultCode, data);
        super.onRestart();
    }

    @Override
    public void onResume() {
        pluginActivity.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        pluginActivity.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        pluginActivity.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        pluginActivity.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        pluginActivity.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onNewIntent(Intent intent) {
        pluginActivity.onNewIntent(intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return pluginActivity.onTouchEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        super.onKeyUp(keyCode, event);
        return pluginActivity.onKeyUp(keyCode, event);
    }

    @Override
    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        pluginActivity.onWindowAttributesChanged(params);
        super.onWindowAttributesChanged(params);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        pluginActivity.onWindowFocusChanged(hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onBackPressed() {
        pluginActivity.onBackPressed();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return pluginActivity.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return pluginActivity.onOptionsItemSelected(item);
    }

    @Override
    public AssetManager getAssets() {
        return proxy.getAssets() == null ? super.getAssets() : proxy.getAssets();
    }

    @Override
    public Resources getResources() {
        return proxy.getResources() == null ? super.getResources() : proxy.getResources();
    }
}
