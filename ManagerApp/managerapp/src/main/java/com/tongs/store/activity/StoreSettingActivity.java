package com.tongs.store.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.view.View.OnClickListener;


import com.tongs.store.R;
import com.tongs.store.util.GlobalVar;
import com.tongs.store.util.Preference;

public class StoreSettingActivity extends ActionBarActivity implements OnClickListener, GlobalVar {

    Switch mAutoLoginSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_setting);
        Preference pref = Preference.getInstance();

        mAutoLoginSwitch = (Switch) findViewById(R.id.auto_login_switch);
        mAutoLoginSwitch.setOnClickListener(this);
        mAutoLoginSwitch.setChecked(pref.getValue(ISAUTOLOGIN, false));
    }

    @Override
    public void onClick(View v)
    {
        if( v.getId() == R.id.auto_login_switch){
            Preference pref = Preference.getInstance();
            if ( mAutoLoginSwitch.isChecked() )
                pref.put(ISAUTOLOGIN, true);
            else
                pref.put(ISAUTOLOGIN, false);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_store_setting, menu);
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
    @Override
    public void onBackPressed(){
        this.finish();
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);
    }
}
