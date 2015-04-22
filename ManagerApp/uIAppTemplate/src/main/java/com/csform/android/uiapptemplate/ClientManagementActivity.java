package com.csform.android.uiapptemplate;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    public static final int PROTOCOL_STATUS_USER_CALL = 3;
    public static final int PROTOCOL_STATUS_USER_CANCLE = 4;

    public static final int CASE_STATUS_USER_CALL = 0;
    public static final int CASE_STATUS_USER_CANCLE = 1;


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

        String url = getText(R.string.server_api_get_url) + "token=" + emailToken
                + "&sid=" + sid;
        setProtocolStatus(PROTOCOL_STATUS_GET_LIST);
        new HttpTask().execute(url);


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
    public static byte[] remoteSyncHttp(String requestUrl, String arrParam[][])
    {
        PrintWriter pw = null;
        HttpURLConnection conn = null;
        byte resultData[] = null;
        try
        {
            URL url = new URL(requestUrl);
            conn = (HttpURLConnection)url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                conn.setUseCaches(false);

                if(arrParam != null)
                {
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    StringBuilder param = new StringBuilder();
                    for (int i=0; i<arrParam.length; i++)
                    {
                        param.append(arrParam[i][0]);
                        param.append("=");
                        param.append(arrParam[i][1]);
                        if ( i != (arrParam.length-1) )
                            param.append("&");
                    }
                    String paramStr = param.toString();
                    conn.setRequestProperty("Content-Length", "" + Integer.toString(paramStr.getBytes().length));

                    pw = new PrintWriter(conn.getOutputStream());
                    pw.print(paramStr);
                    pw.flush();
                    pw.close();
                }
                int responseCode = conn.getResponseCode();
                if ( responseCode == HttpURLConnection.HTTP_OK )
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    InputStream is = conn.getInputStream();
                    int nRead;
                    byte data[] = new byte[10240];
                    while( (nRead = is.read(data)) != -1 )
                    {
                        baos.write(data, 0, nRead);
                    };
                    resultData = baos.toByteArray();
                    baos.close();
                    is.close();

                }
                else
                {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    InputStream is = conn.getErrorStream();
                    int nRead;
                    byte data[] = new byte[10240];
                    while( (nRead = is.read(data)) != -1 )
                    {
                        baos.write(data, 0, nRead);
                    };
                    resultData = baos.toByteArray();
                    baos.close();
                    is.close();
                }
            }
        }
        catch (Exception e)
        {
            Log.e("HttpError", requestUrl);
//            e.printStackTrace();
        }
        finally
        {
            if(conn != null)
            {
                try {
                    conn.disconnect();
                } catch(Exception e){}
            }
        }
        return resultData;
    }

    public String getRemoteData(String url) {
        byte data[] = new byte[1024 * 64];
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            is = response.getEntity().getContent();

            int destLength = Integer.parseInt( response.getFirstHeader("Content-Length").getValue() );
            int totalRead = 0;
            while(true)
            {
                int read = is.read(data, totalRead, 10240);
                if(read <= 0)
                    break;
                totalRead += read;
            }
            httpclient.getConnectionManager().shutdown();
            return new String(data, 0, totalRead);

        } catch (Exception e) {
            Log.e("[GET REQUEST]", "Network exception", e);
        }
        return null;
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
            else if (isSuccess && isProtocolStatus(PROTOCOL_STATUS_USER_ADD)) {
                String url = getText(R.string.server_api_get_url) + "token=" + emailToken
                        + "&sid=" + sid;
                setProtocolStatus(PROTOCOL_STATUS_GET_LIST);
                //new HttpTask().execute(url);
                requestOnUIThread(url);
            } else if (isSuccess && isProtocolStatus(PROTOCOL_STATUS_USER_CALL)) {
                String url = getText(R.string.server_api_get_url) + "token=" + emailToken
                        + "&sid=" + sid;
                setProtocolStatus(PROTOCOL_STATUS_GET_LIST);
                //new HttpTask().execute(url);
                requestOnUIThread(url);
            } else if (isSuccess && isProtocolStatus(PROTOCOL_STATUS_USER_CANCLE)) {
                String url = getText(R.string.server_api_get_url) + "token=" + emailToken
                        + "&sid=" + sid;
                setProtocolStatus(PROTOCOL_STATUS_GET_LIST);
                //new HttpTask().execute(url);
                requestOnUIThread(url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void requestOnUIThread(final String url)
    {
        this.runOnUiThread(new Runnable(){
            public void run()
            {
                new HttpTask().execute(url);
            }
        });
    }


    class HttpTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {

//            InputStream is = getInputStreamFromUrl(params[0]);
//            String result = convertStreamToString(is);

            String response = null;

            if(params[0].equals("http://somabell01.cloudapp.net:8080/store/store/get?token=6001b1ce82bc090f7b29964b888903c318f6d518&sid=1"))
            {
                response = null;
            }

            try {
                //response = getRemoteData(params[0]);
                byte data[] = remoteSyncHttp(params[0], null);
                if(data == null)
                {
                    Thread.currentThread().sleep(1000);
                    data = remoteSyncHttp(params[0], null);
                }
                if(data == null)
                    return null;
                response = new String(data);
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            return response;
        }

        protected void onPostExecute(String result) {

//            Log.d("Server_result", result);
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
            setProtocolStatus(PROTOCOL_STATUS_USER_CALL);
            new HttpTask().execute(url);


        } else if ( mCases == CASE_STATUS_USER_CANCLE){

            Log.v("mCase", mGroupPosition + "&" + mCases);
            String url = getText(R.string.server_api_user_cancle) + "token=" + emailToken
                    + "&sid=" + sid + "&index=" + mGroupPosition;
            Log.v("caseCancle", url);
            setProtocolStatus(PROTOCOL_STATUS_USER_CANCLE);
            new HttpTask().execute(url);
        }

    }

    private void DialogProgress(){
        dialog = ProgressDialog.show(ClientManagementActivity.this, "",
                "인증번호를 서버와 통신중입니다.. 잠시만 기다려 주세요 ...", true);
        // 창을 내린다.
        // dialog.dismiss();
    }
}