package com.dreamaz.gridviewcalendartest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnShow;
    Date selectedDate;
    TextView tvSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShow = findViewById(R.id.btn_show);
        tvSelectedDate = findViewById(R.id.tv_selected_date);

        btnShow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                MyGridViewCalendar myGridViewCalendar = new MyGridViewCalendar();
                myGridViewCalendar.setSelectedDate(new Date());
                myGridViewCalendar.show(getSupportFragmentManager(), "grid_view_calendar");
            }
        });
    }
}
