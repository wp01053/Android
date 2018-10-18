package com.study.android.ex06_edittext2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="lecture";

    EditText inputMessage;
    String strAmount = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputMessage = findViewById(R.id.etMessage);

        inputMessage.addTextChangedListener(watcher);
    }


    TextWatcher watcher = new TextWatcher() {
        String beforeText;

        @Override
        public void onTextChanged(CharSequence str, int start, int before, int count) {
            Log.d(TAG, str.toString() + ":" + strAmount);
            if(!str.toString().equals(strAmount)){
                strAmount = makeStringComma(str.toString().replace(",",""));
                inputMessage.setText(strAmount);
                inputMessage.setSelection(inputMessage.getText().length(),
                        inputMessage.getText().length());
            }
        }

        @Override
        public void beforeTextChanged(CharSequence str, int start, int count, int after) {

            strAmount = str.toString();
        }

        @Override
        public void afterTextChanged(Editable strEditable) {

        }
    } ;

    private String makeStringComma(String str) {
        if(str.length() == 0)
            return "";
        long value = Long.parseLong(str);
        DecimalFormat format = new DecimalFormat("###,##0");
        return format.format(value);
    }


}
