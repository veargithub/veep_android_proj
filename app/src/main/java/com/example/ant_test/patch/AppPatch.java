package com.example.ant_test.patch;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 * Created by 3020mt on 2016/7/27.
 */
public class AppPatch implements Runnable{

    private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static final String SP_PATCH_VERSION = "sp_patch_version";

    public static final String PATCH_FOLDER_NAME = "patch";

    private static AppPatch appPatch;

    public boolean isDone;

    private Context context;
    private String patchName;
    private String urlString;

    private AppPatch() {

    }

    public static final AppPatch getInstance() {
        if (appPatch == null) {
            synchronized (AppPatch.class) {
                if (appPatch == null) {
                    appPatch = new AppPatch();
                }
            }
        }
        return appPatch;
    }

    public final void init(Context context, String patchName, String urlString) {
        this.context = context;
        this.patchName = patchName;
        this.urlString = urlString;
        this.isDone = false;
    }

    @Override
    public void run() {
        downloadPatch(context, patchName, urlString);
    }

    public final void downloadPatch(Context context, String patchName, String urlString) {
        if (urlString == null) return;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
            InputStream inputStream = urlConn.getInputStream();
            File file = createPatch(context, patchName);
            OutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[1 * 1024];
            int downloadNum = 0;
            while((downloadNum = inputStream.read(buffer)) != -1){
                output.write(buffer, 0, downloadNum);
            }
            output.flush();
            saveInfo();
        } catch (Exception e) {}
        finally {
            isDone = true;
        }
    }

    public final void saveInfo() {
        SharedPreferences sp = context.getSharedPreferences("sp_patch", Context.MODE_PRIVATE);
        sp.edit().putString(getPatchSP(context), patchName).commit();
    }

    public static final String getPatchSP(Context context) {
        return "patch_" + getAppVersion(context);
    }

    public static final File createPatch(Context context, String patchName) {
        if (context == null || patchName == null || patchName.equals("")) return null;
        String version = getAppVersion(context);
        File patchDir = getPatchDir(context, version);
        if (!patchDir.exists()) {
            if (!patchDir.mkdirs()) {
                return null;
            }
        }
        File patchFile = new File(patchDir, patchName);
        if (patchFile.exists()) {
            if (!patchFile.delete()) {
                return null;
            }
        }
        try {
            if (patchFile.createNewFile()) {
                return patchFile;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否下载过某补丁
     * @param context
     * @param patchName
     * @return
     */
    public static final boolean hasPatch(Context context, String patchName) {
        if (context == null || patchName == null || patchName.equals("")) return false;
        String version = getAppVersion(context);
        File patchDir = getPatchDir(context, version);
        if (!patchDir.exists()) {
            patchDir.mkdirs();
            return false;
        } else {
            File patchFile = new File(patchDir, patchName);
            if (patchFile.exists()) {
                return true;
            }
            return false;
        }
    }

    public static final void deletePatch(Context context, String patchName) {
        if (context == null || patchName == null || patchName.equals("")) return;
        String version = getAppVersion(context);
        File patchDir = getPatchDir(context, version);
        if (patchDir.exists()) {
            File patchFile = new File(patchDir, patchName);
            if (patchFile.exists()) {
                patchFile.delete();
            }
        }
    }

    /**
     *
     * @param context
     * @param version
     * @return /android/data/packageName/cache/patch/version
     */
    public static final File getPatchDir(Context context, String version) {
        return new File(new File(getCacheDirectory(context), PATCH_FOLDER_NAME), version);
    }

    public static final File getCacheDirectory(Context context) {
        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) { //有这种可能。。
            externalStorageState = "";
        }
        if (MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public static final String getAppVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "no_version";
        }
    }

    /**
     * 是否有外部存储的权限
     * @param context
     * @return
     */
    public static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 获取外部存储的缓存目录
     * @param context
     * @return
     */
    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");//=>>>/根目录/Android/data
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");//==>>>/根目录/Android/data/packagename/patch
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                return null;
            }
        }
        return appCacheDir;
    }
}
