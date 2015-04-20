package com.example.jaecheol.tongs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by JaeCheol on 15. 3. 29..
 */
public class SignupTermsActivity extends ActionBarActivity
                                 implements View.OnClickListener
{

    Button agreeButton1;
    Button agreeButton2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupterms);

        agreeButton1 = (Button)findViewById(R.id.id_agreeButton1);
        agreeButton1.setOnClickListener(this);

        agreeButton2 = (Button)findViewById(R.id.id_agreeButton2);
        agreeButton2.setOnClickListener(this);
        agreeButton2.setEnabled(false);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_agreeButton1 :
                agreeButton2.setEnabled(true);
                agreeButton2.setBackgroundColor(getResources().getColor(R.color.tabsScrollColor));
                agreeButton2.setTextColor(Color.parseColor("#FFFFFF"));
                break;

            case R.id.id_agreeButton2 :
                Intent intent = new Intent(SignupTermsActivity.this, SignupActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }

}