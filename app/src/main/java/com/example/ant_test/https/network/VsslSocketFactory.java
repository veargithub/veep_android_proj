package com.example.ant_test.https.network;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.conn.ssl.SSLSocketFactory;
/**
 * @Title: MySSLSocketFactory.java
 * @Package com.example.ant_test.https.network
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-27 下午5:54:12
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class VsslSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public VsslSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

//        TrustManager tm = new X509TrustManager() {
//            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//            }
//
//            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//            }
//
//            public X509Certificate[] getAcceptedIssuers() {
//                return null;
//            }
//        };
        
        TrustManager tm = new VtrustManager();
        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
}
