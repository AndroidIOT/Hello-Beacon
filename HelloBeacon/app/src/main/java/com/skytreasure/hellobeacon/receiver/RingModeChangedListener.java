package com.skytreasure.hellobeacon.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.skytreasure.hellobeacon.service.MakeMeSilent;

/**
 * Created by akash on 21/4/16.
 */
public class RingModeChangedListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (audioManager.getRingerMode() > AudioManager.RINGER_MODE_VIBRATE) {
            context.startService(new Intent(context, MakeMeSilent.class));
        }
    }
}
