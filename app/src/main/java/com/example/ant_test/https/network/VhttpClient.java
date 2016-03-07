package com.example.ant_test.https.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import android.util.Log;

import com.example.ant_test.https.hostname_verifier.VHostnameVerifier;
import com.example.ant_test.https.network.request.IRequest;
import com.example.ant_test.https.network.request.Ihttps;
import com.example.ant_test.https.network.response.VhttpResponse;
import com.example.ant_test.https.network.response.IResponse;

/**
 * @Title: HttpClient.java
 * @Package com.example.ant_test.https.network
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-13 下午1:10:20
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class VhttpClient {
	
	public static final String HEAD = "HEAD";
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final int HTTP_OK = 200;

	private HttpURLConnection conn = null;

	private InputStream inputStream = null;

	private OutputStream outputStream = null;

	private int mTimeOut = 100000;
	
	private int resCode;
	
	public VhttpClient() {}
	
	public VhttpClient(int timeout) {
		this.mTimeOut = timeout;
	}
	
	public int getResCode() {
		return resCode;
	}
	
	public IResponse getResponse(IRequest request) throws Exception {
		if (request == null) {
			return null;
		}
		int count = 0;
		int retryCount = request.getRetryCount();
		IResponse resp = new VhttpResponse();
		while (!getResponse(request, resp) && count <= retryCount) {
			count++;
		}
		return resp;
	}
	
	private boolean getResponse(IRequest request, IResponse response) throws Exception {
		try {
			this.conn = makeConnection(request);
			setProperty(request);
			setContent(request);
			final int responseCode = conn.getResponseCode();
			makeInputStream(responseCode);
			response.setData(conn, inputStream, request);
			return true;
		} catch (NullPointerException e) {
			// NullPointerException 和 IOException用于捕获cleanup()操作后发生的异常，进行超时重新请求
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			if (e instanceof ConnectException
					|| (e instanceof UnknownHostException)) {
				// 无网络连接时，不做重复请求，直接抛出异常给上一级，ConnectException对应ip，UnknownHostException对应域名
				throw e;
			} else {
				e.printStackTrace();
				return false;
			}
		} catch (Exception e) {// 发生其他异常时，直接返回上一级
			e.printStackTrace();
			throw e;
		} finally {
			cleanup();
		}
	}
	
	private HttpURLConnection makeConnection(IRequest request) throws Exception{
		String url = request.getUrl();
		if (url == null || url.equals("")) {
			throw new Exception("url can not be null");
		}
		if (!url.startsWith("http")) {
			url = "http://" + url;
		}
		HttpURLConnection conn = null;
		URL mUrl = null;
		try {
			mUrl = new URL(url);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if (url.startsWith("https")){
			System.setProperty("http.keepAlive", "false");
		}
		conn = openConnection(mUrl, request);
		conn.setDoInput(true);
		conn.setDoOutput(true);

		conn.setConnectTimeout(mTimeOut);
		conn.setReadTimeout(mTimeOut);
		//System.setProperty("http.agent", "");//设置user-agent
		return conn;
	}
	
	private HttpURLConnection openConnection(URL url, IRequest request) {
		try {
			if (url.getProtocol().toLowerCase().equals("https")) {
				Ihttps https = request.getHttps();
				if (https == null || https.getKeyStore() == null) {
					Log.d("", "trust all");
					trustAllHosts();
				} else {
					Log.d("", "trust special");
					specialHosts(https);
				}
				
				HttpsURLConnection http = (HttpsURLConnection) url.openConnection();
//				((HttpsURLConnection) http).setHostnameVerifier(new HostnameVerifier() {
//					public boolean verify(String hostname, SSLSession session) {
//						return true;
//					}
//				});
				return http;
			} else {
				return (HttpURLConnection) url.openConnection();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	TrustManager[] xtmArray = new VtrustManager[] { new VtrustManager() };// 创建信任规则列表
	
	private void trustAllHosts() {
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, xtmArray, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			// 不进行主机名确认,对所有主机
			HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void specialHosts(Ihttps https) {
		final InputStream is = https.getKeyStore();
		try {
			final KeyStore ks = KeyStore.getInstance("BKS");
            ks.load(is, https.getPassword().toCharArray());
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
            tmf.init(ks);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new VHostnameVerifier(https));
        } catch (Exception e) {
        	
        } finally {
        	try {
        		is.close();
        	} catch (Exception e) {}
        }
	}
	
	HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};
	
	private void setProperty(IRequest request) throws Exception {
		final Hashtable<?, ?> table = request.getRequestProperty();
		if (table != null) {
			Enumeration<?> keys = table.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = (String) table.get(key);
				conn.setRequestProperty(key, value);
			}
		}
	}
	
	private void setContent(IRequest request) throws Exception {
		try {
			final byte[] content = request.getContent();
			if (content != null) {
				if (conn.getRequestProperty("Content-Type") == null) {
					final int contentLength = request.getContentLength();
					conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
					conn.setRequestProperty("Content-Length", Integer.toString(contentLength));
				}
				conn.setRequestMethod(POST);
				makeOutputStream();
				outputStream.write(content);
			} else {
				if (Double.valueOf(android.os.Build.VERSION.SDK) >= 14) {
					// 4.0以上setDoOutput一定要设置成false,不然GET方法会自动转成POST
					conn.setDoOutput(false);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void makeOutputStream() throws Exception {
		outputStream = conn.getOutputStream();
	}
	
	private void makeInputStream(int resCode) throws Exception {
		this.resCode = resCode;
		inputStream = resCode == HTTP_OK ? conn.getInputStream() : conn.getErrorStream();
	}
	
	private void cleanup() {
		try {
			if (inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
			if (outputStream != null) {
				outputStream.close();
				outputStream = null;
			}
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		} catch (Exception e) {

		}
	}
}
