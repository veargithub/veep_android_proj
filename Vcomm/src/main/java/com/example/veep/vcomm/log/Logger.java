package com.example.veep.vcomm.log;

public class Logger {
	private static final String TAG = "Veep";
	
	private static Vlog.EasyLog log4j = Vlog.getLog4j(TAG);
	
	// 带 TAG 标签
	public static void v(String tag, String text) {
		log4j.trace("[" + tag + "]" + text );
	}

	public static void i(String tag, String text) {
		log4j.info("[" + tag + "]" + text );
	}

	public static void d(String tag, String text) {
		log4j.debug("[" + tag + "]" + text );
	}
	
	public static void w(String tag, String text) {
		log4j.warn("[" + tag + "]" + text );
	}

	public static void e(String tag, String text) {
		log4j.error("[" + tag + "]" + text );
	}
	
	public static void e(Exception e) {
		log4j.error(e);
	}

	// 带 TAG 标签

	// 不带TAG 标签
	public static void v(String text) {
		log4j.trace("[" + TAG + "]" + text );
	}

	public static void i(String text) {
		log4j.info("[" + TAG + "]" + text );
	}

	public static void d(String text) {
		log4j.debug("[" + TAG + "]" + text );
	}
	
	public static void w(String text) {
		log4j.warn("[" + TAG + "]" + text );
	}
	
	public static void e(String text) {
		log4j.error("[" + TAG + "]" + text );
	}
	// 不带TAG 标签
}
