package com.example.slavik.swantestapplication;

import java.util.Random;

/**
 * Created by slavik on 6/27/16.
 */
public class RandomId {

    public static String getRandomId(){
        Random rn = new Random();
        int next = rn.nextInt();

        return String.valueOf(next);
    }
}
