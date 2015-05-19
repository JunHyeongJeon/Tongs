package com.csform.android.managerapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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

public class SignUpActivity extends ActionBarActivity implements OnClickListener {

    public static final String LOGIN_PAGE_AND_LOADERS_CATEGORY = "com.csform.android.uiapptemplate.LogInPageAndLoadersActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_NO_TITLE); //Removing ActionBar
        setContentView();
    }

    private void setContentView() {
        setContentView(R.layout.activity_sign_up);

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
            TextView tv = (TextView) v;
            Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();
            new HttpTask().execute("http://somabell01.cloudapp.net:8080/user/auth/sms_request?mdn=01028432492");
        }
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
