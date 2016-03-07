package com.example.ant_test.https.network;

import android.util.Log;

import com.example.ant_test.https.hostname_verifier.VHostnameVerifier;
import com.example.ant_test.https.network.request.IRequest;
import com.example.ant_test.https.network.request.Ihttps;
import com.example.ant_test.https.network.response.IResponse;
import com.example.ant_test.https.network.response.VhttpResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.net.ssl.TrustManagerFactory;

/**
 * @Title: VRealHttpClient.java
 * @Package com.example.ant_test.https.network
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-26 上午10:39:29
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class VRealHttpClient {
	public static final String HEAD = "HEAD";
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final int HTTP_OK = 200;
	private int mTimeOut = 15000;
	
	private int resCode;
	
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
			HttpClient client = getAndroidHttpClient(mTimeOut, request);
			fire(client, request, response);
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
			//cleanup();
		}
	}
	
	private HttpClient getAndroidHttpClient(final int timeOut, IRequest request) throws Exception{
		URL url = makeUrl(request);
		boolean isHttps = isHttps(url);
	    HttpParams params = new BasicHttpParams();
	    params.setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, timeOut);
	    params.setIntParameter(CoreConnectionPNames.SO_TIMEOUT, timeOut);
	    params.setLongParameter(ConnManagerPNames.TIMEOUT, timeOut);
	    params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 1);
	    params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE, new ConnPerRouteBean(1));
	    params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
	    HttpProtocolParams.setUserAgent(params, "android-client-v1.0");
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setContentCharset(params, "utf8");
	    DefaultHttpClient defaultHttpClient = null;
	    if (isHttps) {//https
	    	Log.d(">>>>", "dhttpclient>>>>https");
	    	SchemeRegistry schemeRegistry = new SchemeRegistry();
		    schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	    	if (request.getHttps() == null) {//trust all
	    		Log.d(">>>>", "dhttpclient>>>>trust all");
	    		SSLSocketFactory sslSocketFactory = getSocketFactoryWhenTrustsAll();
	    		schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
	    	} else {//trust special
	    		Log.d(">>>>", "dhttpclient>>>>trust special");
	    		SSLSocketFactory sslSocketFactory = getSocketFactoryWhenTrustSpecial(request.getHttps());
	    		schemeRegistry.register(new Scheme("https", sslSocketFactory, 443));
	    		//TODO
	    	}
	    	ThreadSafeClientConnManager conman = new ThreadSafeClientConnManager(params, schemeRegistry);
	    	defaultHttpClient = new DefaultHttpClient(conman, params);
	    } else {//http
	    	Log.d(">>>>", "dhttpclient>>>>http");
	    	defaultHttpClient = new DefaultHttpClient(params);
	    }
	    return defaultHttpClient;
	}
	
	private URL makeUrl(IRequest request) throws Exception{
		String url = request.getUrl();
		if (url == null || url.equals("")) {
			throw new Exception("url can not be null");
		}
		if (!url.startsWith("http")) {
			url = "http://" + url;
		}
		URL mUrl = null;
//		try {
			mUrl = new URL(url);
//		} catch (IOException e1) {
//			e1.printStackTrace();
//		}
		if (url.startsWith("https")){
			System.setProperty("http.keepAlive", "false");
		}
		return mUrl;
	}
	
	private boolean isHttps(URL url) {
		return url.getProtocol().toLowerCase().equals("https");
	}
	
	private void fire(HttpClient client, IRequest request, IResponse response) throws Exception {
		final String url = request.getUrl();
		final byte[] content = request.getContent();
		if (content == null) {
			HttpGet get = new HttpGet(url);
			setProperty(get, request);
			HttpResponse hr = client.execute(get);
			response.setData(hr, request);
		} else {
			HttpPost post = new HttpPost(url);
			setProperty(post, request);
			post.setEntity(new ByteArrayEntity(content));
			HttpResponse hr = client.execute(post);
			response.setData(hr, request);
		}

	}
	
	private void setProperty(HttpRequestBase hrb, IRequest request) {
		final Hashtable<?, ?> table = request.getRequestProperty();
		if (table != null) {
			Enumeration<?> keys = table.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();
				String value = (String) table.get(key);
				hrb.addHeader(key, value);
			}
		}
	}
	
	private SSLSocketFactory getSocketFactoryWhenTrustsAll() throws Exception{
		KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(null, null);
        SSLSocketFactory sf = new VsslSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return sf;
	}
	
	private SSLSocketFactory getSocketFactoryWhenTrustSpecial(Ihttps https) throws Exception {
		final InputStream is = https.getKeyStore();
		try {
			final KeyStore trustStore = KeyStore.getInstance("BKS");
		    trustStore.load(is, https.getPassword().toCharArray());
		    TrustManagerFactory trustManagerFactory = null;
		    trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		    trustManagerFactory.init(trustStore);
		    SSLSocketFactory socketFactory = new SSLSocketFactory(SSLSocketFactory.TLS, null, null,
			          trustStore, null, null);
		    socketFactory.setHostnameVerifier(new VHostnameVerifier(https));
		    return socketFactory;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				is.close();
			} catch (Exception e) {}
		}
	}
}
