package com.study.android.service1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final
     String TAG =
            "lecture";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void btn1(View v){
        Intent intent = new Intent(getApplicationContext(),MyService1.class);
        startService(intent);
    }
}
