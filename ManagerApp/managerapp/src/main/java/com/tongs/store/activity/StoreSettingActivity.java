package com.tongs.store.activity;

import android.graphics.Bitmap;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Switch;
import android.view.View.OnClickListener;


import com.tongs.store.R;
import com.tongs.store.util.GlobalVar;
import com.tongs.store.util.Preference;

public class StoreSettingActivity extends ActionBarActivity implements OnClickListener, GlobalVar {

    Switch mAutoLoginSwitch;
    private WebView mWebView;
    private String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_setting);

        Preference pref = Preference.getInstance();
        String token = pref.getValue(TOKEN, "");

        mAutoLoginSwitch = (Switch) findViewById(R.id.auto_login_switch);
        mAutoLoginSwitch.setOnClickListener(this);
        mAutoLoginSwitch.setChecked(pref.getValue(ISAUTOLOGIN, false));

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);

        mToken = token;

        String url;
        url = getString(R.string.admin_server) + "token=" +
                mToken + "&next=" +
                getString(R.string.admin_store_setting);
        Log.v("adminURL",url);
       // url = "http://www.naver.com";

        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new CustomWebViewClient());
        //mWebView.setWebChromeClient(new CustomWebChormeClient());

    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onClick(View v)
    {
        if( v.getId() == R.id.auto_login_switch){
            Preference pref = Preference.getInstance();
            if ( mAutoLoginSwitch.isChecked() )
                pref.put(ISAUTOLOGIN, true);
            else
                pref.put(ISAUTOLOGIN, false);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_setting, menu);
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
    @Override
    public void onBackPressed(){
        this.finish();
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);
    }

    private class CustomWebChormeClient extends WebChromeClient {

        /**
         * 페이지를 로딩하는 현재 진행 상황을 전해줍니다.
         * newProgress  현재 페이지 로딩 진행 상황, 0과 100 사이의 정수로 표현.(0% ~ 100%)
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.i("WebView", "Progress: " + String.valueOf(newProgress));
            super.onProgressChanged(view, newProgress);
        }
    }

    private class CustomWebViewClient extends WebViewClient {


     @Override
     public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
     }


     @Override
     public void onLoadResource(WebView view, String url) {
     super.onLoadResource(view, url);
     }


     @Override
     public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
     Log.i("WebView", "History: " + url );
     super.doUpdateVisitedHistory(view, url, isReload);
     }



     @Override
     public void onPageFinished(WebView view, String url) {
     super.onPageFinished(view, url);
     }


     @Override
     public void onFormResubmission(WebView view, Message dontResend,
     Message resend) {
     super.onFormResubmission(view, dontResend, resend);
     }


     @Override
     public void onReceivedError(WebView view, int errorCode,
     String description, String failingUrl) {
     super.onReceivedError(view, errorCode, description, failingUrl);

     switch(errorCode) {
     case ERROR_AUTHENTICATION: break;               // 서버에서 사용자 인증 실패
     case ERROR_BAD_URL: break;                           // 잘못된 URL
     case ERROR_CONNECT: break;                          // 서버로 연결 실패
     case ERROR_FAILED_SSL_HANDSHAKE: break;    // SSL handshake 수행 실패
     case ERROR_FILE: break;                                  // 일반 파일 오류
     case ERROR_FILE_NOT_FOUND: break;               // 파일을 찾을 수 없습니다
     case ERROR_HOST_LOOKUP: break;           // 서버 또는 프록시 호스트 이름 조회 실패
     case ERROR_IO: break;                              // 서버에서 읽거나 서버로 쓰기 실패
     case ERROR_PROXY_AUTHENTICATION: break;   // 프록시에서 사용자 인증 실패
     case ERROR_REDIRECT_LOOP: break;               // 너무 많은 리디렉션
     case ERROR_TIMEOUT: break;                          // 연결 시간 초과
     case ERROR_TOO_MANY_REQUESTS: break;     // 페이지 로드중 너무 많은 요청 발생
     case ERROR_UNKNOWN: break;                        // 일반 오류
     case ERROR_UNSUPPORTED_AUTH_SCHEME: break; // 지원되지 않는 인증 체계
     case ERROR_UNSUPPORTED_SCHEME: break;          // URI가 지원되지 않는 방식
     }
     }

     @Override
     public void onReceivedHttpAuthRequest(WebView view,
     HttpAuthHandler handler, String host, String realm) {
     super.onReceivedHttpAuthRequest(view, handler, host, realm);
     }


     @Override
     public void onScaleChanged(WebView view, float oldScale, float newScale) {
     super.onScaleChanged(view, oldScale, newScale);
     }


     @Override
     public void onTooManyRedirects(WebView view, Message cancelMsg,
     Message continueMsg) {
     super.onTooManyRedirects(view, cancelMsg, continueMsg);
     }

     @Override
     public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
     super.onUnhandledKeyEvent(view, event);
     }

     @Override
     public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
     return super.shouldOverrideKeyEvent(view, event);
     }


     public boolean shouldOverrideUrlLoading(WebView view, String url) {
         return super.shouldOverrideUrlLoading(view, url);
     }
    }
}
