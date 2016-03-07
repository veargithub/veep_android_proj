package com.example.ant_test.plugin.core;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import com.example.ant_test.plugin.impl.IActivityPlugin;
import com.example.ant_test.plugin.impl.IPluginAttach;

import java.lang.reflect.Constructor;

/**
 * Created by chx7078 on 2015/9/29.
 */
public class ProxyImpl {

    private static final String TAG = "ProxyImpl";
    private Activity mProxyActivity;
    private String mTargetActivityClass;
    private String mTargetPackageName;
    private PluginInfo mPluginInfo;
    protected IActivityPlugin mPluginActivity;

    public ProxyImpl(Activity activity) {
        mProxyActivity = activity;
    }

    public void onCreate(Intent intent) {
        if (intent instanceof PluginIntent) {
            PluginIntent pluginIntent = (PluginIntent)intent;
            mTargetPackageName = pluginIntent.pluginPackageName;
            mTargetActivityClass = pluginIntent.pluginClassName;
            Log.d(TAG, "mClass=" + mTargetActivityClass + " mPackageName=" + mTargetPackageName);
            mPluginInfo = PluginManager.getInstance(mProxyActivity).getPlugin(mTargetPackageName);
            launchTargetActivity();
        }
    }

    protected void launchTargetActivity() {
        try {
            Class<?> localClass = mPluginInfo.classLoader.loadClass(mTargetActivityClass);
            Constructor<?> localConstructor = localClass.getConstructor(new Class[] {});
            Object instance = localConstructor.newInstance(new Object[] {});
            mPluginActivity = (IActivityPlugin) instance;
            ((IPluginAttach) mProxyActivity).attach(mPluginActivity);
            Log.d(TAG, "instance = " + instance);
            // attach the proxy activity and plugin package to the mPluginActivity
            mPluginActivity.attach(mProxyActivity, mPluginInfo);

            Bundle bundle = new Bundle();
            //bundle.putInt(DLConstants.FROM, DLConstants.FROM_EXTERNAL);
            mPluginActivity.onCreate(bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AssetManager getAssets() {
        return this.mPluginInfo.assetManager;
    }

    public Resources getResources() {
        return this.mPluginInfo.resources;
    }
}
