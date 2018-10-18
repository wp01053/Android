package com.study.android.list3;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "lecture";
    String[] names = {"이건후", "한동인", "정재엽"};
    String[] names2 = {"여자", "여자2" ,"여자3"};

    String[] ages = {"1999년생", "1999년생", "1999년생"};
    String[] ages2 = {"2000년생", "2000년생", "2000년생"};

    String[] loc = {"서울시 동작구 상도로", "서울시 동작구 상도로", "서울시 동작구 상도로"};
    String[] loc2 = {"서울시 동작구 ", "서울시 동작구 ", "서울시 동작구 "};

    String[] tel = {"010-1111-1111", "010-2222-2222", "010-3333-3333"};

    int[] images = {R.drawable.face1, R.drawable.face2, R.drawable.face3};
    int[] images2 ={R.drawable.face3, R.drawable.face2, R.drawable.face1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        ArrayAdapter<String> adapter;
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);


        final MyAdapter adapter = new MyAdapter();
        ListView listView1 = (ListView) findViewById(R.id.listView1);
        listView1.setAdapter(adapter);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Toast.makeText(getApplicationContext(), "selected : " + tel[position], Toast.LENGTH_SHORT).show();

            }
        });
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return names[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            TextView view1  = new TextView(getApplicationContext());
//            view1.setText(names[position]);
//            view1.setTextSize(40.0f);
//            view1.setTextColor(Color.BLUE);
//
//            LinearLayout layout = new LinearLayout(getApplicationContext());
//            layout.setOrientation(LinearLayout.VERTICAL);
//
//            layout.addView(view1);
//
//            TextView view2 = new TextView(getApplicationContext());
//            view2.setText(ages[position]);
//            view2.setTextSize(40.0f);
//            view2.setTextColor(Color.RED);
//            layout.addView(view2);
//            return layout;
            SingerItemView view = new SingerItemView(getApplicationContext());
            view.setName(names[position]);
            view.setName2(names2[position]);

            view.setloc(loc[position]);
            view.setloc2(loc2[position]);

            view.setAge(ages[position]);
            view.setAge2(ages2[position]);

            view.setImage(images[position]);
            view.setImage2(images2[position]);
            return view;
        }


    }
}
