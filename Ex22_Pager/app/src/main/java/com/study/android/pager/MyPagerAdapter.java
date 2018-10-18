package com.study.android.pager;

import android.view.View;
import android.view.ViewGroup;

interface MyPagerAdapter {
    int getCount();

    boolean isViewFromObject(View view, Object obj);

    void destoryItem(ViewGroup container, int position, Object object);
}
