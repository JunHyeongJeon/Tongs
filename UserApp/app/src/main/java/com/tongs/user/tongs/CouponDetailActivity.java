package com.tongs.user.tongs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.tongs.user.tongs.R;

/**
 * Created by JaeCheol on 15. 5. 15..
 */
public class CouponDetailActivity extends ActionBarActivity {

    Intent intent;
    String couponId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupondetail);

        initCouponDetailActivity();

    }

    private void initCouponDetailActivity() {
        intent = this.getIntent();
        couponId = intent.getStringExtra("coupon_id");

//        Toast.makeText(this.getApplicationContext(), "쿠폰 ID : " + couponId, Toast.LENGTH_LONG);
        Log.d("HELLO", "쿠폰 ID : "+couponId);
    }

}
