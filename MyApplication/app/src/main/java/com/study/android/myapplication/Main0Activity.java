package com.study.android.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main0Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main0);
    }
    public void btn(View v){
        Intent intent = new Intent(Main0Activity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
