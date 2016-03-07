package com.example.ant_test.plugin.core;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import com.example.ant_test.plugin.utils.Utils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Created by chx7078 on 2015/9/25.
 */
public class PluginManager {
    private static final String TAG = "PluginManager";
    private static PluginManager sInstance;
    private Context mContext;
    private Map<String, PluginInfo> pluginMap;//如果数量在1000以上，效率会不行
    private int mFrom = PluginConstants.FROM_INTERNAL;
    //private String mNativeLibDir = null;

    private PluginManager(Context context) {
        mContext = context.getApplicationContext();
        pluginMap = new ArrayMap();
        //mNativeLibDir = mContext.getDir("pluginlib", Context.MODE_PRIVATE).getAbsolutePath();
    }

    public static PluginManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PluginManager.class) {
                if (sInstance == null) {
                    sInstance = new PluginManager(context);
                }
            }
        }
        return sInstance;
    }

    public PluginInfo loadPlugin(String dexPath) {
        PackageInfo packageInfo = Utils.getPackageInfo(mContext, dexPath);
        if (pluginMap.containsKey(packageInfo.packageName)) return null;
        DexClassLoader dexClassLoader = createDexClassLoader(dexPath);
        AssetManager assetManager = createAssetManager(dexPath);
        Resources resources = createResources(assetManager);
        PluginInfo pluginInfo = new PluginInfo(dexClassLoader, resources, packageInfo);
        pluginMap.put(pluginInfo.packageName, pluginInfo);
        return pluginInfo;
    }

    private String dexOutputPath;

    private DexClassLoader createDexClassLoader(String dexPath) {
        File dexOutputDir = mContext.getDir("dex", Context.MODE_PRIVATE);
        dexOutputPath = dexOutputDir.getAbsolutePath();
        DexClassLoader loader = new DexClassLoader(dexPath, dexOutputPath, null, mContext.getClassLoader());
        return loader;
    }

    private AssetManager createAssetManager(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexPath);
            return assetManager;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Resources createResources(AssetManager assetManager) {
        Resources superRes = mContext.getResources();
        Resources resources = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        return resources;
    }

    public PluginInfo getPlugin(String packageName) {
        return this.pluginMap.get(packageName);
    }

    public void startPluginActivity(Context context, PluginIntent pluginIntent) {
        startPluginActivityForResult(context, pluginIntent, -1);
    }

    public void startPluginActivityForResult(Context context, PluginIntent pluginIntent, int requestCode) {
        //TODO 不考虑内部跳转
        if (mFrom == PluginConstants.FROM_INTERNAL) {

        }
        String packageName = pluginIntent.pluginPackageName;
        if (TextUtils.isEmpty(packageName)) {
            throw new NullPointerException("package name can not be empty");
        }
        PluginInfo pluginInfo = getPlugin(packageName);
        if (pluginInfo == null) {
            return;
        }
        String targetActivityName = getPluginActivityFullName(pluginIntent, pluginInfo);
        if (targetActivityName == null) return;
        Class cls = loadPluginClass(pluginInfo.classLoader, targetActivityName);
        if (cls == null) {
            return;
        }
        Class proxyActivityClass = getProxyActivityClass(cls);
        if (proxyActivityClass == null) {
            return;
        }
//        pluginIntent.targetActivityName = targetActivityName;
//        pluginIntent.targetActivityPackageName = packageName;
        performStartActivityForResult(context, pluginIntent, requestCode);
    }

    /** 获取要启动的activity的包名+类名*/
    private String getPluginActivityFullName(PluginIntent pluginIntent, PluginInfo pluginInfo) {
        String className = pluginIntent.pluginClassName;
        if (className == null) {
            className = pluginInfo.defaultActivity;
        }
        if (className == null) return null;
        if (className.startsWith(".")) {
            className = pluginInfo.packageName + className;
        }
        return className;
    }

    /** 获取要启动的activity的class*/
    private Class<?> loadPluginClass(ClassLoader classLoader, String className) {
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className, true, classLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return clazz;
    }

    /** 获取代理activity的class，即外面的壳*/
    private Class<? extends Activity> getProxyActivityClass(Class<?> cls) {
        //TODO
//        if (BasePluginActivity.class.isAssignableFrom(cls)) {
//            return BaseProxyActivity.class;
//        }
        return null;
    }

    private void performStartActivityForResult(Context context, PluginIntent pluginIntent, int requestCode) {
        Log.d(TAG, "launch " + pluginIntent.pluginClassName);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(pluginIntent, requestCode);
        } else {
            context.startActivity(pluginIntent);
        }
    }
}
