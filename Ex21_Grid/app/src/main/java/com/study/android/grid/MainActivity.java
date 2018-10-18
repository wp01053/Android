package com.study.android.grid;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private static final  String TAG = "lecture";

    String[] names ={"홍길동5","홍길동4","홍길동3","홍길동2","홍길동1"};
    String[] ages ={"20","25","55","45","22"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MyAdapter adapter = new MyAdapter();


        GridView gridView1 = findViewById(R.id.gridView1);
        gridView1.setAdapter(adapter);
        gridView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int row = position /2;
                int colum = position %2;

                Log.d(TAG, "Row index : " + row + "Column index : " + colum);
                Log.d(TAG, names[row * 2 + colum]);
            }
        });
    }

    class MyAdapter extends BaseAdapter{


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
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setOrientation(LinearLayout.VERTICAL);

            TextView view = new TextView(getApplicationContext());
            view.setText(names[position]);
            view.setTextSize(40.0f);
            view.setTextColor(Color.BLUE);

            layout.addView(view);

            TextView view1 = new TextView(getApplicationContext());
            view1.setText(ages[position]);
            view1.setTextSize(40.0f);
            view1.setTextColor(Color.RED);

            layout.addView(view1);

            return layout;
        }
    }


}
