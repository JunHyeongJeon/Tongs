package com.tongs.store.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tongs.store.R;
import com.tongs.store.util.GlobalVar;
import com.tongs.store.util.HttpTask;
import com.tongs.store.util.OnHttpReceive;
import com.tongs.store.view.FloatLabeledEditText;

import org.json.JSONObject;


public class SignUpActivity extends ActionBarActivity implements View.OnClickListener, GlobalVar {

    private FloatLabeledEditText mEmailView;
    private FloatLabeledEditText mPasswordView;
    private FloatLabeledEditText mPasswordConfirmView;
    private FloatLabeledEditText mStoreNameView;
    ArrayAdapter<CharSequence>  adspin;
    private int mPosition;

    private String mEmail;
    private String mPassword;
    private String mPasswordConfirm;
    private String mStoreName;

    private boolean mEmailValid = false;
    private boolean mPasswordValid = false;
    private boolean mPasswordConfirmValid = false;
    private boolean mStoreNameValid = false;

    private boolean mUseAgreeDialogRead;
    private boolean mPersonInfoAgreeDialogRead;

    private CheckBox mUseAgreeCheckBox;
    private CheckBox mPersonInfoUseAgreeCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setPrompt("시/도 를 선택하세요.");
        adspin = ArrayAdapter.createFromResource(this, R.array.selected, android.R.layout.simple_spinner_item);

        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mEmailView = (FloatLabeledEditText)findViewById(R.id.sign_up_email);
        mPasswordView = (FloatLabeledEditText)findViewById(R.id.sign_up_password);
        mPasswordConfirmView = (FloatLabeledEditText)findViewById(R.id.sign_up_password_check);
        mStoreNameView = (FloatLabeledEditText)findViewById(R.id.sign_up_store_name);

        Button signUpButton = (Button)findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener((View.OnClickListener) this);

        mUseAgreeCheckBox = (CheckBox)findViewById(R.id.use_agree_check_box);
        mPersonInfoUseAgreeCheckBox = (CheckBox)findViewById(R.id.person_info_agree_check_box);
        mUseAgreeCheckBox.setOnClickListener(this);
        mPersonInfoUseAgreeCheckBox.setOnClickListener(this);

        Button useAgreePost = (Button)findViewById(R.id.pop_use_agree_dialog_button);
        useAgreePost.setOnClickListener(this);
        Button personInfoAgreePost =(Button)findViewById(R.id.pop_person_info_agree_dialog_button);
        personInfoAgreePost.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sign_up_button) {
            requestSignUP();
        }
        else if (v.getId() == R.id.use_agree_check_box){
            if(!mUseAgreeDialogRead) {
                printToast(getString(R.string.dialog_look_agreement));
                mUseAgreeCheckBox.setChecked(false);
            }

        } else if (v.getId() == R.id.person_info_agree_check_box){
            if(!mPersonInfoAgreeDialogRead) {
                printToast(getString(R.string.dialog_look_agreement));
                mPersonInfoUseAgreeCheckBox.setChecked(false);
            }
        } else if(v.getId() == R.id.pop_use_agree_dialog_button){
            popDialog("ㅠㅠ");
            mUseAgreeDialogRead = true;
        } else if(v.getId() == R.id.pop_person_info_agree_dialog_button){
            popDialog("ㅠㅠㅠ");
            mPersonInfoAgreeDialogRead = true;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up, menu);
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
    public void checkStoreInfo() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordConfirmView.setError(null);
        mStoreNameView.setError(null);
        // Store values at the time of the login attempt.
        mEmail = mEmailView.getText().toString();
        mPassword = mPasswordView.getText().toString();
        mPasswordConfirm = mPasswordConfirmView.getText().toString();
        mStoreName = mStoreNameView.getTextString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(mPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!isPasswordValid(mPassword)){
            mPasswordView.setError(getString(R.string.error_invalid_password));

        } else mPasswordValid = true;



        // Check for a valid email address.
        if (TextUtils.isEmpty(mEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(mEmail)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else mEmailValid = true;

        if (TextUtils.isEmpty(mPasswordConfirm)) {
            mPasswordConfirmView.setError(getString(R.string.error_field_required));
            focusView = mPasswordConfirmView;
            cancel = true;
        } else if (!isPasswordConfirmValid(mPassword, mPasswordConfirm)) {
            mPasswordConfirmView.setError(getString(R.string.error_invalid_password_check));

        } else {
            //mPasswordConfirmView.setError(getString(R.string.error_invalid_password_check_test));
            mPasswordConfirmValid = true;
        }

        if(TextUtils.isEmpty(mStoreName))
        {
            mStoreNameView.setError(getString(R.string.error_field_required));
            focusView = mStoreNameView;
            cancel = true;
        } else {
            mStoreNameValid = true;
        }

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    private boolean isPasswordConfirmValid(String password,String passwordConfirm) {
        //TODO: Replace this with your own logic

        return password.equals(passwordConfirm);
    }
    private boolean isStoreNameValid(String Store){
        return true;
    }
    private void requestSignUP(){
        checkStoreInfo();
        registerStore();
    }
    private void registerStore(){
        if( mEmailValid && mPasswordValid && mPasswordConfirmValid && mStoreNameValid){
            Log.v("registerStore", "info is valid");
            String url;
            url = getString(R.string.api_server) + getString(R.string.api_store_register)
                    + "email=" + mEmailView.getTextString() + "&password=" + mPasswordView.getTextString()
                    + "&name=" + mStoreName + "&location=" + adspin.getItem(mPosition);
            requestOnUIThread(PROTOCOL_STATUS_SIGN_UP, url, new OnHttpReceive() {
                @Override
                public void onReceive(int protocol, String data) {
                    try {
                        JSONObject json = new JSONObject(data);
                        String result_code = json.optString("result_code", null);
                        boolean isSuccess = "0".equals(result_code) ? true : false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


         }


    }
    void requestOnUIThread(final int protocol, final String url, final OnHttpReceive onReceive)
    {
        Log.v("URL", url);
        this.runOnUiThread(new Runnable() {
            public void run() {
                new HttpTask(protocol, onReceive).execute(url);
            }
        });
    }
    public void printToast(String string){
        Toast.makeText(SignUpActivity.this, string, Toast.LENGTH_SHORT).show();
    }

    private void popDialog(String ment){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage(ment).setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button

                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.setTitle("Title");
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();
    }
}
