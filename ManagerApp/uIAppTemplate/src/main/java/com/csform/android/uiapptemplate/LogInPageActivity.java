package com.csform.android.uiapptemplate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import com.csform.android.uiapptemplate.util.Preference;
import com.csform.android.uiapptemplate.view.FloatLabeledEditText;

public class LogInPageActivity extends ActionBarActivity implements OnClickListener/*, OnHttpReceive*/ {

	public static final String DARK = "Dark";
	public static final String LIGHT = "Light";
    public static final String ISAUTOLOGIN = "autoLogin";
    public static final String ID = "id";
    public static final String PASSWORD = "password";

    private ScrollView mScrollView;
    private FloatLabeledEditText mEmailView;
    private FloatLabeledEditText mPasswordView;
    public CheckBox mloginKeep;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

        setContentView();
        Preference pref = new Preference(this);

        mloginKeep.setChecked(pref.getValue(ISAUTOLOGIN, false));

        if(mloginKeep.isChecked())
            doAutoLogin();
    }
	
	private void setContentView() {

        setContentView(R.layout.activity_login_page_light);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_centor);


        mScrollView = (ScrollView)findViewById(R.id.login_scroll_view);
        mEmailView = (FloatLabeledEditText)findViewById(R.id.login_email);
        mPasswordView = (FloatLabeledEditText)findViewById(R.id.login_password);
        mEmailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    Log.v("test","test");
                    mEmailView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.smoothScrollBy(0, 1200);
                        }
                    },100);
                }
                else Log.v("test2","test2");
            }
        });
        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                {
                    mPasswordView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScrollView.smoothScrollBy(0, 1200);
                        }
                    },100);
                }
            }
        });

        Button login;
        login = (Button)findViewById(R.id.login_button);
        login.setOnClickListener(this);

        mloginKeep = (CheckBox)findViewById(R.id.login_keep_checkbox);
        mloginKeep.setOnClickListener(this);

        ImageView imageView;
        imageView = (ImageView)findViewById(R.id.login_image_view);
        imageView.setImageResource(R.drawable.logo_y);
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
            doLogin("","");
        }

    }
    private void setAutoLogin(){
        Preference pref = new Preference(this);
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
        Preference pref = new Preference(this);
        String id = pref.getValue(ID, "");
        String password = pref.getValue(PASSWORD, "");
        mEmailView.setText(id);
        mPasswordView.setText(password);
        if (!("".equals(id)) && !("".equals(password)))
            doLogin("","");

    }

    private void doLogin(String Id, String password){
        printToast("doLogin");
    }
    private void DialogProgress(){
        ProgressDialog mDialog = ProgressDialog.show(LogInPageActivity.this, "",
                "잠시만 기다려 주세요 ...", true);

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
    public void moveNextActivity(){
        Intent intent = new Intent(this, ClientManagementActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
    }

    public void printToast(String string){
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }
}
