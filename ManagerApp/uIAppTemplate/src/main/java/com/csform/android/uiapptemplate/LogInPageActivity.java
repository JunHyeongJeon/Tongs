package com.csform.android.uiapptemplate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.csform.android.uiapptemplate.util.Preference;
import com.csform.android.uiapptemplate.util.OnHttpReceive;
import com.csform.android.uiapptemplate.view.FloatLabeledEditText;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LogInPageActivity extends ActionBarActivity implements OnClickListener/*, OnHttpReceive*/ {

	public static final String LOGIN_PAGE_AND_LOADERS_CATEGORY = "com.csform.android.uiapptemplate.LogInPageAndLoadersActivity";
	public static final String DARK = "Dark";
	public static final String LIGHT = "Light";

    private String mLoginEmail;
    private String mLoginPassword;
    private FloatLabeledEditText mEmailView;
    private FloatLabeledEditText mPasswordView;

    private boolean mCheck = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView();
    }
	
	private void setContentView() {

        setContentView(R.layout.activity_login_page_light);

        getSupportActionBar().setCustomView(R.layout.action_bar_centor);

        mEmailView = (FloatLabeledEditText)findViewById(R.id.login_email);
        mPasswordView = (FloatLabeledEditText)findViewById(R.id.login_password);

        Button login;
        login = (Button)findViewById(R.id.login_button);
        login.setOnClickListener(this);

        CheckBox loginKeep;
        loginKeep = (CheckBox)findViewById(R.id.login_keep_checkbox);
        loginKeep.setOnClickListener(this);

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
            doAutoLogin();
        }

	}
    private void checkIdPassword()
    {

        boolean mEmailValid = false;
        boolean mPasswordValid = false;


        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        mLoginEmail = mEmailView.getTextString();
        mLoginPassword = mPasswordView.getTextString();

        Log.v("info", "id:" + mLoginEmail + " pass:" + mLoginPassword);

        View focusView = null;
        // Check for a valid email address.
        if (TextUtils.isEmpty(mLoginEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
        } else if (!isEmailValid(mLoginEmail)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
        } else mEmailValid = true;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(mLoginPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;

        } else mPasswordValid = true;

        focusView.requestFocus();
        if( mEmailValid && mPasswordValid ) {
          //  doLogin();
        } else {
            printToast("로그인에 실패하였습니다.");
        }

    }
    void doAutoLogin(){

        mCheck = !mCheck;

        if("".equals(mLoginEmail) || "".equals(mLoginPassword))
        {
            printToast("저장된 비밀번호가 없습니다.");
        }
        else
        {
            Log.v("LogIn", String.format("email:%s, pw:%s", mLoginEmail, mLoginPassword));
            mEmailView.setText(mLoginEmail);
            mPasswordView.setText(mLoginPassword);

        }
        if(!mCheck) {
            mEmailView.setText("");
            mPasswordView.setText("");
        }

    }
    private void doLogin(String Id, String password){

    }
    private void DialogProgress(){
        ProgressDialog mDialog = ProgressDialog.show(LogInPageActivity.this, "",
                "잠시만 기다려 주세요 ...", true);

        // 창을 내린다.
        // dialog.dismiss();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
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

/*
    public InputStream getInputStreamFromUrl(String url) {
        InputStream content = null;
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            content = response.getEntity().getContent();
        } catch (Exception e) {
            Log.e("[GET REQUEST]", "Network exception", e);
        }
        return content;
    }

    private String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try{
            while ((line = reader.readLine()) != null){
                sb.append(line + "\n");
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                is.close();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
        return sb.toString();

    }
    class HttpTask extends AsyncTask<String , Void , String> {
        protected String doInBackground(String... params)
        {
            InputStream is = getInputStreamFromUrl(params[0]);

            String result = convertStreamToString(is);//

            return result;
        }

        protected void onPostExecute(String result)
        {

        }
    }
        @Override
    public void onReceive(byte[] data) {
        if (data == null)
        {
            // 예외처리
        }
        String result = new String(data);
        Log.d("Hello", result);

        try {
            //JSONObject jsonObject = new JSONObject(result);
            JSONObject json = new JSONObject(result);


            String result_code = json.optString("result_code", null);


            if("0".equals(result_code)){
//                    moveNextActivity();
            } else {
                printToast("로그인에 실패하였습니다.");
            }


            Log.v("jsonObjectCheck", json.toString());


        } catch(JSONException e) {  }

        //result를 처리한다.
    }*/

}
