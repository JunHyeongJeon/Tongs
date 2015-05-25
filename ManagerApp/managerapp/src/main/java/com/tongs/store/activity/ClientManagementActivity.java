package com.tongs.store.activity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tongs.store.R;
import com.tongs.store.adapter.DrawerAdapter;
import com.tongs.store.model.DrawerItem;
import com.tongs.store.util.BackPressCloseHandler;
import com.tongs.store.util.HttpTask;
import com.tongs.store.util.ImageUtil;
import com.tongs.store.util.GlobalVar;
import com.tongs.store.util.OnHttpReceive;
import com.tongs.store.util.Preference;
import com.tongs.store.view.AnimatedExpandableListView;

import org.json.JSONArray;
import org.json.JSONObject;


public class ClientManagementActivity extends ActionBarActivity
        implements View.OnClickListener, GlobalVar {
    static final String TAG = "GCM Demo";

    public static final String LEFT_MENU_OPTION = "com.tongs.store.LeftMenusActivity";
    public static final String LEFT_MENU_OPTION_1 = "Left Menu Option 1";
    public static final String LEFT_MENU_OPTION_2 = "Left Menu Option 2";


    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";

    private AnimatedExpandableListView listView;

    public static final int CASE_STATUS_USER_CALL = 0;
    public static final int CASE_STATUS_USER_CANCEL = 1;
    public static final int CASE_STATUS_USER_MORE_INFORMATION = 2;
    private String mToken;
    private String mTodayDate;
    private long mTime;
    private ProgressDialog mDialog;

    private ListView mDrawerList;
    private List<DrawerItem> mDrawerItems;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    public ImageView mClientTicket;

    private TextView mBeforeTurnTextView;
    private TextView mThisTurnTextView;
    private TextView mAfterTurnTextView;
    private TextView mThisTurnWaitPeopleTextView;
    private TextView mThisTurnWaitTimeTextView;

    private Button mFirstClientMoreInfoButton;
    private Button mFirstClientCancelButton;

    private String mBeforeFirstNumber = "";
    private String mFirstId = "";
    private String mFirstNumber="";
    private String mAfterFirstNumber = "";

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private String regid;

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();

    Context context;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "211629096961";

    private BackPressCloseHandler backPressCloseHandler;

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(savedInstanceState);


        Preference pref = Preference.getInstance();
        String token = pref.getValue(TOKEN,"");
        mToken = token;
        mTodayDate = getTodayDate();

        getTicketList();

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId();

            String msg = "";

            if (regid.isEmpty()) {
                registerInBackground();
            } else {
                Log.i(TAG, "No valid Google Play Services APK found.");
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private String getRegistrationId() {

        Preference pref = Preference.getInstance();
        String registrationId = pref.getValue(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = pref.getValue(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        Log.d("SF", registrationId);
        return registrationId;
    }



    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void setContentView(Bundle savedInstanceState){
        setContentView(R.layout.activity_client_management);

        mBeforeTurnTextView = (TextView)findViewById(R.id.before_turn_textview);
        mThisTurnTextView = (TextView)findViewById(R.id.this_turn_textview);
        mAfterTurnTextView = (TextView)findViewById(R.id.after_turn_textview);
        mThisTurnWaitPeopleTextView = (TextView)findViewById(R.id.this_turn_people_num_textview);
        mThisTurnWaitTimeTextView = (TextView)findViewById(R.id.this_turn_wait_time_textview);

        mFirstClientMoreInfoButton = (Button) findViewById(R.id.first_client_more_infomation_button);
        mFirstClientMoreInfoButton.setOnClickListener(this);

        mFirstClientCancelButton = (Button) findViewById(R.id.first_client_cancel_button);
        mFirstClientCancelButton.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_view);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        prepareNavigationDrawerItems();
        setAdapter();
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu();
            }
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        if (savedInstanceState == null) {
            mDrawerLayout.closeDrawer(mDrawerList);
        }

        Button mClientAddButton;
        mClientAddButton = (Button)findViewById(R.id.client_add);
        mClientAddButton.setOnClickListener((View.OnClickListener) this);

        backPressCloseHandler = new BackPressCloseHandler(this);

    }

    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }


    public String getTodayDate(){

        long now = System.currentTimeMillis();
        mTime = now/1000;
        Log.v("unixTime", now + "");
        Date date = new Date(now);

        SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
        String todayDate = CurYearFormat.format(date) +
                CurMonthFormat.format(date) +
                CurDayFormat.format(date);
        Log.v("getTodayDate", todayDate);

        return todayDate;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }


    private void prepareNavigationDrawerItems() {
        mDrawerItems = new ArrayList<>();
        mDrawerItems.add(
                new DrawerItem(
                        R.string.drawer_icon_linked_in,
                        R.string.drawer_title_setting,
                        DrawerItem.DRAWER_ITEM_TAG_LINKED_IN));
        mDrawerItems.add(
                new DrawerItem(
                        R.string.drawer_icon_blog,
                        R.string.drawer_title_before_client_data,
                        DrawerItem.DRAWER_ITEM_TAG_BLOG));

        mDrawerItems.add(
                new DrawerItem(
                        R.string.drawer_icon_git_hub,
                        R.string.drawer_title_auto_login_release,
                        DrawerItem.DRAWER_ITEM_TAG_GIT_HUB));
        mDrawerItems.add(
                new DrawerItem(
                        R.string.drawer_icon_instagram,
                        R.string.drawer_title_logout,
                        DrawerItem.DRAWER_ITEM_TAG_INSTAGRAM));
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
            headerView = prepareHeaderView(R.layout.header_navigation_drawer_1,
                    "http://pengaja.com/uiapptemplate/avatars/0.jpg",
                    "dev@csform.com");
        } else if (option.equals(LEFT_MENU_OPTION_2)) {
            headerView = prepareHeaderView(R.layout.header_navigation_drawer_2,
                    "http://pengaja.com/uiapptemplate/avatars/0.jpg",
                    "dev@csform.com");
            isFirstType = false;
        }

        BaseAdapter adapter = new DrawerAdapter(this, mDrawerItems, isFirstType);

        mDrawerList.addHeaderView(headerView);//Add header before adapter (for pre-KitKat)
        mDrawerList.setAdapter(adapter);
    }

    private class DrawerItemClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            selectItem(position/*, mDrawerItems.get(position - 1).getTag()*/);
        }
    }

    private View prepareHeaderView(int layoutRes, String url, String email) {
        View headerView = getLayoutInflater().inflate(layoutRes, mDrawerList, false);
        ImageView iv = (ImageView) headerView.findViewById(R.id.image);
        TextView tv = (TextView) headerView.findViewById(R.id.email);

        ImageUtil.displayRoundImage(iv, url, null);
        tv.setText(email);

        return headerView;
    }

    private void selectItem(int position/*, int drawerTag*/) {
        // minus 1 because we have header that has 0 position
        if (position < 1) { //because we have header, we skip clicking on it
            return;
        }
        if( position == 2){
            movePreClientListActivity();

        }
        String drawerTitle = getString(mDrawerItems.get(position - 1).getTitle());
        Toast.makeText(this, "You selected " + drawerTitle + " at position: " + position, Toast.LENGTH_SHORT).show();

        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerItems.get(position - 1).getTitle());
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.client_add)
        {
            recognitionBacode();
        }
        else if (v.getId() == R.id.first_client_more_infomation_button)
        {
            moveClientMoreInfoActivity();
        }
        else if (v.getId() == R.id.first_client_cancel_button)
        {
            removeTicket(mFirstId);
            mBeforeFirstNumber = mFirstNumber;
            mBeforeTurnTextView.setText(mBeforeFirstNumber);
            mFirstNumber = mAfterFirstNumber;
            mThisTurnTextView.setText(mFirstNumber);
        }


    }

    // need to be change
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private String setDialogMent(int status){
        String ment;
        if(status == PROTOCOL_STATUS_USER_CALL) {
            ment = getString(R.string.dialog_user_call);
        }
        else if (status == PROTOCOL_STATUS_USER_CANCEL) {
            ment = getString(R.string.dialog_user_remove);
        } else {
            ment = getString(R.string.dialog_user_more_information);
        }

        return ment;

    }

    private class GroupItem {
        String ticketNum;
        String peopleNum;
        String waitTime;
        String order;
        String pivot;


        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private class ChildItem {
        String title;
        //String hint;
    }

    private class ChildHolder {
        TextView title;
        //TextView hint;
    }

    private class GroupHolder {
        TextView ticketNum;
        TextView peopleNum;
        TextView waitTime;
        TextView order;
        TextView pivot;
    }

    private void pushTicket(String user, String people){

        String url;
        url = getString(R.string.api_server) + getString(R.string.api_store_ticket_push)
                + "token=" + mToken + "&user=" + user + "&pivot=" + mTodayDate
                + "&people=" + people;
        requestOnUIThread(PROTOCOL_STATUS_USER_ADD, url, new OnHttpReceive() {
            @Override
            public void onReceive(int protocol, String data) {
                try {
                    JSONObject json = new JSONObject(data);
                    String result_code = json.optString("result_code", null);
                    boolean isSuccess = "0".equals(result_code) ? true : false;
                    getTicketList();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private void popTicket(String id){
        String url;
        url = getString(R.string.api_server) + getString(R.string.api_store_ticket_pop)
                + "token=" + mToken + "&id=" + id;
        requestOnUIThread(PROTOCOL_STATUS_USER_CALL, url, new OnHttpReceive() {
            @Override
            public void onReceive(int protocol, String data) {
                try {
                    JSONObject json = new JSONObject(data);
                    String result_code = json.optString("result_code", null);
                    boolean isSuccess = "0".equals(result_code) ? true : false;
                    getTicketList();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
    private void removeTicket(String id){
        Log.v("Protocol", "PROTOCOL_STATUS_USER_CANCEL");

        String url;
        url = getString(R.string.api_server) +
                getString(R.string.api_store_ticket_remove) +
                "token=" + mToken +
                "&id=" + id;
        requestOnUIThread(PROTOCOL_STATUS_USER_CANCEL, url, new OnHttpReceive() {
            @Override
            public void onReceive(int protocol, String data) {
                try {
                    JSONObject json = new JSONObject(data);
                    String result_code = json.optString("result_code", null);
                    boolean isSuccess = "0".equals(result_code) ? true : false;
                    getTicketList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void getTicketList() {
        Log.v("Protocol", "PROTOCOL_STATUS_GET_LIST");

//        setProtocolStatus(PROTOCOL_STATUS_GET_LIST);
        String url;
        url = getString(R.string.api_server) +
                getString(R.string.api_store_ticket_list) +
                "token=" + mToken +
                "&pivot=" + mTodayDate +
                "&type=" + "0";

        requestOnUIThread(PROTOCOL_STATUS_GET_LIST, url, new OnHttpReceive() {
            @Override
            public void onReceive(int protocol, String data) {
                try {
                    JSONObject json = new JSONObject(data);
                    String result_code = json.optString("result_code", null);
                    boolean isSuccess = "0".equals(result_code) ? true : false;


                    if (isSuccess && protocol == PROTOCOL_STATUS_GET_LIST) {
                        Log.v("onReceive/Protocol", "PROTOCOL_STATUS_GET_LIST");

                        JSONArray jsonArr = json.optJSONArray("list");
                        if (jsonArr == null || "[]".equals(jsonArr.toString())) {

                            printToast("대기열이 없습니다.");
                            return;
                        }

                        int ticketLen = jsonArr.length();


                        JSONObject obj = jsonArr.optJSONObject(0);


                        mFirstId = obj.optString("id", null);
                        mFirstNumber = obj.optString("number", null);

                        mThisTurnTextView.setText(mFirstNumber);

                        String firstPeople = obj.optString("people", null);
                        mThisTurnWaitPeopleTextView.setText(firstPeople);

                        long firstPopTime = obj.optLong("time", 0);
                        long firstTime = (mTime - firstPopTime) / 60;
                        mThisTurnWaitTimeTextView.setText(firstTime + "");


                        obj = jsonArr.optJSONObject(1);
                        mAfterFirstNumber = "";
                        mAfterFirstNumber = obj.optString("number", null);
                        mAfterTurnTextView.setText(mAfterFirstNumber);

                        List<GroupItem> items = new ArrayList<GroupItem>();

                        final String index[] = new String[ticketLen];
                        for (int i = 1; i < ticketLen; i++) {

                            JSONObject inObj = jsonArr.optJSONObject(i);
                            if (obj == null)
                                break;

                            String id = inObj.optString("id", null);
                            String owner = inObj.optString("owner", null);
                            String store = inObj.optString("store", null);
                            String status = inObj.optString("status", null);
                            String pivot = inObj.optString("pivot", null);
                            String people = inObj.optString("people", null);
                            String number = inObj.optString("number", null);
                            long popTime = inObj.optLong("time", 0);

                            index[i] = id;

                            long time = (mTime - popTime) / 60;

                            //pivot = dateSplit(pivot);

                            // 레코드 생성 및 추가

                            GroupItem item = new GroupItem();

                            item.ticketNum = number;
                            item.peopleNum = people;
                            item.waitTime = time + "";
                            item.order = i + "";
                            item.pivot = pivot;


                            ChildItem child = new ChildItem();
                            child.title = "호출";
                            item.items.add(child);
                            items.add(item);
                        }

                        ExpandListViewAdapter adapter = new ExpandListViewAdapter(ClientManagementActivity.this);
                        adapter.setData(items);

                        listView = (AnimatedExpandableListView) findViewById(R.id.client_list_view);
                        listView.setAdapter(adapter);

                        // In order to show animations, we need to use a custom click handler
                        // for our ExpandableListView.
                        listView.setOnGroupClickListener(new OnGroupClickListener() {

                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v,
                                                        int groupPosition, long id) {
                                Log.v("onGroupClick", "" + groupPosition);
                                if (listView.isGroupExpanded(groupPosition)) {
                                    listView.collapseGroupWithAnimation(groupPosition);
                                } else {
                                    listView.expandGroupWithAnimation(groupPosition);
                                }
                                return true;
                            }

                        });
                        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView parent, View v,
                                                        int groupPosition, int childPosition, long id) {
                                Log.v("ClickgroupPosition", String.valueOf(groupPosition));
                                Log.v("ClickchildPosition", String.valueOf(childPosition));
                                Log.v("onClick", index[groupPosition]);

                                popDialog(setDialogMent(childPosition), childPosition, index[groupPosition]);
                                return false;
                            }
                        });


                        // Set indicator (arrow) to the right
                        Display display = getWindowManager().getDefaultDisplay();
                        Point size = new Point();
                        display.getSize(size);
                        int width = size.x;
                        Resources r = getResources();
                        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                50, r.getDisplayMetrics());
                        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                            listView.setIndicatorBounds(width - px, width);
                        } else {
                            listView.setIndicatorBoundsRelative(width - px, width);
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void recognitionBacode() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE");
        startActivityForResult(intent, 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.v("bacodeNUM", contents);

                String[] bacodeData;
                bacodeData = bacodeSplit(contents);

                String user = bacodeData[1];
                String peopleNum = bacodeData[2];

                pushTicket(user, peopleNum);

            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    void requestOnUIThread(final int protocol, final String url, final OnHttpReceive onReceive)
    {
        Log.v("URL", url);
        this.runOnUiThread(new Runnable() {
            public void run() {
                new HttpTask(protocol, onReceive).execute(url);
            }
        });
    }

    private String[] bacodeSplit(String bacode) {

    //    String[] result = bacode.split("_");
    //    String phoneNumber = result[0];
    //    String peopleNumber = result[2];

        return bacode.split("_");
    }



    private void popDialog(String ment, final int status, final String userId){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage(ment).setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        controlUserStatus(status,userId);
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.setTitle("Title");
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();
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

    private void controlUserStatus(int status, String id) {

        if (status == CASE_STATUS_USER_CALL) {
            popTicket(id);
        }
        else if (status == CASE_STATUS_USER_CANCEL) {
            removeTicket(id);
        }
        else if (status == CASE_STATUS_USER_MORE_INFORMATION){
            viewClientMoreInfo();
        }



    }
    private void viewClientMoreInfo(){
        Log.v("ClientMana/viewClient","viewClientMoreInfo");


    }
    private void DialogProgress(){
        mDialog = ProgressDialog.show(ClientManagementActivity.this, "",
                "인증번호를 서버와 통신중입니다.. 잠시만 기다려 주세요 ...", true);
    }


    private class ExpandListViewAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {


        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExpandListViewAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        public void setData(List<GroupItem> items) {
            this.items = items;
        }

        @Override
        public ChildItem getChild(int groupPosition, int childPosition) {
            return items.get(groupPosition).items.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getRealChildView(int groupPosition, int childPosition,
                                     boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder holder;
            ChildItem item = getChild(groupPosition, childPosition);
            if (convertView == null) {
                holder = new ChildHolder();
                convertView = inflater.inflate(R.layout.client_child_item, parent,
                        false);
                holder.title = (TextView) convertView
                        .findViewById(R.id.textTitle);
				/*holder.hint = (TextView) convertView
						.findViewById(R.id.textHint);*/
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

            return convertView;
        }

        @Override
        public int getRealChildrenCount(int groupPosition) {
            return items.get(groupPosition).items.size();
        }

        @Override
        public GroupItem getGroup(int groupPosition) {
            return items.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return items.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupHolder holder;
            GroupItem item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = inflater.inflate(R.layout.client_group_item, parent,
                        false);
                holder.ticketNum = (TextView) convertView
                        .findViewById(R.id.ticket_number);
                holder.peopleNum = (TextView) convertView
                        .findViewById(R.id.people_number);
                holder.order = (TextView) convertView
                        .findViewById(R.id.grey_titile_bar_order);
                holder.pivot = (TextView) convertView
                        .findViewById(R.id.grey_titile_bar_pivot);
                holder.waitTime = (TextView) convertView
                        .findViewById(R.id.wait_time);

                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.ticketNum.setText(item.ticketNum);
            holder.peopleNum.setText(item.peopleNum);
            holder.order.setText(item.order);
            holder.pivot.setText(item.pivot);
            holder.waitTime.setText(item.waitTime);

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }


    }


    private void movePreClientListActivity(){
        Intent intent = new Intent(this, PreviousClientListActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);

    }
    void moveClientMoreInfoActivity(){

    }

    void sendSMS(){

    }
    public void printToast(String string){
        Toast.makeText(ClientManagementActivity.this, string, Toast.LENGTH_SHORT).show();
    }

    private void sendGCMKey()
    {

        String url = getString(R.string.api_server) +
                getString(R.string.api_store_gcm_init) +
                "token=" + mToken + "&gcm=" + regid;

        requestOnUIThread(PROTOCOL_STATUS_GCM_INIT, url, new OnHttpReceive() {
            @Override
            public void onReceive(int protocol, String data) {
                try {
                    JSONObject json = new JSONObject(data);
                    String result_code = json.optString("result_code", null);
                    boolean isSuccess = "0".equals(result_code) ? true : false;
                    if (isSuccess)
                        Log.v("PROTOCOL_STATUS_GCM", data);
                    else return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    sendGCMKey();

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }


}