package com.example.slavik.swantestapplication;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
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

    int runtime = 1200;

    int delay = 100;
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
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setContentTitle("Swan Test ");
        notificationBuilder.setContentText("Running tests sensor data..");

        batteryStats = new BatteryStats(getApplicationContext());

        startForeground(1, notificationBuilder.build());

        runTests();

        mContext = getApplicationContext();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    void runTests(){

        ExecutorService executorService = Executors.newCachedThreadPool();

        executorService.submit(new Runnable() {
            @Override
            public void run() {
                OutputData.getInstance(mContext).openStream();
                long before = System.currentTimeMillis();
                runPhoneExpressions();
                runWearExpressions();
                long after = System.currentTimeMillis();
                OutputData.getInstance(mContext).closeStream();
                Log.d(TAG, "Tests completed in " + ((after-before)/1000) + " seconds");
            }
        });

    }

    private void runPhoneExpressions() {

        int index = 0;
        for(String expr : SwanExpressionsForTest.phone_expr){
            String newExpr = expr.replace("{$delay}", String.valueOf(delay));
            updateNotification("Running phone expression :"+ index + " of " + SwanExpressionsForTest.phone_expr.length );
            index++;
            runExpression(newExpr);
        }
    }

    private void runWearExpressions() {
        Log.d(TAG, "Nr of expressions" + SwanExpressionsForTest.wear_expr.length);
        int index = 0;
        for(String expr : SwanExpressionsForTest.wear_expr){
            String newExpr = expr.replace("{$delay}", String.valueOf(delay));
            //Log.d(TAG, "Running expr: " + newExpr);
            updateNotification("Running wear expression :"+ index + " of " + SwanExpressionsForTest.phone_expr.length );
            index++;
            runExpression(newExpr);
        }
    }

    private void runExpression(String expr){

        int valueCount = runtime/delay;

        if(expr.contains("heart")){
            valueCount = runtime/1000; // for heartrate is 1 s
        }

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

        long startTime = System.currentTimeMillis();
        registerSWANSensor(expr, valueCount);

        long stopTime = System.currentTimeMillis();

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

        Log.d(TAG, "Results of: "+ valueCount +" runs:" + levelPhone +" " + levelWear + " after: " + levelAfterPhone + " " + levelAfterWear );

        OutputData.getInstance(mContext).addToResult(expr + "," +String.valueOf(valueCount) + "," + levelPhone +","
                + levelWear + "," + levelAfterPhone + "," + levelAfterWear+
                "," + ((stopTime - startTime)/1000)+ "\n");

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
                                //Log.d(TAG, "Value Count " + currentCount);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void updateNotification(String msg){
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        notificationBuilder.setContentText(msg);
        mNotificationManager.notify(1, notificationBuilder.build());
    }
}
