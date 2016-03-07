package com.example.ant_test.log_to_file.emlog;

import android.content.pm.ApplicationInfo;
import android.os.Environment;

import org.apache.log4j.Level;

import java.io.File;

/**
 * @Title: SpecialNetLog.java
 * @Package com.example.ant_test.log_to_file.emlog
 * @Description: 这个包是从EASTMONEY里copy过来的
 * @author Chenxiao
 * @date 2014-7-8 上午9:33:24
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class SpecialNetLog {
	private static final String DIR = android.os.Environment.getExternalStorageDirectory() 
			+ File.separator + "eastmoney" + File.separator +"network_log" + File.separator;
	private static final String FILE_NAME = "emNetworkLog";
	private boolean writeable;
	private static SpecialNetLog snLog = new SpecialNetLog();
	private org.apache.log4j.Logger log4j = null;
	private static final String TAG = "SpecialNetLog";
	
	private SpecialNetLog() {
		final boolean sdCardExist = Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
		if (!sdCardExist) {
			writeable = false;
			return;
		}
		try {
			final LogConfigurator logConfigurator = new LogConfigurator();
			logConfigurator.setFileName(DIR + FILE_NAME);
			logConfigurator.setRootLevel(Level.ALL);
			logConfigurator.setFilePattern("%m%n");
			int flags = 0;
			boolean isDebugMode = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
			if (isDebugMode) {
				logConfigurator.setMaxFileSize(5 * 1024 * 1024);
			} else {
				logConfigurator.setMaxFileSize(512 * 1024);
			}
			logConfigurator.configure();
			log4j = org.apache.log4j.Logger.getLogger(TAG);
		} catch (Exception e) {
			writeable = false;
		}
		writeable = true;
	}
	
	public static final SpecialNetLog getInstance() {
		if (snLog == null) {
			newInstance();
		}
		return snLog;
	}
	
	private static final void newInstance() {
		if (snLog == null) {
			snLog = new SpecialNetLog(); 
		}
	}
	
	public void d(String message) {
		if (writeable) {
			log4j.debug(message);
		}
	}
}
