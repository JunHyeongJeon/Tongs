package com.tongs.store;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongs.store.adapter.SearchAdapter;
import com.tongs.store.model.DummyModel;
import com.tongs.store.util.DummyContent;

public class SearchBarsActivity extends Activity implements OnClickListener {
	
	public static final String SEARCH_BARS_OPTION = "com.tongs.store.SearchBarsActivity";
	public static final String SEARCH_BARS_DARK = "Dark Search Bar";
	public static final String SEARCH_BARS_LIGHT = "Light Search Bar";
	
	private EditText mSearchField;
	private TextView mXMark;
	private View mMicrofon;
	private ListView mListView;
	private ArrayList<DummyModel> searchableArrayList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); // We hide Action Bar
		
		String option = SEARCH_BARS_DARK;
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(SEARCH_BARS_OPTION)) {
			option = extras.getString(SEARCH_BARS_OPTION, SEARCH_BARS_DARK);
		}
		if (option.equals(SEARCH_BARS_DARK)) {
			setContentView(com.tongs.store.R.layout.activity_search_bars_dark);
		} else if (option.equals(SEARCH_BARS_DARK)) {
			setContentView(com.tongs.store.R.layout.activity_search_bars_light);
		} else {
			setContentView(com.tongs.store.R.layout.activity_search_bars_light);
		}
		
		mSearchField = (EditText) findViewById(com.tongs.store.R.id.search_field);
		mXMark = (TextView) findViewById(com.tongs.store.R.id.search_x);
		mMicrofon = findViewById(com.tongs.store.R.id.search_microfon);
		mListView = (ListView) findViewById(com.tongs.store.R.id.list_view);
		
		searchableArrayList = DummyContent.getDummyModelList();
		
		mXMark.setOnClickListener(this);
		mMicrofon.setOnClickListener(this);
		
		mSearchField.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@SuppressLint("DefaultLocale")
			@Override
			public void afterTextChanged(Editable editable) {
				String searchText = editable.toString().trim();
				ArrayList<String> searchedArray = new ArrayList<String>();
				for (DummyModel dm : searchableArrayList) {
					if (dm.getText().toLowerCase().contains(searchText.toLowerCase())) {
						searchedArray.add(dm.getText());
					}
				}
				if (searchText.isEmpty()) {
					mListView.setAdapter(null);
					mXMark.setText(com.tongs.store.R.string.fontello_x_mark);
				} else {
					mListView.setAdapter(new SearchAdapter(SearchBarsActivity.this, searchedArray));
					mXMark.setText(com.tongs.store.R.string.fontello_x_mark_masked);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case com.tongs.store.R.id.search_x:
			mSearchField.setText(null);
			break;
		case com.tongs.store.R.id.search_microfon:
			Toast.makeText(this, "Implement voice search", Toast.LENGTH_LONG).show();
			break;
		}
	}
}
