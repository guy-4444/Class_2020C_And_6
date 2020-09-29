package com.classy.class_2020c_and_6;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {


    private MaterialButton main_BTN_startService;
    private MaterialButton main_BTN_stopService;
    private MaterialButton main_BTN_checkService;
    private MaterialTextView main_LBL_location;

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(MyService.BROADCAST)) {
                Log.d("pttt", "BroadcastReceiver Thread: " + Thread.currentThread().getName());
                int meter = intent.getIntExtra("EXTRA_LOCATION", -1);

                main_LBL_location.setText("" + meter);
            }
        }
    };
    LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("pttt", "Main Thread: " + Thread.currentThread().getName());

        main_BTN_startService = findViewById(R.id.main_BTN_startService);
        main_BTN_stopService = findViewById(R.id.main_BTN_stopService);
        main_BTN_checkService = findViewById(R.id.main_BTN_checkService);
        main_LBL_location = findViewById(R.id.main_LBL_location);

        main_BTN_startService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMyService();
            }
        });

        main_BTN_stopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopMyService();
            }
        });

        main_BTN_checkService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isMyServiceRunning(MyService.class);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(MyService.BROADCAST);

        // Why use LocalBroadcastManager? - https://stackoverflow.com/a/13597414/7147289
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(myReceiver);
    }


    private void startMyService() {
        Intent intent = new Intent(this, MyService.class);
        intent.setAction(MyService.ACTION_START_SERVICE);
        startService(intent);
    }

    private void stopMyService() {
        Intent intent = new Intent(this, MyService.class);
        intent.setAction(MyService.ACTION_STOP_SERVICE);
        startService(intent);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        int counter = 0;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> runs = manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                counter++;
                //return true;
            }
        }

        Log.d("pttt", "Counter= " + counter);
        if (counter > 0)
            return true;
        return false;
    }
}