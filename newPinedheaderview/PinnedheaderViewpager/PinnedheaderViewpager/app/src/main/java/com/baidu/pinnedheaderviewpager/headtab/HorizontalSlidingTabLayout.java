/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.pinnedheaderviewpager.headtab;

import com.baidu.pinnedheaderviewpager.R;
import com.baidu.pinnedheaderviewpager.tabwidget.TabColorizer;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HorizontalSlidingTabLayout extends HorizontalScrollView {

    private static final int TITLE_OFFSET_DIPS = 24;
    private static final int TAB_VIEW_PADDING_DIPS = 16;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 12;
    private static final int DEFAULT_SELECTED_INDICATOR_COLOR = 0xFF33B5E5;
    private static final int DEFAULT_TABVIEW_BG = R.color.color_ffffc5c2;

    private int mTitleOffset;
    private boolean mShoulDistributeEvent = true;
    private int mSelectedDefaultIndicatorColor;
    private int mTabviewPadding = TAB_VIEW_PADDING_DIPS;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mViewPagerChangeListener;

    public SlidingTabContainer mTabContainer;
    public int mTextSizeSp = TAB_VIEW_TEXT_SIZE_SP;
    public int mTextBackgroundResourceId = DEFAULT_TABVIEW_BG;

    public HorizontalSlidingTabLayout(Context context) {
        this(context, null);
    }

    public HorizontalSlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalSlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);
        mTitleOffset = (int) (TITLE_OFFSET_DIPS * getResources().getDisplayMetrics().density);
        mTabContainer = new SlidingTabContainer(context);
        mTabContainer.setGravity(Gravity.CENTER);
        addView(mTabContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mSelectedDefaultIndicatorColor = DEFAULT_SELECTED_INDICATOR_COLOR;
        mTabContainer.setDefaultSelectedIndicatorColor(mSelectedDefaultIndicatorColor);
    }

    public void setCustomTabColorizer(TabColorizer tabColorizer) {
        mTabContainer.setCustomTabColorizer(tabColorizer);
    }

    public void setShoulDistributeEvent(boolean distributeEvently) {
        mShoulDistributeEvent = distributeEvently;
    }

    public void setSelectedIndicatorColors(int... colors) {
        mTabContainer.setDefaultSelectedIndicatorColor(colors);
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerChangeListener = listener;
    }

    public void setmViewPager(ViewPager viewPager) {
        mTabContainer.removeAllViews();
        mViewPager = viewPager;
        if (null != viewPager) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            constructTabContainerView();
        }
    }

    public void setTabViewAttributes(int padding, int colorId, int textSize) {
        mTextBackgroundResourceId = colorId;
        mTextSizeSp = textSize;
        mTabviewPadding = padding;
    }

    /**
     * 生成tab的容器
     */
    protected TextView createSingleTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTextSizeSp);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        textView.setBackgroundResource(outValue.resourceId);


       // textView.setBackgroundResource(mTextBackgroundResourceId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            textView.setAllCaps(true);
        }

        int padding = (int) (mTabviewPadding * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);

        return textView;
    }

    /**
     *
     * */
    private void constructTabContainerView() {
        PagerAdapter adapter = mViewPager.getAdapter();
        View.OnClickListener tabCickListener = new TabClickListener();
        for (int i = 0; i < adapter.getCount(); i++) {
            View tabView = null;
            TextView tabTitleView = null;

            tabView = createSingleTabView(getContext());

            if (mShoulDistributeEvent) {
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tabView.getLayoutParams();
                lp.width = 0;
                lp.weight = 1;
            }

            if (TextView.class.isInstance(tabView)) {
                tabTitleView = (TextView) tabView;
            }

            tabTitleView.setText(adapter.getPageTitle(i));

            final TextView nextText = tabTitleView;

            tabTitleView.post(new Runnable() {
                @Override
                public void run() {
                    int height = nextText.getHeight();
                    int width = nextText.getWidth();

                    Log.e("value", "vaule :" + height + ", width:" + width);

                }
            });

            tabView.setOnClickListener(tabCickListener);
            mTabContainer.addView(tabView);
            if (i == mViewPager.getCurrentItem()) {
                tabView.setSelected(true);
            }

        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (null != mViewPager) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
        int tabChildCount = mTabContainer.getChildCount();
        if (0 == tabChildCount || tabIndex < 0 || tabIndex >= tabChildCount) {
            return;
        }

        View selectedChild = mTabContainer.getChildAt(tabIndex);
        if (null != selectedChild) {
            int targetScrollX = selectedChild.getLeft() + positionOffset;
            if (tabIndex > 0 || positionOffset > 0) {
                targetScrollX -= mTitleOffset;
            }
            scrollTo(targetScrollX, 0);
        }

    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {
        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabContainerChildCount = mTabContainer.getChildCount();
            if (0 == tabContainerChildCount || position >= tabContainerChildCount) {
                return;
            }

            mTabContainer.onViewPagerChange(position, positionOffset);
            View selectedTitle = mTabContainer.getChildAt(position);
            int extraOffset = (selectedTitle != null) ? (int) (positionOffset * selectedTitle.getWidth()) : 0;
            scrollToTab(position, extraOffset);
            if (null != mViewPagerChangeListener) {
                mViewPagerChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
            if (null != mViewPagerChangeListener) {
                mViewPagerChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (ViewPager.SCROLL_STATE_IDLE != mScrollState) {
                mTabContainer.onViewPagerChange(position, 0f);
                scrollToTab(position, 0);
            }
        }

    }

    private class TabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            for (int i = 0; i < mTabContainer.getChildCount(); i++) {
                if (view == mTabContainer.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                }
            }
        }
    }

}
