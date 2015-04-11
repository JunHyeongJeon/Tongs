package com.example.jaecheol.tab;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jaecheol.tongs_v10.R;

/**
 * Created by JaeCheol on 15. 4. 7..
 */
public class Tab1 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState)
    {
        View v = inflater.inflate(R.layout.tab_1, container, false);
        return v;
    }
}
