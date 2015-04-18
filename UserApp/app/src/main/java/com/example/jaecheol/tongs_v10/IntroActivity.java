package com.example.jaecheol.tongs_v10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
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

        startButton = (Button) findViewById(R.id.id_startButton);
        startButton.setOnClickListener(this);

        autoLoginCheck();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_startButton:
//                Intent intent = new Intent(IntroActivity.this, SignupTermsActivity.class);
                Intent intent = new Intent(IntroActivity.this, SignupActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }

    private void autoLoginCheck() {
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        authToken = mPref.getString("auth_token", null);

        if (authToken != null) {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            intent.putExtra("auth_token", authToken);
            startActivity(intent);
            this.finish();
        }
    }

}
