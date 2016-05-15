/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.headerviewpager;

import android.support.v7.widget.RecyclerView;

public abstract class RecyclerViewFragment extends ScrollTabHolderFragment {

    protected RecyclerView mRecyclerView;
    protected int mScrollY;

    protected abstract void setScrollOnLayoutManager(int scrollY);

    protected void setRecyclerViewOnScrollListener() {
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                mScrollY += dy;

                if (mScrollTabHolder != null) {
                    mScrollTabHolder.onRecyclerViewScroll(recyclerView, dx, dy, mScrollY, mPosition);
                }
            }
        });
    }

    @Override
    public void adjustScroll(int scrollHeight, int headerHeight) {
        if (mRecyclerView == null) return;

        mScrollY = headerHeight - scrollHeight;
        setScrollOnLayoutManager(mScrollY);
    }
}
