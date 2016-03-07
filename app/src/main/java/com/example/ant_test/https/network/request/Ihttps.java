package com.example.ant_test.https.network.request;

import java.io.InputStream;

/**
 * @Title: Ihttps.java
 * @Package com.example.ant_test.https.network.request
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-14 下午1:34:15
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public interface Ihttps {
	
	public InputStream getKeyStore();
	
	public String getPassword();
	
	public boolean verifyHostname();
	
	public boolean verifyExpiration();
}
