package com.example.ant_test.https.network.request;

import java.io.InputStream;

import android.content.Context;

/**
 * @Title: Https.java
 * @Package com.example.ant_test.https.network.request
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-14 下午1:38:22
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class Https implements Ihttps{
	
	private final InputStream is;
	private final String password;
	private final boolean verifyHostname;
	private final boolean verifyExpiration;
	
	public Https(Context ctx, int resKeyStore, int resPassword) {
		this.is = ctx.getResources().openRawResource(resKeyStore);
		this.password = ctx.getResources().getString(resPassword);
		this.verifyHostname = true;
		this.verifyExpiration = false;
	}
	
	public Https(Context ctx, int resKeyStore, int resPassword, boolean verifyHostname, boolean verifyExpiration) {
		this.is = ctx.getResources().openRawResource(resKeyStore);
		this.password = ctx.getResources().getString(resPassword);
		this.verifyHostname = verifyHostname;
		this.verifyExpiration = verifyExpiration;
	}

	@Override
	public InputStream getKeyStore() {
		return is;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean verifyHostname() {
		return verifyHostname;
	}

	@Override
	public boolean verifyExpiration() {
		return verifyExpiration;
	}

}
