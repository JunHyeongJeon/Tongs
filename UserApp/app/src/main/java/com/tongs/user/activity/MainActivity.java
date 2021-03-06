package com.tongs.user.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import com.google.zxing.BarcodeFormat;
import com.tongs.user.adapter.DrawerAdapter;
import com.tongs.user.adapter.ViewPagerAdapter;
import com.tongs.user.tab.SlidingTabLayout;
import com.tongs.user.tab.StoreTab;
import com.tongs.user.tab.TicketTab;
import com.tongs.user.tool.BarcodeGenerator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity
{
    Toolbar toolbar;
    ViewPager pager;

    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;

    CharSequence titles[] = {"매장", "대기표"};
    int numOfTabs = 2;

    int currentNum = 0;


    /* drawer variable */
    String TITLES[] = { "나의 쿠폰" };
    int ICONS[] = { R.drawable.coupon_icon };

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
    String uid;

    StoreTab storeTab;
    TicketTab ticketTab;

    private final static int ACTIVITY_SUMMON = 0;
    private final static int ACTIVITY_BARCODE = 1;

    String hid;
    String sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerBarcode();

        getDataFromSharedPref();
        setToolbar();
        setTabView();
        setDrawer();
        setBarcode();
        wakeLockCheck();

        getDataFromIntent();
    }


    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        processPush(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void processPush(Intent intent) {
        if (intent == null) {
            Log.d("HELLO", "GCM Intent Fail");
        } else {
            Bundle bundle;
            bundle = intent.getExtras();

            String collapseKey = null;

            if (bundle.get("store") != null)
                sid = bundle.getString("store").toString();
            Log.d("HELLO", "GCM Data (store) : " + sid);

            collapseKey = bundle.get("collapse_key").toString();
            Log.d("HELLO", "collapseKey : " + collapseKey);

            if (collapseKey.equals("change")) {

                TicketTab ticketTab = (TicketTab) adapter.getTab(1);
                if (ticketTab != null) {
                    ticketTab.getWaitingTicket();
                }
            } else if (collapseKey.equals("turn")) {

                if (SummonActivity.g_isOpened == false) {
                    hid = bundle.get("hyper").toString();
                    sid = bundle.get("store").toString();

                    Intent intent2 = new Intent(MainActivity.this, SummonActivity.class);
                    intent2.putExtra("hid", hid);
                    intent2.putExtra("sid", sid);
                    intent2.putExtra("authToken", authToken);
                    startActivityForResult(intent2, ACTIVITY_SUMMON);
                } else {
                    Log.e("HELLO", "PUSH SKIP");
                }
            }
        }

        pager.setCurrentItem(1, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_renew:
                renewAll();
                break;
            case R.id.action_logout:
                logout();
                break;

            case R.id.action_summon:
                Intent intent = new Intent(MainActivity.this, SummonActivity.class);
                intent.putExtra("authToken", authToken);
                intent.putExtra("sid", sid);
                intent.putExtra("hid", hid);
                startActivityForResult(intent, ACTIVITY_SUMMON);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        Bundle extraBundle;
        if( requestCode == ACTIVITY_SUMMON )    {
            Log.d("HELLO", "SUMMON ACTIVITY CLOSE");

            if( resultCode == RESULT_CANCELED ) {
                cancelTicket();
            }
            else if( resultCode == RESULT_OK )  {
                acceptSummon();
            }
        }
        else if( requestCode == ACTIVITY_BARCODE )  {
            Log.d("HELLO", "BARCODE ACTIVITY CLOSE");

            if( resultCode == RESULT_OK ) {
                currentNum = intent.getIntExtra("currentNum", currentNum);

                registerBarcode();

                mAdapter = new DrawerAdapter(TITLES, ICONS, currentNum, barcode, handler);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
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
                return getResources().getColor(R.color.colorBackground);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        storeTab = (StoreTab)adapter.getTab(0);
        ticketTab = (TicketTab)adapter.getTab(1);
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
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.app_name, R.string.app_name){

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

        if( currentNum <= 0 )   {
            barcode = BitmapFactory.decodeResource(getResources(), R.drawable.no_num);
            currentNum = 0;
            return;
        }

        try {
            String barcodeContents = mobileNumber + "_" + uid + "_" + currentNum;
            barcode = barcodeGenerator.encodeAsBitmap(barcodeContents, BarcodeFormat.CODE_128, 600, 400);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renewAll()    {
        /* Store Renew */
        StoreTab storeTab = (StoreTab)adapter.getTab(0);
        if(storeTab == null)
            return;
        storeTab.getStoreList();

        /* Ticket Renew */
        TicketTab ticketTab = (TicketTab)adapter.getTab(1);
        if(ticketTab == null)
            return;
        ticketTab.getWaitingTicket();
    }


    private void getDataFromIntent() {

        Intent intent = this.getIntent();
        authToken = intent.getStringExtra("auth_token");
    }


    private void getDataFromSharedPref()    {

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mobileNumber = mPref.getString("number", null);
        uid = mPref.getString("uid", null);
        hid = mPref.getString("hid", "1");
    }

    private void cancelTicket() {
        String url = getText(R.string.api_server)
                + "user/ticket/remove"
                + "?token=" + authToken;

        IHttpRecvCallback cb = new IHttpRecvCallback(){
            public void onRecv(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String result_code = json.get("result_code").toString();
                    Log.d("Hello", result_code);
                    if( "-1".equals(result_code) )  {
                        Log.d("HELLO", "대기표 취소 실패");
                        return;
                    }

                    Log.d("HELLO", "대기표 취소 성공");
                }
                catch(Exception e){}
            }
        };
        new HttpTask(cb).execute(url);
    }

    private void acceptSummon() {
        String url = getText(R.string.api_server)
                + "user/ticket/accept"
                + "?token=" + authToken;

        IHttpRecvCallback cb = new IHttpRecvCallback(){
            public void onRecv(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String result_code = json.get("result_code").toString();
                    Log.d("Hello", result_code);
                    if( "-1".equals(result_code) )  {
                        Log.d("HELLO", "대기호출 확인 실패");
                        return;
                    }

                    Log.d("HELLO", "대기표 확인 성공");
                }
                catch(Exception e){}
            }
        };
        new HttpTask(cb).execute(url);
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

    private void wakeLockCheck() {
        Bundle bundle = getIntent().getExtras();

        if( bundle.get("store") != null )   {
            processPush(getIntent());
        }
    }

    class ServiceHandler extends Handler
    {
        public void handleMessage(Message msg)
        {
            Intent intent;

            switch (msg.what)
            {
                case 11:
                    currentNum++;
                    registerBarcode();

                    mAdapter = new DrawerAdapter(TITLES, ICONS, currentNum, barcode, handler);
                    mRecyclerView.setAdapter(mAdapter);
                    break;

                case 12:
                    currentNum--;
                    registerBarcode();

                    mAdapter = new DrawerAdapter(TITLES, ICONS, currentNum, barcode, handler);
                    mRecyclerView.setAdapter(mAdapter);
                    break;

                case 21:
                    if( currentNum > 0 ) {
                        intent = new Intent(MainActivity.this, BarcodeActivity.class);
                        intent.putExtra("uid", uid);
                        intent.putExtra("currentNum", currentNum);
                        intent.putExtra("mobileNumber", mobileNumber);

                        startActivityForResult(intent, ACTIVITY_BARCODE);
                    }
                    break;

                case 22:
                    intent = new Intent(MainActivity.this, CouponActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }


    private static String convertStreamToString(InputStream is)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024*64);
        byte data[] = new byte[10240];
        while(true) {
            try {
                int len = is.read(data);
                if (len == -1)
                    break;
                baos.write(data, 0, len);
            } catch (Exception e) { }
        }
        String str = new String(baos.toByteArray());
        return str;
    }


    public InputStream getInputStreamFromUrl(String url) {
        InputStream content = null;
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            if(response.getStatusLine().getStatusCode() != 200)
            {
                // 네트워크 오류입니다.
                Log.d("Hello", "Network Error");
            }
            content = response.getEntity().getContent();
        } catch (Exception e) {
            Log.d("[GET REQUEST]", "Network exception", e);
        }

        return content;
    }

    interface IHttpRecvCallback
    {
        public void onRecv(String result);
    }

    class HttpTask extends AsyncTask<String , Void , String> {

        IHttpRecvCallback m_cb;
        HttpTask(IHttpRecvCallback cb)
        {
            m_cb = cb;
        }

        protected String doInBackground(String... params)
        {
            InputStream is = getInputStreamFromUrl(params[0]);

            String result = convertStreamToString(is);

            return result;
        }

        protected void onPostExecute(String result)
        {
            Log.d("Hello", result);
            if(m_cb != null)
            {
                m_cb.onRecv(result);
                return;
            }
        }
    }
}