package com.example.ant_test.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * @Title: WebViewActivity.java
 * @Package com.example.ant_test.webview
 * @Description: TODO(description)
 * @author Chenxiao
 * @date 2014-4-30 上午10:42:29
 * @version V1.0
 * @company East Money Information Co., Ltd.
 */
public class WebViewActivity extends Activity{
	private WebView wv;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		WebView webview = new WebView(this);
		setContentView(webview);
		 

		webview.getSettings().setJavaScriptEnabled(true);

		final Activity activity = this;
		webview.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				// Activities and WebViews measure progress with different scales.
		    	// The progress meter will automatically disappear when we reach 100%
				Log.d(">>>>", progress + "");
		    	activity.setProgress(progress);
			}
			
		});
		webview.setWebViewClient(new WebViewClient() {
		    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		    	Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
		    }
		    public void onPageFinished(WebView view, String url) {
		    	Log.d(">>>>", "onPageFinished");
				super.onPageFinished(view, url);
			}
		});

		webview.loadUrl("http://developer.android.com/");

	}

}
