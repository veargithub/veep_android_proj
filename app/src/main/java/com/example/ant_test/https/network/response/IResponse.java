package com.example.ant_test.https.network.response;

import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.http.HttpResponse;

import com.example.ant_test.https.network.request.IRequest;

/**
 * @Title: IResponse.java
 * @Package com.example.ant_test.https.network
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-13 上午10:01:53
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public interface IResponse {
	public void setData(HttpURLConnection conn, InputStream inputStream, IRequest request) throws Exception;
	public void setData(HttpResponse httpResponse, IRequest request) throws Exception;
}
