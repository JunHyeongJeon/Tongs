package com.csform.android.managerapp.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.csform.android.managerapp.R;

@SuppressLint("ValidFragment")
public class Tab3 extends Fragment {
		Context mContext;
		
		public Tab3(Context context) {
			mContext = context;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater,
				ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.activity_tab3, null);

	    	return view;
		}

}