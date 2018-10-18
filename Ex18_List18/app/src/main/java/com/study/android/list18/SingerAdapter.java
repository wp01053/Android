package com.study.android.list6;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.study.android.list18.R;
import com.study.android.list18.SingerItem;
import com.study.android.list18.SingerItemView;

import java.util.ArrayList;

public class SingerAdapter extends BaseAdapter {
    private static final String TAG = "lecture";

    Context context;
    ArrayList<SingerItem> items = new ArrayList<>();

    public SingerAdapter(Context context) {
        this.context = context;
    }

    public void addItem(SingerItem item) {items.add(item);}

    @Override
    public int getCount() {return  items.size(); }

    @Override
    public Object getItem(int position) {
        return  items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View converView, ViewGroup parent) {

        SingerItemView view = null;
        if(converView == null) {
            view = new SingerItemView(context);
        }else {
            view = (SingerItemView) converView;
        }

        final SingerItem item = items.get(position);
        view.setName(item.getName());
        view.setTel(item.getTelnum());
        view.setImage(item.getResId());
//        *****************************************************
//        리스트 뷰안의 버튼 클릭 이벤트 처리
        Button button1 = view.findViewById(R.id.button1);
        button1.setFocusable(false);
        button1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String str = "tel:" + item.getTelnum();

                Log.d(TAG,str);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(str));
                context.startActivity(intent);
            }
        });
//        *****************************************************
        return  view;
    }
}