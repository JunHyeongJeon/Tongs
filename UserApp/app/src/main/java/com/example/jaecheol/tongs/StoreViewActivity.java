package com.example.jaecheol.tongs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * Created by JaeCheol on 15. 3. 31..
 */
public class StoreViewActivity extends ActionBarActivity
                               implements View.OnClickListener    {

    WebView webView;
    Toolbar storeviewToolbar;

    String sid;
    float resolution;

    Button pushButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeview);

        initWebView();
        setToolbar();
        initButton();
    }


    void setToolbar()   {
        storeviewToolbar = (Toolbar)findViewById(R.id.toolbar_storeview);

        if(storeviewToolbar != null) {
            setSupportActionBar(storeviewToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)  {
        switch( item.getItemId() ) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initWebView() {

        Intent intent = this.getIntent();
        sid = intent.getStringExtra("sid");

        webView = (WebView)findViewById(R.id.id_webStoreView);

        resolution = getResources().getDisplayMetrics().density;
        resolution = 2;
        String url = getString(R.string.storeview_url)
                + "?sid=" + sid
                + "&resolution=" + resolution;

        Log.d("HELLO", "STOREVIEW URL : " + url);

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

        webView.loadUrl(url);
        webView.addJavascriptInterface(this, "webBridge");
    }

    void initButton()   {
        pushButton = (Button)findViewById(R.id.id_pushButton);
        pushButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch ( view.getId() )    {
            case R.id.id_pushButton :
                Log.d("HELLO", "PUSH!!!!");
                break;
        }
    }
}
