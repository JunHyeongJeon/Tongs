package com.tongs.store.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tongs.store.R;
import com.tongs.store.util.BackPressCloseHandler;
import com.tongs.store.util.GlobalVar;
import com.tongs.store.util.Preference;
import com.tongs.store.view.FloatLabeledEditText;
import com.tongs.store.util.OnHttpReceive;
import org.json.JSONException;
import org.json.JSONObject;

public class LogInPageActivity extends ActionBarActivity implements OnClickListener, OnHttpReceive, GlobalVar {

	public static final String DARK = "Dark";
	public static final String LIGHT = "Light";



    private ScrollView mScrollView;
    private FloatLabeledEditText mEmailView;
    private FloatLabeledEditText mPasswordView;
    private CheckBox mloginKeep;
    private ProgressDialog mDialog;

    private BackPressCloseHandler backPressCloseHandler;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView();
        Preference pref = Preference.getInstance();
        boolean isChecked = pref.getValue(ISAUTOLOGIN, false);
        mloginKeep.setChecked(isChecked);

        if(mloginKeep.isChecked())
            doAutoLogin();
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login_page_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public void onReceive(int protocol, String data) {
        if(null != mDialog)
            mDialog.dismiss();
        Log.v("OnReceive/Login", data);
        if("Error".equals(data))
            printToast(getString(R.string.toast_login_server_fail));
        else {
            JSONObject json = null;
            try {
                json = new JSONObject(data);
                Log.v("OnReceive/Json", json.toString());

                String resultCode = json.optString("result_code", null);
                String token = json.optString(TOKEN, null);
                Preference pref = Preference.getInstance();
                pref.put(TOKEN, token);


                if("0".equals(resultCode)) {
                    Log.v("LoginAc/onReceive", "로그인성공");
                    moveActivity(ClientManagementActivity.class);
                }
                else
                    printToast(getString(R.string.toast_login_account_fail));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    void requestOnUIThread(final int protocol, final String url)
    {
        final OnHttpReceive onReceive = this;
        this.runOnUiThread(new Runnable() {
            public void run() {
                new com.tongs.store.util.HttpTask(protocol, onReceive).execute(url);
            }
        });
    }

    private void setContentView() {

        setContentView(R.layout.activity_login_page_light);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        TextView toolbarText = (TextView) findViewById(R.id.toolbar_title);
        toolbarText.setText("LOG IN");
       // getSupportActionBar
            //toolbar.setTi


        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.action_bar_login);


        mScrollView = (ScrollView)findViewById(R.id.login_scroll_view);
        mEmailView = (FloatLabeledEditText)findViewById(R.id.login_email);
        mPasswordView = (FloatLabeledEditText)findViewById(R.id.login_password);

        Button login,signUp;
        login = (Button)findViewById(R.id.login_button);
        login.setOnClickListener(this);
        signUp = (Button)findViewById(R.id.sign_up_button);
        signUp.setOnClickListener(this);
        mloginKeep = (CheckBox)findViewById(R.id.login_keep_checkbox);
        mloginKeep.setOnClickListener(this);

        ImageView imageView;
        imageView = (ImageView)findViewById(R.id.login_image_view);
        imageView.setImageResource(R.drawable.logo_y);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
	}


	@Override
	public void onClick(View v)
    {
        if(v.getId() == R.id.login_button)
        {
            checkIdPassword();
        }
        else if(v.getId()== R.id.login_keep_checkbox)
        {
            setAutoLogin();

        }
        else if(v.getId() == R.id.sign_up_button){
            moveActivity(SignUpActivity.class);
        }

	}
    private void checkIdPassword()
    {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String LoginEmail = mEmailView.getTextString();
        String LoginPassword = mPasswordView.getTextString();

        Log.v("info", "id:" + LoginEmail + " pass:" + LoginPassword);

        boolean cancel = false;
        View focusView = null;
        // Check for a valid email address.
        if (TextUtils.isEmpty(LoginEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(LoginEmail)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(LoginPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!isPasswordValid(LoginPassword)) {
           mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            doLogin(LoginEmail,LoginPassword);
        }

    }
    private void setAutoLogin(){
        Preference pref = Preference.getInstance();
        if(mloginKeep.isChecked()) {
            Log.v("isCheck","true");
            pref.put(ID, mEmailView.getTextString());
            pref.put(PASSWORD, mPasswordView.getTextString());
            pref.put(ISAUTOLOGIN, true);
        }
        else {
            Log.v("isCheck","false");
            pref.put(ISAUTOLOGIN, false);
        }
    }
    private void doAutoLogin(){
        Preference pref = Preference.getInstance();
        String id = pref.getValue(ID, "");
        String password = pref.getValue(PASSWORD, "");
        mEmailView.setText(id);
        mPasswordView.setText(password);
        if (!("".equals(id)) && !("".equals(password)))
            doLogin(id,password);

    }

    private void doLogin(String id, String password){
        Log.v("LoginAc/doLogin", "ID: " + id + " PASSWORD: " + password);

        if( "".equals(id)|| "".equals(password)){
            return;
        }
        else
        {
            progressDialog();
            String url = getString(R.string.api_server) + getString(R.string.api_store_login)
                    + "email=" + id + "&password=" + password;
            Log.v("LoginAc/doLogin", url);
            requestOnUIThread(PROTOCOL_STATUS_MANAGER_LOGIN, url);
        }
    }
    private void progressDialog(){
        mDialog = ProgressDialog.show(LogInPageActivity.this, "",
                getString(R.string.dialog_login), true);

        // 창을 끄기
        // dialog.dismiss();
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void printToast(String string){
        Toast.makeText(LogInPageActivity.this, string, Toast.LENGTH_SHORT).show();
    }


    private void moveActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);

    }

}
