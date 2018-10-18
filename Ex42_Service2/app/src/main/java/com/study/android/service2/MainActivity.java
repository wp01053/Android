package com.study.android.service2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "lecture";

    AlarmManager am;
    Intent intent;
    PendingIntent receiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "111111111111");

        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        intent = new Intent(this, AlarmReceiver.class);
        receiver = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d(TAG, "1111122222222222");

    }

    public void btn1(View v) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        Log.d(TAG, "22222222222");
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), receiver);
        Log.d(TAG, "33333333333333333");

    }

    public void btn2(View v) {
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 10 * 1000, receiver);
        Log.d(TAG, "4444444444444444");

    }

    public void btn3(View v) {
        Log.d(TAG, "55555555555555555");
        am.cancel(receiver);
    }


}
