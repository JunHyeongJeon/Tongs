package com.tongs.user.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.tongs.user.adapter.CouponAdapter;
import com.tongs.user.item.CouponItem;
import com.tongs.user.tab.SlidingTabLayout;
import com.tongs.user.tab.ValidCouponTab;

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
public class CouponActivity extends ActionBarActivity
{

    Toolbar toolbar;

    ViewPager pager;

    CharSequence titles[] = {"유효한 쿠폰", "사용한 쿠폰"};
    int numOfTabs = 2;

    SlidingTabLayout tabs;

    private ListView couponList;

    private SwipeRefreshLayout swipeRefreshLayout;

    LinearLayout noCouponLayout;

    String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        setToolbar();

        initUsedCouponTab();
        getDataFromSharedPref();
        getCouponList();
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


    private void setToolbar() {

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.toolbar_coupon);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }


    void initUsedCouponTab()   {
        couponList = (ListView)findViewById(R.id.id_couponList);
        couponList.setOnItemClickListener(onClickListItem);

        noCouponLayout = (LinearLayout)findViewById(R.id.noCouponLayout);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.id_usedcouponlayout);
        swipeRefreshLayout.setColorScheme(R.color.color1, R.color.color2, R.color.color3, R.color.color4);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCouponList();
            }
        });

        showCouponLayout(false);
    }

    private void getDataFromSharedPref()    {

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        authToken = mPref.getString("auth_token", null);
    }

    private void showCouponLayout(boolean flag) {

        if( flag )  {
            couponList.setVisibility(View.VISIBLE);
            noCouponLayout.setVisibility(View.GONE);
        }
        else    {
            couponList.setVisibility(View.GONE);
            noCouponLayout.setVisibility(View.VISIBLE);
        }
    }

    private ListView.OnItemClickListener onClickListItem = new ListView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            CouponItem newsData = (CouponItem)couponList.getItemAtPosition(position);

            showCouponView(newsData.getId(), newsData.getValid());
        }
    };

    private void showCouponView(String cid, Boolean valid)   {
        if( !valid ) {
            Toast.makeText(getApplicationContext(), "사용 가능한 쿠폰이 아닙니다", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(CouponActivity.this, ValidCouponTab.class);
        intent.putExtra("cid", cid);
        startActivity(intent);
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
                        swipeRefreshLayout.setRefreshing(false);
                        return;
                    }

                    ArrayList<CouponItem> listData = new ArrayList<CouponItem>();

                    JSONArray couponJArray = json.getJSONArray("list");
                    if( couponJArray.length() > 0 ) {
                        showCouponLayout(true);
                    }
                    else    {
                        showCouponLayout(false);
                    }
                    for(int i=0; i<couponJArray.length(); i++)   {
                        JSONObject coupon = couponJArray.getJSONObject(i);

                        CouponItem newData = new CouponItem();
                        newData.setUrl(coupon.getString("img"));
                        newData.setId(coupon.getString("id"));
                        newData.setTitle(coupon.getString("title"));
                        newData.setLocation(coupon.getString("location"));
                        newData.setDescription(coupon.getString("content"));
                        newData.setValid(coupon.getBoolean("valid"));

                        listData.add(newData);
                    }

                    couponList.setAdapter(new CouponAdapter(getApplicationContext(), listData));

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
