package com.example.jaecheol.tongs;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by JaeCheol on 15. 5. 15..
 */
public class SummonActivity extends ActionBarActivity
                            implements View.OnClickListener
{
    Intent intent;

    Vibrator vibe;
    long[] vibePattern = {1000, 1500};

    Button checkButton;
    Button cancelButton;

    String hid;
    String sid;
    String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summon);

        summonInit();

        vibrate(true);
    }

    private void summonInit()   {
        intent = new Intent();
        hid = intent.getStringExtra("hid");
        sid = intent.getStringExtra("sid");
        authToken = intent.getStringExtra("authToken");

        vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        checkButton = (Button)findViewById(R.id.id_checkButton);
        checkButton.setOnClickListener(this);

        cancelButton = (Button)findViewById(R.id.id_cancelButton);
        cancelButton.setOnClickListener(this);
    }

    public void vibrate(boolean flag)   {
        if( flag ) {
            vibe.vibrate(vibePattern, 0);
        }
        else    {
            vibe.cancel();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_checkButton:
                this.setResult(RESULT_OK);
                break;

            case R.id.id_cancelButton:
                this.setResult(RESULT_CANCELED);
                break;
        }

        vibrate(false);

        this.finish();
    }


    public void getStoreInfo() {
        String url = getText(R.string.api_server)
                + "user/store/get"
                + "?token=" + authToken
                + "&hyper=" + hid
                + "&id=" + sid;

        IHttpRecvCallback cb = new IHttpRecvCallback(){
            public void onRecv(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String result_code = json.get("result_code").toString();
                    Log.d("Hello", result_code);
                    if( "-1".equals(result_code) )  {
                        return;
                    }

                    String name = json.getString("name");
                    String location = json.getString("location");


                }
                catch(Exception e){}
            }
        };
        new HttpTask(cb).execute(url);
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