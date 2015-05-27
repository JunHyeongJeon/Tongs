package com.tongs.user.tab;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.tongs.user.adapter.StoreAdapter;
import com.tongs.user.ble.BleManager;
import com.tongs.user.item.StoreItem;
import com.tongs.user.activity.BarcodeGenerator;
import com.tongs.user.activity.R;
import com.tongs.user.activity.StoreViewActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by JaeCheol on 15. 4. 7..
 */
public class StoreTab extends Fragment implements View.OnClickListener {

    Bitmap barcode;
    ImageView barcodeView;
    String barcodeContents;
    Button barcodeGenerateButton;
    TextView currentNumText;

    Button scanBLEButton;

    LinearLayout barcodeLayout;
    LinearLayout noBarcodeLayout;

    String mobileNumber;
    String peopleNumber;
    String uid;
    String sid;
    String hid = "1";
    String authToken;
    EditText peopleEditText;
    TextView hyperText;

    BarcodeGenerator barcodeGenerator;

    AlertDialog dialog;

    ProgressDialog mProgressDialog;

    boolean isStoreSearched;

    private ServiceHandler handler;

    private ListView listView;
    private StoreAdapter adapter;

    private RelativeLayout searchLayout;

    private ArrayList<String> hyperList[] = new ArrayList[2];

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState)
    {
        view = inflater.inflate(R.layout.tab_store, container, false);

        initStoreTab();
        getDataFromSharedPref();

        getHyperList();

        return view;
    }

    @Override
    public void onResume()   {
        getStoreList();
        super.onResume();
    }

    private void initStoreTab() {

//        adapter = new StoreAdapter();
        listView = (ListView) view.findViewById(R.id.id_storeListView);
//        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onClickListItem);

        searchLayout = (RelativeLayout) view.findViewById(R.id.searchLayout);
        searchLayout.setOnClickListener(this);

        hyperText = (TextView) view.findViewById(R.id.id_hyperText);

        for (int i = 0; i < 2; i++) {
            hyperList[i] = new ArrayList<String>();
        }
    }

    private ListView.OnItemClickListener onClickListItem = new ListView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> a, View v, int position, long id) {
            StoreItem newsData = (StoreItem) listView.getItemAtPosition(position);

            sid = newsData.getId();
            showStoreView();
        }
    };


    private void getDataFromSharedPref()    {

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        authToken = mPref.getString("auth_token", null);
    }

    private void setBarcode(View view)
    {
//        barcodeGenerator = new BarcodeGenerator();
//
//        barcodeView = (ImageView)view.findViewById(R.id.id_barcodeView);
//        barcodeLayout = (LinearLayout)view.findViewById(R.id.id_barcodeLayout);
//        noBarcodeLayout = (LinearLayout)view.findViewById(R.id.id_noBarcodeLayout);
//        currentNumText = (TextView)view.findViewById(R.id.id_currentNum);
//
//        barcodeGenerateButton = (Button)view.findViewById(R.id.id_barcodeGenerateButton);
//        barcodeGenerateButton.setOnClickListener(this);
//
//        showBarcodeLayout(false);
    }

    private void setDialog(View view)
    {
        // AlertDialog 객체 선언
        dialog = createInputDialog();

        // Context 얻고, 해당 컨텍스트의 레이아웃 정보 얻기
        Context context = getActivity();
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 레이아웃 설정
        View layout = layoutInflater.inflate(R.layout.dialog_barcodegenerate,
                (ViewGroup) view.findViewById(R.id.id_popup_root));
        peopleEditText = (EditText)layout.findViewById(R.id.id_popup_edittext);

        // Input 소프트 키보드 보이기
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        // AlertDialog에 레이아웃 추가
        dialog.setView(layout);
    }

    private void showBarcodeLayout(boolean isBarcodeExist) {
        if (isBarcodeExist == true) {

            barcodeLayout.setVisibility(View.VISIBLE);
            noBarcodeLayout.setVisibility(View.GONE);
        } else {

            barcodeLayout.setVisibility(View.GONE);
            noBarcodeLayout.setVisibility(View.VISIBLE);
        }
    }


    public void onClick(View v) {

        switch ( v.getId() )    {
            case R.id.searchLayout :
                showHyperDialog();
                break;
        }
    }

    public void removeStoreList()   {
//        adapter.removeList();
//        adapter.notifyDataSetChanged();
    }

    public void getStoreList()   {

        removeStoreList();

        String url = getText(R.string.api_server)
                + "user/store/list"
                + "?token=" + authToken
                + "&hyper=" + hid;

        IHttpRecvCallback cb = new IHttpRecvCallback(){
            public void onRecv(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String result_code = json.get("result_code").toString();
                    if( result_code == "-1" ) {
                        Log.d("HELLO", "getStoreListFail");
                        return;
                    }

                    ArrayList<StoreItem> listData = new ArrayList<StoreItem>();

                    JSONArray storeJArray = json.getJSONArray("list");
                    for(int i=0; i<storeJArray.length(); i++)   {
                        JSONObject store = storeJArray.getJSONObject(i);

                        StoreItem newData = new StoreItem();
                        newData.setUrl(store.getString("img"));
                        newData.setId(store.getString("id"));
                        newData.setName(store.getString("name"));
                        newData.setLocation(store.getString("location"));
                        newData.setWaitingNum(store.getString("wait"));
                        newData.setDescription(store.getString("description"));

                        listData.add(newData);
                    }

                    listView.setAdapter(new StoreAdapter(view.getContext(), listData));
                }
                catch(Exception e){}
            }
        };
        new HttpTask(cb).execute(url);
    }

    private void getHyperList() {
        String url = getText(R.string.api_server)
                + "user/hyper/list"
                + "?token=" + authToken;

        IHttpRecvCallback cb = new IHttpRecvCallback(){
            public void onRecv(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String result_code = json.get("result_code").toString();
                    if( result_code == "-1" ) {
                        Log.d("HELLO", "getHyperListFail");
                        return;
                    }

                    if( hyperList[0] != null ) {
                        hyperList[0].clear();
                        hyperList[1].clear();
                    }
                    JSONArray hyperJArray = json.getJSONArray("list");
                    for(int i=0; i<hyperJArray.length(); i++)   {
                        JSONObject hyper = hyperJArray.getJSONObject(i);

                        hyperList[0].add(hyper.getString("id"));
                        hyperList[1].add(hyper.getString("name"));
                    }

                    hyperText.setText(" 현재마켓 : " + hyperList[1].get(Integer.parseInt(hid)-1));
                }
                catch(Exception e){}
            }
        };
        new HttpTask(cb).execute(url);
    }

    private void showHyperDialog()   {

        int len = hyperList[0].size();
        final CharSequence items[] = new CharSequence[len];
        for(int i=0; i<len; i++)   {
            items[i] = hyperList[1].get(i);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("마켓을 선택해주세요");
        builder.setIcon(R.drawable.store_icon);
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (!hid.equals(String.valueOf(item + 1))) {
                    hid = String.valueOf(item + 1);
                    getStoreList();

                    SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                    SharedPreferences.Editor editor = mPref.edit();
                    editor.putString("hid", hid);
                    editor.commit();

                    hyperText.setText(" 현재마켓 : " + items[item]);
                }
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void setBarcodeContents(String _barcodeContents) {
        barcodeContents = _barcodeContents;
    }

    public String getBarcodeContents()  {
        return barcodeContents;
    }

    public void registerBarcode()    {

        try {
            barcode = barcodeGenerator.encodeAsBitmap(barcodeContents, BarcodeFormat.CODE_128, 600, 400);
        } catch (Exception e) {
            e.printStackTrace();
        }

        barcodeView.setImageBitmap(barcode);
        barcodeGenerateButton.setText("인원수 변경");

        showBarcodeLayout(true);
    }


    private AlertDialog createInputDialog() {

        EditText edittext= new EditText(this.getActivity());

        AlertDialog dialogBox = new AlertDialog.Builder(this.getActivity())
                .setTitle("안내")
                .setMessage("몇명이서 오셨나요?")
                .setView(edittext)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 확인 버튼 눌렀을때 액션
                        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        mobileNumber = mPref.getString("number", null);
                        uid = mPref.getString("uid", null);
                        if( peopleEditText != null )
                            peopleNumber = peopleEditText.getText().toString();

                        if( mobileNumber == null ) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                            "등록된 정보가 없습니다. 다시 로그인해주세요.",
                                            Toast.LENGTH_SHORT);
                        }
                        else    {

                            setBarcodeContents(uid + "_" + mobileNumber + "_" + peopleNumber);
                            currentNumText.setText("현재 인원은 " + peopleNumber + "명 입니다.");
                            registerBarcode();
                        }
                    }
                })
                .setNeutralButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // 취소 버튼 눌렀을때 액션 구현
                    }
                }).create();
        return dialogBox;
    }


    class ServiceHandler extends Handler
    {
        public void handleMessage(Message msg)
        {
            String scanData;

            switch (msg.what)
            {
                case 111:
                    Log.d("BLE", "SEARCH COMPLETE");
                    break;
                case 112:
                    scanData = (String)msg.obj;
                    if( scanData != null) {
                        Log.d("BLE", scanData);

                        parseDataToBeacon(scanData);
                    }
//                    m_webView.loadUrl("javascript:output('"+scanData+"')");
                    break;
                case 113:
                    scanData = (String)msg.obj;
                    Log.d("JACH", "load success");

//                    m_webView.loadUrl("javascript:getSoundScanJson('"+scanData+"')");
                    break;

                default:
                    super.handleMessage(msg);
            }
        }
    }

    public void scanBeacon()
    {
        BleManager.getInstance(this.getView().getContext(), handler).scanLeDevice(true);
    }

    private void parseDataToBeacon(String scanData) {

        String beaconData[] = scanData.split("_");

        String beconName=beaconData[0];
        String uuid=beaconData[1];
        String major=beaconData[2];
        String minor=beaconData[3];
        String accurancy=beaconData[4];

        getStoreInfo(major, minor);
    }

    private void getStoreInfo(String major, String minor)  {

        if( "0".equals(major) || "0".equals(minor) )
            return;

        String url = getText(R.string.api_server)
                + "user/beacon/get"
                + "?major=" + major
                + "&minor=" + minor;

        IHttpRecvCallback cb = new IHttpRecvCallback(){
            public void onRecv(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String result_code = json.get("result_code").toString();
                    Log.d("Hello", result_code);
                    if( "-1".equals(result_code) )
                        return;

                    sid = json.getString("sid");

                    if( isStoreSearched == false )  {
                        isStoreSearched = true;
                        showStoreView();
                    }
                }
                catch(Exception e){}
            }
        };
        new HttpTask(cb).execute(url);
    }

    private void showStoreView()  {

        Intent intent = new Intent(getView().getContext(),StoreViewActivity.class);
        intent.putExtra("sid", sid);
        startActivity(intent);
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

        HttpTask(IHttpRecvCallback cb) {
            m_cb = cb;
        }

        protected String doInBackground(String... params) {
            InputStream is = getInputStreamFromUrl(params[0]);

            String result = convertStreamToString(is);

            return result;
        }

        protected void onPostExecute(String result) {
            Log.d("Hello", result);
            if (m_cb != null) {
                m_cb.onRecv(result);
                return;
            }
        }
    }
}
