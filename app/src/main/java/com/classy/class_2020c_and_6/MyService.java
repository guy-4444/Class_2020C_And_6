package com.classy.class_2020c_and_6;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MyService extends Service {

    public static final String BROADCAST = "com.classy.class_2020c_and_6.NEW_LOCATION_DETECTED";

    public static final String ACTION_START_SERVICE = "ACTION_START_SERVICE";
    public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";

    private boolean isServiceRunningRightNow;
    private CountDownTimer countDownTimer;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        String action = intent.getAction();
        if (action == null) {
            return Service.START_STICKY;
        }

        if (action.equals(ACTION_START_SERVICE)) {

            if (!isServiceRunningRightNow) {
                isServiceRunningRightNow = true;
                startTimer();
            }

        } else if (action.equals(ACTION_STOP_SERVICE)) {
            countDownTimer.cancel();
            stopSelf();
        }


        return Service.START_STICKY;
    }

    int counter = 0;
    private void startTimer() {
        countDownTimer =  new CountDownTimer(120000, 3000) {

            public void onTick(long millisUntilFinished) {
                Log.d("pttt", "TimerTask Thread: " + Thread.currentThread().getName());

                counter += 100;
                Intent intent = new Intent(BROADCAST);
                intent.putExtra("EXTRA_LOCATION", counter);
                LocalBroadcastManager.getInstance(MyService.this).sendBroadcast(intent);
                // sendBroadcast(intent); = global send broadcast
            }

            public void onFinish() { }

        }.start();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
