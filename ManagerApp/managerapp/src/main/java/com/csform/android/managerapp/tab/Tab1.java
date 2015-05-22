package com.csform.android.managerapp.tab;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.csform.android.managerapp.R;

@SuppressLint("ValidFragment")
public class Tab1 extends Fragment {
		Context mContext;
		
		public Tab1(Context context) {
			mContext = context;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater,
				ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.activity_tab1, null);

			Button button1;
			button1 = (Button) view.findViewById(R.id.button1);
			
			button1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Uri uri = Uri.parse("http://whdghks913.tistory.com");
			    	Intent it  = new Intent(Intent.ACTION_VIEW,uri);
			    	startActivity(it);
				}
	    	});
			
	    	return view;
		}

}