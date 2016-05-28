/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.pinnedheaderviewpager.scrolldelegate;

import android.widget.AbsListView;

public interface ListViewScrollDelegate {
    void adjustScroll(int scrollHeight, int headerHeight);
    void onListViewScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
    void adjustListview(int scrollHeight, int headerHeight);
}
