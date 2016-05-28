/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.pinnedheaderviewpager.fragmentcontainer;

import com.baidu.pinnedheaderviewpager.scrolldelegate.ListViewScrollDelegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

public abstract class ListviewFragment extends Fragment implements ListViewScrollDelegate {

    protected static final String ARG_POSITION = "position";
    protected ListViewScrollDelegate mScrollDelegate;
    protected int mPosition;
    protected ListView mListView;
    protected int mSlidingTabHeight;
    protected boolean mForbiddenSccroll = false;

    /**
     * fragment的布局文件
     */
    public abstract int getLayoutId();

    /**
     * listview的布局id
     */
    public abstract int getListViewId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);
        outValue();
        View layoutView = inflater.inflate(getLayoutId(), container, false);
        mListView = (ListView) layoutView.findViewById(getListViewId());
        setConfiguration();


        return layoutView;
    }

    public void setConfiguration() {
        setListViewOnScrollListener();
    }

    /**
     * 设置listview的adapter
     */
    public void setAdapter() {

    }




    public void outValue() {
        Log.e("outvalue", "listvalue");
    }


    /**
     * 添加吸顶效果的占位headerview
     * 注释：一定要放在listview设置adapter之前
     */
    public void setListViewHeader(int headerHeight) {
        View view = new FrameLayout(getContext());

        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.height = headerHeight;
        view.setLayoutParams(lp);
        mListView.addHeaderView(view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mScrollDelegate = (ListViewScrollDelegate) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implemenent ListViewScrollDelegate");
        }
    }

    @Override
    public void onDetach() {
        mScrollDelegate = null;
        super.onDetach();
    }

    /**
     * 这个方法有点取巧：每次veiwpage切换时都会将将fragment中的listview展示index为1的item
     *
     * @param scrollHeight : headerview与headverview滑动的距离之差
     *
     * @param headerHeight : headerview的高度
     * */
    @Override
    public void adjustScroll(int scrollHeight, int headerHeight) {
        if (null == mListView) {
            return;
        }

        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
            return;
        }

         mListView.setSelectionFromTop(1, scrollHeight);
    }


    @Override
    public void adjustListview(int scrollHeight, int headerHeight) {
        if (null == mListView) {
            return;
        }


    }

    @Override
    public void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount,
                                 int pagePosition) {
    }

    protected void setListViewOnScrollListener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mForbiddenSccroll) {
                    mForbiddenSccroll = false;
                    return;
                }

                if (null != mScrollDelegate) {
                    mScrollDelegate
                            .onListViewScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
                }

            }
        });
    }

}
