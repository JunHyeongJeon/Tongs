package com.example.jaecheol.tongs_v10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

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

        Intent intent = this.getIntent();

        sid = intent.getIntExtra("sid", 0);
        number = intent.getIntExtra("number", 0);
        createTime = intent.getIntExtra("createTime", 0);
        extraTime = intent.getIntExtra("extraTime", 0);

        storeNameView = (TextView)findViewById(R.id.id_storeNameText);
        currentNumView = (TextView)findViewById(R.id.id_currentNumText);
        receiptNumView = (TextView)findViewById(R.id.id_numberText);
        expectWaitView = (TextView)findViewById(R.id.id_expectWaitText);
        createTimeView = (TextView)findViewById(R.id.id_createTimeText);

        storeNameView.setText(sid);
        receiptNumView.setText(number);
        expectWaitView.setText(extraTime);
        createTimeView.setText(createTime);

        Toast toast = Toast.makeText(getApplicationContext(),
                "대기표 액티비티 입니다.", Toast.LENGTH_SHORT);
        toast.show();

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
}
