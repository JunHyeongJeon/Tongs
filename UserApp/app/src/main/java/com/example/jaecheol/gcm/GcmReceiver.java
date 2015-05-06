package com.example.jaecheol.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by JaeCheol on 15. 4. 19..
 */
public class GcmReceiver extends BroadcastReceiver{

    public GcmReceiver()
    {
        Log.d("Hello", "=======================receive");
    }
    @Override
    public void onReceive(Context context, Intent intent)   {
        Toast.makeText(context,
                        "android.intent.action.GCMRECV",
                         Toast.LENGTH_LONG).show();
        Log.d("Hello", "receive");
    }
}
