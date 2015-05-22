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
import android.widget.ListView;

import com.example.jaecheol.adapter.CouponAdapter;
import com.example.jaecheol.tongs.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by JaeCheol on 15. 5. 19..
 */
public class UsedCouponTab extends Fragment
        implements View.OnClickListener
{

    private ListView couponList;
    private CouponAdapter adapter;

    private View view;

    String authToken;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState) {
        view = inflater.inflate(R.layout.tab_usedticket, container, false);

        initUsedCouponTab();
        getDataFromSharedPref();
        getCouponList();
//        addDummyList();

        return view;
    }

    @Override
    public void onResume()   {
        getCouponList();
        super.onResume();
    }

    public void onClick(View v) {

        switch ( v.getId() )    {


        }
    }

    void initUsedCouponTab()   {

        adapter =  new CouponAdapter();
        couponList = (ListView)view.findViewById(R.id.id_couponList);
        couponList.setAdapter(adapter);
    }

    private void getDataFromSharedPref()    {

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        authToken = mPref.getString("auth_token", null);
    }

    private void addDummyList() {
        adapter.add("0", "10", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 아메리카노 20%할인!", "오늘까지");
        adapter.add("1", "11", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 카페라떼 20%할인!", "오늘까지");
        adapter.add("2", "12", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 카페모카 20%할인!", "오늘까지");
        adapter.add("3", "13", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 자몽에이드 20%할인!", "오늘까지");
        adapter.add("4", "14", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 레몬에이드 20%할인!", "오늘까지");
        adapter.add("5", "15", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 카푸치노 20%할인!", "오늘까지");
    }

    public void getCouponList() {
        String url = getText(R.string.api_server)
                + "user/coupon/list"
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

                    String sid = null;
                    String couponId = null;
                    String title = null;
                    String content = null;
                    String time = null;

                    JSONArray couponJArray = json.getJSONArray("list");
                    for(int i=0; i<couponJArray.length(); i++)   {
                        JSONObject coupon = couponJArray.getJSONObject(i);


                        title = coupon.get("title").toString();
                        content = coupon.get("content").toString();

                        adapter.add(sid, couponId, title, content, time);
                        adapter.notifyDataSetChanged();
                        Log.d("HELLO", sid + "  " + couponId + "  " + title + "  "
                                + content + "  " + time);

                    }

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
