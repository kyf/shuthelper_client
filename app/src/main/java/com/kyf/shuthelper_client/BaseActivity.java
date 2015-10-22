package com.kyf.shuthelper_client;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.bigkoo.alertview.AlertView;

public abstract class BaseActivity extends AppCompatActivity {

    protected int mLayout = R.layout.activity_main;

    protected AlertView alertView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(mLayout);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent ev){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(alertView != null && alertView.isShowing()){
                alertView.dismiss();
                return true;
            }
        }

        return super.onKeyDown(keyCode, ev);
    }
}
