package com.csform.android.uiapptemplate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class LogInPageActivity extends Activity implements OnClickListener {

	public static final String LOGIN_PAGE_AND_LOADERS_CATEGORY = "com.csform.android.uiapptemplate.LogInPageAndLoadersActivity";
	public static final String DARK = "Dark";
	public static final String LIGHT = "Light";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE); //Removing ActionBar
		String category = LIGHT;
		Bundle extras = getIntent().getExtras();
		if (extras != null && extras.containsKey(LOGIN_PAGE_AND_LOADERS_CATEGORY)) {
			category = extras.getString(LOGIN_PAGE_AND_LOADERS_CATEGORY, DARK);
		}
		setContentView(category);
	}
	
	private void setContentView(String category) {
		if (category.equals(DARK)) {
			setContentView(R.layout.activity_login_page_dark);
		} else if (category.equals(LIGHT)) {
			setContentView(R.layout.activity_login_page_light);
		}
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
            if(v.getId() == R.id.login){



            }else if(v.getId()== R.id.register){
                Toast.makeText(this, "good", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_left, R.anim.slide_out_left);
            }else if(v.getId()== R.id.skip){


            }

        }
	}

}
