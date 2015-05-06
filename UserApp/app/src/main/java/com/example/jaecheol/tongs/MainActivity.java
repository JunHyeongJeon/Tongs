package com.example.jaecheol.tongs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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

//        if(intent == null)
//        {
            mdn = "";
            title = "진국";
            ticketNum = "13";
            currentNum = "4";
//        }
//        else {
//            Bundle b = intent.getBundleExtra("data");
//            b.getStringArray("ticket");
//
//            mdn = b.getString("mobile_number");
//            title = b.getString("title");
//            ticketNum = b.getString("ticket_num");
//            currentNum = b.getString("current_num");
//            Log.d("GCM", b.toString());
//        }


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