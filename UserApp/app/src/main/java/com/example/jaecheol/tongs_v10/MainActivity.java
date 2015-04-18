package com.example.jaecheol.tongs_v10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity
                          implements View.OnClickListener//, NavigationDrawerCallbacks
{
    Toolbar toolbar;
    ViewPager pager;

    com.example.jaecheol.tab.ViewPagerAdapter adapter;
    com.example.jaecheol.tab.SlidingTabLayout tabs;

    CharSequence titles[]={"바코드","대기표"};
    int numOfTabs =2;

    String authToken;

    int sid;
    int number;
    int createTime;
    int extraTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();
        setTabView();

        getDataFromIntent();
    }



    private void setToolbar()   {

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setTabView()   {

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new com.example.jaecheol.tab.ViewPagerAdapter(getSupportFragmentManager(), titles, numOfTabs);

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

    private void getDataFromIntent()    {

        Intent intent = this.getIntent();
        authToken = intent.getStringExtra("auth_token");
    }


    public void onClick(View v) {
        switch (v.getId()) {
        }
    }



    public static String convertStreamToString(InputStream is) {
/*
 * To convert the InputStream to String we use the BufferedReader.readLine()
 * method. We iterate until the BufferedReader return null which means
 * there's no more data to read. Each line will appended to a StringBuilder
 * and returned as String.
 */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
