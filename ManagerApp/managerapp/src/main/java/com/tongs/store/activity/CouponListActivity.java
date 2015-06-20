package com.tongs.store.activity;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tongs.store.R;
import com.tongs.store.util.GlobalVar;
import com.tongs.store.util.Preference;

public class CouponListActivity extends ActionBarActivity implements GlobalVar {


    private WebView mWebView;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_list);


        Preference pref = Preference.getInstance();
        String token = pref.getValue(TOKEN, "");

        mWebView = (WebView) findViewById(R.id.webview);
        WebSettings setting = mWebView.getSettings();
        setting.setBuiltInZoomControls(false);
        setting.setSupportZoom(false);
        setting.setJavaScriptEnabled(true);
        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);

        mToken = token;

        String url;
        url = getString(R.string.admin_server) + "token=" +
                mToken + "&next=" +
                getString(R.string.admin_store_coupon_list);
        Log.v("adminURL", url);
        // url = "http://www.naver.com";

        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClientClass());
    }


    private class WebViewClientClass extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap fav) {
            Log.e("WV", "Start:" + url);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.e("WV", "Finish:"+url);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coupon_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
