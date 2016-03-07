package com.example.ant_test.image_loader.downloader;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import com.example.ant_test.image_loader.util.IoUtils;
import android.content.Context;
import android.net.Uri;

/**
 * @Title: BaseImageDownloader.java
 * @Package com.example.ant_test.image_loader.downloader
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-9-26 下午1:49:06
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class BaseImageDownloader implements IImageDownloader{
	
	/** {@value} */
	public static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5 * 1000; // milliseconds
	/** {@value} */
	public static final int DEFAULT_HTTP_READ_TIMEOUT = 20 * 1000; // milliseconds

	/** {@value} */
	protected static final int BUFFER_SIZE = 32 * 1024; // 32 Kb
	/** {@value} */
	protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";//如果uri里出现这些字符,不重新encode

	protected static final int MAX_REDIRECT_COUNT = 5;//如果请求失败的重连次数

	//protected static final String CONTENT_CONTACTS_URI_PREFIX = "content://com.android.contacts/";

	//private static final String ERROR_UNSUPPORTED_SCHEME = "UIL doesn't support scheme(protocol) by default [%s]. " + "You should implement this support yourself (BaseImageDownloader.getStreamFromOtherSource(...))";

	protected final Context context;
	protected final int connectTimeout;
	protected final int readTimeout;

	public BaseImageDownloader(Context context) {
		this.context = context.getApplicationContext();
		this.connectTimeout = DEFAULT_HTTP_CONNECT_TIMEOUT;
		this.readTimeout = DEFAULT_HTTP_READ_TIMEOUT;
	}

	public BaseImageDownloader(Context context, int connectTimeout, int readTimeout) {
		this.context = context.getApplicationContext();
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}
	
	@Override
	public InputStream getStream(String imageUri) throws IOException {
		switch (Scheme.ofUri(imageUri)) {
		case HTTP:
		case HTTPS:
			return getStreamFromNetwork(imageUri);
		case FILE:
			return getStreamFromFile(imageUri);
		default:
			return getStreamFromUnknown(imageUri);
		}
		
	}

	/**
	 * 从网络上下载图片(注：这个类里new的inputstream并没有被close，因为之后对图片进行压缩的时候还要用到，在压缩完之后再close)
	 * @param imageUri 图片的uri
	 * @return
	 * @throws IOException
	 */
	protected InputStream getStreamFromNetwork(String imageUri) throws IOException {
		HttpURLConnection conn = createConnection(imageUri);

		int redirectCount = 0;
		while (conn.getResponseCode() / 100 == 3 && redirectCount < MAX_REDIRECT_COUNT) {
			conn = createConnection(conn.getHeaderField("Location"));
			redirectCount++;
		}

		InputStream imageStream;
		try {
			imageStream = conn.getInputStream();
		} catch (IOException e) {
			IoUtils.clearAndCloseStream(conn.getErrorStream());
			throw e;
		}
		return imageStream;
	}
	
	protected InputStream getStreamFromFile(String imageUri) throws IOException {
		String filePath = Scheme.FILE.crop(imageUri);
		return new BufferedInputStream(new FileInputStream(filePath));
	}
	
	protected InputStream getStreamFromUnknown(String imageUri) throws IOException {
		throw new UnsupportedOperationException(imageUri);
	}
	
	protected HttpURLConnection createConnection(String url) throws IOException {
		String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
		HttpURLConnection conn = (HttpURLConnection) new URL(encodedUrl).openConnection();
		conn.setConnectTimeout(connectTimeout);
		conn.setReadTimeout(readTimeout);
		return conn;
	}
}
