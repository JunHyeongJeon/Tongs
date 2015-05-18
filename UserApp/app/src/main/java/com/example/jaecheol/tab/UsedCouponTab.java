package com.example.jaecheol.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jaecheol.tongs.R;

/**
 * Created by JaeCheol on 15. 5. 19..
 */
public class UsedCouponTab extends Fragment
        implements View.OnClickListener
{
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState)
    {
        View v = inflater.inflate(R.layout.tab_usedticket, container, false);

        return v;
    }

    public void onClick(View v) {

        switch ( v.getId() )    {


        }
    }
}
