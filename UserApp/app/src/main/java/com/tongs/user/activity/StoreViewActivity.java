package com.tongs.user.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by JaeCheol on 15. 3. 31..
 */
public class StoreViewActivity extends ActionBarActivity
                               implements View.OnClickListener {

    WebView webView;
    Toolbar storeviewToolbar;

    String sid;
    String uid;
    String hid;
    String authToken;
    String peopleNumber;
    float resolution;

    AlertDialog dialog;
    Button pushButton;
    EditText peopleEditText;

    TextView currentPeopleText;
    TextView expectTimeText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storeview);

        getDataFromIntent();
        getDataFromSharedPref();

        initWebView();
        setToolbar();
        initComponent();
        getWaitingInfo();
    }


    void setToolbar() {
        storeviewToolbar = (Toolbar) findViewById(R.id.toolbar_storeview);

        if (storeviewToolbar != null) {
            setSupportActionBar(storeviewToolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDataFromIntent() {

        Intent intent = this.getIntent();
        sid = intent.getStringExtra("sid");
        hid = intent.getStringExtra("hid");
    }

    void getDataFromSharedPref()   {

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        uid = mPref.getString("uid", null);
        authToken = mPref.getString("auth_token", null);
    }

    private void initWebView() {

        webView = (WebView)findViewById(R.id.id_webStoreView);

        resolution = getResources().getDisplayMetrics().density;
        resolution = 2;
        String url = getString(R.string.storeview_url)
                + "?sid=" + sid
                + "&resolution=" + resolution;

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

    void initComponent()   {
        pushButton = (Button)findViewById(R.id.id_pushButton);
        pushButton.setOnClickListener(this);

        currentPeopleText = (TextView)findViewById(R.id.storeView_currentNum);
        expectTimeText = (TextView)findViewById(R.id.storeView_expectTime);

        setDialog();
    }

    @Override
    public void onClick(View view) {
        switch ( view.getId() )    {
            case R.id.id_pushButton :
                dialog.show();
                break;
        }
    }

    private void setDialog()
    {
        // AlertDialog 객체 선언
        dialog = createInputDialog();

        // Context 얻고, 해당 컨텍스트의 레이아웃 정보 얻기
        Context context = getApplicationContext();
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 레이아웃 설정
        View layout = layoutInflater.inflate(R.layout.dialog_barcodegenerate,
                (ViewGroup) findViewById(R.id.id_popup_root));
        peopleEditText = (EditText)layout.findViewById(R.id.id_popup_edittext);

        // Input 소프트 키보드 보이기
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // AlertDialog에 레이아웃 추가
        dialog.setView(layout);
    }

    private void getWaitingInfo()   {

        String url = getText(R.string.api_server)
                + "user/ticket/get"
                + "?token=" + authToken;

        IHttpRecvCallback cb = new IHttpRecvCallback(){
            public void onRecv(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String result_code = json.get("result_code").toString();
                    Log.d("Hello", result_code);
                    if( "-1".equals(result_code) )  {
                        return;
                    }

                    String currentNum = json.getString("people");
                    String expectTime = "SJC";//json.getString("expect_time");

                    modifyWaitInfo(currentNum, expectTime);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        new HttpTask(cb).execute(url);

    }

    private void modifyWaitInfo(String currentNum, String expectTime)    {

        currentPeopleText.setText(currentNum);
        expectTimeText.setText(expectTime);
    }


    private AlertDialog createInputDialog() {

        EditText edittext= new EditText(this);

        AlertDialog dialogBox = new AlertDialog.Builder(this)
                .setTitle("안내")
                .setMessage("몇명이서 오셨나요?")
                .setView(edittext)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 확인 버튼 눌렀을때 액션
                        if (peopleEditText != null)
                            peopleNumber = peopleEditText.getText().toString();

                        String url = getText(R.string.api_server)
                                + "user/ticket/push"
                                + "?token=" + authToken
                                + "&store=" + sid
                                + "&people=" + peopleNumber;

                        IHttpRecvCallback cb = new IHttpRecvCallback() {
                            public void onRecv(String result) {
                                try {
                                    JSONObject json = new JSONObject(result);
                                    String result_code = json.get("result_code").toString();
                                    Log.d("Hello", result_code);
                                    if ("-1".equals(result_code)) {
                                        Log.d("HELLO", "대기열 추가 실패");
                                        return;
                                    }
                                    Log.d("HELLO", "대기열 추가 성공");

                                } catch (Exception e) {
                                }
                            }
                        };
                        new HttpTask(cb).execute(url);

                    }
                })
                .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 취소 버튼 눌렀을때 액션 구현
                    }
                }).create();
        return dialogBox;
    }




    private static String convertStreamToString(InputStream is)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024*64);
        byte data[] = new byte[10240];
        while(true) {
            try {
                int len = is.read(data);
                if (len == -1)
                    break;
                baos.write(data, 0, len);
            } catch (Exception e) { }
        }
        String str = new String(baos.toByteArray());
        return str;
    }



    public InputStream getInputStreamFromUrl(String url) {
        InputStream content = null;
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            if(response.getStatusLine().getStatusCode() != 200)
            {
                // 네트워크 오류입니다.
                Log.d("Hello", "Network Error");
            }
            content = response.getEntity().getContent();
        } catch (Exception e) {
            Log.d("[GET REQUEST]", "Network exception", e);
        }

        return content;
    }

    interface IHttpRecvCallback
    {
        public void onRecv(String result);
    }

    class HttpTask extends AsyncTask<String , Void , String> {

        IHttpRecvCallback m_cb;
        HttpTask(IHttpRecvCallback cb)
        {
            m_cb = cb;
        }

        protected String doInBackground(String... params)
        {
            InputStream is = getInputStreamFromUrl(params[0]);

            String result = convertStreamToString(is);

            return result;
        }

        protected void onPostExecute(String result)
        {
            Log.d("Hello", result);
            if(m_cb != null)
            {
                m_cb.onRecv(result);
                return;
            }
        }
    }
}
