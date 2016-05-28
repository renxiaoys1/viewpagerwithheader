/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.pinnedheaderviewpager.tabwidget;

public class CurrentTabColorizer implements TabColorizer {
    private int[] mIndicatorColors;
    @Override
    public final int getIndicatorColor(int position) {
        return mIndicatorColors[position % mIndicatorColors.length];
    }

    public void setIndicatorColors(int... colors) {
        mIndicatorColors = colors;
    }
}
