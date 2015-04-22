package com.csform.android.uiapptemplate;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

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
import java.util.concurrent.atomic.AtomicInteger;


public class SignUpActivityForth extends ActionBarActivity {

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private ProgressDialog dialog;
    private boolean sendSucces = false;

    private String authToken;

    private String regid;

    static final String TAG = "GCM Demo";

    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;

    /**
     * Substitute you own sender ID here. This is the project number you got
     * from the API Console, as described in "Getting Started."
     */
    String SENDER_ID = "211629096961";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.activity_sign_up_activity_forth);
        SharedPreferences mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        authToken = mPref.getString("emailToken", null);


        Button mBackButton = (Button)findViewById(R.id.sign_up_forth_back_button);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityBack();

            }
        });
        Button mNextButton = (Button)findViewById(R.id.sign_up_forth_next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //attemptSignup();
                initGCM();
                DialogProgress();

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_up_activity_forth, menu);
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

    private void initGCM()  {
        Log.i("a", "AA");
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

    private void sendGCMKey()
    {

        String url = getText(R.string.Server_URL)
                + "store/auth/gcm?"
                + "token=" + authToken
                + "&gcm=" + regid;

        Log.v("URL", url);
        IHttpRecvCallback cb = new IHttpRecvCallback() {
            public void onRecv(String result) {
                try {
                    Log.v("result", result);
                    JSONObject json = new JSONObject(result);
                    String result_code = json.get("result_code").toString();
                    Log.d("Hello", "gcm 전달 : " + result_code);
                    if( "-1".equals(result_code) )
                        return;

                    else {
                        ActivityNext();
                    }

                } catch (Exception e) { }
            }
        };
        new HttpTask(cb).execute(url);
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
        return getSharedPreferences(SignUpActivityForth.class.getSimpleName(),
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
                    sendGCMKey();


                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
              //      sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                //    storeRegistrationId(context, regid);
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

    public void ActivityBack(){
        this.finish();
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);

    }
    public void ActivityNext(){
        Intent intent = new Intent(this, LogInPageActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);

    }

    private void DialogProgress(){
        dialog = ProgressDialog.show(SignUpActivityForth.this, "",
                "잠시만 기다려 주세요 ...", true);
        // 창을 내린다.
        // dialog.dismiss();
    }


}
