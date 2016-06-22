package com.example.slavik.swantestapplication;

import android.content.Context;
import android.util.Log;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
public class BatteryStats {

    volatile Object result;

    Context mContext;

    String REQUEST_CODE = "1122";

    public BatteryStats(Context context){
        this.mContext = context;
    }

    Object batteryRemainingPhone(){

        String myExpression = "self@battery:level?delay=0$server_storage=FALSE{ANY,0}";

        return getSingleValueFromExpression(myExpression);
    }

    Object batteryRemainingWear(){
        String myExpression = "wear@battery:level?delay=0$server_storage=FALSE{ANY,0}";

        return getSingleValueFromExpression(myExpression);
    }

    Object getSingleValueFromExpression(String expression){

        result = null;
        try {
            ExpressionManager.registerValueExpression(mContext, REQUEST_CODE,
                    (ValueExpression) ExpressionFactory.parse(expression),
                    new ValueExpressionListener() {
                        /* Registering a listener to process new values from the registered sensor*/
                        @Override
                        public void onNewValues(String id,
                                                TimestampedValue[] arg1) {
                            if (arg1 != null && arg1.length > 0) {
                                Log.d("BAttery", "Got result");
                                result = arg1[0].getValue();
                                ExpressionManager.unregisterExpression(mContext, REQUEST_CODE);
                            }
                            Log.d("BAtt", "Got null");
                        }
                    });
        } catch (SwanException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExpressionParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        while(result == null){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return new Integer(1);
    }
}
