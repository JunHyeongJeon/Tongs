package com.csform.android.uiapptemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.csform.android.uiapptemplate.view.AnimatedExpandableListView;
import com.csform.android.uiapptemplate.view.AnimatedExpandableListView.AnimatedExpandableListAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This is an example usage of the AnimatedExpandableListView class.
 *
 * It is an activity that holds a listview which is populated with 100 groups
 * where each group has from 1 to 100 children (so the first group will have one
 * child, the second will have two children and so on...).
 */


public class ClientManagementActivity extends ActionBarActivity {


    private String emailToken;

    private AnimatedExpandableListView listView;
    private int m_protocolStatus = 0;
    public static final int PROTOCOL_STATUS_USER_ADD = 1;
    public static final int PROTOCOL_STATUS_GET_LIST = 2;

    private String sid = "1";
    private String uid = null;
    private String num = null;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list_view);


        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        emailToken = mPref.getString("emailToken", null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button mClientAddButton = (Button) findViewById(R.id.client_add);
        mClientAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddClient();


            }
        });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void AddClient() {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "CODE_39,CODE_93,CODE_128,DATA_MATRIX,ITF,CODABAR,EAN_13,EAN_8,UPC_A,QR_CODE");
        startActivityForResult(intent, 0);

      /*  if( !("".equals(intent.getStringExtra("SCAN_RESULT"))) ) {
            uid = intent.getStringExtra("SCAN_RESULT"); //content에 바코드 값이 들어갑니다.
            Log.v("Hello","dldld");
        }*/
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

    public InputStream getInputStreamFromUrl(String url) {
        InputStream content = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            content = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("[GET REQUEST]", "Network exception", e);
        }
        return content;
    }

    private static String convertStreamToString(InputStream is) {
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

    public void processReceive(String strJson) {
        try {
            JSONObject json = new JSONObject(strJson);

            String result_code = json.optString("result_code", null);
            boolean isSuccess = "0".equals(result_code) ? true : false;

            Log.v("result", m_protocolStatus + " : " + isSuccess);

            if (isSuccess && isProtocolStatus(PROTOCOL_STATUS_GET_LIST)) {
                Log.v("json", json.toString());

                JSONArray jsonArr = json.optJSONArray("tickets");
                if (jsonArr == null)
                    return;


                int ticketLen = jsonArr.length();
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>(ticketLen);
                for (int i = 0; i < ticketLen; i++) {
                    JSONObject obj = jsonArr.optJSONObject(i);
                    if (obj == null)
                        break;

                    String ticketNo = obj.optString("ticket", null);
                    String uid = obj.optString("uid", null);
                    String num = obj.optString("num", null);
                    String gcm = obj.optString("gcm", null);
                    String mdn = obj.optString("mdn", null);
                    String createTime = obj.optString("createTime", null);

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("ticket", "대기표 번호 : " + ticketNo);
                    map.put("uid", uid + "번째 고객");
                    map.put("num", "인원수 : " + num);
                    map.put("gcm", gcm);
                    map.put("mdn", "전화번호 : " + mdn);
                    map.put("createTime", createTime);
                    list.add(map);
                    // 레코드 생성 및 추가
                }

                /** Keys used in Hashmap */


                String[] from = {"ticket", "mdn", "num"};

                /** Ids of views in listview_layout */
                int[] to = {R.id.tv_country, R.id.tv_country_details, R.id.iv_flag};



//              ListViewLoaderTask listViewLoaderTask = new ListViewLoaderTask();
//              listViewLoaderTask.execute(result);
/*
                SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), list, R.layout.lv_layout, from, to);

                ListView listView = (ListView) findViewById(R.id.lv_countries);
                listView.setAdapter(adapter);
*/
                List<GroupItem> items = new ArrayList<GroupItem>();

                // Populate our list with g5{
                GroupItem item = new GroupItem();

                item.title = "대기번호";


                ChildItem child = new ChildItem();
                child.title = "호출";
                //child.hint = "Too awesome";

                item.items.add(child);
                child = new ChildItem();
                child.title = "대기열 삭제";
                //child.hint = "Too awesome";

                item.items.add(child);
                child = new ChildItem();
                child.title = "고객 상세정보";
                //child.hint = "Too awesome";

                item.items.add(child);


                items.add(item);


                ExampleAdapter adapter;

                adapter = new ExampleAdapter(this);
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
                String url = getText(R.string.server_api_get_url) + "token=" + emailToken
                        + "&sid=" + sid;
                setProtocolStatus(PROTOCOL_STATUS_GET_LIST);
                new HttpTask().execute(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class HttpTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            InputStream is = getInputStreamFromUrl(params[0]);

            String result = convertStreamToString(is);//

            return result;
        }

        protected void onPostExecute(String result) {
            Log.d("Server_result", result);

            processReceive(result);
        }
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
        setProtocolStatus(PROTOCOL_STATUS_USER_ADD);

        new HttpTask().execute(url);

    }

    public void setProtocolStatus(int status) {
        m_protocolStatus = status;
    }

    public int getProtocolStatus() {
        return m_protocolStatus;
    }

    public boolean isProtocolStatus(int status) {
        return m_protocolStatus == status ? true : false;
    }

}