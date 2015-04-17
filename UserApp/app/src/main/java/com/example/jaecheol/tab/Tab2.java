package com.example.jaecheol.tab;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jaecheol.tongs_v10.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by JaeCheol on 15. 4. 7..
 */
public class Tab2 extends Fragment {

    WaitingTicket waitingTicket;

    String authToken;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState)
    {
        View v = inflater.inflate(R.layout.tab_2, container, false);

        initWaitingTicket(v);

        getWaitingTicket(v);


        return v;
    }

    private void initWaitingTicket(View v)   {
        waitingTicket = new WaitingTicket();

        waitingTicket.expectTime = (TextView)v.findViewById(R.id.id_expectText);
        waitingTicket.currentNum = (TextView)v.findViewById(R.id.id_currentNum);
        waitingTicket.storeName =  (TextView)v.findViewById(R.id.id_storeNameText);
        waitingTicket.waitingNum = (Button)v.findViewById(R.id.id_waitingnum);

    }

    private void getWaitingTicket(View v) {

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(v.getContext());
        authToken = mPref.getString("auth_token", null);

        String url = getText(R.string.Server_URL)
                + "user/waiting/get"
                + "?token=" + authToken;

        IHttpRecvCallback cb = new IHttpRecvCallback(){
            public void onRecv(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String result_code = json.get("result_code").toString();
                    Log.d("Hello", result_code);
                    if( "-1".equals(result_code) )
                        return;

                    String waitingNum = json.getString("ticket");
                    String currentNum = json.getString("");
                    String storeName = json.getString("");
                    String extraTime = json.getString("");

                    waitingTicket.setWaitingNum(waitingNum);
                    waitingTicket.setCurrentNum(currentNum);
                    waitingTicket.setStoreName(storeName);
                    waitingTicket.setExpectTime(extraTime);
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
            Log.d("Hello", "Start");
            InputStream is = getInputStreamFromUrl(params[0]);

            Log.d("Hello", "Get");
            String result = convertStreamToString(is);

            return result;
        }

        protected void onPostExecute(String result)
        {
            if(m_cb != null)
            {
                m_cb.onRecv(result);
                return;
            }
            Log.d("Hello", result);
        }
    }
    private class WaitingTicket {
        Button waitingNum;
        TextView storeName;
        TextView currentNum;
        TextView expectTime;

        void setWaitingNum(String text)  {
            waitingNum.setText(text);
        }
        void setStoreName(String text)   {
            storeName.setText(text);
        }
        void setCurrentNum(String text)  {
            currentNum.setText(text);
        }
        void setExpectTime(String text)  {
            expectTime.setText(text);
        }

    }
}
