package com.example.ant_test.image_loader.util;

import static android.os.Environment.MEDIA_MOUNTED;

import java.io.File;
import java.io.IOException;


import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

/**
 * @Title: StorageUtils.java
 * @Package com.example.ant_test.image_loader.util
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-10-9 下午2:08:49
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class StorageUtils {
	
	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";
	private static final String INDIVIDUAL_DIR_NAME = "vil-images";

	/**
	 * 获取image loader的缓存路径，即在“公共”缓存路径下再新建个文件夹
	 */
	public static File getIndividualCacheDirectory(Context context) {
		File cacheDir = getCacheDirectory(context);
		File individualCacheDir = new File(cacheDir, INDIVIDUAL_DIR_NAME);
		if (!individualCacheDir.exists()) {
			if (!individualCacheDir.mkdir()) {
				individualCacheDir = cacheDir;
			}
		}
		return individualCacheDir;
	}
	
	/**
	 * 获取应用的“公共”缓存路径
	 */
	public static File getCacheDirectory(Context context) {
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
		if (appCacheDir == null) {//如果没有获取到外部存储路径，则使用设备的ROM，这个路径是一定存在的
			String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
			Log.w("", "Can't define system cache directory! " + cacheDirPath + " will be used.");
			appCacheDir = new File(cacheDirPath);
		}
		return appCacheDir;
	}
	
	/**
	 * 获取外部存储的路径，一般是sd卡
	 */
	private static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");//=>>>/根目录/Android/data
		File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");//==>>>/根目录/Android/data/packagename/cache
		if (!appCacheDir.exists()) {//如果该缓存路径不存在
			if (!appCacheDir.mkdirs()) {//如果创建该缓存失败
				Log.w("", "Unable to create external cache directory");
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();//在缓存目录下新建.nomedia文件，不知道干嘛用的
			} catch (IOException e) {
				Log.i("", "Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCacheDir;
	}
	
	/**
	 * 判断应用是否有外部存储的权限
	 */
	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}
	
}
