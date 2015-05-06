package com.example.jaecheol.tongs;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * Created by JaeCheol on 15. 4. 4..
 */
public class WaitingTicketActivity extends ActionBarActivity {

    int sid;
    int number;
    int createTime;
    int extraTime;

    TextView storeNameView;
    TextView currentNumView;
    TextView receiptNumView;
    TextView expectWaitView;
    TextView createTimeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waitingticket);

//        Intent intent = this.getIntent();
//
//        sid = intent.getIntExtra("sid", 0);
//        number = intent.getIntExtra("number", 0);
//        createTime = intent.getIntExtra("createTime", 0);
//        extraTime = intent.getIntExtra("extraTime", 0);
//
//        storeNameView  = (TextView)findViewById(R.id.id_storeNameText);
//        currentNumView = (TextView)findViewById(R.id.id_currentNumText);
//        receiptNumView = (TextView)findViewById(R.id.id_numberText);
//        expectWaitView = (TextView)findViewById(R.id.id_expectWaitText);
//        createTimeView = (TextView)findViewById(R.id.id_createTimeText);
//
//        storeNameView.setText(sid);
//        receiptNumView.setText(number);
//        expectWaitView.setText(extraTime);
//        createTimeView.setText(createTime);
//
//        Toast toast = Toast.makeText(getApplicationContext(),
//                "대기표 액티비티 입니다.", Toast.LENGTH_SHORT);
//        toast.show();

    }

}
