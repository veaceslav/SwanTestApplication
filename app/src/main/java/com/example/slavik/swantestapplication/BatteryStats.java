package com.example.slavik.swantestapplication;

import android.content.Context;
import android.util.Log;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

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

class MyMutable<T>{
    T value;

    void setValue(T value){
        this.value = value;
    }

    T getValue(){
        return this.value;
    }
}

public class BatteryStats {

    Context mContext;


    public BatteryStats(Context context){
        this.mContext = context;
    }

    Object energyRemainingPhone(){

        String myExpression = "self@battery:level{ANY,0}";

        return getSingleValueFromExpression(myExpression, RandomId.getRandomId());
    }

    Object energyRemainingWear(){

        String myExpression = "self@battery:level{ANY,0}";

        return getSingleValueFromExpression(myExpression, RandomId.getRandomId());
    }

    Object batteryRemainingPhone(){

        String myExpression = "self@battery:level{ANY,0}";

        return getSingleValueFromExpression(myExpression, RandomId.getRandomId());
    }

    Object batteryRemainingWear(){
        String myExpression = "wear@battery:level{ANY,0}";

        return getSingleValueFromExpression(myExpression, RandomId.getRandomId());
    }

    public synchronized Object getSingleValueFromExpression(String expression, final String code){

        final CountDownLatch latch = new CountDownLatch(1);
        final MyMutable<Object> value = new MyMutable<>();

        try {
            ExpressionManager.registerValueExpression(mContext, code,
                    (ValueExpression) ExpressionFactory.parse(expression),
                    new ValueExpressionListener() {
                        /* Registering a listener to process new values from the registered sensor*/
                        @Override
                        public void onNewValues(String id,
                                                TimestampedValue[] arg1) {
                            if (arg1 != null && arg1.length > 0) {
                                if(latch.getCount() != 0) {
                                    ExpressionManager.unregisterExpression(mContext, code);
                                    Log.d("BAttery", "Got result");
                                    value.setValue(arg1[0].getValue());
                                    latch.countDown();
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

        return value.getValue();

    }
}
