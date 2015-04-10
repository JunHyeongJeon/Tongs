package com.example.jaecheol.tongs_v10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by JaeCheol on 15. 4. 5..
 */
public class IntroActivity extends ActionBarActivity
                           implements View.OnClickListener {

    Button startButton;

    String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        startButton = (Button)findViewById(R.id.id_startButton);
        startButton.setOnClickListener(this);

        autoLoginCheck();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_startButton:
                Intent intent = new Intent(IntroActivity.this, SignupTermsActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }

    private void autoLoginCheck()
    {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        authToken = mPref.getString("auth_token", null);

        if( authToken != null ) {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            intent.putExtra("auth_token", authToken);
            startActivity(intent);
            this.finish();
        }
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
