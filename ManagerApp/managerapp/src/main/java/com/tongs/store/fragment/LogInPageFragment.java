package com.tongs.store.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.tongs.store.activity.LogInPageActivity;
import com.tongs.store.R;
import com.tongs.store.adapter.SubcategoryAdapter;

public class LogInPageFragment extends Fragment implements OnItemClickListener {

	private ListView mListView;
	private List<String> mLogInPages;

	public static final String LOGIN_PAGE_AND_LOADERS_CATEGORY = "com.tongs.store.LogInPageAndLoadersActivity";

	public static LogInPageFragment newInstance() {
		return new LogInPageFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLogInPages = new ArrayList<String>();
		mLogInPages.add(LogInPageActivity.LIGHT);
		mLogInPages.add(LogInPageActivity.DARK);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login_page_and_loaders, container, false);
		
		mListView = (ListView) rootView.findViewById(R.id.list_view);
		mListView.setAdapter(new SubcategoryAdapter(getActivity(), mLogInPages));
		mListView.setOnItemClickListener(this);
		
		return rootView;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(getActivity(), LogInPageActivity.class);
		intent.putExtra(LOGIN_PAGE_AND_LOADERS_CATEGORY, mLogInPages.get(position));
		startActivity(intent);
	}
}
