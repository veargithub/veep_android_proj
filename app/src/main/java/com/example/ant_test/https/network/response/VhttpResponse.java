package com.example.ant_test.https.network.response;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.util.Log;
import com.example.ant_test.https.network.request.VhttpRequest;
import com.example.ant_test.https.network.request.IRequest;

/**
 * @Title: HttpResponse.java
 * @Package com.example.ant_test.https.network.response
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-11-13 下午4:05:27
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class VhttpResponse implements IResponse{
	
	public String content;
	private int length = 0;
	public short msg_id = -1;

	@Override
	public void setData(HttpURLConnection conn, InputStream inputStream, IRequest req) throws Exception {
		VhttpRequest request = (VhttpRequest) req;
		StringBuffer buffer = new StringBuffer();
		String charSet = request.encoding;
		final String iContentType = conn.getHeaderField("content-type");
		if (iContentType != null) {
			try {
				String[] split1 = iContentType.split(";");
				charSet = split1[1].split("=")[1];
			} catch (Exception e) {}
		}
		Log.v("Encoding", "" + conn.getHeaderField("content-type"));
		InputStreamReader inputStreamReader;
		final String zip = conn.getHeaderField("Content-Encoding");
		if (request.needEncoding) {
			if ("gzip".equals(zip)) {
				Log.d("", "ggggggggzip");
				inputStream = new GZIPInputStream(inputStream);
			}
			inputStreamReader = new InputStreamReader(inputStream, charSet);
		} else {
			inputStreamReader = new InputStreamReader(inputStream);
		}
		int oneChar;
		while ((oneChar = inputStreamReader.read()) > -1) {
			buffer.append((char) oneChar);
		}

		content = buffer.toString();
		length = content.getBytes().length;
		Log.v("HttpClient", "Response Length = " + length);
		msg_id = request.msg_id;	
	}

	@Override
	public void setData(HttpResponse httpResponse, IRequest req)
			throws Exception {
		VhttpRequest request = (VhttpRequest) req;
		StringBuffer buffer = new StringBuffer();
		HttpEntity entity = httpResponse.getEntity();
		String iContentType = entity.getContentType().getValue();
		String charSet = request.encoding;
		if (iContentType != null) {
			try {
				String[] split1 = iContentType.split(";");
				charSet = split1[1].split("=")[1];
			} catch (Exception e) {}
		}
		String zip = "";
		if (entity.getContentEncoding() != null) {
			zip = entity.getContentEncoding().getValue();
		}
		Log.d(">>>>", "HttpResponse:" + iContentType + " " + charSet + " " + zip);
		InputStream inputStream = entity.getContent();
		InputStreamReader inputStreamReader;
		if (request.needEncoding) {
			if ("gzip".equals(zip)) {
				Log.d("", "ggggggggzip");
				inputStream = new GZIPInputStream(inputStream);
			}
			inputStreamReader = new InputStreamReader(inputStream, charSet);
		} else {
			inputStreamReader = new InputStreamReader(inputStream);
		}
		int oneChar;
		while ((oneChar = inputStreamReader.read()) > -1) {
			buffer.append((char) oneChar);
		}

		content = buffer.toString();
		length = content.getBytes().length;
		Log.v("HttpClient", "Response Length = " + length);
		msg_id = request.msg_id;
	}
}
