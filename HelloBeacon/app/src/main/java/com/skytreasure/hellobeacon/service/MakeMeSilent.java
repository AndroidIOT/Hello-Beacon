package com.skytreasure.hellobeacon.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.skytreasure.hellobeacon.activity.MainActivity;
import com.skytreasure.hellobeacon.constants.ApplicationConstants;

import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

/**
 * Created by akash on 21/4/16.
 */
public class MakeMeSilent extends Service implements BeaconConsumer {

    private BeaconManager mBeaconManager;
    private AudioManager mAudioManager;
    private SharedPreferences mSharedPreferences;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext=this;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mBeaconManager = BeaconManager.getInstanceForApplication(this);
        mBeaconManager.setBackgroundMode(true);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        mBeaconManager.bind(this);
    }

    @Override
    public void onDestroy() {
        mBeaconManager.unbind(this);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onBeaconServiceConnect() {
        mBeaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {

                Log.d(ApplicationConstants.LOG_TAG, "Region id - " + region.getUniqueId());

                if (mAudioManager.getRingerMode() != AudioManager.RINGER_MODE_VIBRATE) {
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                    Log.d(ApplicationConstants.LOG_TAG, "RINGER_MODE_VIBRATE set");
                    //sendNotification();
                }
                boolean switchSoundOn = mSharedPreferences.getBoolean(ApplicationConstants.SHARED_PREFERENCES_KEY_TURN_SOUND_ON, false);
                if (!switchSoundOn) {
                    stopSelf();
                }
            }

            @Override
            public void didExitRegion(Region region) {
                boolean switchSoundOn = mSharedPreferences.getBoolean(ApplicationConstants.SHARED_PREFERENCES_KEY_TURN_SOUND_ON, false);
                if (switchSoundOn) {
                    mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                }
                Log.d(ApplicationConstants.LOG_TAG, "I no longer see a beacon");
                Log.d(ApplicationConstants.LOG_TAG, "Region id - " + region.getUniqueId());
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
            }
        });

        try {
            //TODO: Update your beacon UUID
            Identifier identifier = Identifier.parse("F7826DA6-4FA2-4E98-8024-BC5B71E0893E"); //Office beacon
          //  Identifier identifier2 = Identifier.parse("F7826DA6-4FA2-4E98-8024-BC5B71E0893E"); //kontakt
            mBeaconManager.startMonitoringBeaconsInRegion(new Region(ApplicationConstants.OFFICE, identifier, null, null));
            //mBeaconManager.startMonitoringBeaconsInRegion(new Region(ApplicationConstants.REGION2_ID, identifier2, null, null));
        } catch (RemoteException e) {
            Log.e(ApplicationConstants.LOG_TAG, e.getMessage(), e);
        }
    }


    private void sendNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setContentTitle("Set to Vibrate Mode")
                        .setContentText("Your Beacon is nearby.");


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(new Intent(this, MainActivity.class));
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

}
