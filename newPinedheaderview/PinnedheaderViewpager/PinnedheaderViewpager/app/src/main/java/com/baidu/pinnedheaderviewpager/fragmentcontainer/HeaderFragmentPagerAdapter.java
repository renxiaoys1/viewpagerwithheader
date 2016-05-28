/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.pinnedheaderviewpager.fragmentcontainer;

import com.baidu.pinnedheaderviewpager.scrolldelegate.ListViewScrollDelegate;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.view.ViewGroup;

public abstract class HeaderFragmentPagerAdapter extends FragmentPagerAdapter {
    private SparseArrayCompat<ListViewScrollDelegate> mScrollDelegate;
    private int mFragmentCount;

    public HeaderFragmentPagerAdapter(FragmentManager fm, int num) {
        super(fm);
        mScrollDelegate = new SparseArrayCompat<>();
        mFragmentCount = num;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        mScrollDelegate.put(position, (ListViewScrollDelegate) object);
        return object;
    }

    @Override
    public int getCount() {
        return mFragmentCount;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public SparseArrayCompat<ListViewScrollDelegate> getScrollDelegates() {
        return mScrollDelegate;
    }



}
