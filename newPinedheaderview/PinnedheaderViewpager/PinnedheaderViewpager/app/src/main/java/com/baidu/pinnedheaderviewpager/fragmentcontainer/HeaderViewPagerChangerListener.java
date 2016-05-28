/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.pinnedheaderviewpager.fragmentcontainer;

import com.baidu.pinnedheaderviewpager.scrolldelegate.ListViewScrollDelegate;

import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

public class HeaderViewPagerChangerListener implements ViewPager.OnPageChangeListener {
    private static final String TAG = "HeaderViewPagerChangerListener";

    protected ViewPager mViewPager;
    protected View mHeaderView;
    protected int mFragmentNum;

    protected HeaderFragmentPagerAdapter mAdapter;

    public HeaderViewPagerChangerListener(ViewPager viewpager, HeaderFragmentPagerAdapter adapter, View headerView) {
        mViewPager = viewpager;
        mAdapter = adapter;
        mFragmentNum = mAdapter.getCount();
        mHeaderView = headerView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int currentItem = mViewPager.getCurrentItem();
        if (positionOffsetPixels > 0) {
            SparseArrayCompat<ListViewScrollDelegate> scrollDelegateArray = mAdapter.getScrollDelegates();
            ListViewScrollDelegate fragmentScrollDelegate;
            if (position < currentItem) {
                fragmentScrollDelegate = scrollDelegateArray.get(position);
            } else {
                fragmentScrollDelegate = scrollDelegateArray.get(position + 1);
            }

            fragmentScrollDelegate.adjustScroll((int) (mHeaderView.getHeight() + mHeaderView.getTranslationY()),
                    mHeaderView.getHeight());

        }
    }

    @Override
    public void onPageSelected(int position) {
        SparseArrayCompat<ListViewScrollDelegate> scrollDelegateArray = mAdapter.getScrollDelegates();

        if (null == scrollDelegateArray || scrollDelegateArray.size() != mFragmentNum) {
            return;
        }

        ListViewScrollDelegate fragmentScrollDelegate = scrollDelegateArray.get(position);

        fragmentScrollDelegate.adjustScroll((int) (mHeaderView.getHeight() + mHeaderView.getTranslationY()),
                mHeaderView.getHeight());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

        if (state == ViewPager.SCROLL_STATE_IDLE) {



        }
    }

}
