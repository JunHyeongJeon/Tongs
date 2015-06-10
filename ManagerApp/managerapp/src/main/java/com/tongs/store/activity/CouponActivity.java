package com.tongs.store.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tongs.store.R;
import com.tongs.store.util.GlobalVar;
import com.tongs.store.util.Preference;

import org.json.JSONObject;


public class CouponActivity extends ActionBarActivity implements GlobalVar {

    public String mToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);


        Preference pref = Preference.getInstance();
        String token = pref.getValue(TOKEN, "");
        String couponId = pref.getValue(COUPON_ID, "");
        Log.v("onCreate", couponId);
        mToken = token;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_coupon, menu);
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

    private void getCoupon(){

//        String url;
//        url = getString(R.string.api_server) + getString(R.string.api_store_ticket_push)
//                + "token=" + mToken + "&user=" + user + "&pivot=" + mTodayDate
//                + "&people=" + people;
//        requestOnUIThread(PROTOCOL_STATUS_USER_PUSH, url, new OnHttpReceive() {
//            @Override
//            public void onReceive(int protocol, String data) {
//                try {
//                    JSONObject json = new JSONObject(data);
//                    String result_code = json.optString("result_code", null);
//
//                    if("0".equals(result_code))
//                        printToast("고객을 대기열에 추가하였습니다.");
//                    else if("-2".equals(result_code))
//                        printToast("중복 대기 회원입니다.");
//                    else {
//                        printToast("고객대기를 실패하였습니다.");
//                    }
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//        });

    }

}
