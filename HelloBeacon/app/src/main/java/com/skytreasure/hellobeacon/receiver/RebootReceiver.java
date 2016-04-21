package com.skytreasure.hellobeacon.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.skytreasure.hellobeacon.service.MakeMeSilent;

/**
 * Created by akash on 21/4/16.
 */
public class RebootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, MakeMeSilent.class));
    }
}
