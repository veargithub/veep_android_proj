package com.example.ant_test.plugin.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.ant_test.plugin.core.PluginInfo;

import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * Created by chx7078 on 2015/9/24.
 */
public class Utils {
    public static PackageInfo getPackageInfo(Context context, String apkFilepath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pkgInfo = null;
        try {
            pkgInfo = pm.getPackageArchiveInfo(apkFilepath, PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pkgInfo;
    }

}
