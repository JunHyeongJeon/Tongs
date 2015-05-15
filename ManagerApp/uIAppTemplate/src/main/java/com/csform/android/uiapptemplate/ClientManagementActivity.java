package com.csform.android.uiapptemplate;

import java.util.ArrayList;
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
import com.csform.android.uiapptemplate.view.AnimatedExpandableListView;
import com.csform.android.uiapptemplate.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;


import org.json.JSONArray;
import org.json.JSONObject;

import static com.csform.android.uiapptemplate.util.ManagementMethod.isProtocolStatus;
import static com.csform.android.uiapptemplate.util.ManagementMethod.setProtocolStatus;

/**
 * This is an example usage of the AnimatedExpandableListView class.
 *
 * It is an activity that holds a listview which is populated with 100 groups
 * where each group has from 1 to 100 children (so the first group will have one
 * child, the second will have two children and so on...).
 */


public class ClientManagementActivity extends ActionBarActivity implements View.OnClickListener, OnHttpReceive{

    private AnimatedExpandableListView listView;
    private int m_protocolStatus = 0;

    public static final int CASE_STATUS_USER_CALL = 0;
    public static final int CASE_STATUS_USER_CANCLE = 1;

    private String emailToken = "";
    private String sid = "1";
    private String uid = null;
    private String num = null;


