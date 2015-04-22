package com.csform.android.uiapptemplate;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class LogInPageActivity extends Activity implements OnClickListener {

	public static final String LOGIN_PAGE_AND_LOADERS_CATEGORY = "com.csform.android.uiapptemplate.LogInPageAndLoadersActivity";
	public static final String DARK = "Dark";
	public static final String LIGHT = "Light";
    private ProgressDialog dialog;

    private String signUpEmail;
    private String signUpPassword;
    private String emailToken;
    private String loginEmail;
    private String loginPassword;
    private FloatLabeledEditText mEmailView;
    private FloatLabeledEditText mPasswordView;

    private boolean mEmailValid = false;
    private boolean mPasswordValid = false;
    private boolean mEmailPasswordVaild = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); //Removing ActionBar
		String category = LIGHT;
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(LOGIN_PAGE_AND_LOADERS_CATEGORY)) {
			category = extras.getString(LOGIN_PAGE_AND_LOADERS_CATEGORY, DARK);
		}
		setContentView(category);



        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mEmailView = (FloatLabeledEditText)findViewById(R.id.login_email);
        mPasswordView = (FloatLabeledEditText)findViewById(R.id.login_password);
        signUpEmail = mPref.getString("email", null);
        signUpPassword = mPref.getString("password", null);
        emailToken = mPref.getString("emailToken", null);
	}
	
	private void setContentView(String category) {
		if (category.equals(DARK)) {
			setContentView(R.layout.activity_login_page_dark);
		} else if (category.equals(LIGHT)) {
			setContentView(R.layout.activity_login_page_light);
		}
		TextView login, register, skip;
		login = (TextView) findViewById(R.id.login);
		register = (TextView) findViewById(R.id.register);
		skip = (TextView) findViewById(R.id.skip);
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		skip.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v instanceof TextView) {
            if(v.getId() == R.id.login){


                Login();


            }else if(v.getId()== R.id.register){
//                Toast.makeText(this, "good", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SignUpActivityFirst.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
            }else if(v.getId()== R.id.skip){
                Log.v("email", signUpEmail);
                Log.v("password", signUpPassword);
                Log.v("emailToken", emailToken);
            }

        }
	}
    private void Login(){
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        loginEmail = mEmailView.getText().toString();
        loginPassword = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(loginEmail)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(loginEmail)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else if (!loginEmail.equals(signUpEmail)) {
            mEmailView.setError(getString(R.string.error_not_same_email));
            focusView = mEmailView;
            cancel = true;

        } else mEmailValid = true;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(loginPassword)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!loginPassword.equals(signUpPassword)) {
            mPasswordView.setError(getString(R.string.error_not_same_password));
        }
        else mPasswordValid = true;


        if(loginPassword.equals(signUpPassword) && loginEmail.equals(signUpEmail))
            mEmailPasswordVaild = true;


        if( mEmailValid && mPasswordValid && mEmailPasswordVaild) {
            //DialogProgress();
            //String url = getString(R.string.server_api_email_request) + "email=" + loginEmail;
            //new HttpTask().execute(url);
            ActivityNext();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "로그인에 실패하였습니다.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);

        }

    }
    private void DialogProgress(){
        dialog = ProgressDialog.show(LogInPageActivity.this, "",
                "잠시만 기다려 주세요 ...", true);
        // 창을 내린다.
        // dialog.dismiss();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

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
            Log.d("Hello", result);
            dialog.dismiss();

            try {
                //JSONObject jsonObject = new JSONObject(result);
                JSONObject json = new JSONObject(result);


                String result_code = json.get("result_code").toString();


                if("0".equals(result_code)){
//                    ActivityNext();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "로그인에 실패하였습니다.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);

                }


                Log.v("jsonObjectCheck", json.toString());

                /*
                JSONArray nameArray =  jsonObject.names();
                JSONArray valArray = jsonObject.toJSONArray(nameArray);


                Log.d("Hello", nameArray.getString(0) + "  " + valArray.getString(0));
                */
            } catch(JSONException e) {  }

            //result를 처리한다.
        }
    }

    public void ActivityNext(){
        Intent intent = new Intent(this, ClientManagementActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
    }



}
