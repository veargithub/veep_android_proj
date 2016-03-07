package com.example.ant_test.https.network.request;

import java.util.Hashtable;

import com.example.ant_test.https.network.listener.HttpListener;

/**
 * @Title: HttpRequest.java
 * @Package com.example.ant_test.https.network.request
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-13 下午4:14:25
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class VhttpRequest implements IRequest{
	
	public String url;

    public boolean needEncoding = true;

    //public String encoding = "GB2312";

    public static final String UTF_8 = "UTF-8";
    
    public String encoding = UTF_8;

    public short msg_id;

    public String postParams;
    
    public boolean needGZip = false;

    public Hashtable<String, String> requestProperties;
    
    public HttpListener httpListener;
    
    public Ihttps https;
    
    public int retryCount = 3;
    
    public VhttpRequest(String url) {
        this.url = url;
    }

    public VhttpRequest(String url, boolean needEncoding) {
        this.url = url;
        this.needEncoding = needEncoding;
    }
    
    public VhttpRequest(String url, boolean needEncoding, boolean needGZip) {
    	this.url = url;
    	this.needEncoding = needEncoding;
    	this.needGZip = needGZip;
    	requestProperties = new Hashtable<String, String>();
    	requestProperties.put("Accept-Encoding", "gzip");
    }

//    public void setHttpListener(HttpListener listener) {
//    	this.httpListener = listener;
//    }
    
	@Override
	public byte[] getContent() {
		if (postParams != null) {
            return postParams.getBytes();
		}
        return null;
	}

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public int getContentLength() {
		return postParams == null ? 0 : postParams.length();
	}

	@Override
	public Hashtable<?, ?> getRequestProperty() {
		return requestProperties;
	}

	@Override
	public HttpListener getHttpListener() {
		return httpListener;
	}

	@Override
	public int getRetryCount() {
		return retryCount;
	}

	@Override
	public Ihttps getHttps() {
		return https;
	}

}
