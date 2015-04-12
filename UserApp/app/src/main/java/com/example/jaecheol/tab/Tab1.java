package com.example.jaecheol.tab;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.jaecheol.tongs_v10.BarcodeGenerator;
import com.example.jaecheol.tongs_v10.R;
import com.google.zxing.BarcodeFormat;

/**
 * Created by JaeCheol on 15. 4. 7..
 */
public class Tab1 extends Fragment {

    Bitmap barcode;
    ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle SavedInstanceState)
    {
        View v = inflater.inflate(R.layout.tab_1, container, false);
        imageView = (ImageView)v.findViewById(R.id.imageView);

        BarcodeGenerator barcodeGenerator = new BarcodeGenerator();
        try {
            barcode = barcodeGenerator.encodeAsBitmap("Hello", BarcodeFormat.CODE_128, 600, 400);
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageView.setImageBitmap(barcode);

        return v;
    }
}
