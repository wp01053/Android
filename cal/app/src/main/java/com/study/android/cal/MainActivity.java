package com.study.android.cal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "lecture";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void Btnadd(View v){
        EditText num1 = findViewById(R.id.num1);
        EditText num2 = findViewById(R.id.num2);

        Button add = findViewById(R.id.add);

        TextView rel = findViewById(R.id.rel);

        int n1 = Integer.parseInt(num1.getText().toString());
        int n2 = Integer.parseInt(num2.getText().toString());

        rel.setText(n1+"+"+n2+" =  "+Integer.toString(n1+n2));
    }

    public void Btnsub(View v){

        EditText num1 = findViewById(R.id.num1);
        EditText num2 = findViewById(R.id.num2);

        Button sub = findViewById(R.id.sub);

        TextView rel = findViewById(R.id.rel);

        int n1 = Integer.parseInt(num1.getText().toString());
        int n2 = Integer.parseInt(num2.getText().toString());

        rel.setText(n1+"-"+n2+" =  "+Integer.toString(n1-n2));

    }

    public void Btnmul(View v){

        EditText num1 = findViewById(R.id.num1);
        EditText num2 = findViewById(R.id.num2);

        Button mul = findViewById(R.id.mul);

        TextView rel = findViewById(R.id.rel);

        int n1 = Integer.parseInt(num1.getText().toString());
        int n2 = Integer.parseInt(num2.getText().toString());

        rel.setText(n1+"*"+n2+" =  "+Integer.toString(n1*n2));

    }

    public void Btndiv(View v){


        EditText num1 = findViewById(R.id.num1);
        EditText num2 = findViewById(R.id.num2);

        Button div = findViewById(R.id.div);

        TextView rel = findViewById(R.id.rel);

        int n1 = Integer.parseInt(num1.getText().toString());
        int n2 = Integer.parseInt(num2.getText().toString());

        rel.setText(n1+"/"+n2+" =  "+Integer.toString(n1/n2));
    }


}
