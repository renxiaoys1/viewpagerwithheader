/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.headerviewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FirstScrollViewFragment extends ScrollViewFragment {

    public static final String TAG = FirstScrollViewFragment.class.getSimpleName();

    public static Fragment newInstance(int position) {
        FirstScrollViewFragment fragment = new FirstScrollViewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public FirstScrollViewFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPosition = getArguments().getInt(ARG_POSITION);

        View view = inflater.inflate(R.layout.fragment_first_scroll_view, container, false);
        mScrollView = (NotifyingScrollView) view.findViewById(R.id.scrollview);
        setScrollViewOnScrollListener();
        return view;
    }
}

