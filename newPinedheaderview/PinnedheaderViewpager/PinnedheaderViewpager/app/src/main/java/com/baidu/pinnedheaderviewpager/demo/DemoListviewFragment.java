/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.pinnedheaderviewpager.demo;

import com.baidu.pinnedheaderviewpager.R;
import com.baidu.pinnedheaderviewpager.fragmentcontainer.ListviewFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ArrayAdapter;

/**
 * listviewfragment的实例
 *
 * */
public class DemoListviewFragment extends ListviewFragment {

    public DemoListviewFragment() {
    }


    public static Fragment newInstance(int position) {
        ListviewFragment fragment = new DemoListviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 设置基础距离参数
     *
     * headview的高度
     *
     * tabview的高度
     *
     * 设置listview的headview
     *
     * */
    @Override
    public void setConfiguration() {
        super.setConfiguration();

        int headerHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mSlidingTabHeight = getResources().getDimensionPixelSize(R.dimen.sliding_tab_height);
        setListViewHeader(headerHeight);
        setAdapter();
    }


    @Override
    public void setAdapter() {
        int resourId = R.array.countName;

        switch (mPosition) {
            case 0:
                resourId = R.array.treeName;
                break;
            case 1:
                resourId = R.array.fruitName;
                break;
            case 2:
                resourId = R.array.fruitName;
                break;
            case 3:
                resourId = R.array.countName;
                break;
        }



        /***
         * 根据业务需求进行item样式的修改
         *
         * */
        String[] nameArrays = getResources().getStringArray(resourId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,
                nameArrays);
        mListView.setAdapter(adapter);
    }



    /**
     * fragment的布局文件
     */
    @Override
    public int getLayoutId() {
        return R.layout.listviewfragment;
    }

    /**
     * listview的布局id
     */
    @Override
    public int getListViewId() {
        return R.id.listview;
    }


    @Override
    public void outValue() {
        Log.e("outvalue", "demolistvalue");
    }


}
