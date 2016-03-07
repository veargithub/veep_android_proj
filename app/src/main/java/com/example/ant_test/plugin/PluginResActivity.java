package com.example.ant_test.plugin;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chx7078 on 2015/9/18.
 */
public class PluginResActivity extends Activity{

    String apkPath = "/mnt/sdcard/furtest.apk";
    AssetManager mAssetManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, apkPath);
            mAssetManager = assetManager;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if (mAssetManager != null) {
            Resources hostResources = super.getResources();
            Resources mResources = new Resources(mAssetManager, hostResources.getDisplayMetrics(), hostResources.getConfiguration());

            String str1 = mResources.getString(mResources.getIdentifier("str_plugin1", "string", "fur.veep.com.furtest"));
            Log.d(">>>>", "plugin str:" + str1);
        }
    }
}
