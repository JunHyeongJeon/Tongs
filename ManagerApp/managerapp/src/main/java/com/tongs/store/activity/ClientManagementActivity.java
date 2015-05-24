package com.tongs.store.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
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


    public static final String LEFT_MENU_OPTION = "com.tongs.store.LeftMenusActivity";
    public static final String LEFT_MENU_OPTION_1 = "Left Menu Option 1";
    public static final String LEFT_MENU_OPTION_2 = "Left Menu Option 2";

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

    private String mBeforeFirstId = "";
    private String mFirstId = "";
    private String mAfterFirstId = "";

    private String mThisTurnWaitPeople = "0";
    private String mThisTurnWaitTime = "0";

    private BackPressCloseHandler backPressCloseHandler;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(savedInstanceState);


        Preference pref = Preference.getInstance();
        String token = pref.getValue(TOKEN,"");
        mToken = token;
        mTodayDate = getTodayDate();

        getTicketList();
        viewTicketList();


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
        //mDrawerList.setAdapter(new DrawerAdapter(this, mDrawerItems));
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


        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.action_bar_client_management);
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
            mBeforeFirstId = mFirstId;
            mBeforeTurnTextView.setText(mBeforeFirstId);
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

    /*
    @Override
    public void onReceive(int protocol, String data) {
        Log.v("ClientMa/onReceive",data);


    }
    */
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

    /**
     * Adapter for our list of {@link GroupItem}s.
     */


    private void pushTicket(String user, String people){

//        setProtocolStatus(PROTOCOL_STATUS_USER_ADD);
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
        //setProtocolStatus(PROTOCOL_STATUS_USER_CALL);
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

//        setProtocolStatus(PROTOCOL_STATUS_USER_CANCLE);
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
                }
                catch(Exception e){
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
                        if (jsonArr == null || "[]".equals(jsonArr.toString()) ) {
                            /*
                            List<GroupItem> items = new ArrayList<GroupItem>();

                            GroupItem item = new GroupItem();
                            item.ticketNum = "000";
                            items.add(item);

                            ExpandListViewAdapter adapter = new ExpandListViewAdapter(ClientManagementActivity.this);
                            adapter.setData(items);

                            listView = (AnimatedExpandableListView) findViewById(R.id.client_list_view);
                            listView.setAdapter(adapter);

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
                                */
                            printToast("대기열이 없습니다.");
                            return;
                        }

                        int ticketLen = jsonArr.length();


                        JSONObject obj = jsonArr.optJSONObject(0);




                        mFirstId = obj.optString("number", null);
                        mThisTurnTextView.setText(mFirstId);

                        String firstPeople = obj.optString("people",null);
                        mThisTurnWaitPeopleTextView.setText(firstPeople);

                        long firstPopTime = obj.optLong("time", 0);
                        long firstTime = (mTime - firstPopTime)/60;
                        mThisTurnWaitTimeTextView.setText(firstTime+"");


                        obj = jsonArr.optJSONObject(1);
                        mAfterFirstId ="";
                        mAfterFirstId = obj.optString("number", null);
                        mAfterTurnTextView.setText(mAfterFirstId);

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
                            String people = inObj.optString("people",null);
                            String number = inObj.optString("number", null);
                            long popTime = inObj.optLong("time",0);

                            index[i] = id;

                            long time = (mTime - popTime)/60;

                            //pivot = dateSplit(pivot);

                            // 레코드 생성 및 추가

                            GroupItem item = new GroupItem();

                            item.ticketNum = id;
                            item.peopleNum = people;
                            item.waitTime = time+"";
                            item.order = i+"";
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
                                Log.v("onGroupClick",""+groupPosition);
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
                                                        int groupPosition, int childPosition , long id) {
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
    private void viewTicketList(){

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
}