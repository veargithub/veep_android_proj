package com.example.ant_test.https.network.request;

import java.util.Hashtable;

import com.example.ant_test.https.network.listener.HttpListener;

/**
 * @Title: IRequest.java
 * @Package com.example.ant_test.https.network
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-13 上午9:58:34
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public interface IRequest {
	
	public byte[] getContent();

    public String getUrl();

    public int getContentLength();

    public Hashtable<?, ?> getRequestProperty();
    
    public HttpListener getHttpListener();
    
    public int getRetryCount();
    
    public Ihttps getHttps();
}
