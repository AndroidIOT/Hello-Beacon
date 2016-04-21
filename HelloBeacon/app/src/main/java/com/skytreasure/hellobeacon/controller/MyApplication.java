package com.skytreasure.hellobeacon.controller;

import android.app.Application;
import android.content.Intent;

import com.skytreasure.hellobeacon.service.MakeMeSilent;

/**
 * Created by akash on 21/4/16.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MakeMeSilent.class));
    }
}
