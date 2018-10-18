package com.study.android.intorex;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class IntroActivity extends AppCompatActivity {
    private static final String TAG = "lecture";

    private static final int STOPSPLASH = 0;
    private static final long SPLASHTIME =2000;

    private Handler splashHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Intent intent;

            switch (msg.what){
                case STOPSPLASH:
                    intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fadein, R.anim.hold);
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    @Override
    protected void onResume(){
        super.onResume();

        Message msg = new Message();
        msg.what = STOPSPLASH;
        splashHandler.sendMessageDelayed(msg, SPLASHTIME);
    }
}
