package com.example.jaecheol.tongs_v10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity
                            implements View.OnClickListener
{
    Toolbar toolbar;
    ViewPager pager;
    com.example.jaecheol.tab.ViewPagerAdapter adapter;
    com.example.jaecheol.tab.SlidingTabLayout tabs;
    CharSequence Titles[]={"바코드","대기표"};
    int Numboftabs =2;
    int numOfTabs;
    String titles[] = {""};

    Button ShowTicketButton;

    String authToken;

    int sid;
    int number;
    int createTime;
    int extraTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new com.example.jaecheol.tab.ViewPagerAdapter(getSupportFragmentManager(), Titles, Numboftabs);

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

        Intent intent = this.getIntent();
        authToken = intent.getStringExtra("auth_token");

        Toast toast = Toast.makeText(getApplicationContext(),
                            "메인 액티비티 입니다.", Toast.LENGTH_SHORT);
        toast.show();

    }


    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.button :
//                BarcodeView barcodeView = new BarcodeView(this.getApplicationContext());
//                barcodeView.setBarcodeNum("12345");
//                barcodeView.invalidate();
//                Log.d("Hello", barcodeView.getBarcodeNum());
//                break;
//            case R.id.id_showTicketButton :

//                int resultCode = -1;
//
//                try {
//
//                    HttpClient client = new DefaultHttpClient();
////                        String postURL = String.valueOf(R.string.Server_URL);
//                    String postURL = "http://tong.kr/user/waiting/get";
//                    HttpPost post = new HttpPost(postURL);
//
//                    List<NameValuePair> params = new ArrayList<NameValuePair>();
//                    params.add(new BasicNameValuePair("token", authToken));
//
//                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
//                    post.setEntity(ent);
//                    HttpResponse responsePOST = client.execute(post);
//                    HttpEntity resEntity = responsePOST.getEntity();
//
//                    if( resEntity != null ) {
//                        Log.i("RESPONSE ", EntityUtils.toString(resEntity));
//
//                        InputStream instream = resEntity.getContent();
//                        String result = convertStreamToString(instream);
//
//                        JSONObject json = new JSONObject(result);
//
//                        JSONArray nameArray =json.names();
//                        JSONArray valArray = json.toJSONArray(nameArray);
//
//                        resultCode = valArray.getInt(0);
//                        sid = valArray.getInt(2);
//                        number = valArray.getInt(3);
//                        createTime = valArray.getInt(4);
//                        extraTime = valArray.getInt(5);
//
//                        instream.close();
//                    }
//
//                } catch (Exception e)   {
//                    e.printStackTrace();
//                }
//
//                if( resultCode == 0 )   {
//                    Intent intent = new Intent(MainActivity.this, WaitingTicketActivity.class);
//
//                    intent.putExtra("sid", sid);
//                    intent.putExtra("number", number);
//                    intent.putExtra("createTime", createTime);
//                    intent.putExtra("extraTime", extraTime);
//
//                    startActivity(intent);
//                    finish();
//                }
//                else if( resultCode == 1 )  {
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "대기표를 불러오는데 실패했습니다.", Toast.LENGTH_LONG);
//                    toast.show();
//                }
//                else if( resultCode == -1 ) {
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "대기표가 존재하지 않습니다.", Toast.LENGTH_LONG);
//                    toast.show();
//                }
//
//                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
