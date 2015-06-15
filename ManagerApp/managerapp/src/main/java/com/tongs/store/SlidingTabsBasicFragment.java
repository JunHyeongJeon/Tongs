/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tongs.store;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tongs.store.activity.LogInPageActivity;
import com.tongs.store.activity.PreviousClientListActivity;
import com.tongs.store.util.GlobalVar;
import com.tongs.store.util.HttpTask;
import com.tongs.store.util.OnHttpReceive;
import com.tongs.store.util.Preference;
import com.tongs.store.view.AnimatedExpandableListView;
import com.tongs.store.view.SlidingTabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A basic sample which shows how to use {@link SlidingTabLayout}
 * to display a custom {@link ViewPager} title strip which gives continuous feedback to the user
 * when scrolling.
 */
public class SlidingTabsBasicFragment extends Fragment implements GlobalVar {


    private String mToken;
    private String mTodayDate;
    private long mTime;
    private AnimatedExpandableListView listView;

    private ProgressDialog mDialog;

    private int mPos = R.layout.previous_client_group_complete_item;


    static final String LOG_TAG = "SlidingTabsBasicFragment";

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
    private ViewPager mViewPager;

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {


          return inflater.inflate(com.tongs.store.R.layout.fragment_sample, container, false);


    }



    // BEGIN_INCLUDE (fragment_onviewcreated)
    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     *
     * We set the {@link ViewPager}'s adapter to be an instance of {@link SamplePagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(com.tongs.store.R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(com.tongs.store.R.id.sliding_tabs);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)

        Preference pref = Preference.getInstance();
        String token = pref.getValue(TOKEN, "");
        mToken = token;
        mTodayDate = getTodayDate();



    }


    // END_INCLUDE (fragment_onviewcreated)

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)
        /**
         * Return the title of the item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p>
         * Here we construct one using the position value, but for real application the title should
         * refer to the item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            String title = null;
            if ( position == 0)
                title = "입장완료";
            else if(position == 1)
                title = "예약부도";
            else
                title = "예약취소";
            return title;
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        View m_view[] = new View[3];
        /**
         * Instantiate the {@link View} which should be displayed at {@code position}. Here we
         * inflate a layout from the apps resources and then change the text view to signify the position.
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if(m_view[position] != null)
            {
                container.addView(m_view[position]);
                return m_view[position];
            }


            // Inflate a new layout from our resources
            View view = null;

            int pos;
            if ( position == 0) {

                mPos = R.layout.previous_client_group_complete_item;

                view = getActivity().getLayoutInflater().inflate(com.tongs.store.R.layout.complete_entry_client_list_tab,
                        container, false);

                pos = R.id.complete_entry_client_list_view;
               // getTicketList(pos);
                // Add the newly created View to the ViewPager
                getTicketList(pos, "1");

            }
            else if (position == 1)
            {
                mPos = R.layout.previous_client_group_not_comming_item;

                view = getActivity().getLayoutInflater().inflate(com.tongs.store.R.layout.not_complete_entry_client_list_tab,
                        container, false);
             //   getTicketList(pos);
                pos = R.id.not_complete_entry_client_list_view;
                getTicketList(pos, "2");

            }
            else {
                mPos = R.layout.previous_client_group_cancle_item;

                view = getActivity().getLayoutInflater().inflate(com.tongs.store.R.layout.cancel_entry_client_list_tab,
                        container, false);
                pos = R.id.cancel_entry_client_list_view;
                getTicketList(pos, "3");
              //  getTicketList(pos);

            }
            container.addView(view);

            m_view[position] = view;

            // Return the View
            return view;

        }

        /**
         * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
         * {@link View}.
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            }


    }

    private void getTicketList(final int reId, String type) {
        Log.v("Protocol", "PROTOCOL_STATUS_GET_LIST");

//        setProtocolStatus(PROTOCOL_STATUS_GET_LIST);
        String url;
        url = getString(R.string.api_server) +
                getString(R.string.api_store_ticket_list) +
                "token=" + mToken +
                "&pivot=" + "20150610" +
                "&type=" + type;

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

                            Activity activity = SlidingTabsBasicFragment.this.getActivity();
                            ExpandListViewAdapter adapter = new ExpandListViewAdapter(activity);
                            adapter.setData(items);

                            listView = (AnimatedExpandableListView) activity.findViewById(reId);
                            listView.setAdapter(adapter);

                            Display display = activity.getWindowManager().getDefaultDisplay();
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

                        ExpandListViewAdapter adapter = new ExpandListViewAdapter(SlidingTabsBasicFragment.this.getActivity());
                        adapter.setData(items);

                        listView = (AnimatedExpandableListView) getActivity().findViewById(reId);
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
                        Display display = getActivity().getWindowManager().getDefaultDisplay();
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
                convertView = inflater.inflate(mPos, parent,
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
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                new HttpTask(protocol, onReceive).execute(url);
            }
        });
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



}
