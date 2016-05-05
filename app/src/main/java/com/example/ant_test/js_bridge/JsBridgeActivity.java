package com.example.ant_test.js_bridge;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.ant_test.webview.WebViewActivity;


/**
 * Created by 3020mt on 2016/3/3.
 * js bridge demo
 * java和js相互调用
 */
public class JsBridgeActivity extends Activity {

    private String param1 = "java param";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        final WebView webview = new WebView(this);
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
                webview.loadUrl("javascript:javaCallJs(" + "'hello world'" + ")");
            }
        });

        webview.addJavascriptInterface(new MyCustomHandler(this), "Bridge");

        webview.loadUrl("file:///android_asset/jsBridge.html");



    }

    class MyCustomHandler {
        Context context;

        MyCustomHandler(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void doSomething() {//js掉java
            Log.d("MyCustomHandler", "doSomething@MyCustomHandler");
            Toast.makeText(this.context, "doSomething@MyCustomHander", Toast.LENGTH_LONG).show();
        }

        public void doSomething2() {//js掉java
            Log.d("MyCustomHandler", "doSomething2@MyCustomHandler");
            Toast.makeText(this.context, "doSomething2@MyCustomHandler", Toast.LENGTH_LONG).show();
        }
    }
}
