package com.study.android.ex05_edittext1;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "lecture";

    TextView textView1;
    EditText etId;
    EditText etPwd;
    EditText etYear;
    EditText etMonth;
    EditText etDay;

    String sId;
    String sPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.textView1);

        etId = findViewById(R.id.etId);
        etPwd = findViewById(R.id.etPwd);
        etYear = findViewById(R.id.etYear);
        etMonth = findViewById(R.id.etMonth);
        etDay = findViewById(R.id.etDay);

        etPwd.addTextChangedListener(watcher);

    }


    public void onKeydownCLicked(View v){

        InputMethodManager mgr =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(v.getWindowToken(),0);
    }

    public void onLoginClicked(View v){
        sId =etId.getText().toString();
        sPwd = etPwd.getText().toString();

        if(sId.length() < 3){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("알림")
                    .setMessage("아이디를 입력해 주세요")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("닫기" ,null)
                    .show();
            etId.requestFocus();
            return;

        }else if(sPwd.length() < 5){
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("알림")
                    .setMessage("비밀번호를 정확히 입력해 주세요")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("닫기" ,null)
                    .show();
            etPwd.requestFocus();
            return;
        }
    }

    TextWatcher watcher = new TextWatcher() {
        String beforeText;

        @Override
        public void onTextChanged(CharSequence str, int start, int before, int count) {
            byte[] bytes = null;
            try {
                bytes = str.toString().getBytes("8859_1");
                int strCount = bytes.length;
                textView1.setText(strCount + " / 8 바이트");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }

        @Override
        public void beforeTextChanged(CharSequence str, int start, int count, int after) {
            beforeText = str.toString();
            Log.d(TAG, "beforeTextChanged : " + beforeText);

        }

        @Override
        public void afterTextChanged(Editable strEditable) {
            String str = strEditable.toString();
            Log.d(TAG, "afterTextChange : " + str);
            try {
                byte[] strBytes = str.getBytes("8859_1");
                if (strBytes.length > 8) {
                    etPwd.setText(beforeText);
                    etPwd.setSelection(beforeText.length() - 1, beforeText.length() - 1);

                }

            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
    } ;

}
