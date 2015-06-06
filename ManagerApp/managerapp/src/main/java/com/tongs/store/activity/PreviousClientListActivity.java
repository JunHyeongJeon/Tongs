package com.tongs.store.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tongs.store.R;
import com.tongs.store.SlidingTabsBasicFragment;
import com.tongs.store.util.GlobalVar;
import com.tongs.store.util.HttpTask;
import com.tongs.store.util.OnHttpReceive;
import com.tongs.store.util.Preference;
import com.tongs.store.view.AnimatedExpandableListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PreviousClientListActivity extends ActionBarActivity implements GlobalVar {


    private String mToken;
    private String mTodayDate;
    private long mTime;
    private AnimatedExpandableListView listView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_client_list);

        Preference pref = Preference.getInstance();
        String token = pref.getValue(TOKEN,"");
        mToken = token;
        mTodayDate = getTodayDate();


        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }
        getTicketList();

    }
    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);

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

    public String dateSplit(String date){
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat month = new SimpleDateFormat("MM");
        SimpleDateFormat day = new SimpleDateFormat("dd");
        String result = year.format(date) + "." +
                month.format(date) + "." +
                day.format(date) + ".";
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_previous_client_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
                convertView = inflater.inflate(R.layout.previous_client_group_item, parent,
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


    void requestOnUIThread(final int protocol, final String url, final OnHttpReceive onReceive)
    {
        Log.v("URL", url);
        this.runOnUiThread(new Runnable() {
            public void run() {
                new HttpTask(protocol, onReceive).execute(url);
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
                "&type=" + "1";

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
                        if (jsonArr == null) {
                            List<GroupItem> items = new ArrayList<GroupItem>();

                            GroupItem item = new GroupItem();
                            item.ticketNum = "000";
                            items.add(item);

                            ExpandListViewAdapter adapter = new ExpandListViewAdapter(PreviousClientListActivity.this);
                            adapter.setData(items);

                            listView = (AnimatedExpandableListView) findViewById(R.id.complete_entry_client_list_view);
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

                        List<GroupItem> items = new ArrayList<GroupItem>();

                        final String index[] = new String[ticketLen];
                        for (int i = 0; i < ticketLen; i++) {

                            JSONObject obj = jsonArr.optJSONObject(i);
                            if (obj == null)
                                break;

                            String id = obj.optString("id", null);
                            String owner = obj.optString("owner", null);
                            String store = obj.optString("store", null);
                            String status = obj.optString("status", null);
                            String pivot = obj.optString("pivot", null);
                            String people = obj.optString("people", null);
                            long popTime = obj.optLong("time", 0);

                            index[i] = id;

                            long time = (mTime - popTime) / 60;

                            //pivot = dateSplit(pivot);

                            // 레코드 생성 및 추가

                            GroupItem item = new GroupItem();

                            item.ticketNum = id;
                            item.peopleNum = people;
                            item.waitTime = time + "";
                            item.order = i + "";
                            item.pivot = pivot;


                            ChildItem child = new ChildItem();
                            child.title = "호출";
                            item.items.add(child);
                            items.add(item);
                        }

                        ExpandListViewAdapter adapter = new ExpandListViewAdapter(PreviousClientListActivity.this);
                        adapter.setData(items);

                        listView = (AnimatedExpandableListView) findViewById(R.id.complete_entry_client_list_view);
                        listView.setAdapter(adapter);

                        // In order to show animations, we need to use a custom click handler
                        // for our ExpandableListView.
                        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

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




}
