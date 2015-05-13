package com.example.jaecheol.tongs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.example.jaecheol.drawer.DrawerAdapter;
import com.example.jaecheol.tab.SlidingTabLayout;
import com.example.jaecheol.tab.Tab2;
import com.example.jaecheol.tab.ViewPagerAdapter;
import com.google.zxing.BarcodeFormat;

import java.io.ByteArrayOutputStream;


public class MainActivity extends ActionBarActivity //, NavigationDrawerCallbacks
{
    Toolbar toolbar;
    ViewPager pager;

    com.example.jaecheol.tab.ViewPagerAdapter adapter;
    com.example.jaecheol.tab.SlidingTabLayout tabs;

    CharSequence titles[] = {"바코드", "대기표"};
    int numOfTabs = 2;

    int currentNum = 0;


    /* drawer variable */
    String TITLES[] = { "나의 쿠폰" };
    int ICONS[] = { R.mipmap.ic_launcher };

    String NAME = "Jaecheol GOD";
    String EMAIL = "jcgod413@gmail.com";
    int BARCODE = R.drawable.no_barcodes;

    BarcodeGenerator barcodeGenerator;
    Bitmap barcode;

    private ServiceHandler handler;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    String authToken;

    String mobileNumber;



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

        getDataFromSharedPref();
        setToolbar();
        setTabView();
        setDrawer();
        setBarcode();
        setBarcode();

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
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), titles, numOfTabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout)findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

//         Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }


    private void setDrawer()    {

        handler = new ServiceHandler();

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mAdapter = new DrawerAdapter(TITLES, ICONS, currentNum, barcode, handler);       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
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

    private void setBarcode()   {
        barcodeGenerator = new BarcodeGenerator();
    }

    public void registerBarcode()    {

        try {
            String barcodeContents = mobileNumber + "_" + currentNum;
            Log.d("HELLO", barcodeContents);
            barcode = barcodeGenerator.encodeAsBitmap(barcodeContents, BarcodeFormat.CODE_128, 600, 400);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void getDataFromIntent() {

        Intent intent = this.getIntent();
        authToken = intent.getStringExtra("auth_token");
    }


    private void getDataFromSharedPref()    {

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mobileNumber = mPref.getString("number", null);
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

    class ServiceHandler extends Handler
    {
        public void handleMessage(Message msg)
        {
            Intent intent;

            switch (msg.what)
            {
                case 11:
                    Log.d("HELLO", "PLUS");
                    currentNum++;
                    registerBarcode();

                    mAdapter = new DrawerAdapter(TITLES, ICONS, currentNum, barcode, handler);
                    mRecyclerView.setAdapter(mAdapter);

                    break;
                case 12:
                    Log.d("HELLO", "MINUS");
                    if( currentNum > 0 ) {
                        currentNum--;
                        registerBarcode();

                        mAdapter = new DrawerAdapter(TITLES, ICONS, currentNum, barcode, handler);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    break;
                case 21:
                    if( currentNum == 0 ) {
                        return;
                    }

                    intent = new Intent(MainActivity.this, BarcodeActivity.class);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    barcode.compress(Bitmap.CompressFormat.PNG, 100, baos);

                    intent.putExtra("barcode", baos.toByteArray());
                    startActivity(intent);
                    break;
                case 22:
                    intent = new Intent(MainActivity.this, CouponActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

}

