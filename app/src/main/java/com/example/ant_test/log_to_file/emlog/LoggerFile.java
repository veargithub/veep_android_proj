package com.example.ant_test.log_to_file.emlog;

import org.apache.log4j.Level;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.util.Log;

/**
 * 2013-01-06 改用log4j去写文件了
 * 
 * @author XSS
 * 
 */
public class LoggerFile {

	/**
	 * log4j 包装类，增加变量needWrite控制是否打日志
	 * 
	 * @author XSS
	 * 
	 */

	public static boolean hasConfigured = false;

	// 文件路径Sd卡下面
	public static final String PATH = android.os.Environment
			.getExternalStorageDirectory() + "/eastmoney/network_log/";

	// 日志名称
	public static final String FILE_NAME = "eastmoney_log222";

	/**
	 * 打印日志的抽象类,子类根据是否需要打印日志自己填充功能
	 * 
	 * @author XSS
	 * 
	 */
	public static abstract class Log4jWrapper {

		public abstract void trace(Object message);

		public abstract void trace(Object message, Throwable t);

		public abstract void debug(Object message);

		public abstract void debug(Object message, Throwable t);

		public abstract void info(Object message);

		public abstract void info(Object message, Throwable t);

		public abstract void warn(Object message);

		public abstract void warn(Object message, Throwable t);

		public abstract void warn(Throwable t);

		public abstract void error(Object message);

		public abstract void error(Object message, Throwable t);

		public abstract void error(Throwable t);

		public abstract void fatal(Object message);

		public abstract void fatal(Object message, Throwable t);

		public abstract void fatal(Throwable t);
	}

	public static boolean configure() {

		final boolean sdCardExist = Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
		if (!sdCardExist) {
			return false;
		}

		try {
			final LogConfigurator logConfigurator = new LogConfigurator();
			logConfigurator.setFileName(PATH + FILE_NAME);
			logConfigurator.setRootLevel(Level.ALL);
			logConfigurator.setFilePattern("%m%n");
			int flags = 0;
			// 当debug模式时候每个文件5m,当非debug模式时，每个文件512
			boolean isDebugMode = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
			if (isDebugMode) {
				logConfigurator.setMaxFileSize(5 * 1024 * 1024);
			} else {
				logConfigurator.setMaxFileSize(512 * 1024);
			}
			// 这个log不能换成logger
			Log.i("isDebugMode", "configure() flags===>>>>" + flags
					+ ",ApplicationInfo.FLAG_DEBUGGABLE===>>>"
					+ ApplicationInfo.FLAG_DEBUGGABLE + ",isDebugMode==>>"
					+ isDebugMode);
			logConfigurator.configure();
			hasConfigured = true;

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static boolean configure(Context context) {

		final boolean sdCardExist = Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
		if (!sdCardExist) {
			return false;
		}

		try {
			final LogConfigurator logConfigurator = new LogConfigurator();
			logConfigurator.setFileName(PATH + FILE_NAME);
			logConfigurator.setRootLevel(Level.ALL);
			logConfigurator.setFilePattern("%d - [%p::%c] - %m%n");
			int flags = 0;
			try {
				PackageInfo packageInfo = context.getPackageManager()
						.getPackageInfo(context.getPackageName(), 0);
				flags = packageInfo.applicationInfo.flags;
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 当debug模式时候每个文件5m,当非debug模式时，每个文件512
			boolean isDebugMode = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
			if (isDebugMode) {
				logConfigurator.setMaxFileSize(5 * 1024 * 1024);
			} else {
				logConfigurator.setMaxFileSize(512 * 1024);
			}
			// 这个log不能换成logger
			Log.i("isDebugMode", "configure(Context context) flags===>>>>" + flags
					+ ",ApplicationInfo.FLAG_DEBUGGABLE===>>>"
					+ ApplicationInfo.FLAG_DEBUGGABLE + ",isDebugMode==>>"
					+ isDebugMode);
			logConfigurator.configure();
			hasConfigured = true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Log4jWrapper getLog4j(String str) {

		final boolean sdCardExist = Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);

		// Log.i("", "sdCardExist===>>>"+sdCardExist);

		// 根据是否存在SD卡，返回两个Log

		if (sdCardExist) {
			if (!hasConfigured) {
				boolean b = configure();
				if (b) {
					return new LogToFile(org.apache.log4j.Logger.getLogger(str));
				} else {
					return new LogNotToFile(str);
				}
			}
			return new LogToFile(org.apache.log4j.Logger.getLogger(str));
		} else {
			return new LogNotToFile(str);
		}

	}

}
