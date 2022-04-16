package com.sallet.cold.utils;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sallet.cold.R;
import com.sallet.cold.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.BindView;

public class TestWeb extends BaseActivity {
    @BindView(R.id.webView)
    WebView myWebView;

    private static final String LOGTAG = "TestWeb";
    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testweb);

        ButterKnife.bind(this);
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);//设置JS可用
        myWebView.addJavascriptInterface(new JsInteration(), "control");//传递对象进行交互
        myWebView.setWebChromeClient(new WebChromeClient() {});
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {//当页面加载完成
                super.onPageFinished(view, url);


                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){//当Android SDK>=4.4时
                    callEvaluateJavascript(myWebView);
                }else {
                    callMethod(myWebView);
                }
            }

        });
        myWebView.loadUrl("file:///android_asset/index.html");
    }

    private void callMethod(WebView webView) {
        String call = "javascript:sayHello()";

        call = "javascript:alertMessage(\"content\")";

        // call = "javascript:toastMessage(\"Hello World\")";

        //call = "javascript:sumToJava(1,2)";

        //call = "javascript:mult1(3,3)";

        webView.loadUrl(call);

    }

    public class JsInteration {

        @JavascriptInterface
        public void toastMessage(String message) {

            Toast.makeText(TestWeb.this, message, Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void onSumResult(int result) {

            System.out.println(LOGTAG+"_onSumResult result=" + result);
        }

        @JavascriptInterface
        public void onMultResult(int result) {

            System.out.println(LOGTAG+"_onMultResult result=" + result);
        }
    }

    private void callEvaluateJavascript(WebView webView) {

        webView.evaluateJavascript("address('prepare wet soldier fall pool midnight crash boy civil assist usual mesh')", new ValueCallback<String>() {

            @Override
            public void onReceiveValue(String value) {

                System.out.println(LOGTAG+"_onReceiveValue value=" + value);
                Toast.makeText(TestWeb.this, "onReceiveValue value=" + value, Toast.LENGTH_LONG).show();
            }});
    }
}
