package com.tongs.store.fragment;

import com.tongs.store.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Coming soon.
 * */
public class CalendarsFragment extends Fragment {

	public static CalendarsFragment newInstance() {
		return new CalendarsFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_calendars, container, false);
		
		return rootView;
	}
}
