package com.tongs.store;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongs.store.adapter.DrawerAdapter;
import com.tongs.store.model.DrawerItem;
import com.tongs.store.util.ImageUtil;

public class LeftMenusActivity extends ActionBarActivity {

	public static final String LEFT_MENU_OPTION = "com.tongs.store.LeftMenusActivity";
	public static final String LEFT_MENU_OPTION_1 = "Left Menu Option 1";
	public static final String LEFT_MENU_OPTION_2 = "Left Menu Option 2";
	
	private ListView mDrawerList;
	private List<DrawerItem> mDrawerItems;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(com.tongs.store.R.layout.activity_main);

		Toolbar toolbar = (Toolbar) findViewById(com.tongs.store.R.id.toolbar);
		setSupportActionBar(toolbar);
		mDrawerLayout = (DrawerLayout) findViewById(com.tongs.store.R.id.drawer_layout);
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(com.tongs.store.R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(com.tongs.store.R.id.list_view);

		mDrawerLayout.setDrawerShadow(com.tongs.store.R.drawable.drawer_shadow, GravityCompat.START);
		prepareNavigationDrawerItems();
		setAdapter();
		//mDrawerList.setAdapter(new DrawerAdapter(this, mDrawerItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
				com.tongs.store.R.string.drawer_open,
				com.tongs.store.R.string.drawer_close) {
			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			mDrawerLayout.openDrawer(mDrawerList);
		}
	}
	private void setAdapter() {
		String option = LEFT_MENU_OPTION_1;
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(LEFT_MENU_OPTION)) {
			option = extras.getString(LEFT_MENU_OPTION, LEFT_MENU_OPTION_1);
		}

		boolean isFirstType = true;

		View headerView = null;
		if (option.equals(LEFT_MENU_OPTION_1)) {
			headerView = prepareHeaderView(com.tongs.store.R.layout.header_navigation_drawer_1,
					"http://pengaja.com/uiapptemplate/avatars/0.jpg",
					"dev@csform.com");
		} else if (option.equals(LEFT_MENU_OPTION_2)) {
			headerView = prepareHeaderView(com.tongs.store.R.layout.header_navigation_drawer_2,
					"http://pengaja.com/uiapptemplate/avatars/0.jpg",
					"dev@csform.com");
			isFirstType = false;
		}

		BaseAdapter adapter = new DrawerAdapter(this, mDrawerItems, isFirstType);

		mDrawerList.addHeaderView(headerView);//Add header before adapter (for pre-KitKat)
		mDrawerList.setAdapter(adapter);
	}

	
	private View prepareHeaderView(int layoutRes, String url, String email) {
		View headerView = getLayoutInflater().inflate(layoutRes, mDrawerList, false);
		ImageView iv = (ImageView) headerView.findViewById(com.tongs.store.R.id.image);
		TextView tv = (TextView) headerView.findViewById(com.tongs.store.R.id.email);
		
		ImageUtil.displayRoundImage(iv, url, null);
		tv.setText(email);
		
		return headerView;
	}

	private void prepareNavigationDrawerItems() {
		mDrawerItems = new ArrayList<>();
		mDrawerItems.add(
				new DrawerItem(
						com.tongs.store.R.string.drawer_icon_linked_in,
						com.tongs.store.R.string.drawer_title_linked_in,
						DrawerItem.DRAWER_ITEM_TAG_LINKED_IN));
		mDrawerItems.add(
				new DrawerItem(
						com.tongs.store.R.string.drawer_icon_blog,
						com.tongs.store.R.string.drawer_title_blog,
						DrawerItem.DRAWER_ITEM_TAG_BLOG));
		mDrawerItems.add(
				new DrawerItem(
						com.tongs.store.R.string.drawer_icon_git_hub,
						com.tongs.store.R.string.drawer_title_git_hub,
						DrawerItem.DRAWER_ITEM_TAG_GIT_HUB));
		mDrawerItems.add(
				new DrawerItem(
						com.tongs.store.R.string.drawer_icon_instagram,
						com.tongs.store.R.string.drawer_title_instagram,
						DrawerItem.DRAWER_ITEM_TAG_INSTAGRAM));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(com.tongs.store.R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position/*, mDrawerItems.get(position - 1).getTag()*/);
		}
	}

	private void selectItem(int position/*, int drawerTag*/) {
		// minus 1 because we have header that has 0 position
		if (position < 1) { //because we have header, we skip clicking on it
			return;
		}
		String drawerTitle = getString(mDrawerItems.get(position - 1).getTitle());
		Toast.makeText(this, "You selected " + drawerTitle + " at position: " + position, Toast.LENGTH_SHORT).show();
		
		mDrawerList.setItemChecked(position, true);
		setTitle(mDrawerItems.get(position - 1).getTitle());
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	@Override
	public void setTitle(int titleId) {
		setTitle(getString(titleId));
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
}
