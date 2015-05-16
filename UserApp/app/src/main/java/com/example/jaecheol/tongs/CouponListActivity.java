package com.example.jaecheol.tongs;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.jaecheol.adapter.CouponAdapter;

/**
 * Created by JaeCheol on 15. 5. 10..
 */
public class CouponListActivity extends ActionBarActivity{

    private ListView couponList;
    private CouponAdapter adapter;

    Toolbar couponToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        initCouponActivity();
        setToolbar();

        addDummyList();
    }

    void initCouponActivity()   {

        adapter =  new CouponAdapter(getApplicationContext());
        couponList = (ListView)findViewById(R.id.id_couponList);
        couponList.setAdapter(adapter);
    }

    void setToolbar()   {
        couponToolbar = (Toolbar)findViewById(R.id.toolbar_coupon);

        setSupportActionBar(couponToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void addDummyList() {
        adapter.add("0", "10", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 아메리카노 20%할인!", "오늘까지");
        adapter.add("1", "11", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 카페라떼 20%할인!", "오늘까지");
        adapter.add("2", "12", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 카페모카 20%할인!", "오늘까지");
        adapter.add("3", "13", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 자몽에이드 20%할인!", "오늘까지");
        adapter.add("4", "14", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 레몬에이드 20%할인!", "오늘까지");
        adapter.add("5", "15", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 카푸치노 20%할인!", "오늘까지");
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
}
