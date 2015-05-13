package com.csform.android.uiapptemplate;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.csform.android.uiapptemplate.font.FontelloTextView;
import com.csform.android.uiapptemplate.view.kbv.KenBurnsView;

public class SplashScreensActivity extends Activity {

	public static final String SPLASH_SCREEN_OPTION = "com.csform.android.uiapptemplate.SplashScreensActivity";
	public static final String SPLASH_SCREEN_OPTION_1 = "Option 1";
	public static final String SPLASH_SCREEN_OPTION_2 = "Option 2";
	public static final String SPLASH_SCREEN_OPTION_3 = "Option 3";
	
	private KenBurnsView mKenBurns;
	private FontelloTextView mLogo;
	private TextView mwelcomeText;
	private ImageView mSplashImage;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); //Removing ActionBar
		setContentView(R.layout.activity_splash_screen);
		
		mKenBurns = (KenBurnsView) findViewById(R.id.ken_burns_images);
		//mLogo = (FontelloTextView) findViewById(R.id.logo);
		//mwelcomeText = (TextView) findViewById(R.id.welcome_text);
		mKenBurns.setImageResource(R.drawable.splash_screen_background);
		mSplashImage = (ImageView) findViewById(R.id.splash_image_view);
		mSplashImage.setImageResource(R.drawable.splash_screen_logo);

		setAnimation(SPLASH_SCREEN_OPTION_3);

		getUserInfo();

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
	
	private void animation3() {
		ObjectAnimator alphaAnimation_welcome = ObjectAnimator.ofFloat(mwelcomeText, "alpha", 0.0F, 1.0F);

        alphaAnimation_welcome.setStartDelay(1700);
        alphaAnimation_welcome.setDuration(500);
        alphaAnimation_welcome.start();


	}
    //화면 터치 이벤트
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch(action) {
            case MotionEvent.ACTION_DOWN :    //화면을 터치했을때
                Intent intent = new Intent(this, LogInPageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
                finish();
                break;
            case MotionEvent.ACTION_UP :    //화면을 터치했다 땠을때
                break;
            case MotionEvent.ACTION_MOVE :    //화면을 터치하고 이동할때
                break;
        }
        return super.onTouchEvent(event);
    }
	public static String getVersionName(Context context)
	{
		try {
			PackageInfo pi= context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return pi.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			return null;
		}
	}

	public String getPhoneNumber(Context context){
		TelephonyManager systemService = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String PhoneNumber = systemService.getLine1Number();
		PhoneNumber = PhoneNumber.substring(PhoneNumber.length()-10,PhoneNumber.length());
		PhoneNumber="0"+PhoneNumber;
		return PhoneNumber;

		//--얻어온 전화번호에 자동으로 하이픈(-) 추가--
		//PhoneNumber = PhoneNumberUtils.formatNumber(PhoneNumber);
		//--원하는 에디트텍스트(여기서는 m_EditText_Phone)를 가져와서 setHint메소드로 디폴트값을 줌--
		// //m_EditText_phone.setHint(PhoneNumber);
		//--해당 에디트텍스트를 사용자입력 금지시킴--
		//m_EditText_phone.setEnabled(false);
		}
	private void getUserInfo(){

		// get user infomation
		String appVer = getVersionName(this);
		String num = getPhoneNumber(this);
		String model = Build.MODEL;
		String osVer = Build.VERSION.RELEASE;
		Log.v("userInfo", "appVer:"+appVer+" num:"+num+" model:"+model +" osVer:"+osVer );

	}
}
