package com.tongs.store.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tongs.store.R;
import com.tongs.store.util.GlobalVar;
import com.tongs.store.util.HttpTask;
import com.tongs.store.util.OnHttpReceive;
import com.tongs.store.util.Preference;
import com.tongs.store.view.kbv.KenBurnsView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


public class SplashScreensActivity extends Activity implements GlobalVar{

	public static final String SPLASH_SCREEN_OPTION = "com.tongs.store.activity.SplashScreensActivity";
	public static final String SPLASH_SCREEN_OPTION_1 = "Option 1";
	public static final String SPLASH_SCREEN_OPTION_2 = "Option 2";
	public static final String SPLASH_SCREEN_OPTION_3 = "Option 3";
	public static final String APPVERSION = "appVersion";
    public static final String PHONENUMBER = "phoneNumber";
    public static final String OSVERSION = "osVersion";
    public static final String PHONEMODEL = "phoneModel";

	private KenBurnsView mKenBurns;
	private ImageView mSplashImage;
	private boolean isClickStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); //Removing ActionBar
		setContentView(R.layout.activity_splash_screen);

		Preference.getInstance().init(this.getApplicationContext());
		
		mKenBurns = (KenBurnsView) findViewById(R.id.ken_burns_images);
		mKenBurns.setImageResource(R.drawable.splash_screen_background);
		mSplashImage = (ImageView) findViewById(R.id.splash_image_view);
		mSplashImage.setImageResource(R.drawable.splash_screen_logo);
		setAnimation(SPLASH_SCREEN_OPTION_2);
		getUserInfo();
		requestGlobalSet();
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				if( !isClickStatus ){
					super.handleMessage(msg);
					startActivity(new Intent(SplashScreensActivity.this, LogInPageActivity.class));
					finish();
				}
			}
		};
		handler.sendEmptyMessageDelayed(0, 3000);


	}
	
	/** Animation depends on category.
	 * */
	private void setAnimation(String category) {
		if (category.equals(SPLASH_SCREEN_OPTION_1)) {
			animation1();
		} else if (category.equals(SPLASH_SCREEN_OPTION_2)) {
			animation2();
		} else if (category.equals(SPLASH_SCREEN_OPTION_3)) {
			animation2();
			//animation3();
		}
	}

	private void animation1() {
		ObjectAnimator scaleXAnimation = ObjectAnimator.ofFloat(mSplashImage, "scaleX", 5.0F, 1.0F);
		scaleXAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		scaleXAnimation.setDuration(1200);
		ObjectAnimator scaleYAnimation = ObjectAnimator.ofFloat(mSplashImage, "scaleY", 5.0F, 1.0F);
		scaleYAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		scaleYAnimation.setDuration(1200);
		ObjectAnimator alphaAnimation = ObjectAnimator.ofFloat(mSplashImage, "alpha", 0.0F, 1.0F);
		alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		alphaAnimation.setDuration(1200);
		AnimatorSet animatorSet = new AnimatorSet();
		animatorSet.play(scaleXAnimation).with(scaleYAnimation).with(alphaAnimation);
		animatorSet.setStartDelay(500);
		animatorSet.start();
	}
	
	private void animation2() {
		mSplashImage.setAlpha(1.0F);
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.translate_top_to_center);
		mSplashImage.startAnimation(anim);






	}
//
//	private void animation3() {
//		ObjectAnimator alphaAnimation_welcome = ObjectAnimator.ofFloat(mwelcomeText, "alpha", 0.0F, 1.0F);
//
//        alphaAnimation_welcome.setStartDelay(1700);
//        alphaAnimation_welcome.setDuration(500);
//        alphaAnimation_welcome.start();
//  }
    //화면 터치 이벤트
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN :    //화면을 터치했을때
                isClickStatus = true;
				moveActivity();
                finish();
                break;
            case MotionEvent.ACTION_UP :    //화면을 터치했다 땠을때
                break;
            case MotionEvent.ACTION_MOVE :    //화면을 터치하고 이동할때
                break;
        }
        return super.onTouchEvent(event);
    }
	private static String getVersionName(Context context)
	{
		try {
			PackageInfo pi= context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		}
	}

	private String getPhoneNumber(Context context){
		TelephonyManager systemService = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String PhoneNumber = systemService.getLine1Number();
		PhoneNumber = PhoneNumber.substring(PhoneNumber.length()-10,PhoneNumber.length());
		PhoneNumber="0"+PhoneNumber;
		return PhoneNumber;

		}
	private void getUserInfo(){

		// get user infomation
		String appVer = getVersionName(this);
		String num;
		try{
			num = getPhoneNumber(this);
		}
		catch (Exception e){
			Log.v("getUserInfo", "not opening");
			num = "";
		}
		String model = Build.MODEL;
		String osVer = Build.VERSION.RELEASE;

		Log.v("userInfo", "appVer:" + appVer + " num:" + num + " model:" + model + " osVer:" + osVer);

        setUserInfo(appVer, num, model, osVer);

	}
    private void setUserInfo(String appVer, String num, String model, String osVer){

        Preference pref = Preference.getInstance();
        pref.put(APPVERSION, appVer);
        pref.put(PHONENUMBER,num);
        pref.put(PHONEMODEL, model);
        pref.put(OSVERSION, osVer);

    }
	private void moveActivity(){
		Intent intent = new Intent(this, LogInPageActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);

	}
	void requestGlobalSet(){

		Log.v("Protocol", "PROTOCOL_STATUS_GET_GLOBAL_SET");

//        setProtocolStatus(PROTOCOL_STATUS_USER_CANCLE);
		String url;
		url = getString(R.string.api_server) +
				getString(R.string.api_store_global_set_request	);
		requestOnUIThread(PROTOCOL_STATUS_GET_GLOBAL_SET, url, new OnHttpReceive() {
			@Override
			public void onReceive(int protocol, String data) {
				try {
					JSONObject json = new JSONObject(data);
					String result_code = json.optString("result_code", null);
					boolean isSuccess = "0".equals(result_code) ? true : false;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	void requestOnUIThread(final int protocol, final String url, final OnHttpReceive onReceive)
	{
		Log.v("URL", url);
		this.runOnUiThread(new Runnable() {
			public void run() {
				new HttpTask(protocol, onReceive).execute(url);
			}
		});
	}


}


