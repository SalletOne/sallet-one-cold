package com.sallet.cold.utils.js;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;

import com.google.gson.JsonSyntaxException;
import com.sallet.cold.utils.JsonUtils;
import com.sallet.cold.utils.L;

import org.web3j.utils.Strings;

import java.util.List;

public abstract class JsWrapperHelper implements JsWrapper {
    public static final String JAVASCRIPT = "javascript:";
    public static final String TAG = "JsWrapperHelper";
    protected Callback mCachedCallBack;
    private final Context mContext;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final String mJsFilePath;
    protected final WebView mWebView;

    public interface Callback {
        void call(String str, JsResult jsResult);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public JsWrapperHelper(Context context, String str,String str2,final Callback callback) {
        this.mContext = context;
        this.mJsFilePath = str;
        this.mWebView = new WebView(context);
        this.mWebView.addJavascriptInterface(this, "android");
        WebSettings settings = this.mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        this.mWebView.setWebViewClient(new WebViewClient() {
            /* class com.bitbill.www.common.base.model.js.JsWrapperHelper.AnonymousClass1 */

            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                webView.loadUrl(str);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    executeJS("getAdaAddress('" + str2 + "')",callback);
                }
            }
        });
        this.mWebView.setWebChromeClient(new WebChromeClient() {
            /* class com.bitbill.www.common.base.model.js.JsWrapperHelper.AnonymousClass2 */

            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                if (consoleMessage.message().matches("Uncaught \\w*Error")) {
                    String str = JsWrapperHelper.TAG;
                    L.d(str, "onConsoleMessage() called with: consoleMessage = [" + consoleMessage + "]");
                }
                return super.onConsoleMessage(consoleMessage);
            }
        });
        this.mWebView.loadUrl(str);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void executeAsync(String str, Callback callback) {
        if (!Strings.isEmpty(str)) {
            str.substring(0, str.indexOf("("));
            L.d(TAG, "loadUrl() called with: jsFunction = [" + str + "], callback = [" + callback + "]");
            this.mCachedCallBack = callback;
            this.mWebView.evaluateJavascript("javascript:" + str, null);
            return;
        }
        throw new NullPointerException("jsFuction is null");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void executeJS(String str, final Callback callback) {
        if (!Strings.isEmpty(str)) {
            final String str2 = "javascript:" + str;
            final String substring = str.substring(0, str.indexOf("("));
            L.d(TAG, "executeJS() called with: jsFunction = [" + str + "], callback = [" + callback + "]");
            if (Looper.myLooper() != Looper.getMainLooper()) {
                new Handler(this.mContext.getMainLooper()).post(new Runnable() {
                    /* class com.bitbill.www.common.base.model.js.JsWrapperHelper.AnonymousClass3 */

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void run() {
                        JsWrapperHelper.this.mWebView.evaluateJavascript(str2, new ValueCallback<String>() {
                            /* class com.bitbill.www.common.base.model.js.JsWrapperHelper.AnonymousClass3.AnonymousClass1 */

                            public void onReceiveValue(String str) {
                                JsWrapperHelper.this.parseJsResult(substring, callback, str);
                            }
                        });
                    }
                });
            } else {
                this.mWebView.evaluateJavascript(str2, new ValueCallback<String>() {
                    /* class com.bitbill.www.common.base.model.js.JsWrapperHelper.AnonymousClass4 */

                    public void onReceiveValue(String str) {
                        JsWrapperHelper.this.parseJsResult(substring, callback, str);
                    }
                });
            }
        } else {
            throw new NullPointerException("jsFuction is null");
        }
    }

    /* access modifiers changed from: protected */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void executeJS(final String str, List<Object> list, final Callback callback) {
        if (!Strings.isEmpty(str)) {
            StringBuilder sb = new StringBuilder("javascript:");
            sb.append(str);
            sb.append("(");
            if (list.size()>0) {
                for (int i = 0; i < list.size(); i++) {
                    if (i > 0) {
                        sb.append(",");
                    }
                    Object obj = list.get(i);
                    if (obj instanceof String) {
                        sb.append("'");
                        sb.append(obj);
                        sb.append("'");
                    } else {
                        sb.append(obj);
                    }
                }
            }
            sb.append(")");
            final String sb2 = sb.toString();
            String str2 = TAG;
            L.d(str2, "executeJS() called with: jsFunction = [" + sb2 + "], callback = [" + callback + "]");
            if (Looper.myLooper() != Looper.getMainLooper()) {
                new Handler(this.mContext.getMainLooper()).post(new Runnable() {
                    /* class com.bitbill.www.common.base.model.js.JsWrapperHelper.AnonymousClass5 */

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void run() {
                        JsWrapperHelper.this.mWebView.evaluateJavascript(sb2, new ValueCallback<String>() {
                            /* class com.bitbill.www.common.base.model.js.JsWrapperHelper.AnonymousClass5.AnonymousClass1 */

                            public void onReceiveValue(String str) {
                                JsWrapperHelper.this.parseJsResult(str, callback, str);
                            }
                        });
                    }
                });
            } else {
                this.mWebView.evaluateJavascript(sb2, new ValueCallback<String>() {
                    /* class com.bitbill.www.common.base.model.js.JsWrapperHelper.AnonymousClass6 */

                    public void onReceiveValue(String str) {
                        JsWrapperHelper.this.parseJsResult(str, callback, str);
                    }
                });
            }
        } else {
            throw new NullPointerException("jsFunctionName is null");
        }
    }

    /* access modifiers changed from: protected */
    public void parseJsResult(String str, Callback callback, String str2) {
        JsResult jsResult = null;
        L.d(TAG, "parseJsResult() called with:jsFunctionName = [" + str + "], value = [" + str2 + "]");
        if (callback != null) {
            if (!Strings.isEmpty(str2)) {
                if (str2.startsWith("\"")) {
                    str2 = str2.substring(1);
                }
                if (str2.endsWith("\"")) {
                    str2 = str2.substring(0, str2.length() - 1);
                }
                if (str2.contains("\\\"")) {
                    str2 = str2.replace("\\\"", "\"");
                }
                if (str2.contains("\\'")) {
                    str2 = str2.replace("\\'", "'");
                }
                if (str2.contains("\\\\")) {
                    str2 = str2.replace("\\\\", "\\");
                }
                try {
                    jsResult = (JsResult) JsonUtils.deserialize(str2, JsResult.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
//                this.mHandler.post(new Runnable(str, jsResult) {
//                    /* class com.bitbill.www.common.base.model.js.$$Lambda$JsWrapperHelper$041Di6Z06K0O3O4cJR7GSSAN4U */
//                    private final /* synthetic */ String f$1;
//                    private final /* synthetic */ JsResult f$2;
//
//                    {
//                        this.f$1 = r2;
//                        this.f$2 = r3;
//                    }
//
//                    public final void run() {
//                        JsWrapperHelper.lambda$parseJsResult$0(Callback.this, this.f$1, this.f$2);
//                    }
//                });
            }
            jsResult = null;
//            this.mHandler.post(new Runnable(str, jsResult) {
//                /* class com.bitbill.www.common.base.model.js.$$Lambda$JsWrapperHelper$041Di6Z06K0O3O4cJR7GSSAN4U */
//                private final /* synthetic */ String f$1;
//                private final /* synthetic */ JsResult f$2;
//
//                {
//                    this.f$1 = r2;
//                    this.f$2 = r3;
//                }
//
//                public final void run() {
//                    JsWrapperHelper.lambda$parseJsResult$0(Callback.this, this.f$1, this.f$2);
//                }
//            });
        }
    }

    @JavascriptInterface
    public void generateEosKeyPairCallback(String str) {
        Callback callback = this.mCachedCallBack;
        if (callback != null) {
            parseJsResult("generateEosKeyPairCallback", callback, str);
        }
    }
}
