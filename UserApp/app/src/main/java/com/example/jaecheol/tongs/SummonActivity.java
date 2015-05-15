package com.example.jaecheol.tongs;

import android.content.Context;
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

    Vibrator vibe;
    long[] vibePattern = {1000, 1500};

    Button checkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summon);

        summonInit();

        callSummon();
    }

    private void summonInit()   {

        vibe = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        checkButton = (Button)findViewById(R.id.id_checkButton);
        checkButton.setOnClickListener(this);
    }

    public void callSummon()   {
        vibe.vibrate(vibePattern, 0);
    }

    public void checkSummon()   {
        vibe.cancel();
    }

    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_checkButton:
                checkSummon();
                break;
        }
    }
}
