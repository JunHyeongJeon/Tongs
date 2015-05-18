package com.example.jaecheol.tab;

import android.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaecheol.ble.BleManager;
import com.example.jaecheol.adapter.StoreAdapter;
import com.example.jaecheol.tongs.BarcodeGenerator;
import com.example.jaecheol.tongs.R;
import com.example.jaecheol.tongs.StoreViewActivity;
import com.google.zxing.BarcodeFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by JaeCheol on 15. 4. 7..
 */
public class Tab1 extends Fragment implements View.OnClickListener {

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
    String authToken;
    EditText peopleEditText;

    BarcodeGenerator barcodeGenerator;

    AlertDialog dialog;

    boolean isStoreSearched;

    private ServiceHandler handler;

    private ListView listView;
    private StoreAdapter adapter;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState)
    {
        view = inflater.inflate(R.layout.tab_1, container, false);

        adapter = new StoreAdapter(getActivity());
        listView = (ListView)view.findViewById(R.id.id_storeListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(onClickListItem);

//        addDummyList();

        return view;
    }

    @Override
    public void onResume()   {
        getStoreList(1);
        super.onResume();
    }


    private ListView.OnItemClickListener onClickListItem = new ListView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            Toast.makeText(view.getContext(), adapter.getItem(arg2).toString(), Toast.LENGTH_SHORT).show();
        }
    };

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
//            case R.id.id_barcodeGenerateButton :
//                dialog.show();
//
//                break;
//
//            case R.id.id_scanBLEButton :
//
                //Intent intent2 = new Intent();
                //intent2.setAction("android.intent.action.GCMRECV");
//                intent2.setData(Uri.parse(extras.toString()));
                //getActivity().sendBroadcast(intent2);

//            {
//                MainActivity activity = (MainActivity)getActivity();
//                activity.processPush(null);
//                break;
//                isStoreSearched = false;
//                scanBeacon();
//            }


        }
    }

    public void removeStoreList()   {
        adapter.removeList();
        adapter.notifyDataSetChanged();
    }

    public void getStoreList(int storeNum)   {

        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        authToken = mPref.getString("auth_token", null);
        if( authToken == null ) {
            /* 로그인 해제 */
        }

        removeStoreList();

        String url = getText(R.string.api_server)
                + "user/store/list"
                + "?token=" + authToken
                + "&hyper=" + storeNum;

        IHttpRecvCallback cb = new IHttpRecvCallback(){
            public void onRecv(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String result_code = json.get("result_code").toString();
                    if( result_code == "-1" ) {
                        Log.d("HELLO", "getStoreListFail");
                        return;
                    }

                    String storeId;
                    String storeName;
                    String storeLocation;
                    String storeDescription;
                    String storeWaitingNum="0";

                    JSONArray storeJArray = json.getJSONArray("list");
                    for(int i=0; i<storeJArray.length(); i++)   {
                        JSONObject store = storeJArray.getJSONObject(i);


                        storeId = store.get("id").toString();
                        storeName = store.get("name").toString();
                        storeLocation = store.get("location").toString();
                        storeDescription = store.get("description").toString();

                        adapter.add(storeId, storeName,
                                storeLocation, storeDescription,
                                storeWaitingNum);
                        adapter.notifyDataSetChanged();
                        Log.d("HELLO", storeId + "  " + storeName + "  " + storeLocation + "  "
                                        + storeDescription + "  " + storeWaitingNum);

                    }

                }
                catch(Exception e){}
            }
        };
        new HttpTask(cb).execute(url);
    }

    public void addDummyList() {

        adapter.add("0",
                "애슐리",
                "서울특별시 관악구 봉천동 862-1 라붐아울렛 9층",
                "애슐리 서울대입구점 입니다.",
                "15");
        adapter.add("1",
                "델라코트",
                "서울특별시 강남구 삼성동 159 코엑스 H113",
                "코엑스 맛집 델라코트!.",
                "21");
        adapter.add("2",
                "황태 명가",
                "서울특별시 강남구 삼성동 107",
                "토속음식 전문점",
                "9");
        adapter.add("3",
                "애슐리",
                "서울특별시 관악구 봉천동 862-1 라붐아울렛 9층",
                "애슐리 서울대입구점 입니다.",
                "15");
        adapter.add("4",
                "델라코트",
                "서울특별시 강남구 삼성동 159 코엑스 H113",
                "코엑스 맛집 델라코트!.",
                "21");
        adapter.add("5",
                "황태 명가",
                "서울특별시 강남구 삼성동 107",
                "토속음식 전문점",
                "9");
        adapter.add("6",
                "애슐리",
                "서울특별시 관악구 봉천동 862-1 라붐아울렛 9층",
                "애슐리 서울대입구점 입니다.",
                "15");
        adapter.add("7",
                "델라코트",
                "서울특별시 강남구 삼성동 159 코엑스 H113",
                "코엑스 맛집 델라코트!.",
                "21");
        adapter.add("8",
                "황태 명가",
                "서울특별시 강남구 삼성동 107",
                "토속음식 전문점",
                "9");
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
        HttpTask(IHttpRecvCallback cb)
        {
            m_cb = cb;
        }

        protected String doInBackground(String... params)
        {
            InputStream is = getInputStreamFromUrl(params[0]);

            String result = convertStreamToString(is);

            return result;
        }

        protected void onPostExecute(String result)
        {
            Log.d("Hello", result);
            if(m_cb != null)
            {
                m_cb.onRecv(result);
                return;
            }
        }
    }
}
