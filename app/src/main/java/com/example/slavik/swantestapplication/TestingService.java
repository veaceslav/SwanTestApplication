package com.example.slavik.swantestapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by slavik on 6/22/16.
 */
public class TestingService extends Service {

    Notification.Builder notificationBuilder;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();



        notificationBuilder = new Notification.Builder(this);
        notificationBuilder.setContentTitle("Swan Test ");
        notificationBuilder.setContentText("Running tests sensor data..");


        // testSwanExpression();

        startForeground(1, notificationBuilder.build());

    }

    void runTests(){

    }

}
