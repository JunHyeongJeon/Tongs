package com.csform.android.uiapptemplate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity_second);


        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.sign_up_email);
        mPasswordView = (EditText) findViewById(R.id.sign_up_password);

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
                //ActivityNext();

            }
        });
        Button mSendButton = (Button)findViewById(R.id.send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Send();

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
        EditText phoneNumberEditText = (EditText)findViewById(R.id.phone_number);
        String phoneNumber;
        phoneNumber = phoneNumberEditText.getText().toString();

        if(phoneNumber != null && isValidCellPhoneNumber(phoneNumber)){


            String url = getText(R.string.sign_up_sms_check_url) + phoneNumber;
            new HttpTask().execute(url);

            Toast toast = Toast.makeText(getApplicationContext(),
                    "인증번호를 전송하였습니다", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "올바른 전화번호를 입력해주세요", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
    }
    public void attemptSignup() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if(!isPasswordValid(password)){
            mPasswordView.setError(getString(R.string.error_invalid_password));

        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
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

    private static String convertStreamToString(InputStream is)
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
                JSONObject jsonObject = new JSONObject(result);

                JSONArray nameArray =  jsonObject.names();
                JSONArray valArray = jsonObject.toJSONArray(nameArray);


                Log.d("Hello", nameArray.getString(0) + "  " + valArray.getString(0));

            } catch(JSONException e) {  }

            //result를 처리한다.
        }
    }


}
