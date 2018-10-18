package com.study.android.list3;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SingerItemView extends LinearLayout {


    TextView textView1;
    TextView textView2;
    TextView textView3;

    TextView textView4;
    TextView textView5;
    TextView textView6;

    ImageView imageView1;
    ImageView imageView2;

    public SingerItemView(Context context){
        super(context);

        LayoutInflater inflater =
                (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.singer_item_view, this, true);

        textView1 = findViewById(R.id.textView1);

        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);
        textView6 = findViewById(R.id.textView6);


        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
    }

    public void setName(String name) {
        textView1.setText(name);
    }
    public void setName2(String name2) {
        textView4.setText(name2);
    }


    public void setAge(String age) {
        textView2.setText(age);

    }

    public void setAge2(String age2) {
        textView5.setText(age2);

    }
    public void setloc(String loc){
        textView3.setText(loc);

    }

    public void setloc2(String loc2){

        textView6.setText(loc2);
    }

    public void setImage(int imgNum) {
        imageView1.setImageResource(imgNum);
    }

    public void setImage2(int imgNum2) {
        imageView2.setImageResource(imgNum2);
    }



}
