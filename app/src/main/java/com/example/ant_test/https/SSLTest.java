package com.example.ant_test.https;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.example.ant_test.R;

import android.content.Context;


public class SSLTest {
	
	private static final int PORT_ONE_WAY = 10002;//单向
	
	private static final int PORT_TWO_WAY = 10004;//双向
	
	private static int PORT = PORT_ONE_WAY;
	
	/**
	 * 
	 * @param context 
	 * @param useTwoWay true表示双向验证，false表示单向验证
	 */
	public static final void test(Context context, boolean useTwoWay) {
		try {
			InputStream clientTruststoreIs = context.getResources().openRawResource(R.raw.test2_for_emulator);
		    KeyStore trustStore = KeyStore.getInstance("BKS");
		    trustStore.load(clientTruststoreIs, "password".toCharArray());
		    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		    trustManagerFactory.init(trustStore);
		    KeyStore keyStore = null;
		    SSLSocketFactory socketFactory = null;
		    if (useTwoWay) {
		    	System.out.println("双向");
		    	PORT = PORT_TWO_WAY;
			    InputStream keyStoreStream = context.getResources().openRawResource(R.raw.test2_client_for_emulator);
			    keyStore = KeyStore.getInstance("PKCS12");
			    keyStore.load(keyStoreStream, "password".toCharArray());
			    KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			    keyManagerFactory.init(keyStore, "password".toCharArray());
			    socketFactory = new SSLSocketFactory(SSLSocketFactory.TLS, keyStore, "password", trustStore, null, null);
		    } else {
		    	PORT = PORT_ONE_WAY;
		    	System.out.println("单向");
		    	socketFactory = new SSLSocketFactory(SSLSocketFactory.TLS, null, null, trustStore, null, null);
		    }
		    HttpParams params = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
		    HttpConnectionParams.setSoTimeout(params, 20 * 1000);
		    SchemeRegistry schReg = new SchemeRegistry();
		    schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		    schReg.register(new Scheme("https", socketFactory, 443));
		    ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
		    DefaultHttpClient client = new DefaultHttpClient(conMgr, params);
		    String url = "https://10.0.2.2:" + PORT + "/veep/index.html";
		    HttpGet httpGet = new HttpGet(url);
		    System.out.println("url:" + url);
		    HttpResponse response = client.execute(httpGet);
		    HttpEntity httpEntity = response.getEntity();
		    InputStream is = httpEntity.getContent();
		    BufferedReader read = new BufferedReader(new InputStreamReader(is));
		    String str = null;
		    while ((str = read.readLine()) != null) {
		        System.out.println(str);
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
