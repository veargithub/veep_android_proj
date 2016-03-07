package com.example.ant_test.https.hostname_verifier;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.regex.Pattern;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import org.apache.http.conn.ssl.X509HostnameVerifier;

import com.example.ant_test.https.network.request.Ihttps;

import android.util.Log;

/**
 * @Title: MyHostnameVerifier.java
 * @Package com.example.ant_test.https.hostname_verifier
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-17 下午3:13:41
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class VHostnameVerifier implements X509HostnameVerifier{
	private Ihttps https;
	
	public VHostnameVerifier(Ihttps https) {
		this.https = https;
	}

	@Override
	public boolean verify(String host, SSLSession session) {
		System.out.println("verfify1");
		try {
			javax.security.cert.X509Certificate[] certs = session.getPeerCertificateChain();
			return verify(host, certs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void verify(String host, SSLSocket ssl) throws IOException {
		System.out.println("verfify2");
		javax.security.cert.X509Certificate[] certs = ssl.getSession().getPeerCertificateChain();
		if (verify(host, certs)) {
			return;
		}
	    throw new IOException("Mismatched host in SSL certificate");
	}

	@Override
	public void verify(String host, X509Certificate cert) throws SSLException {
		System.out.println("verfify3");
		String dname = cert.getSubjectDN().getName();
		if (verifyHostname(host, dname)) {
			return;
		}
		throw new SSLException("Mismatched host in SSL certificate");
	}

	@Override
	public void verify(String host, String[] cns,
			String[] subjectAlts) throws SSLException {
		System.out.println("verfify4");
		
	}
	
	private boolean verify(String host, javax.security.cert.X509Certificate[] certs) {
		boolean verifyHostname = true;
		boolean verifyExpiration = false;
		if (this.https != null) {
			verifyHostname = https.verifyHostname();
			verifyExpiration = https.verifyExpiration();
		}
		Log.d(">>>>", "verify:" + verifyHostname + " " + verifyExpiration);
		for (javax.security.cert.X509Certificate cert : certs) {
			String dname = cert.getSubjectDN().getName();
			Date notAfter = cert.getNotAfter();
			if (!verifyHostname || verifyHostname(host, dname) ) {
				if (!verifyExpiration || verifyExpiration(notAfter)) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean verifyHostname(String host, String dname) {
		String[] tokens = dname.split(",");
		for(String token : tokens) {
	       	Log.d(">>>>", "token:" + token);
	        String[] keyVal = token.split("=");
	        if(keyVal.length > 1 && keyVal[0].equals("CN") && domainMatch(host, keyVal[1])) {
	        	return true;
	        }
	    }
		return false;
	}

	private boolean domainMatch(String host, String strMatcher) {
		if (strMatcher == null || host == null) return false;
		String strDomainPattern = strMatcher.replaceAll("[*]+", "[\\\\w\\\\d]+");
		Pattern pattern = Pattern.compile(strDomainPattern);
		return pattern.matcher(host).matches();
	}
	
	private boolean verifyExpiration(Date notAfter) {
		Date now = new Date();
		if (now.after(notAfter)) {
			Log.d(">>>>", "expired");
			return false;
		} else {
			Log.d(">>>>", "not expired");
		}
		return true;
	}
}
