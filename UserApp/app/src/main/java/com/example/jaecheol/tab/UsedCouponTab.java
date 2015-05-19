package com.example.jaecheol.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jaecheol.adapter.CouponAdapter;
import com.example.jaecheol.tongs.R;

/**
 * Created by JaeCheol on 15. 5. 19..
 */
public class UsedCouponTab extends Fragment
        implements View.OnClickListener
{

    private ListView couponList;
    private CouponAdapter adapter;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState)
    {
        view = inflater.inflate(R.layout.tab_usedticket, container, false);

        initCouponActivity();
        addDummyList();

        return view;
    }

    public void onClick(View v) {

        switch ( v.getId() )    {


        }
    }

    void initCouponActivity()   {

        adapter =  new CouponAdapter();
        couponList = (ListView)view.findViewById(R.id.id_couponList);
        couponList.setAdapter(adapter);
    }

    private void addDummyList() {
        adapter.add("0", "10", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 아메리카노 20%할인!", "오늘까지");
        adapter.add("1", "11", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 카페라떼 20%할인!", "오늘까지");
        adapter.add("2", "12", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 카페모카 20%할인!", "오늘까지");
        adapter.add("3", "13", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 자몽에이드 20%할인!", "오늘까지");
        adapter.add("4", "14", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 레몬에이드 20%할인!", "오늘까지");
        adapter.add("5", "15", "스타벅스 할인쿠폰!", "쿠폰을 가져오시면 카푸치노 20%할인!", "오늘까지");
    }
}
