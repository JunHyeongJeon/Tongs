package com.example.jaecheol.tongs_v10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URLDecoder;
import java.net.URLEncoder;


/**
 * Created by JaeCheol on 15. 3. 27..
 */
public class SignupActivity extends ActionBarActivity
        implements View.OnClickListener//,RadioGroup.OnCheckedChangeListener
{
    DatePicker datePicker;
    RadioGroup radioGroup;

    int birthYear;
    int birthMonth;
    int birthDay;
    int uid;
    int resultNum;
    int state = 0;

    String sex;
    String number;
    String authToken;
    String birthdate;
    String certificationNumber;

    Button startButton;
    Button certificButton;

    EditText editText;
    TextView tooltip;

    Spinner ageSpinner;
    Spinner genderSpinner;
    ArrayAdapter<CharSequence> ageAdapter;
    ArrayAdapter<CharSequence> genderAdapter;

    LinearLayout infoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        infoLayout = (LinearLayout)findViewById(R.id.id_infoLayout);
        infoLayout.setVisibility(LinearLayout.INVISIBLE);


        ageAdapter = ArrayAdapter.createFromResource(this, R.array.selected_age,
                android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ageSpinner = (Spinner)findViewById(R.id.id_ageSpinner);
        ageSpinner.setPrompt("나이대를 선택하세요.");

        ageSpinner.setAdapter(ageAdapter);
        ageSpinner.setSelection(ageAdapter.getPosition("20대"));
        ageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SignupActivity.this,
                        ageAdapter.getItem(position) + "를 선택 했습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        genderAdapter = ArrayAdapter.createFromResource(this, R.array.selected_gender,
                android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        genderSpinner = (Spinner)findViewById(R.id.id_genderSpinner);
        genderSpinner.setPrompt("성별을 선택하세요.");

        genderSpinner.setAdapter(genderAdapter);
        genderSpinner.setSelection(genderAdapter.getPosition("남성"));
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(SignupActivity.this,
                        genderAdapter.getItem(position) + "을 선택 했습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        startButton = (Button)findViewById(R.id.id_startButton);
        startButton.setOnClickListener(this);

        certificButton = (Button)findViewById(R.id.id_certificButton);
        certificButton.setOnClickListener(this);

        editText = (EditText)findViewById(R.id.id_editText);

        tooltip = (TextView)findViewById(R.id.id_tooltip);
    }







    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_certificButton :
                String buttonText = (String)certificButton.getText().toString();

                if( buttonText.equals("인증") == true ) {

                    number = editText.getText().toString();

                    int resultCode = -1;
                    String url = getText(R.string.Server_URL)
                            + "user/auth/sms_request"
                            + "?mdn=" + number;

                    state = 1;
                    new HttpTask().execute(url);

                } else {
                    // 서버로 인증번호 전송

                    certificationNumber = editText.getText().toString();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "입력된 인증번호는 " + certificationNumber, Toast.LENGTH_SHORT);
                    toast.show();

                    state = 2;
                    String url = getText(R.string.Server_URL)
                            + "user/auth/sms_check"
                            + "?mdn=" + number
                            + "&code=" + certificationNumber;

                    new HttpTask().execute(url);

                }
                break;

            case R.id.id_startButton:

                JSONObject json = new JSONObject();
                try {
                    json.put("mobile_number", number);
                    json.put("sex", sex);
                    json.put("birthdate", birthdate);
                    json.put("auth_token", authToken);

                    String jsonUrl = URLEncoder.encode(json.toString(), "UTF-8");
                    Log.d("Hello", "encode " + jsonUrl);

                    String str = URLDecoder.decode(jsonUrl, "UTF-8");
                    Log.d("Hello", "decode " + jsonUrl);


                    state = 3;
                    String url = getText(R.string.Server_URL)
                            + "user/join"
                            + "?token=" + authToken
                            + "&data=" + jsonUrl;

                    new HttpTask().execute(url);

                } catch (Exception e)   { }



//                String jsonObj = NetworkActivity.sendJsonDataToServer(1, 0, json,
//                        "http://tongs.kr/user/join");
//                String[][] result = NetworkActivity.jsonParserList(jsonObj);

//                if (result == null) {
//                    Toast toast2 = Toast.makeText(getApplicationContext(),
//                            "서버에서 응답이 없습니다.", Toast.LENGTH_SHORT);
//                    toast2.show();
//                } else {
//
//                    if (result[0][1] == "success") {
//                        Toast toast2 = Toast.makeText(getApplicationContext(),
//                                "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT);
//                        toast2.show();
//
//                        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                        SharedPreferences.Editor editor = mPref.edit();
//                        editor.putString("auth_token", authToken);
//                        editor.putString("birth_date", birthdate);
//                        editor.putString("number", number);
//                        editor.putString("sex", sex);
//                        editor.commit();
//
//                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
//                        intent.putExtra("authToken", authToken);
//                        startActivity(intent);
//                        this.finish();
//                    } else {
//                        Toast toast2 = Toast.makeText(getApplicationContext(),
//                                "로그인에 실패하셨습니다. (" + result[1][1] + ")", Toast.LENGTH_SHORT);
//                        toast2.show();
//                    }
//                }
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    private static String convertStreamToString(InputStream is)
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;

        try
        {
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public InputStream getInputStreamFromUrl(String url) {
        InputStream content = null;
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            content = response.getEntity().getContent();
        } catch (Exception e) {
            Log.d("[GET REQUEST]", "Network exception", e);
        }
        return content;
    }

    class HttpTask extends AsyncTask<String , Void , String> {
        protected String doInBackground(String... params)
        {
            Log.d("Hello", "Start");
            InputStream is = getInputStreamFromUrl(params[0]);

            Log.d("Hello", "Get");
            String result = convertStreamToString(is);

            return result;
        }

        protected void onPostExecute(String result)
        {
            Log.d("Hello", result);

            try {
                JSONObject json = new JSONObject(result);
                Log.d("Hello", json.get("result_code").toString());
                int resultCode = Integer.parseInt(json.get("result_code").toString());
                if( resultCode == -1 )  {
                    return;
                }

                switch( state ) {
                    case 1 :
                        Toast toast = Toast.makeText(getApplicationContext(),
                                number + "로 전송된 인증번호를 입력하세요", Toast.LENGTH_SHORT);
                        toast.show();

                        tooltip.setText("인증 번호");
                        editText.setText(null);
                        editText.setHint("인증 번호");
                        certificButton.setText("확인");
                        break;

                    case 2 :
                        Toast toast2 = Toast.makeText(getApplicationContext(),
                                "인증에 성공하셨습니다. 추가 정보를 입력하세요.", Toast.LENGTH_LONG);
                        toast2.show();

                        infoLayout.setVisibility(LinearLayout.VISIBLE);
                        certificButton.setEnabled(false);
                        certificButton.setTextColor(getResources().getColor(android.R.color.tertiary_text_light));
                        certificButton.setBackgroundColor(getResources().getColor(android.R.color.background_light));
                        editText.setEnabled(false);

                        authToken = json.get("token").toString();

                        uid = Integer.parseInt(json.get("uid").toString());
                        break;

                    case 3 :
                        Toast toast3 = Toast.makeText(getApplicationContext(),
                                "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT);
                        toast3.show();

                        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = mPref.edit();
                        editor.putString("auth_token", authToken);
                        editor.putString("birth_date", birthdate);
                        editor.putString("number", number);
                        editor.putString("sex", sex);
                        editor.commit();

                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        intent.putExtra("authToken", authToken);
                        startActivity(intent);
                        break;
                }


            } catch (JSONException e) { }
        }
    }

}

