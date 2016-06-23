package com.example.slavik.swantestapplication;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
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

        batteryStats = new BatteryStats(getApplicationContext());

        startForeground(1, notificationBuilder.build());

        runTests();

    }

    void runTests(){

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                runPhoneExpressions();
                runWearExpressions();
            }
        });

    }

    private void runPhoneExpressions() {

        runExpression(SwanExpressionsForTest.phone_expr[0]);
//        for(String expr : SwanExpressionsForTest.phone_expr){
//            runExpression(expr);
//        }
    }

    private void runWearExpressions() {

    }

    private void runExpression(String expr){
        int valueCount = 1000;

        int levelPhone = (int)batteryStats.batteryRemainingPhone();
        int levelWear  = (int)batteryStats.batteryRemainingWear();
        Log.d(TAG, "Starting expression");

        registerSWANSensor(expr, valueCount);

        int levelAfterPhone = (int)batteryStats.batteryRemainingPhone();
        int levelAfterWear  = (int)batteryStats.batteryRemainingWear();

        Log.d(TAG, "Results of 1000 runs:" + levelPhone +" " + levelWear + " after: " + levelAfterPhone + " " + levelAfterWear );

    }
    private void registerSWANSensor(String myExpression, final int valueCount){

        final CountDownLatch latch = new CountDownLatch(1);
        try {
            ExpressionManager.registerValueExpression(this, REQUEST_CODE,
                    (ValueExpression) ExpressionFactory.parse(myExpression),
                    new ValueExpressionListener() {

                        int currentCount = 0;
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
                                    ExpressionManager.unregisterExpression(getApplicationContext(), REQUEST_CODE);
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
