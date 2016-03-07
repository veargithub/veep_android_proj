package com.example.ant_test.call_remote_function;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

import com.example.ant_test.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import dalvik.system.DexClassLoader;

/**
 * 这个类用来演示如何调用其他应用的某个方法
 */
public class CallRemoteFunctionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_remote_function);
        callRemoteFunction();
    }

    private void callRemoteFunction() {
        Intent intent = new Intent("com.example.furtest", null);
        PackageManager pm = getPackageManager();
        final List<ResolveInfo> plugins = pm.queryIntentActivities(intent, 0);
        ResolveInfo rinfo = plugins.get(0);
        ActivityInfo ainfo = rinfo.activityInfo;
        String div = System.getProperty("path.separator");
        String packageName = ainfo.packageName;
        String dexPath = ainfo.applicationInfo.sourceDir;
        String dexOutputDir = getApplicationInfo().dataDir;
        String libPath = ainfo.applicationInfo.nativeLibraryDir;

        Log.i("Host", "dexPath:" + dexPath + ", dexOutputDir:" + dexOutputDir + ", libPath:" + libPath);

        DexClassLoader cl = new DexClassLoader(dexPath, dexOutputDir, libPath, this.getClass().getClassLoader());
        try{
            Class<?> clazz = cl.loadClass(packageName + ".PluginClass");
            Object object = clazz.newInstance();
            Class[] params = new Class[2];
            params[0] = Integer.TYPE;
            params[1] = Integer.TYPE;
            Method action = clazz.getMethod("function1", params);
            Integer ret = (Integer) action.invoke(object, 12, 34);
            Log.i("Host", "reture value is " + ret);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
