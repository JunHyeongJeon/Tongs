package com.example.jaecheol.tongs;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Created by JaeCheol on 15. 3. 27..
 */
public class SignupActivity extends ActionBarActivity
        implements View.OnClickListener//,RadioGroup.OnCheckedChangeListener
{

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    static final String TAG = "GCM Demo";

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;

    String regid;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "211629096961";

    String sex;
    String uid;
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

        context = getApplicationContext();

        infoLayout = (LinearLayout)findViewById(R.id.id_infoLayout);
        infoLayout.setVisibility(LinearLayout.INVISIBLE);

        initGCM();


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

                                uid = json.get("uid").toString();

                                SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editor = mPref.edit();
                                editor.putString("uid", uid);
                                editor.commit();

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


                String url = getText(R.string.Server_URL)
                        + "user/auth/gcm"
                        + "?token=" + authToken
                        + "&gcm=" + regid;
                IHttpRecvCallback cb = new IHttpRecvCallback() {
                    public void onRecv(String result) {
                        try {
                            JSONObject json = new JSONObject(result);
                            String result_code = json.get("result_code").toString();
                            Log.d("Hello", "gcm 전달 : " + result_code);
                            if( "-1".equals(result_code) )
                                return;


                        } catch (Exception e) { }
                    }
                };
                new HttpTask(cb).execute(url);

                break;
        }
    }

    public void initGCM()  {
        Log.i("a","AA");
        if( checkPlayServices() )   {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            Log.d("HHGKGKORKGOR", regid);

            if( regid.isEmpty() )   {
                registerInBackground();
            }
        }
        else    {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        Log.d("SF", registrationId);
        return registrationId;
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(SignupActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }


    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(TAG, msg);
            }
        }.execute(null, null, null);
    }


    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
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

