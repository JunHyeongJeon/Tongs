package com.tongs.hostapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class SignupActivity extends Activity {

    //public static SignupActivity sSignupActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //sSignupActivity = SignupActivity.this;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //text view
        TextView agreementUsetextView = (TextView)findViewById(R.id.agreement_use);
        agreementUsetextView.setText(readTxt(R.raw.collectpersonalinformation));
        agreementUsetextView.setMovementMethod(new ScrollingMovementMethod());
        TextView agreementPersonInfoUsetextView = (TextView)findViewById(R.id.agreement_personal_information_use);
        agreementPersonInfoUsetextView.setText(readTxt(R.raw.useagreement));
        agreementPersonInfoUsetextView.setMovementMethod(new ScrollingMovementMethod());

        //button
        Button mBackButton = (Button)findViewById(R.id.sign_up_back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityBack();

            }
        });
        final Button mNextButton = (Button)findViewById(R.id.sign_up_next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityNext();

            }
        });
        mNextButton.setEnabled(false);
        //checkbox
        final CheckBox agreementUsecheckbox = (CheckBox)findViewById(R.id.agreement_use_check);
        final CheckBox agreementPersonInfoUsecheckbox =
                (CheckBox)findViewById(R.id.agreement_personal_information_use_check);
        agreementUsecheckbox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(agreementUsecheckbox.isChecked()&&agreementPersonInfoUsecheckbox.isChecked())
                    mNextButton.setEnabled(true);
                else mNextButton.setEnabled(false);
            }

        });
        agreementPersonInfoUsecheckbox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(agreementUsecheckbox.isChecked()&&agreementPersonInfoUsecheckbox.isChecked())
                    mNextButton.setEnabled(true);
                else mNextButton.setEnabled(false);
            }

        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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

    private String readTxt(int Id) {
        String data = null;
        InputStream inputStream = getResources().openRawResource(Id);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }

            data = new String(byteArrayOutputStream.toByteArray(),"UTF-8");
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    //dkdkd

    /*
    Inputsting is = FileInputStrim(파일)
    int ret = is.read(data);
    string str = new string(data);
     */
    public void ActivityBack(){
       // Intent intent = new Intent(this, LoginActivity.class);
       // startActivity(intent);
       // overridePendingTransition(R.anim.fade, R.anim.cycle_7);
        this.finish();
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);

    }
    public void ActivityNext(){
        Intent intent = new Intent(this, SignupActivity_page1.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
    }
}
