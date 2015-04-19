package com.example.jaecheol.tab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jaecheol.ble.BleManager;
import com.example.jaecheol.tongs.BarcodeGenerator;
import com.example.jaecheol.tongs.R;
import com.google.zxing.BarcodeFormat;

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
    EditText peopleEditText;

    BarcodeGenerator barcodeGenerator;

    AlertDialog dialog;


    private ServiceHandler handler;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState)
    {
        View view = inflater.inflate(R.layout.tab_1, container, false);

        setBarcode(view);
        setDialog(view);

        scanBLEButton = (Button)view.findViewById(R.id.id_scanBLEButton);
        scanBLEButton.setOnClickListener(this);
        handler = new ServiceHandler();

        return view;
    }

    private void setBarcode(View view)
    {
        barcodeGenerator = new BarcodeGenerator();

        barcodeView = (ImageView)view.findViewById(R.id.id_barcodeView);
        barcodeLayout = (LinearLayout)view.findViewById(R.id.id_barcodeLayout);
        noBarcodeLayout = (LinearLayout)view.findViewById(R.id.id_noBarcodeLayout);
        currentNumText = (TextView)view.findViewById(R.id.id_currentNum);

        barcodeGenerateButton = (Button)view.findViewById(R.id.id_barcodeGenerateButton);
        barcodeGenerateButton.setOnClickListener(this);

        showBarcodeLayout(false);
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
            case R.id.id_barcodeGenerateButton :
                dialog.show();

                break;

            case R.id.id_scanBLEButton :
                scanBeacon();
                break;
        }
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

                        if (mobileNumber.isEmpty() == false) {

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
                    break;
                case 112:
                    scanData = (String)msg.obj;
                    if( scanData != null) {
                        Log.d("BLE", scanData);

//                        beacon.


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

        String beconName=null;
        String uuid=null;
        String major=null;
        String minor=null;
        String accurancy=null;

        int index=0;
        char ch;
        while( (ch = scanData.charAt(index++)) == ':' )    {
            beconName += ch;
        }
        while( (ch = scanData.charAt(index++)) == ',' )    {
            uuid += ch;
        }
        while( (ch = scanData.charAt(index++)) == '/' )    {
            major += ch;
        }
        while( (ch = scanData.charAt(index++)) == ',' )    {
            minor += ch;
        }
        while( (ch = scanData.charAt(index++)) == ';' )    {
            accurancy += ch;
        }
    }
}
