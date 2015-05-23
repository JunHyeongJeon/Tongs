package com.tongs.store;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.tongs.store.adapter.ImageGalleryCategoryAdapter;
import com.tongs.store.util.DummyContent;

public class ImageGalleryCategoryActivity extends ActionBarActivity {
	
	public static final String IMAGE_GALLERY_CATEGORY = "com.tongs.store.ImageGalleryCategoryActivity";
	public static final String ANIMALS_CATEGORY = "Category 1 (Animals)";
	public static final String DOGS_SUBCATEGORY = "Subcategory 1 (Dogs)";
	public static final String MUSIC_CATEGORY = "Category 2 (Music)";
	
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.tongs.store.R.layout.list_view);
		
		mListView = (ListView) findViewById(com.tongs.store.R.id.list_view);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		String subcategory = ANIMALS_CATEGORY;
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(IMAGE_GALLERY_CATEGORY)) {
			subcategory = extras.getString(IMAGE_GALLERY_CATEGORY);
		}
		setAdapter(subcategory);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setAdapter(String subcategory) {
		BaseAdapter adapter = null;
		boolean addHeaderView = false;//You can request list view header by setting this variable to true
		String title = "";
		if (subcategory.equals(ANIMALS_CATEGORY)) {
			adapter = new ImageGalleryCategoryAdapter(this, DummyContent.getImageGalleryAnimalCategories(), false);
			addHeaderView = true;
			title = ANIMALS_CATEGORY;
		} else if (subcategory.equals(MUSIC_CATEGORY)) {
			adapter = new ImageGalleryCategoryAdapter(this, DummyContent.getImageGalleryMusicCategories(), true);
			addHeaderView = false;
			title = MUSIC_CATEGORY;
		}
		if (addHeaderView) {
			View headerView = getLayoutInflater().inflate(com.tongs.store.R.layout.header_image_gallery, mListView, false);
			((TextView) headerView.findViewById(com.tongs.store.R.id.header_title)).setText(title);
			mListView.addHeaderView(headerView);
		} else {
			setTitle(title);
		}
		mListView.setAdapter(adapter);
	}
}
