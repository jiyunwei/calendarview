package com.example.myviewlist.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myviewlist.R;

public class WebViewActivity extends AppCompatActivity {
    private static final String TAG = "TestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Log.d(TAG, "onCreate: ");

        WebView mWebView = findViewById(R.id.mWebView);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "MYOBJECT");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                Log.d(TAG, "onPageFinished: -------------------------------");

                StringBuilder sb = new StringBuilder();

// You can also inject the JS code through WebView, when release the codes below。
                sb.append("function test(){");
                sb.append("var body = document.getElementsByTagName('body')[0];");
                sb.append("body.style.backgroundColor = '#00ff00';");
                sb.append("body.style.background = '#00ff00';");

                sb.append("var pTag = document.getElementsByTagName('p');");
                sb.append("for (var i = 0; i < pTag.length; i++) {");
                sb.append("pTag[i].style.color='#ff0000'");
                sb.append("}");

                sb.append("var pdiv = document.getElementsByTagName('div');");
                sb.append("for (var i = 0; i < pdiv.length; i++) {");
                sb.append("pdiv[i].style.color='#ff0000'");
                sb.append("}");

                sb.append("var aT = document.getElementsByTagName('a');");
                sb.append("for (var i = 0; i < aT.length; i++) {");
                sb.append("aT[i].style.color='#ff0000'");
                sb.append("}");

//                sb.append("if (objAccount != null) {str += objAccount.value;}");
//                sb.append("window.MYOBJECT.processHTML(str);");
//                sb.append("return true;");
                sb.append("};");

                view.loadUrl("javascript:" + sb.toString());
                view.loadUrl("javascript:test()");

            }



        });

//        String sUrl = "file:///android_asset/new_file.html";
        String sUrl = "https://cache.amap.com/h5/h5/publish/241/index.html";
        mWebView.loadUrl(sUrl);




    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        public void processHTML(String html) {
            AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
            builder.setTitle("AlertDialog from app")
                    .setMessage(html)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }
                            })
                    .setCancelable(false).show();

        }
    }
}