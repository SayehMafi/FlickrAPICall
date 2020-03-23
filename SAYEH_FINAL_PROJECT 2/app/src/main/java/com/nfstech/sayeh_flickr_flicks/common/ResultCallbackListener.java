package com.nfstech.sayeh_flickr_flicks.common;

/*
 * Common request/response listener.
 */
public interface ResultCallbackListener<R, E> {
    void onSuccess(R response);

    void onFailure(E error);
}
