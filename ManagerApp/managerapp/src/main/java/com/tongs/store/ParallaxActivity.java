package com.tongs.store;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.tongs.store.adapter.DefaultAdapter;
import com.tongs.store.util.DummyContent;
import com.tongs.store.view.PullToZoomListView;

public class ParallaxActivity extends ActionBarActivity {
	
	public static final String TAG = "Parallax";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.tongs.store.R.layout.activity_parallax);
		
		PullToZoomListView listView = (PullToZoomListView) findViewById(com.tongs.store.R.id.list_view);
		listView.setEnableZoom(true);
		listView.setParallax(true);
		listView.showHeadView();
		listView.setAdapter(new DefaultAdapter(this, DummyContent.getDummyModelList(), false));
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
