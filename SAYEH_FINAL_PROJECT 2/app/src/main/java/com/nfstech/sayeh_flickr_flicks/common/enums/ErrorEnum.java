package com.nfstech.sayeh_flickr_flicks.common.enums;

import android.support.annotation.StringRes;

import com.nfstech.sayeh_flickr_flicks.R;

public enum ErrorEnum {
    UNABLE_TO_FETCH_DATA(R.string.error_unable_fetch_data),
    INVALID_DATA(R.string.error_invalid_data);

    @StringRes
    int mErrorResource;

    ErrorEnum(int errorResource) {
        this.mErrorResource = errorResource;
    }

    @StringRes
    public int getErrorResource() {
        return this.mErrorResource;
    }
}
