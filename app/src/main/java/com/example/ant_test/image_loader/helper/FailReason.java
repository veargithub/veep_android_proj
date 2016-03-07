package com.example.ant_test.image_loader.helper;

public class FailReason {
	private final FailType type;

	private final Throwable cause;

	public FailReason(FailType type, Throwable cause) {
		this.type = type;
		this.cause = cause;
	}

	/** @return {@linkplain FailType Fail type} */
	public FailType getType() {
		return type;
	}

	/** @return Thrown exception/error, can be <b>null</b> */
	public Throwable getCause() {
		return cause;
	}

	/** Presents type of fail while image loading */
	public static enum FailType {
		/** 下载图片的时候或者存储文件的时候*/
		IO_ERROR,
		/**
		 * 解码图片的时候
		 */
		DECODING_ERROR,
		
		NETWORK_DENIED,
		
		/** 发生out of memory的时候 */
		OUT_OF_MEMORY,
		/** 发生其他乱七八糟的异常的时候 */
		UNKNOWN
	}
}
