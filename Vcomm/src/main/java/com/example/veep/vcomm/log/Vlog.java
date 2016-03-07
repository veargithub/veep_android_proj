package com.example.veep.vcomm.log;

import android.content.pm.ApplicationInfo;
import android.os.Environment;

import org.apache.log4j.Level;

/**
 * @Title: Vlog.java
 * @Package com.example.ant_test.log_to_file.log
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-7-3 下午3:43:09
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class Vlog {
	public static boolean hasConfigured = false;
    public static final String EXTERNAL_PATH = android.os.Environment.getExternalStorageDirectory() + "";
	public static final String DEFAULT_PATH = EXTERNAL_PATH + "/veep/log/";
	public static final String DEFAULT_FILE_NAME = "veep_log";
    private static String path = DEFAULT_PATH;
    private static String fileName = DEFAULT_FILE_NAME;

    public static void setPathAndName(String path_, String name_) {
        if (path != null) path = EXTERNAL_PATH + path_;
        if (fileName != null) fileName = name_;
    }
	
	public static boolean configure() {
		final boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (!sdCardExist) {
			return false;
		}
		try {
			final LogConfigurator logConfigurator = new LogConfigurator();
			logConfigurator.setFileName(path + fileName);
			logConfigurator.setRootLevel(Level.ALL);
			logConfigurator.setFilePattern("%d - [%p::%c] - %m%n");
			int flags = 0;
			// 当debug模式时候每个文件5m,当非debug模式时，每个文件512
			boolean isDebugMode = (flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
			if (isDebugMode) {
				logConfigurator.setMaxFileSize(5 * 1024 * 1024);
			} else {
				logConfigurator.setMaxFileSize(512 * 1024);
			}
			logConfigurator.configure();
			hasConfigured = true;
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static EasyLog getLog4j(String str) {
		final boolean sdCardExist = Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);
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

    public static EasyLog getLog4j(String path, String fileName, String str) {
        setPathAndName(path, fileName);
        return getLog4j(str);
    }

    public static abstract class EasyLog {
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
}
