package com.study.android.lifecycle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class NewActivity extends AppCompatActivity {
    String sName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        Intent intent = getIntent();
        sName = intent.getStringExtra("CustomerName");
        Toast.makeText(getApplicationContext(), "oncreate()호출됨", Toast.LENGTH_SHORT ).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "onStart()호출됨", Toast.LENGTH_SHORT ).show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "onResume()호출됨", Toast.LENGTH_SHORT ).show();

    }
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "onPause()호출됨", Toast.LENGTH_SHORT ).show();

    }
    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(getApplicationContext(), "onStop()호출됨", Toast.LENGTH_SHORT ).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "onDestroy()호출됨", Toast.LENGTH_SHORT ).show();

    }



}
