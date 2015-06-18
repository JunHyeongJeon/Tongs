package com.tongs.store.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import com.tongs.store.R;
import com.tongs.store.SlidingTabsBasicFragment;
import com.tongs.store.util.GlobalVar;


public class PreviousClientListActivity extends ActionBarActivity implements GlobalVar {





    private CustomDialog mCustomDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_previous_client_list);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_previous_client_activity);
//        setSupportActionBar(toolbar);


        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            SlidingTabsBasicFragment fragment = new SlidingTabsBasicFragment();
            transaction.replace(R.id.sample_content_fragment, fragment);
            transaction.commit();
        }


        mCustomDialog = new CustomDialog(this);
        mCustomDialog.show();

    }


    private void popDialog(String ment){
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage(ment).setCancelable(false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                                          }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alt_bld.create();
        alert.setTitle("Title");
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();

    }



    @Override
    public void onBackPressed() {
        this.finish();
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_previous_client_list, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }



    public class CustomDialog extends Dialog {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.dimAmount = 0.8f;
            //  lpWindow.width = WindowManager.LayoutParams.MATCH_PARENT;
            //  lpWindow.height = WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(lpWindow);

            setContentView(R.layout.custom_data_get_dialog);

            setLayout();
            setTitle(mTitle);
            setContent(mContent);


            //  setClickListener(mLeftClickListener , mRightClickListener);
        }

        public CustomDialog(Context context) {
            // Dialog 배경을 투명 처리 해준다.
            super(context , android.R.style.Theme_Translucent_NoTitleBar);
        }

        public CustomDialog(Context context , String title ,
                            View.OnClickListener singleListener) {
            super(context , android.R.style.Theme_Translucent_NoTitleBar);
            this.mTitle = title;
            //this.mLeftClickListener = singleListener;
        }

        public CustomDialog(Context context , String title , String content ,
                            View.OnClickListener leftListener , View.OnClickListener rightListener) {
            super(context , android.R.style.Theme_Translucent_NoTitleBar);
            this.mTitle = title;
            this.mContent = content;


        }

        private void setTitle(String title){
            //  mTitleView.setText(title);
        }

        private void setContent(String content){
            //   mContentView.setText(content);
        }

        private void setClickListener(View.OnClickListener left , View.OnClickListener right){
            if(left!=null && right!=null){


            }else if(left!=null && right==null){


            }else {


            }
        }

        private String mTitle;
        private String mContent;

        private Button mConfirmButton;
        private Button mCancleButton;

        private DatePicker mDatePicker;


        /*
         * Layout
         */
        private void setLayout(){

            mConfirmButton = (Button) findViewById(R.id.previous_dialog_confirm);
            mConfirmButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                       //     mDatePicker.get
                            mCustomDialog.dismiss();
                        }
                    }
            );
            mCancleButton = (Button) findViewById(R.id.previous_dialog_cancle);
            mCancleButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mCustomDialog.dismiss();
                        }
                    }
            );
        }

    }

}
