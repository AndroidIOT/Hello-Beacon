package com.skytreasure.hellobeacon.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.skytreasure.hellobeacon.R;
import com.skytreasure.hellobeacon.constants.ApplicationConstants;
import com.skytreasure.hellobeacon.service.MakeMeSilent;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(MainActivity.this, MakeMeSilent.class));
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //Set to true if it wants to automatically set vibrate mode off when you go out of range.
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(ApplicationConstants.SHARED_PREFERENCES_KEY_TURN_SOUND_ON, true);
        editor.apply();
    }
}