    private int mGroupPosition;
    private ProgressDialog dialog;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list_view);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.action_bar_centor);
        Button mClientAddButton;
        mClientAddButton = (Button)findViewById(R.id.client_add);
        mClientAddButton.setOnClickListener((View.OnClickListener) this);

    }

    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.client_add)
        {
            AddClient();
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
        String strJson = new String(data);

        try {
            JSONObject json = new JSONObject(strJson);

            String result_code = json.optString("result_code", null);
            boolean isSuccess = "0".equals(result_code) ? true : false;

            Log.v("result", m_protocolStatus + " : " + isSuccess);

            if (isSuccess &&
                    isProtocolStatus(ManagementMethod.PROTOCOL_STATUS_GET_LIST)) {
                Log.v("json", json.toString());

                JSONArray jsonArr = json.optJSONArray("tickets");
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

                    String ticketNo = obj.optString("ticket", null);
                    String uid = obj.optString("uid", null);
                    String num = obj.optString("num", null);
                    //    String gcm = obj.optString("gcm", null);
                    String mdn = obj.optString("mdn", null);
                    String createTime = obj.optString("createTime", null);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("ticket", "대기표 번호 : " + ticketNo);
                    map.put("uid", uid + "번째 고객");
                    map.put("num", "인원수 : " + num);
                    //   map.put("gcm", gcm);
                    map.put("mdn", "전화번호 : " + mdn);
                    map.put("createTime", createTime);
                    list.add(map);
                    // 레코드 생성 및 추가


                    GroupItem item = new GroupItem();

                    item.title = "대기번호 : " + ticketNo + "\n" +
                            //        "고객전화번호 : " + mdn + "\n" +
                            "인원수 : " + num + "\n";



                    ChildItem child = new ChildItem();
                    child.title = "호출";
                    //child.hint = "Too awesome";

                    item.items.add(child);
                    child = new ChildItem();
                    child.title = "대기열 삭제";
                    //child.hint = "Too awesome";


                    // 고객의 상세정보 보기
                    //item.items.add(child);
                    //child = new ChildItem();
                    //child.title = "고객 상세정보";
                    //child.hint = "Too awesome";

                    item.items.add(child);


                    items.add(item);
                }

                /** Keys used in Hashmap */


                //       String[] from = {"ticket", "mdn", "num"};

                /** Ids of views in listview_layout */
                //        int[] to = {R.id.tv_country, R.id.tv_country_details, R.id.iv_flag};


/*
                SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), list, R.layout.lv_layout, from, to);

                ListView listView = (ListView) findViewById(R.id.lv_countries);
                listView.setAdapter(adapter);
*/
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
                        // We call collapseGroupWithAnimation(int) and
                        // expandGroupWithAnimation(int) to animate group
                        // expansion/collapse.
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
                        mGroupPosition = groupPosition;
                        switch (childPosition) {
                            case CASE_STATUS_USER_CALL: {
                                DialogYesNo(getString(R.string.case_status_user_call) ,CASE_STATUS_USER_CALL );

                                break;
                            }
                            case CASE_STATUS_USER_CANCLE: {
                                DialogYesNo(getString(R.string.case_status_user_cancle), CASE_STATUS_USER_CANCLE);
                                break;
                            }
                            case 2: {
                                // 고객의 상세 정보 관리
                                break;
                            }
                        }
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
            /*
            else if (isSuccess && isProtocolStatus(ManagementMethod.PROTOCOL_STATUS_USER_ADD)) {
                String url = getText(R.string.server_api_get_url) + "token=" + emailToken
                        + "&sid=" + sid;
                setProtocolStatus(ManagementMethod.PROTOCOL_STATUS_GET_LIST);

                requestOnUIThread(url);
            } else if (isSuccess && isProtocolStatus(ManagementMethod.PROTOCOL_STATUS_USER_CALL)) {
                String url = getText(R.string.server_api_get_url) + "token=" + emailToken
                        + "&sid=" + sid;
                setProtocolStatus(ManagementMethod.PROTOCOL_STATUS_GET_LIST);
                requestOnUIThread(url);
            } else if (isSuccess && isProtocolStatus(ManagementMethod.PROTOCOL_STATUS_USER_CANCLE)) {
                String url = getText(R.string.server_api_get_url) + "token=" + emailToken
                        + "&sid=" + sid;
                setProtocolStatus(ManagementMethod.PROTOCOL_STATUS_GET_LIST);
                requestOnUIThread(url);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class GroupItem {
        String title;
        List<ChildItem> items = new ArrayList<ChildItem>();
    }

    private static class ChildItem {
        String title;
        //String hint;
    }

    private static class ChildHolder {
        TextView title;
        //TextView hint;
    }

    private static class GroupHolder {
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

    private void pushTicket(){

    }

    private void popTicket(){

    }
    private void removeTicket(){

    }
    private void getTicketList(){

    }
    private void AddClient() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE");
        startActivityForResult(intent, 0);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.v("onActivityResult", contents);
                bacodeSplitAndSend(contents);
                // Handle successful scan
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

    private void bacodeSplitAndSend(String bacode) {

        String[] result = bacode.split("_");
        //d  Log.v("Hello2", emailToken);
        uid = result[0];
        num = result[2];

        String url = getText(R.string.server_api_put_url) + "token=" + emailToken +
                "&uid=" + uid + "&sid=" + sid + "&num=" + num;

        Log.v("url", url);
        //userAdd = true;
        setProtocolStatus(ManagementMethod.PROTOCOL_STATUS_USER_ADD);

        new HttpTask(this).execute(url);

    }



    private void DialogYesNo(String ment, final int cases){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage(ment).setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    // Action for 'Yes' Button
                        UserStatusControl(cases);
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

    private void UserStatusControl(int mCases){

        if (mCases == CASE_STATUS_USER_CALL){
            Log.v("mCase", mGroupPosition + "&" + mCases);

            String url = getText(R.string.server_api_user_call) +"token=" + emailToken
                    +"&sid=" + sid + "&index=" + mGroupPosition;
            Log.v("caseCall", url);
            setProtocolStatus(ManagementMethod.PROTOCOL_STATUS_USER_CALL);
            new HttpTask(this).execute(url);


        } else if ( mCases == CASE_STATUS_USER_CANCLE){

            Log.v("mCase", mGroupPosition + "&" + mCases);
            String url = getText(R.string.server_api_user_cancle) + "token=" + emailToken
                    + "&sid=" + sid + "&index=" + mGroupPosition;
            Log.v("caseCancle", url);
            setProtocolStatus(ManagementMethod.PROTOCOL_STATUS_USER_CANCLE);
            new HttpTask(this).execute(url);
        }

    }

    private void DialogProgress(){
        dialog = ProgressDialog.show(ClientManagementActivity.this, "",
                "인증번호를 서버와 통신중입니다.. 잠시만 기다려 주세요 ...", true);
        // 창을 내린다.
        // dialog.dismiss();
    }
}