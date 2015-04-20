package com.example.jaecheol.tongs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by JaeCheol on 15. 3. 31..
 */
public class StoreViewActivity extends ActionBarActivity {

    WebView webView;

    String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeview);

        initWebView();
    }

    private void initWebView() {

        Intent intent = this.getIntent();
        sid = intent.getStringExtra("sid");

        webView = (WebView)findViewById(R.id.id_webStoreView);

        WebSettings settings = webView.getSettings();
        WebViewClient webViewClient = new WebViewClient(){
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            };

            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            };

        };

        webView.setWebViewClient(webViewClient);
        settings.setJavaScriptEnabled(true);

        String url = "http://somabell01.cloudapp.net/tool/shop.php?sid=" + sid;

        webView.loadUrl(url);
        webView.addJavascriptInterface(this, "webBridge");
    }
}
