package com.kyf.shuthelper_client;

import android.app.Application;
import android.os.Handler;

/**
 * Created by keyf on 2015/10/23.
 */
public class MyApplication extends Application {

    private Handler myHandler;

    public void setHandler(Handler handler){
        myHandler = handler;
    }

    public Handler getHandler(){
        return myHandler;
    }

}
