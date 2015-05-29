package com.tongs.user.tab;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tongs.user.activity.BarcodeGenerator;
import com.tongs.user.activity.R;
import com.google.zxing.BarcodeFormat;
import com.tongs.user.asynctask.ImageDownloaderTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by JaeCheol on 15. 5. 19..
 */
public class ValidCouponTab extends Fragment
        implements View.OnClickListener
{
    View view;

    String authToken;
    String uid;
    String mobileNumber;

    String sid;

    LinearLayout noValidCouponLayout;
    RelativeLayout validCouponLayout;

    ImageView couponImageView;
    ImageView couponBarcode;
    Button couponTitle;
    TextView couponContents;
    TextView couponLocation;
    TextView couponTime;

    Bitmap barcode;

    BarcodeGenerator barcodeGenerator;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState)
    {
        view = inflater.inflate(R.layout.tab_validcoupon, container, false);

        initValidTab();
        getDataFromSharedPref();
        setBarcode();

        showCouponLayout(false);

        return view;
    }

    @Override
    public void onResume()   {
        getCouponInfo();
        super.onResume();
    }

    public void onClick(View v) {

        switch ( v.getId() )    {


        }
    }

    private void setBarcode()   {
        barcodeGenerator = new BarcodeGenerator();

        try {
            String barcodeContents = mobileNumber + "_" + uid;
            barcode = barcodeGenerator.encodeAsBitmap(barcodeContents, BarcodeFormat.CODE_128, 600, 200);
        } catch (Exception e) {
            e.printStackTrace();
        }

        couponBarcode.setImageBitmap(barcode);
    }

    private void initValidTab() {

        noValidCouponLayout = (LinearLayout)view.findViewById(R.id.noValidCouponLayout);
        validCouponLayout = (RelativeLayout)view.findViewById(R.id.validCouponLayout);

        couponImageView = (ImageView)view.findViewById(R.id.id_validCouponImage);
        couponBarcode = (ImageView)view.findViewById(R.id.id_validCouponBarcode);
        couponTitle = (Button)view.findViewById(R.id.id_validCouponTitle);
        couponLocation = (TextView)view.findViewById(R.id.id_validCouponLocation);
        couponContents = (TextView)view.findViewById(R.id.id_validCouponContents);
        couponTime = (TextView)view.findViewById(R.id.id_validCouponTime);
    }

    private void showCouponLayout(boolean flag) {

        if( flag )  {
            validCouponLayout.setVisibility(View.VISIBLE);
            noValidCouponLayout.setVisibility(View.GONE);
        }
        else    {
            validCouponLayout.setVisibility(View.GONE);
            noValidCouponLayout.setVisibility(View.VISIBLE);
        }
    }

    private void getDataFromSharedPref()    {

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        authToken = mPref.getString("auth_token", null);
        mobileNumber = mPref.getString("number", null);
        uid = mPref.getString("uid", null);
    }

    public void getCouponInfo() {
    String url = getText(R.string.api_server)
            + "user/coupon/get"
            + "?token=" + authToken;

    IHttpRecvCallback cb = new IHttpRecvCallback(){
        public void onRecv(String result) {
            try {
                JSONObject json = new JSONObject(result);
                String result_code = json.get("result_code").toString();
                Log.d("Hello", result_code);
                if( "-1".equals(result_code) )  {
                    showCouponLayout(false);
                    return;
                }
                else    {
                    showCouponLayout(true);
                }

                String sid = json.getString("target");
                String name = json.getString("store");
                String title = json.getString("title");
                String location = json.getString("location");
                String content = json.getString("content");
                String url = json.getString("img");
                String time = json.getString("end");
//                String time = String.valueOf(Integer.parseInt(json.getString("end"))
//                        - Integer.parseInt(json.getString("start")));

                couponTitle.setText(title);
                couponLocation.setText(location);
                couponContents.setText(content);
                couponTime.setText(time);

                // get Image and register in image view
                new ImageDownloaderTask(couponImageView).execute(url);
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

