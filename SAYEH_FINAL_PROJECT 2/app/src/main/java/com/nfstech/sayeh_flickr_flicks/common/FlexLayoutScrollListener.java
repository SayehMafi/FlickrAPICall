package com.nfstech.sayeh_flickr_flicks.common;

import com.google.android.flexbox.FlexboxLayoutManager;

public abstract class FlexLayoutScrollListener extends PaginationScrollListener {

    public FlexLayoutScrollListener(FlexboxLayoutManager layoutManager) {
        super(layoutManager);
    }

    @Override
    public int getFirstVisibleItemPosition() {
        return ((FlexboxLayoutManager) mLayoutManager).findFirstVisibleItemPosition();
    }
}
