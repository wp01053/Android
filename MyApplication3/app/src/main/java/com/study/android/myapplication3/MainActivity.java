package com.study.android.myapplication3;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Messenger;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.SEND_SMS},1
            );
        }

        Messenger messenger = new Messenger(getApplicationContext());
        messenger.sendMessageTo("01035242947", "ttt");
    }

    class Messenger {
        private Context mContext;

        public Messenger(Context mContext) {
            this.mContext = mContext;
        }

        public void sendMessageTo(String phoneNum, String message) {

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNum, null, message, null, null);

            Toast.makeText(mContext, "Message completed", Toast.LENGTH_SHORT).show();
        }
    }
}
