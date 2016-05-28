/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.pinnedheaderviewpager;

import com.baidu.pinnedheaderviewpager.demo.DemoListviewFragment;
import com.baidu.pinnedheaderviewpager.fragmentcontainer.HeaderFragmentPagerAdapter;
import com.baidu.pinnedheaderviewpager.fragmentcontainer.HeaderViewPagerChangerListener;
import com.baidu.pinnedheaderviewpager.headtab.HorizontalSlidingTabLayout;
import com.baidu.pinnedheaderviewpager.scrolldelegate.ListViewScrollDelegate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AbsListView;

public class MainActivity extends FragmentActivity implements ListViewScrollDelegate {


    private String[] mFragmentNames;
    public static final String HEADETRANLATIONVALUEY = "header_translate_value_y";
    protected View mHeaderView;
    protected HorizontalSlidingTabLayout mTabContainer;
    protected ViewPagerAdapter mAdapter;
    private ViewPager mViewPager;


    protected int mHeadHeight;
    protected int mMinHeaderHeight;
    protected int mMinHeaderTranslation;
    protected int mNumFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFragmentNames = getResources().getStringArray(R.array.FragmentName);

        initViews();
        initValues();

        if (null != savedInstanceState) {
            mHeaderView.setTranslationY(savedInstanceState.getFloat(HEADETRANLATIONVALUEY));
        }
        setAdapter();
    }


    public void initViews() {
        mHeaderView = findViewById(R.id.view_header);
        mTabContainer = (HorizontalSlidingTabLayout) findViewById(R.id.tab_view);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    /**
     * 初始化viewpager
     * */
    protected void setAdapter() {
        if (null == mAdapter) {
            mAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mNumFragments);
        }

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mNumFragments);
        mTabContainer.setOnPageChangeListener(getViewPagerChangeListener());
        mTabContainer.setmViewPager(mViewPager);

    }


    /**
     * 设置headveiw以及tab的高度，以及headview最大的滑动距离
     *
     * */
    public void initValues() {
        int tabHeight = getResources().getDimensionPixelSize(R.dimen.sliding_tab_height);
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mHeadHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = tabHeight - mHeadHeight;

        mNumFragments = 4;
    }

    protected HeaderViewPagerChangerListener getViewPagerChangeListener() {
        return new HeaderViewPagerChangerListener(mViewPager, mAdapter, mHeaderView);
    }


    @Override
    public void adjustScroll(int scrollHeight, int headerHeight) {

    }


    @Override
    public void adjustListview(int scrollHeight, int headerHeight) {

    }

    /**
     * 监听listview的item情况
     *
     * @param view : listview
     *
     * @param firstVisibleItem ： listview第一个可见的item
     *
     * @param visibleItemCount ： listview可见的item总数
     *
     * @param totalItemCount ： listview的item总数
     *
     * @param pagePosition: viewpager的index
     * */
    @Override
    public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int
            pagePosition) {
        if (pagePosition == mViewPager.getCurrentItem()) {
            scrollHeader(getListViewScrollValue(view));
        }

    }


    /**
     * 计算headveiw应该滑动的距离
     *
     * 取listview与headerview距离的最小值
     * */
    protected void scrollHeader(int scrollY) {
      //   scrollY = Math.max(-(int)mHeaderView.getTranslationY(), scrollY);
        float translationY = Math.max(-scrollY, mMinHeaderTranslation);
        mHeaderView.setTranslationY(translationY);
    }


    /**
     * 计算listview滑动的总距离
     *
     * @param view : listview
     *
     * */
    public int getListViewScrollValue(AbsListView view) {
        View child = view.getChildAt(0);
        if (null == child) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = child.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeadHeight;
        }

        return firstVisiblePosition * child.getHeight() - top + headerHeight;
    }





    private class ViewPagerAdapter extends HeaderFragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm, int numFragments) {
            super(fm, numFragments);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            switch (position) {
                case 0:
                    fragment = DemoListviewFragment.newInstance(0);
                    break;
                case 1:
                    fragment = DemoListviewFragment.newInstance(1);
                    break;
                case 2:
                    fragment = DemoListviewFragment.newInstance(2);
                    break;
                case 3:
                    fragment = DemoListviewFragment.newInstance(3);
                    break;
                default:
                    throw new IllegalArgumentException("index out of max size, current postion:" + position);
            }

            return fragment;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mFragmentNames[0];
                case 1:
                    return mFragmentNames[1];
                case 2:
                    return mFragmentNames[2];
                case 3:
                    return mFragmentNames[3];
                default:
                    throw new IllegalArgumentException("current index out of max framgent size, current index:" +
                            position);

            }
        }

    }


}
