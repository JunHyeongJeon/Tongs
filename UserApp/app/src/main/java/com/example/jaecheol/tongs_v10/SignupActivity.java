package com.example.jaecheol.tongs_v10;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


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

//                try {
//
//                    HttpClient client = new DefaultHttpClient();
////                        String postURL = String.valueOf(R.string.Server_URL);
//                    String postURL = "http://tong.kr/user/auth/sms_request";
//                    HttpPost post = new HttpPost(postURL);
//
//                    List<NameValuePair> params = new ArrayList<NameValuePair>();
//                    params.add(new BasicNameValuePair("mdn", number));
//
//                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
//                    post.setEntity(ent);
//                    HttpResponse responsePOST = client.execute(post);
//                    HttpEntity resEntity = responsePOST.getEntity();
//
//                    if( resEntity != null ) {
//                        Log.i("RESPONSE ", EntityUtils.toString(resEntity));
//
//                        InputStream instream = resEntity.getContent();
//                        String result2 = convertStreamToString(instream);
//
//                        JSONObject json2 = new JSONObject(result2);
//
//                        JSONArray nameArray =json2.names();
//                        JSONArray valArray = json2.toJSONArray(nameArray);
//
//                        resultCode = valArray.getInt(0);
//
//                        instream.close();
//                    }
//
//                } catch (Exception e)   {
//                    e.printStackTrace();
//                }
//
//                if( resultCode == 0 )   {

                    tooltip.setText("인증 번호");
                    editText.setText(null);
                    editText.setHint("인증 번호");
                    certificButton.setText("확인");

                    Toast toast = Toast.makeText(getApplicationContext(),
                            number + "로 전송된 인증번호를 입력하세요", Toast.LENGTH_SHORT);
                    toast.show();
//                }
//                else    {
//
//                    Toast toast = Toast.makeText(getApplicationContext(),
//                            "서버가 응답이 없거나 잘못된 번호를 입력하셨습니다.", Toast.LENGTH_SHORT);
//                    toast.show();
//                }
            }
            else    {
                // 서버로 인증번호 전송

                certificationNumber = editText.getText().toString();
                Toast toast = Toast.makeText(getApplicationContext(),
                        "입력된 인증번호는 " + certificationNumber, Toast.LENGTH_SHORT);
                toast.show();

                number = editText.getText().toString();

                int resultCode = -1;

//                try {
//
//                    HttpClient client = new DefaultHttpClient();
////                        String postURL = String.valueOf(R.string.Server_URL);
//                    String postURL = "http://tong.kr/user/auth/sms_check";
//                    HttpPost post = new HttpPost(postURL);
//
//                    List<NameValuePair> params = new ArrayList<NameValuePair>();
//                    params.add(new BasicNameValuePair("mdn", number));
//                    params.add(new BasicNameValuePair("code", certificationNumber));
//
//                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
//                    post.setEntity(ent);
//                    HttpResponse responsePOST = client.execute(post);
//                    HttpEntity resEntity = responsePOST.getEntity();
//
//                    if( resEntity != null ) {
//                        Log.i("RESPONSE ", EntityUtils.toString(resEntity));
//
//                        InputStream inputStream = resEntity.getContent();
//                        String result3 = convertStreamToString(inputStream);
//
//                        JSONObject json3 = new JSONObject(result3);
//
//                        JSONArray nameArray =json3.names();
//                        JSONArray valArray = json3.toJSONArray(nameArray);
//
//                        resultCode = valArray.getInt(0);
//                        authToken = valArray.getString(2);
//                        uid = valArray.getInt(3);
//
//                        inputStream.close();
//                    }
//
//                } catch (Exception e)   {
//                    e.printStackTrace();
//                }
//
//
//                if( resultCode == 0 )   {

                    infoLayout.setVisibility(LinearLayout.VISIBLE);
                    certificButton.setEnabled(false);
                    certificButton.setTextColor(android.R.color.tertiary_text_light);
                    certificButton.setBackgroundColor(android.R.color.background_light);
                    editText.setEnabled(false);

                    Toast toast2 = Toast.makeText(getApplicationContext(),
                            "인증에 성공하셨습니다. 추가 정보를 입력하세요.", Toast.LENGTH_LONG);
                    toast2.show();
//                }
//                else    {
//
//                    Toast toast2 = Toast.makeText(getApplicationContext(),
//                            "인증에 실패하셨습니다.", Toast.LENGTH_LONG);
//                    toast2.show();
//                }
            }
            break;

           case R.id.id_startButton:

//                JSONObject obj = new JSONObject();
//                try {
//                    obj.put("mobile_number", number);
//                    obj.put("sex", sex);
//                    obj.put("birthdate", birthdate);
//                    obj.put("auth_token", authToken);
//
//                } catch (JSONException e )  { }
//                String json = obj.toString();
//
//                String jsonObj = NetworkActivity.sendJsonDataToServer(1, 0, json,
//               "http://tongs.kr/user/join");
//                String[][] result = NetworkActivity.jsonParserList(jsonObj);
//
//                if( result == null )    {
//                    Toast toast2 = Toast.makeText(getApplicationContext(),
//                            "서버에서 응답이 없습니다.", Toast.LENGTH_SHORT);
//                    toast2.show();
//                }
//                else {
//
//                    if (result[0][1] == "success") {
                        Toast toast2 = Toast.makeText(getApplicationContext(),
                                "회원가입에 성공하셨습니다.", Toast.LENGTH_SHORT);
                        toast2.show();

//                        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                        SharedPreferences.Editor editor = mPref.edit();
//                        editor.putString("auth_token", authToken);
//                        editor.putString("birth_date", birthdate);
//                        editor.putString("number", number);
//                        editor.putString("sex", sex);
//                        editor.commit();

                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
//                        intent.putExtra("authToken", authToken);
                        startActivity(intent);
                        this.finish();
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


    public static String convertStreamToString(InputStream is) {
/*
 * To convert the InputStream to String we use the BufferedReader.readLine()
 * method. We iterate until the BufferedReader return null which means
 * there's no more data to read. Each line will appended to a StringBuilder
 * and returned as String.
 */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}