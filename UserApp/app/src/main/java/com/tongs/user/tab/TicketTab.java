package com.tongs.user.tab;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tongs.user.activity.R;

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
public class TicketTab extends Fragment
                  implements View.OnClickListener   {

    public WaitingTicket waitingTicket;

    Button refreshButton;

    String authToken;

    RelativeLayout waitTicketLayout;
    RelativeLayout noWaitTicketLayout;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState)
    {
        View v = inflater.inflate(R.layout.tab_ticket, container, false);

        initWaitingTicket(v);

        return v;
    }

    @Override
    public void onResume()   {
        getWaitingTicket();
        super.onResume();
    }


    private void initWaitingTicket(View v)   {
        waitingTicket = new WaitingTicket();
//                    String storeName = json.getJSONObject("store").getString("brand_name");

        waitTicketLayout = (RelativeLayout)v.findViewById(R.id.id_waitTicketLayout);
        noWaitTicketLayout = (RelativeLayout)v.findViewById(R.id.id_noWaitTicketLayout);

        waitingTicket.expectTime = (TextView)v.findViewById(R.id.id_expectText);
        waitingTicket.currentNum = (TextView)v.findViewById(R.id.id_currentText);
        waitingTicket.storeName =  (TextView)v.findViewById(R.id.id_storeNameText);
        waitingTicket.waitingNum = (Button)v.findViewById(R.id.id_waitingnum);

        showTicketLayout(false);
    }

    public void showTicketLayout(boolean isTicketExist)    {
        if( isTicketExist == true ) {
            noWaitTicketLayout.setVisibility(View.GONE);

            waitTicketLayout.setVisibility(View.VISIBLE);
//            refreshButton.setVisibility(View.VISIBLE);
        }
        else    {
            noWaitTicketLayout.setVisibility(View.VISIBLE);

            waitTicketLayout.setVisibility(View.GONE);
//            refreshButton.setVisibility(View.GONE);
        }
    }

    public void getWaitingTicket() {

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        authToken = mPref.getString("auth_token", null);

        String url = getText(R.string.api_server)
                + "user/ticket/get"
                + "?token=" + authToken;

        IHttpRecvCallback cb = new IHttpRecvCallback(){
            public void onRecv(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String result_code = json.get("result_code").toString();
                    if( "-1".equals(result_code) ) {
                        Toast.makeText(getActivity(),
                                "대기표가 존재하지 않습니다.",
                                Toast.LENGTH_LONG).show();
                        showTicketLayout(false);
                        return;
                    }

                    String waitingNum = json.get("number").toString();
                    String currentNum = json.get("people").toString();
                    String storeName = json.get("store").toString();

                    waitingTicket.setWaitingNum(waitingNum);
                    waitingTicket.setCurrentNum(currentNum);
                    waitingTicket.setStoreName(storeName);
//                    waitingTicket.setExpectTime(extraTime);

                    showTicketLayout(true);
                }
                catch(Exception e){}
            }
        };
        new HttpTask(cb).execute(url);

    }

    public void onClick(View v) {
        switch (v.getId()) {

        }
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

    public class WaitingTicket {
        Button waitingNum;
        TextView storeName;
        TextView currentNum;
        TextView expectTime;

        public void setWaitingNum(String text)  {
            waitingNum.setText(text);
        }
        public void setStoreName(String text)   {
            if( text.length() > 10 )    {
                storeName.setTextSize(26);
            }
            else    {
                storeName.setTextSize(36);
            }
            storeName.setText(text);
        }
        public void setCurrentNum(String text)  {
            currentNum.setText(text);
        }
        public void setExpectTime(String text)  {
            expectTime.setText(text);
        }

    }

}
