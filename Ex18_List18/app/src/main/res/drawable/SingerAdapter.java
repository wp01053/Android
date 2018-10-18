package com.study.android.list5;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class SingerAdapter extends BaseAdapter {
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
//        SingerItemView view = new SingerItemView(getApplicationContext());
        SingerItemView view = null;
        if(converView == null) {
            view = new SingerItemView(context);
        }else {
            view = (SingerItemView) converView;
        }

        SingerItem item = items.get(position);
        view.setName(item.getName());
        view.setAge(item.getAge());
        view.setImage(item.getResId());

        return  view;
    }
}