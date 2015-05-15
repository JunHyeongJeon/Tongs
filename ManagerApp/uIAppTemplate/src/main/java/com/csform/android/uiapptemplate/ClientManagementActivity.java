package com.csform.android.uiapptemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

import android.widget.TextView;

import com.csform.android.uiapptemplate.util.HttpTask;
import com.csform.android.uiapptemplate.util.ManagementMethod;
import com.csform.android.uiapptemplate.util.OnHttpReceive;
import com.csform.android.uiapptemplate.util.Preference;
import com.csform.android.uiapptemplate.view.AnimatedExpandableListView;
import com.csform.android.uiapptemplate.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;


import org.json.JSONArray;
import org.json.JSONObject;

import static com.csform.android.uiapptemplate.util.ManagementMethod.isProtocolStatus;
import static com.csform.android.uiapptemplate.util.ManagementMethod.setProtocolStatus;
import static com.csform.android.uiapptemplate.util.ManagementValue.TOKEN;
import static com.csform.android.uiapptemplate.util.ManagementValue.PROTOCOL_STATUS_GET_LIST;
import static com.csform.android.uiapptemplate.util.ManagementValue.PROTOCOL_STATUS_USER_ADD;
import static com.csform.android.uiapptemplate.util.ManagementValue.PROTOCOL_STATUS_USER_CALL;
import static com.csform.android.uiapptemplate.util.ManagementValue.PROTOCOL_STATUS_USER_CANCLE;



public class ClientManagementActivity extends ActionBarActivity implements View.OnClickListener, OnHttpReceive{

    private AnimatedExpandableListView listView;

    public static final int CASE_STATUS_USER_CALL = 0;
    public static final int CASE_STATUS_USER_CANCLE = 1;

    private String mToken;
    private String mTodayDate;
    private int mGroupPosition;
    private ProgressDialog dialog;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();

        Preference pref = new Preference(this);
        mToken = pref.getValue(TOKEN,"");

        getTicketList();
        viewTicketList();

