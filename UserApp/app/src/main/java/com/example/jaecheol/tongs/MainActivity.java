package com.example.jaecheol.tongs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jaecheol.drawer.MyAdapter;
import com.example.jaecheol.tab.Tab2;


public class MainActivity extends ActionBarActivity
                          implements View.OnClickListener//, NavigationDrawerCallbacks
{
    Toolbar toolbar;
    ViewPager pager;

    com.example.jaecheol.tab.ViewPagerAdapter adapter;
    com.example.jaecheol.tab.SlidingTabLayout tabs;

    CharSequence titles[] = {"바코드", "대기표"};
    int numOfTabs = 2;


    /* drawer variable */
    String TITLES[] = {"Home","Events","Mail","Shop","Travel"};
    int ICONS[] = {R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};

    String NAME = "Jaecheol GOD";
    String EMAIL = "jcgod413@gmail.com";
    int PROFILE = R.drawable.profile;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle



    String authToken;

    int sid;
    int number;
    int createTime;
    int extraTime;

    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //title = savedInstanceState.getString("title", null);

        setToolbar();
        setTabView();
        setDrawer();

        getDataFromIntent();
    }


    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        processPush(intent);

    }

    public void processPush(Intent intent)
    {
        String mdn;
        String title;
        String ticketNum;
        String currentNum;

        if(intent == null)
        {
            mdn = "";
            title = "진국";
            ticketNum = "13";
            currentNum = "4";
        }
        else {
            Bundle b = intent.getBundleExtra("data");
            b.getStringArray("ticket");


//            title = json.getJSONObject("ticket").getString("ticket");
//            Log.d("Hello", title);
//            String currentNum = json.getJSONObject("store").getString("current_num");
//            Log.d("Hello", currentNum);
//            String storeName = json.getJSONObject("store").getString("brand_name");
//            Log.d("Hello", storeName);

            // json 값 제대로 읽어와야함

            mdn = b.getString("mobile_number");
            title = b.getString("title");
            ticketNum = b.getString("ticket_num");
            currentNum = b.getString("current_num");
            Log.d("GCM", b.toString());
        }


        pager.setCurrentItem(1, true);
        Tab2 tab2 = (Tab2)adapter.getTab(1);

        tab2.waitingTicket.setStoreName(title);
        tab2.waitingTicket.setCurrentNum(currentNum);
        tab2.waitingTicket.setWaitingNum(ticketNum);
        tab2.showTicketLayout(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
//            case R.id.action_registerEmail:
//                registerEmail();
//                break;
            case R.id.action_logout:
                logout();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setToolbar() {

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setTabView() {

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter = new com.example.jaecheol.tab.ViewPagerAdapter(getSupportFragmentManager(), titles, numOfTabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (com.example.jaecheol.tab.SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new com.example.jaecheol.tab.SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }


    private void setDrawer()    {

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new MyAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.app_name,R.string.app_name){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // F
    }



    private void getDataFromIntent() {

        Intent intent = this.getIntent();
        authToken = intent.getStringExtra("auth_token");
    }

    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    private void registerEmail() {

        Toast toast = Toast.makeText(getApplicationContext(),
                "이메일 등록", Toast.LENGTH_LONG);
        toast.show();
    }

    private void logout()   {

        Toast toast = Toast.makeText(getApplicationContext(),
                "로그아웃", Toast.LENGTH_LONG);
        toast.show();

        // SharedPreferences에 담겨있는 정보들 모두 삭제.
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = mPref.edit();
        editor.clear();
        editor.commit();

        // 초기 페이지로 되돌림.
        Intent intent = new Intent(MainActivity.this, IntroActivity.class);
        startActivity(intent);
        this.finish();
    }

}