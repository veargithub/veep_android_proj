package com.example.util;

import java.io.File;
import java.io.IOException;

import android.os.Environment;
import android.util.Log;

/**
 * @Title: ExternalUtil.java
 * @Package com.example.util
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-3 下午4:06:07
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class ExternalUtil {
	private static final String TAG = "ExternalUtil";
	/**
	 * 是否存在外部存储
	 * @return
	 */
	public static final boolean isExternalStorageAvailable() {
		final boolean exists = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (exists) {
			final File esDir = Environment.getExternalStorageDirectory();
			if (esDir == null) {
				Log.d(TAG, "esDir is null");
				return false;
			} else {
				Log.d(TAG, "esDir is not null");
				final String esPath = esDir.getPath();
				Log.d(TAG, "esPath:" + esPath);
				final File testFile = new File(esPath + "/test");
				if (testFile.exists()) {
					Log.d(TAG, "testFile has existed.");
					return true;
				} else {
					Log.d(TAG, "testFile has not existed.");
					try {
						if (!testFile.createNewFile()) {
							Log.d(TAG, "create new file error.");
							return false;
						}
						testFile.delete();
					} catch (IOException e) {
						e.printStackTrace();
						return false;
					}
					return true;
				}
			}
		} else {
			Log.d(TAG, "getExternalStorageState().equals: false");
			return false;
		}
	}
}
