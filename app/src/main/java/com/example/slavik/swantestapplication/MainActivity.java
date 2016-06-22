package com.example.slavik.swantestapplication;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Context main_context;
    final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_context = this;
        Button start_button = (Button)findViewById(R.id.start_service_bttn);
        Button stop_button = (Button)findViewById(R.id.stop_service_bttn);

        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Starting service");
                Intent i = new Intent(main_context, TestingService.class);
                startService(i);

            }
        });

        stop_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(main_context, TestingService.class);
                stopService(i);
            }
        });
    }
}
