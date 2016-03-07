package com.example.ant_test.https;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;

import javax.net.ssl.TrustManagerFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;

import org.apache.http.conn.ssl.SSLSocketFactory;

import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.content.Context;

import com.example.ant_test.R;

public class SSLuseHttpClientTest {
	
	/** 是否双向认证 */
	private static final boolean TWO_WAY = false;
	
	private static final int PORT_ONE_WAY = 10002;//模拟器专用,test2_for_emulator
	
//	private static final int PORT = 10003;//过期的证书，模拟器专用，test3_for_emulator
	
	private static final int PORT_TWO_WAY = 10004;//模拟器专用，双向认证，服务器test2_for_emulator，客户端test2_client_for_emulator
	
	private static int PORT = PORT_ONE_WAY;
	
	public static final void TEST(Context context) {
		try {
		      // setup truststore to provide trust for the server certificate

		      // load truststore certificate
		      InputStream clientTruststoreIs = context.getResources().openRawResource(R.raw.test2_for_emulator);
		      KeyStore trustStore = null;
		      trustStore = KeyStore.getInstance("BKS");
		      trustStore.load(clientTruststoreIs, "password".toCharArray());

		      System.out.println("Loaded server certificates: " + trustStore.size());

		      // initialize trust manager factory with the read truststore
		      TrustManagerFactory trustManagerFactory = null;
		      trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		      trustManagerFactory.init(trustStore);

		      // setup client certificate
		      KeyStore keyStore = null;
		      
		      // initialize SSLSocketFactory to use the certificates
		      SSLSocketFactory socketFactory = null;
		      
		      if (TWO_WAY) {
		    	  PORT = PORT_TWO_WAY;
		    	  // load client certificate
			      InputStream keyStoreStream = context.getResources().openRawResource(R.raw.test2_client_for_emulator);
			     
			      keyStore = KeyStore.getInstance("PKCS12");
			      keyStore.load(keyStoreStream, "password".toCharArray());
	
			      System.out.println("Loaded client certificates: " + keyStore.size());
	
			      // initialize key manager factory with the read client certificate
			      KeyManagerFactory keyManagerFactory = null;
			      keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			      keyManagerFactory.init(keyStore, "password".toCharArray());
			      
			      socketFactory = new SSLSocketFactory(SSLSocketFactory.TLS, keyStore, "password",
				          trustStore, null, null);
		      } else {
		    	  socketFactory = new SSLSocketFactory(SSLSocketFactory.TLS, null, null,
				          trustStore, null, null);
		      }
		      
		      //socketFactory.setHostnameVerifier(new MyHostnameVerifier());

		      // Set basic data
		      HttpParams params = new BasicHttpParams();
		      HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		      HttpProtocolParams.setContentCharset(params, "UTF-8");
		      HttpProtocolParams.setUseExpectContinue(params, true);
		      HttpProtocolParams.setUserAgent(params, "Android app/1.0.0");

		      // Make pool
		      ConnPerRoute connPerRoute = new ConnPerRouteBean(12);
		      ConnManagerParams.setMaxConnectionsPerRoute(params, connPerRoute);
		      ConnManagerParams.setMaxTotalConnections(params, 20);

		      // Set timeout
		      HttpConnectionParams.setStaleCheckingEnabled(params, false);
		      HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
		      HttpConnectionParams.setSoTimeout(params, 20 * 1000);
		      HttpConnectionParams.setSocketBufferSize(params, 8192);

		      // Some client params
		      HttpClientParams.setRedirecting(params, false);

		      // Register http/s shemas!
		      SchemeRegistry schReg = new SchemeRegistry();
		      schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		      schReg.register(new Scheme("https", socketFactory, 443));
		      ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
		      DefaultHttpClient sClient = new DefaultHttpClient(conMgr, params);

		      String url = "https://10.0.2.2:" + PORT + "/veep/index.html";
		      HttpGet httpGet = new HttpGet(url);
		      System.out.println("url:" + url);
		      HttpResponse response = sClient.execute(httpGet);
		      HttpEntity httpEntity = response.getEntity();

		      InputStream is = httpEntity.getContent();
		      BufferedReader read = new BufferedReader(new InputStreamReader(is));
		      String query = null;
		      while ((query = read.readLine()) != null)
		        System.out.println(query);

		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		
	}
}
