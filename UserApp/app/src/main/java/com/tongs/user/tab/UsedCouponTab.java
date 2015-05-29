package com.tongs.user.tab;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tongs.user.activity.R;
import com.tongs.user.adapter.CouponAdapter;
import com.tongs.user.item.CouponItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by JaeCheol on 15. 5. 19..
 */
public class UsedCouponTab extends Fragment
{

    private ListView couponList;
    private CouponAdapter adapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private View view;

    String authToken;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState) {
        view = inflater.inflate(R.layout.tab_usedcoupon, container, false);

        initUsedCouponTab();
        getDataFromSharedPref();
        getCouponList();

        return view;
    }

    @Override
    public void onResume()   {
        getCouponList();
        super.onResume();
    }

    void initUsedCouponTab()   {
        couponList = (ListView)view.findViewById(R.id.id_couponList);

        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.id_usedcouponlayout);
        swipeRefreshLayout.setColorScheme(R.color.color1, R.color.color2, R.color.color3, R.color.color4);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCouponList();
            }
        });
    }

    private void getDataFromSharedPref()    {

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        authToken = mPref.getString("auth_token", null);
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

                    ArrayList<CouponItem> listData = new ArrayList<CouponItem>();

                    JSONArray couponJArray = json.getJSONArray("list");
                    for(int i=0; i<couponJArray.length(); i++)   {
                        JSONObject coupon = couponJArray.getJSONObject(i);

                        CouponItem newData = new CouponItem();
//                        newData.setUrl(coupon.getString("img"));
                        newData.setId(coupon.getString("target"));
                        newData.setTitle(coupon.getString("title"));
                        newData.setLocation(coupon.getString("location"));
                        newData.setDescription(coupon.getString("content"));

                        listData.add(newData);
                    }

                    couponList.setAdapter(new CouponAdapter(view.getContext(), listData));

                    swipeRefreshLayout.setRefreshing(false);
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
