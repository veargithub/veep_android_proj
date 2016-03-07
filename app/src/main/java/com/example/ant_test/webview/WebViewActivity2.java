package com.example.ant_test.webview;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by chx7078 on 2015/8/26.
 */
public class WebViewActivity2 extends Activity{
    WebView mWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebView = new WebView(this);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this, "injectedObj");
        mWebView.loadUrl("file:///android_asset/index.html");//貌似只能直接读取根目录下的文件，再建个文件夹就不行
    }
}
