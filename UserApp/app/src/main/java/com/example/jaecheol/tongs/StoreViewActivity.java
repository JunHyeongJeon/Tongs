package com.example.jaecheol.tongs;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import com.example.jaecheol.tongs_v10.R;

/**
 * Created by JaeCheol on 15. 3. 31..
 */
public class StoreViewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        Toast toast = Toast.makeText(getApplicationContext(),
                "바코드 액티비티 입니다.", Toast.LENGTH_SHORT);
        toast.show();
    }


}
