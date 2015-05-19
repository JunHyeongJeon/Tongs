package com.example.jaecheol.tongs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

/**
 * Created by JaeCheol on 15. 5. 10..
 */
public class BarcodeActivity extends ActionBarActivity  {

    Intent intent;
    Bitmap barcode;

    ImageView barcodeView;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        barcodeActivityInit();
        setToolbar();
    }

    private void barcodeActivityInit() {

        setContentView(R.layout.activity_barcode);

        intent = getIntent();
        byte barcodeData[] = intent.getByteArrayExtra("barcode");
        barcode = BitmapFactory.decodeByteArray(barcodeData, 0, barcodeData.length);

        barcodeView = (ImageView)findViewById(R.id.id_barcodeView);
        barcodeView.setImageBitmap(barcode);
    }

    private void setToolbar() {

        // Creating The Toolbar and setting it as the Toolbar for the activity
        toolbar = (Toolbar) findViewById(R.id.toolbar_barcode);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
