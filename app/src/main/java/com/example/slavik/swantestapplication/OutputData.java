package com.example.slavik.swantestapplication;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by slavik on 6/27/16.
 */
public class OutputData {

    FileOutputStream outputStream;
    private static OutputData ourInstance = null;
    String filename = "resultData.txt";
    Context mContext;

    public static OutputData getInstance(Context context) {
        if(ourInstance == null)
            ourInstance = new OutputData(context);
        return ourInstance;
    }

    private OutputData(Context context) {
        mContext = context;
    }


    public void openStream(){
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File ("/sdcard/swanData");
        dir.mkdirs();
        File file = new File(dir, filename);

        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void closeStream(){
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addToResult(String line){
        try {
            outputStream.write(line.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