        mTodayDate = getTodayDate();


    }
    public String getTodayDate(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
        String todayDate = CurYearFormat.format(date) + CurMonthFormat.format(date) + CurDayFormat.format(date);
        Log.v("getTodayDate", todayDate);

        return todayDate;

    }
    private void setContentView(){
        setContentView(R.layout.activity_expandable_list_view);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_client_management);
        Button mClientAddButton;
        mClientAddButton = (Button)findViewById(R.id.client_add);
        mClientAddButton.setOnClickListener((View.OnClickListener) this);

    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.client_add)
        {
            recognitionBacode();
        }


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReceive(String data) {
        Log.v("ClientMa/onReceive",data);

        try {
            JSONObject json = new JSONObject(data);

            String result_code = json.optString("result_code", null);
            boolean isSuccess = "0".equals(result_code) ? true : false;


            if (isSuccess && isProtocolStatus(PROTOCOL_STATUS_GET_LIST)) {
                Log.v("onReceive/Protocol", "PROTOCOL_STATUS_GET_LIST");

                JSONArray jsonArr = json.optJSONArray("list");
                if (jsonArr == null) {
                    List<GroupItem> items = new ArrayList<GroupItem>();

                    GroupItem item = new GroupItem();
                    item.title = "대기열에 아무도 없습니다.";
                    items.add(item);

                    ExampleAdapter adapter = new ExampleAdapter(this);
                    adapter.setData(items);

                    listView = (AnimatedExpandableListView) findViewById(R.id.list_view);
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

                    return;
                }

                int ticketLen = jsonArr.length();
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(ticketLen);

                List<GroupItem> items = new ArrayList<GroupItem>();


                for (int i = 0; i < ticketLen; i++) {
                    JSONObject obj = jsonArr.optJSONObject(i);
                    if (obj == null)
                        break;

                    String id = obj.optString("id", null);
                    String owner = obj.optString("owner", null);
                    String store = obj.optString("store", null);
                    String pivot = obj.optString("pivot", null);
                    String status = obj.optString("status", null);
                    String time = obj.optString("time", null);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("id", id);
                    map.put("owner", owner);
                    map.put("store", store);
                    map.put("pivot",pivot);
                    map.put("status", status);
                    map.put("time", time);

                    list.add(map);
                    // 레코드 생성 및 추가


                    GroupItem item = new GroupItem();

                    item.title = "ID : " + id + "\n"
                            + "owner : " + owner + "\n"
                            + "status : " + status + "\n";



                    ChildItem child = new ChildItem();
                    child.title = "호출";

                    item.items.add(child);
                    child = new ChildItem();
                    child.title = "대기열 삭제";


                    item.items.add(child);
                    child = new ChildItem();
                    child.title = "고객 상세정보";

                    item.items.add(child);

                    items.add(item);
                }

                ExampleAdapter adapter = new ExampleAdapter(this);
                adapter.setData(items);

                listView = (AnimatedExpandableListView) findViewById(R.id.list_view);
                listView.setAdapter(adapter);

                // In order to show animations, we need to use a custom click handler
                // for our ExpandableListView.
                listView.setOnGroupClickListener(new OnGroupClickListener() {

                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v,
                                                int groupPosition, long id) {
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

            // 서버로 부터 고객의 리스트를 받아온다.

            else if (isSuccess && isProtocolStatus(PROTOCOL_STATUS_USER_ADD)) {
                getTicketList();
            } else if (isSuccess && isProtocolStatus(PROTOCOL_STATUS_USER_CALL)) {
                getTicketList();
            } else if (isSuccess && isProtocolStatus(PROTOCOL_STATUS_USER_CANCLE)) {
                getTicketList();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GroupItem {
        String title;
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
        TextView title;
    }

    /**
     * Adapter for our list of {@link GroupItem}s.
     */
    private class ExampleAdapter extends AnimatedExpandableListAdapter {
        private LayoutInflater inflater;

        private List<GroupItem> items;

        public ExampleAdapter(Context context) {
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
                convertView = inflater.inflate(R.layout.list_item, parent,
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
            //holder.hint.setText(item.hint);

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
                convertView = inflater.inflate(R.layout.group_item, parent,
                        false);
                holder.title = (TextView) convertView
                        .findViewById(R.id.textTitle);
                convertView.setTag(holder);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }

            holder.title.setText(item.title);

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

    private void pushTicket(String user, String people){

        setProtocolStatus(PROTOCOL_STATUS_USER_ADD);
        String url;
        url = getString(R.string.api_server) + getString(R.string.api_store_ticket_push)
                + "token=" + mToken + "&user=" + user + "&pivot=" + mTodayDate;
        requestOnUIThread(url);

    }

    private void popTicket(String id){

    }
    private void removeTicket(String id){

    }
    private void getTicketList(){

        String url;
        setProtocolStatus(PROTOCOL_STATUS_GET_LIST);
        url = getString(R.string.api_server) +
                getString(R.string.api_store_ticket_list) +
                "token=" + mToken +
                "&pivot=" + "20150513" +
                "&type=" + "0";

        requestOnUIThread(url);
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

                String phoneNum = bacodeData[0];
                String peopleNum = bacodeData[2];

                pushTicket(phoneNum, peopleNum);

            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    void requestOnUIThread(final String url)
    {
        final OnHttpReceive onReceive = this;
        this.runOnUiThread(new Runnable(){
            public void run()
            {
                new HttpTask(onReceive).execute(url);
            }
        });
    }

    private String[] bacodeSplit(String bacode) {

    //    String[] result = bacode.split("_");
    //    String phoneNumber = result[0];
    //    String peopleNumber = result[2];

        return bacode.split("_");
    }



    private void DialogYesNo(String ment, final int cases){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage(ment).setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    // Action for 'Yes' Button
                        userStatusControl(cases);
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("Title");
        // Icon for AlertDialog
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();
    }

    private void userStatusControl(int cases) {

        if (cases == CASE_STATUS_USER_CALL) {
            Log.v("mCase", mGroupPosition + "&" + cases);
            popTicket("");
        }
        else if ( cases == CASE_STATUS_USER_CANCLE){

            Log.v("mCase", mGroupPosition + "&" + cases);
            removeTicket("");
        }


    }
    private void DialogProgress(){
        dialog = ProgressDialog.show(ClientManagementActivity.this, "",
                "인증번호를 서버와 통신중입니다.. 잠시만 기다려 주세요 ...", true);
        // 창을 내린다.
        // dialog.dismiss();
    }
}