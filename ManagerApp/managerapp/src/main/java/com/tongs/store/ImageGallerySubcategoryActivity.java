package com.tongs.store;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.tongs.store.adapter.ImageGallerySubcategoryAdapter;
import com.tongs.store.model.ImageGallerySubcategoryModel;

public class ImageGallerySubcategoryActivity extends ActionBarActivity {

	public static final String IMAGE_GALLERY_SUBCATEGORY = "com.tongs.store.ImageGallerySubcategoryActivity";
	public static final String IMAGE_GALLERY_LAYOUT_ON_TOP = "com.tongs.store.ImageGallerySubcategoryActivity.layoutOnTop";
	private ArrayList<ImageGallerySubcategoryModel> mSubcategories;
	
	private ListView mListView;
	private boolean mIsLayoutOnTop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.tongs.store.R.layout.list_view);
		
		mListView = (ListView) findViewById(com.tongs.store.R.id.list_view);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			if (extras.containsKey(IMAGE_GALLERY_SUBCATEGORY)) {
				mSubcategories = extras.getParcelableArrayList(IMAGE_GALLERY_SUBCATEGORY);
			} else {
				mSubcategories = new ArrayList<>();
			}
			if (extras.containsKey(IMAGE_GALLERY_LAYOUT_ON_TOP)) {
				mIsLayoutOnTop = extras.getBoolean(IMAGE_GALLERY_LAYOUT_ON_TOP, false);
			}
		}
		setAdapter();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setAdapter() {
		BaseAdapter adapter = new ImageGallerySubcategoryAdapter(this, mSubcategories, mIsLayoutOnTop);
		mListView.setAdapter(adapter);
	}
}
