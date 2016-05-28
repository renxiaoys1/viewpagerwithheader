/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.pinnedheaderviewpager.headtab;

import com.baidu.pinnedheaderviewpager.R;
import com.baidu.pinnedheaderviewpager.tabwidget.CurrentTabColorizer;
import com.baidu.pinnedheaderviewpager.tabwidget.TabColorizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

public class SlidingTabContainer extends LinearLayout {
    private static final int DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 0;
    private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 0x26;
    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 3;
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF3385E5;
    private static final int DEFAULT_INDICATOR_PADDING = 40;

    private int mBottomBorderThickness;
    private Paint mBottomBorderPaint;

    private int mSelectedIndictorThickness;
    private Paint mSelectedIndicatorPaint;


    private int mDefaultBottomBorderColor;

    private int mSelectedPosition;

    private float mSelectionOffset;

    private TabColorizer mCustomTabColorizer;
    private CurrentTabColorizer mDefaultTabColorizer;

    public SlidingTabContainer(Context context) {
        this(context, null);
    }

    public SlidingTabContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabContainer(Context context, AttributeSet attrs, int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        setWillNotDraw(false);
        final float density = getResources().getDisplayMetrics().density;
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, outValue, true);
        int themeForGroundColor = outValue.data;

        mDefaultBottomBorderColor = setColorAlpha(themeForGroundColor, DEFAULT_BOTTOM_BORDER_COLOR_ALPHA);
        mDefaultTabColorizer = new CurrentTabColorizer();
        mDefaultTabColorizer.setIndicatorColors(DEFAULT_SELECTED_INDICATOR_COLOR);

        mBottomBorderThickness = (int) (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density);
        mBottomBorderPaint = new Paint();
        mBottomBorderPaint.setColor(Color.BLUE);

        mSelectedIndictorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
        mSelectedIndicatorPaint = new Paint();
    }


    /**
     * 参数设置
     * */
    void setCustomTabColorizer(TabColorizer tabColorizer) {
        mCustomTabColorizer = tabColorizer;
        invalidate();
    }

    void setDefaultSelectedIndicatorColor(int... colors) {
        mCustomTabColorizer = null;
        mDefaultTabColorizer.setIndicatorColors(colors);
        invalidate();
    }

    void setSelectedIndicatorThicknessDips(int thickness, boolean dpUnit) {
        if (dpUnit) {
            float density = getResources().getDisplayMetrics().density;
            mBottomBorderThickness = (int) density * thickness;
        } else {
            mBottomBorderThickness = thickness;
        }

        invalidate();
    }


    void setInitParams(TabColorizer tabColorizer, int bottomBorderThickness, int indicatorThickness, boolean dpUnit,
                       int...
            colors ) {

        mCustomTabColorizer = tabColorizer;
        mDefaultTabColorizer.setIndicatorColors(colors);
        if (dpUnit) {
            float density = getResources().getDisplayMetrics().density;
            mBottomBorderThickness = (int) density * indicatorThickness;
        } else {
            mBottomBorderThickness = indicatorThickness;
        }
        invalidate();
    }


    /**
     * viewpage切换时，需要重绘
     *
     * */
    void onViewPagerChange(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int childCount = getChildCount();
        TabColorizer tabColorizer = mCustomTabColorizer != null ? mCustomTabColorizer : mDefaultTabColorizer;

        if (childCount > 0) {
            View selectedTitle = getChildAt(mSelectedPosition);
            int leftX = selectedTitle.getLeft();
            int rightX = selectedTitle.getRight();
            int color = tabColorizer.getIndicatorColor(mSelectedPosition);

            if (mSelectionOffset > 0f && mSelectedPosition < (childCount - 1)) {
                int nextColor = tabColorizer.getIndicatorColor(mSelectedPosition);
                View nextTitle = getChildAt( mSelectedPosition + 1);
                leftX = getIndicatorViewWidth(nextTitle.getLeft(), leftX);
                rightX = getIndicatorViewWidth(nextTitle.getRight(), rightX);
            }

            mSelectedIndicatorPaint.setColor(Color.RED);

            canvas.drawRect(leftX + DEFAULT_INDICATOR_PADDING, height - mSelectedIndictorThickness, rightX, height,
                    mSelectedIndicatorPaint);
        }

        canvas.drawRect(0, height - mBottomBorderThickness, getWidth(), height, mBottomBorderPaint);

    }



    /**
     * 获取header的选中项的左右坐标点
     *
     * */
    private int getIndicatorViewWidth(int sourceWidth, int leftWidth) {
        int result = 0;

        result = (int) (mSelectionOffset * sourceWidth + (1.0f - mSelectionOffset) * leftWidth);
        return result;

    }


    private static int setColorAlpha(int color1, byte alpha) {
        return Color.argb(alpha, Color.red(color1), Color.green(color1), Color.blue(color1));
    }



}
