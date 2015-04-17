package com.csform.android.uiapptemplate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpActivitySecond extends ActionBarActivity {




    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;
    private boolean mEmailValid = false;
    private boolean mPasswordValid = false;
    private boolean mPasswordConfirmValid = false;

    private String email;
    private String password;
    private String passwordConfirm;
    private String emailToken;
    private String emailResultCode = "-1";
    private boolean succeseFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity_second);


        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.sign_up_email);
        mPasswordView = (EditText) findViewById(R.id.sign_up_password);
        mPasswordConfirmView = (EditText) findViewById(R.id.sign_up_password_confirm);
        Button mBackButton = (Button)findViewById(R.id.sign_up_back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityBack();

            }
        });
        Button mNextButton = (Button)findViewById(R.id.sign_up_next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
                if(mEmailValid && mPasswordValid && mPasswordConfirmValid){

                    Send();
                    //ActivityNext();
                }


            }
        });




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_activity_second, menu);
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
    public void ActivityBack(){
        this.finish();
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);

    }
    public void ActivityNext(){
        Intent intent = new Intent(this, SignUpActivityThird.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
    }
    public void Send(){
        Log.v("Send", "SendFunction");
        if(email != null && isEmailValid(email)) {

            if(!succeseFlag) {
                String url = getText(R.string.sign_up_email) + email;
                new HttpTask().execute(url);

                Log.v("ResultCodeCheck", emailResultCode);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "이메일이 전송중입니다. 한번 더 눌러주세요.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
            else if(succeseFlag){
                Log.v("ResultCodeCheck", emailResultCode);
                SaveManagerData();
                ActivityNext();
                succeseFlag = false;
            }

        }



    }
    public void attemptSignup() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mPasswordConfirmView.setError(null);
        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        passwordConfirm = mPasswordConfirmView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));

        } else mPasswordValid = true;



        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        } else mEmailValid = true;

        if (TextUtils.isEmpty(passwordConfirm)) {
            mPasswordConfirmView.setError(getString(R.string.error_field_required));
            focusView = mPasswordConfirmView;
            cancel = true;
        } else if (!isPasswordConfirmValid(password, passwordConfirm)) {
            mPasswordConfirmView.setError(getString(R.string.error_invalid_password_check));

        } else {
            //mPasswordConfirmView.setError(getString(R.string.error_invalid_password_check_test));
            mPasswordConfirmValid = true;
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


    public boolean isValidCellPhoneNumber(String cellphoneNumber) {
        boolean returnValue = false;
        Log.i("cell", cellphoneNumber);
        String regex = "^\\s*(010|011|012|013|014|015|016|017|018|019)(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(cellphoneNumber);
        if (m.matches()) {
            returnValue = true;
        }
        return returnValue;
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
    private void SaveManagerData(){
        Log.v("SaveManagerData", "I'm SaveManagerDataFunction");
        Log.v("email", email);
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        Log.v("emailToken" ,emailToken);
        editor.putString("emailToken",emailToken);

        editor.commit();



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

            try {
                //JSONObject jsonObject = new JSONObject(result);
                JSONObject json = new JSONObject(result);


                String result_code = json.get("result_code").toString();
                //Log.v("result_code", result_code);
                emailResultCode = result_code;

                String token = json.get("token").toString();
                //Log.v("token", token);
                emailToken = token;
                SaveManagerData();

                if("0".equals(result_code))
                    succeseFlag = true;
                //  result_code가 fail때에 대한 처리 시작


                //  result_code가 fail때에 대한 처리 끝


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


}
