package com.example.ant_test.image_loader.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Title: IoUtil.java
 * @Package com.example.ant_test.image_loader.util
 * @Description: 
 * @author Chenxiao
 * @date 2014-9-22 上午10:32:44
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class IoUtils {

	public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 KB
	
	public static final int DEFAULT_IMAGE_TOTAL_SIZE = 500 * 1024; // 500 Kb
	
	public static final int CONTINUE_LOADING_PERCENTAGE = 75;
	
	/**
	 * 把输入流复制到输出流，TODO 可能还需要个listener去监听
	 * @param is input stream
	 * @param os output stream
	 * @return 如果成功返回true，否则false
	 * @throws IOException
	 */
	public static boolean copyStream(InputStream is, OutputStream os, CopyListener listener) throws IOException {
		return copyStream(is, os, listener, DEFAULT_BUFFER_SIZE);
	}
	
	/**
	 * 见上面那个方法
	 * @param is
	 * @param os
	 * @param bufferSize 指定一次拷贝多少字节
	 * @return
	 * @throws IOException
	 */
	public static boolean copyStream(InputStream is, OutputStream os, CopyListener listener, int bufferSize)
			throws IOException {
		int current = 0;
		int total = is.available();
		if (total <= 0) {
			total = DEFAULT_IMAGE_TOTAL_SIZE;
		}

		final byte[] bytes = new byte[bufferSize];
		int count;
		if (shouldStopLoading(listener, current, total)) return false;
		while ((count = is.read(bytes, 0, bufferSize)) != -1) {
			os.write(bytes, 0, count);
			current += count;
			if (shouldStopLoading(listener, current, total)) return false;
		}
		os.flush();
		return true;
	}
	
	private static boolean shouldStopLoading(CopyListener listener, int current, int total) {
		if (listener != null) {
			boolean shouldContinue = listener.onBytesCopied(current, total);
			if (!shouldContinue) {
				if (100 * current / total < CONTINUE_LOADING_PERCENTAGE) {
					return true; // if loaded more than 75% then continue loading anyway
				}
			}
		}
		return false;
	}
	
	/**
	 * 为了重用connection，必须对errorstream里的数据进行清除
	 * @param is
	 */
	public static void clearAndCloseStream(InputStream is) {
		final byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
		try {
			while (is.read(bytes, 0, DEFAULT_BUFFER_SIZE) != -1) {
			}
		} catch (IOException e) {
			
		} finally {
			closeStream(is);
		}
	}

	/**
	 * 关闭流（inputstream和outputstream都实现了closeable接口）
	 * @param closeable
	 */
	public static void closeStream(Closeable closeable) {
		try {
			closeable.close();
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 下载图片时的监听器
	 */
	public static interface CopyListener {
		/**
		 * @param current 当前下载了多少字节
		 * @param total   这张图片总共有多少字节
		 * @return <b>true</b> - 如果本次下载要被中断则返回false，否则返回true
		 */
		boolean onBytesCopied(int current, int total);
	}
}
