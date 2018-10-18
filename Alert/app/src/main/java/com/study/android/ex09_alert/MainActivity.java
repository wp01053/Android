package com.study.android.ex09_alert;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "lecture";

    public static  CustomProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void onBtn1(View v){
        MyUtil.AlertShow(MainActivity.this, "아이디를 입력해 주세요", "알림");
    }


    public void onBtn2(View v){
        MyUtil.AlertShow(MainActivity.this, "아이디를 입력해 주세요");
    }


    public void onBtn3(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setMessage("앱을 종료하시겠습니까?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("알림")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "ID value is" + Integer.toString(id)
                        , Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getApplicationContext(), "ID value is" + Integer.toString(id)
                                , Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });


    AlertDialog alert = builder.create();
    alert.show();
}
public  void onBtn4Clicked(View v){
        if(progressDialog==null || !progressDialog.isShowing()){
            progressDialog = CustomProgressDialog.show(MainActivity.this,"", "처리중입니다." ,true, true, null);
        }
}



}
