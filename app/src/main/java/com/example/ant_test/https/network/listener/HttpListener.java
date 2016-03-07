package com.example.ant_test.https.network.listener;

import com.example.ant_test.https.network.request.IRequest;
import com.example.ant_test.https.network.response.IResponse;

/**
 * @Title: HttpListener.java
 * @Package com.example.ant_test.https.network.listener
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-13 上午10:01:05
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public interface HttpListener {
	
	public void onCompleted(IResponse response);
	
	public void onError(IRequest request);
	
	public boolean acceptResponse(IRequest request);
}
