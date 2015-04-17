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


public class SignUpActivityThird extends ActionBarActivity {


    private String emailToken = null;
    private String smsResultCode ="-1";
    private boolean succeceFlag = false;
    private EditText mPhoneNumber;
    private Button mSendButton;
    private String phoneNumber;
    private String code;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_activity_third);


        // Set up the login form.
        mPhoneNumber = (EditText) findViewById(R.id.phone_number);


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
                //attemptSignup();
                //ActivityNext();

            }
        });

        mSendButton = (Button)findViewById(R.id.send_button);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = (String)mSendButton.getTag();
                if("check".equals(tag)) {
                    Check();
                }else {
                    Send();
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
        EditText phoneNumberEditText = (EditText)findViewById(R.id.phone_number);

        phoneNumber = phoneNumberEditText.getText().toString();

        if(phoneNumber != null && isValidCellPhoneNumber(phoneNumber)){

            phoneNumber = extract_numeral(phoneNumber);
            Log.v("PhoneNumber",phoneNumber);
            String url = getText(R.string.sign_up_sms_check_url) + phoneNumber;
            new HttpTask().execute(url);
            if(!succeceFlag) {

                Toast toast = Toast.makeText(getApplicationContext(),
                        "인증번호를 전송하였습니다. 인증번호를 입력하세요.", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();


                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                ChangeToEnterNumber();
            }
                else if(succeceFlag){
                //ChangeToEnterNumber();

            }

        }
        else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "올바른 전화번호를 입력해주세요", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }
    }
    public String extract_numeral(String str){

        String numeral = "";
        if( str == null )
        {
            numeral = null;
        }
        else {
            String patternStr = "\\d"; //숫자를 패턴으로 지정
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(str);

            while(matcher.find()) {
                numeral += matcher.group(0); //지정된 패턴과 매칭되면 numeral 변수에 넣는다. 여기서는 숫자!!
            }
        }

        return numeral;
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
                JSONObject json = new JSONObject(result);

                String result_code = json.get("result_code").toString();
                smsResultCode = result_code;
                if("0".equals(result_code))
                    succeceFlag = true;

                Log.v("jsonObjectCheck", json.toString());

            } catch(JSONException e) {  }

            //result를 처리한다.
        }
    }
    private void ChangeToEnterNumber(){

        mPhoneNumber.setText(null);
        mPhoneNumber.setHint(R.string.enter_certification_number);
        mSendButton.setText(R.string.button_certification);
        mSendButton.setTag("check");
    }
    private void Check(){
        Toast toast = Toast.makeText(getApplicationContext(),
                "Check", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        emailToken = mPref.getString("emailToken", null);
        password = mPref.getString("password", null);
        code = mPhoneNumber.getText().toString();
        Log.v("code",code);
        Log.v("emailToken",emailToken);

        if(!"".equals(mPhoneNumber.getText())){
            String url = getText(R.string.sign_up_sms_check) + emailToken +
                    "&code="+code + "&password=" + password +"&mdn=" + phoneNumber;
            Log.v("URL_LOGIN",url);
            new HttpTask().execute(url);
        }

    }


}
