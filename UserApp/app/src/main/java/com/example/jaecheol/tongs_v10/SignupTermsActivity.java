package com.example.jaecheol.tongs_v10;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
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