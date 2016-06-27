package com.example.slavik.swantestapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

import interdroid.swancore.swanmain.ExpressionManager;
import interdroid.swancore.swanmain.SwanException;
import interdroid.swancore.swanmain.ValueExpressionListener;
import interdroid.swancore.swansong.ExpressionFactory;
import interdroid.swancore.swansong.ExpressionParseException;
import interdroid.swancore.swansong.TimestampedValue;
import interdroid.swancore.swansong.ValueExpression;

/**
 * Created by slavik on 6/22/16.
 */
public class TestingService extends Service {

    Notification.Builder notificationBuilder;

    BatteryStats batteryStats;

    Lock lock;

    volatile boolean unlocked;

    final String TAG = "TestingService";

    String REQUEST_CODE = "1234";

    Context mContext;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();

        ExecutorService executorService = Executors.newCachedThreadPool();

        notificationBuilder = new Notification.Builder(this);
        notificationBuilder.setContentTitle("Swan Test ");
        notificationBuilder.setContentText("Running tests sensor data..");

        batteryStats = new BatteryStats(getApplicationContext());

        startForeground(1, notificationBuilder.build());

        runTests();

        mContext = getApplicationContext();

    }

    void runTests(){

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                runPhoneExpressions();
                runWearExpressions();
                Log.d(TAG, "Tests completed");
            }
        });

    }

    private void runPhoneExpressions() {

        runExpression(SwanExpressionsForTest.phone_expr[0]);
        runExpression(SwanExpressionsForTest.phone_expr[1]);


//        for(String expr : SwanExpressionsForTest.phone_expr){
//            runExpression(expr);
//        }
    }

    private void runWearExpressions() {
        runExpression(SwanExpressionsForTest.wear_expr[0]);
    }

    private void runExpression(String expr){
        int valueCount = 200;

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int levelPhone = (int)batteryStats.batteryRemainingPhone();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int levelWear  = (int)batteryStats.batteryRemainingWear();
        Log.d(TAG, "Starting expression");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        registerSWANSensor(expr, valueCount);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int levelAfterPhone = (int)batteryStats.batteryRemainingPhone();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int levelAfterWear  = (int)batteryStats.batteryRemainingWear();

        Log.d(TAG, "Results of 1000 runs:" + levelPhone +" " + levelWear + " after: " + levelAfterPhone + " " + levelAfterWear );



    }
    private void registerSWANSensor(String myExpression, final int valueCount){

        final CountDownLatch latch = new CountDownLatch(1);
        final String id = RandomId.getRandomId();
        try {
            ExpressionManager.registerValueExpression(mContext, id,
                    (ValueExpression) ExpressionFactory.parse(myExpression),
                    new ValueExpressionListener() {

                        volatile int currentCount = 0;
                        volatile boolean registered =true;
                        /* Registering a listener to process new values from the registered sensor*/
                        @Override
                        public void onNewValues(String id,
                                                TimestampedValue[] arg1) {
                            if (arg1 != null && arg1.length > 0) {
                                String value = arg1[0].getValue().toString();
                                currentCount++;
                                Log.d(TAG, "Value Count " + currentCount);
                                if(currentCount == valueCount){
                                    latch.countDown();
                                    Log.d(TAG, "Calling unregister");
                                    ExpressionManager.unregisterExpression(mContext, id);
                                }
                            }
                        }
                    });
        } catch (SwanException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExpressionParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
