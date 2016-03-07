package com.example.ant_test.plugin.core;

import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

/**
 * Created by chx7078 on 2015/9/25.
 */
public class PluginInfo {

    public String packageName;
    public String defaultActivity;
    public DexClassLoader classLoader;
    public AssetManager assetManager;
    public Resources resources;
    public PackageInfo packageInfo;

    public PluginInfo(DexClassLoader classLoader, Resources resources, PackageInfo packageInfo) {
        this.classLoader = classLoader;
        this.resources = resources;
        this.packageInfo = packageInfo;
        this.packageName = packageInfo.packageName;
        this.defaultActivity = getDefaultActivityName();
    }

    @Override
    public String toString() {
        return "PluginInfo{" +
                "packageName='" + packageName + '\'' +
                ", defaultActivity='" + defaultActivity + '\'' +
                '}';
    }

    private final String getDefaultActivityName() {
        if (packageInfo.activities != null && packageInfo.activities.length > 0) {
            return packageInfo.activities[0].name;
        }
        return "";
    }

}
