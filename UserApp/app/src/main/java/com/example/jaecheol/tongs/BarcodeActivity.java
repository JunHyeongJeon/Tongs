package com.example.jaecheol.tongs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;

/**
 * Created by JaeCheol on 15. 5. 10..
 */
public class BarcodeActivity extends ActionBarActivity
                             implements View.OnClickListener
{

    Intent intent;
    Bitmap barcode;

    int currentNum;
    String uid;
    String mobileNumber;
    ImageView barcodeView;

    BarcodeGenerator barcodeGenerator;

    Toolbar toolbar;

    Button plusButton;
    Button minusButton;
    TextView currentText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        barcodeActivityInit();
        setToolbar();
        setBarcode();
        registerBarcode();
    }

    private void barcodeActivityInit() {

        setContentView(R.layout.activity_barcode);

        intent = getIntent();
        byte barcodeData[] = intent.getByteArrayExtra("barcode");
//        barcode = BitmapFactory.decodeByteArray(barcodeData, 0, barcodeData.length);

        uid = intent.getStringExtra("uid");
        currentNum = intent.getIntExtra("currentNum", 0);
//        num = intent.getStringExtra("currentNum");
        mobileNumber = intent.getStringExtra("mobileNumber");
//        currentNum = Integer.getInteger(num);

        barcodeView = (ImageView)findViewById(R.id.id_barcodeView);
//        barcodeView.setImageBitmap(barcode);

        plusButton = (Button)findViewById(R.id.id_plusButtonB);
        plusButton.setOnClickListener(this);
        minusButton = (Button)findViewById(R.id.id_minusButtonB);
        minusButton.setOnClickListener(this);
        currentText = (TextView)findViewById(R.id.id_currentNumTextB);
    }

    private void setToolbar() {

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.toolbar_barcode);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setBarcode()   {
        barcodeGenerator = new BarcodeGenerator();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_plusButtonB :
                currentNum++;
                break;
            case R.id.id_minusButtonB :
                currentNum--;
                break;
        }
        registerBarcode();
    }


    public void registerBarcode()    {

        if( currentNum <= 0 )   {
            barcode = BitmapFactory.decodeResource(getResources(), R.drawable.no_num);
            currentNum = 0;
            return;
        }

        try {
            String barcodeContents = mobileNumber + "_" + uid + "_" + currentNum;
            barcode = barcodeGenerator.encodeAsBitmap(barcodeContents, BarcodeFormat.CODE_128, 600, 400);
        } catch (Exception e) {
            e.printStackTrace();
        }

        barcodeView.setImageBitmap(barcode);
        currentText.setText("현재 인원수 " + currentNum + "명");
        intent.putExtra("currentNum", currentNum);
        this.setResult(RESULT_OK, intent);

    }
}
