package com.example.jaecheol.tongs_v10;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        startButton = (Button)findViewById(R.id.id_startButton);
        startButton.setOnClickListener(this);

        certificButton = (Button)findViewById(R.id.id_certificButton);
        certificButton.setOnClickListener(this);
        certificButton.setTag("certific");

        editText = (EditText)findViewById(R.id.id_editText);

        tooltip = (TextView)findViewById(R.id.id_tooltip);
    }


    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.id_certificButton :
                String tag = (String)certificButton.getTag();
                if( "certific".equals(tag) ) {

                    number = editText.getText().toString();

                    String url = getText(R.string.Server_URL)
                            + "user/auth/sms_request"
                            + "?mdn=" + number;

                    IHttpRecvCallback cb = new IHttpRecvCallback(){
                        public void onRecv(String result) {
                            try {
                                JSONObject json = new JSONObject(result);
                                String result_code = json.get("result_code").toString();
                                Log.d("Hello", result_code);
                                if( "-1".equals(result_code) )
                                    return;

                                Toast toast = Toast.makeText(getApplicationContext(),
                                        number + "로 전송된 인증번호를 입력하세요", Toast.LENGTH_SHORT);
                                toast.show();

                                tooltip.setText("인증 번호");

                                editText.setText(null);
                                editText.setHint("인증 번호");

                                certificButton.setText("확인");
                                certificButton.setTag("check");
                            }
                            catch(Exception e){}
                        }
                    };
                    new HttpTask(cb).execute(url);

                } else if( "check".equals(tag) ) {
                    // 서버로 인증번호 전송

                    certificationNumber = editText.getText().toString();

                    String url = getText(R.string.Server_URL)
                            + "user/auth/sms_check"
                            + "?mdn=" + number
                            + "&code=" + certificationNumber;
                    IHttpRecvCallback cb = new IHttpRecvCallback() {
                        public void onRecv(String result) {
                            try {
                                JSONObject json = new JSONObject(result);
                                String result_code = json.get("result_code").toString();
                                Log.d("Hello", result_code);
                                if( "-1".equals(result_code) )
                                    return;

                                Toast toast2 = Toast.makeText(getApplicationContext(),
                                        "인증에 성공하셨습니다. 추가 정보를 입력하세요.", Toast.LENGTH_LONG);
                                toast2.show();

                                infoLayout.setVisibility(LinearLayout.VISIBLE);
                                certificButton.setEnabled(false);
                                certificButton.setTextColor(getResources().getColor(android.R.color.tertiary_text_light));
                                certificButton.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                certificButton.setText("인증 성공");
                                editText.setEnabled(false);

                                authToken = json.get("token").toString();

                                uid = Integer.parseInt(json.get("uid").toString());

                            } catch (Exception e) { }
                        }
                    };

                    new HttpTask(cb).execute(url);

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

                    String str = URLDecoder.decode(jsonUrl, "UTF-8");
                    Log.d("Hello", "decode " + jsonUrl);

                    String url = getText(R.string.Server_URL)
                            + "user/join"
                            + "?token=" + authToken
                            + "&data=" + jsonUrl;

                    IHttpRecvCallback cb = new IHttpRecvCallback() {
                        public void onRecv(String result) {
                            try {
                                JSONObject json = new JSONObject(result);
                                String result_code = json.get("result_code").toString();
                                Log.d("Hello", result_code);
                                if( "-1".equals(result_code) )
                                    return;

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

                            } catch (Exception e) { }
                        }
                    };

                    new HttpTask(cb).execute(url);

                } catch (Exception e)   { }

                break;
        }
    }


    private static String convertStreamToString(InputStream is)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024*64);
        byte data[] = new byte[10240];
        while(true) {
            try {
                int len = is.read(data);
                if (len == -1)
                    break;
                baos.write(data, 0, len);
            } catch (Exception e) { }
        }
        String str = new String(baos.toByteArray());
        return str;
    }



    public InputStream getInputStreamFromUrl(String url) {
        InputStream content = null;
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            if(response.getStatusLine().getStatusCode() != 200)
            {
                // 네트워크 오류입니다.
                Log.d("Hello", "Network Error");
            }
            content = response.getEntity().getContent();
        } catch (Exception e) {
            Log.d("[GET REQUEST]", "Network exception", e);
        }
        return content;

    }


    interface IHttpRecvCallback
    {
        public void onRecv(String result);
    }


    class HttpTask extends AsyncTask<String , Void , String> {

        IHttpRecvCallback m_cb;
        HttpTask(IHttpRecvCallback cb)
        {
            m_cb = cb;
        }

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
            if(m_cb != null)
            {
                m_cb.onRecv(result);
                return;
            }
            Log.d("Hello", result);
        }
    }

}

