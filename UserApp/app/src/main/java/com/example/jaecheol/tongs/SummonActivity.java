package com.example.jaecheol.tongs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by JaeCheol on 15. 5. 15..
 */
public class SummonActivity extends ActionBarActivity
                            implements View.OnClickListener
{
    Intent intent;

    Vibrator vibe;
    long[] vibePattern = {1000, 1500};

    Button checkButton;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summon);

        summonInit();

        vibrate(true);
    }

    private void summonInit()   {
        intent = new Intent();

        vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        checkButton = (Button)findViewById(R.id.id_checkButton);
        checkButton.setOnClickListener(this);

        cancelButton = (Button)findViewById(R.id.id_cancelButton);
        cancelButton.setOnClickListener(this);
    }

    public void vibrate(boolean flag)   {
        if( flag ) {
            vibe.vibrate(vibePattern, 0);
        }
        else    {
            vibe.cancel();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_checkButton:
                this.setResult(RESULT_OK);
                break;

            case R.id.id_cancelButton:
                this.setResult(RESULT_CANCELED);
                break;
        }

        vibrate(false);

        this.finish();
    }
}
