package com.nfstech.sayeh_flickr_flicks.common;

import android.support.annotation.Nullable;

import com.nfstech.sayeh_flickr_flicks.dao.Photo;
import com.nfstech.sayeh_flickr_flicks.responsemodel.PhotoResponseModel;

public class ResponseToDao {

    @Nullable
    public static Photo toDao(PhotoResponseModel photoResponseModel) {
        Photo photo = null;
        if (photoResponseModel != null) {
            photo = new Photo(photoResponseModel.getId(), getFlickrPhotoUrl(photoResponseModel));
        }

        return photo;
    }

    private static String getFlickrPhotoUrl(PhotoResponseModel photoResponseModel) {
        int FARMID = photoResponseModel.getFarm();
        String SERVERID = photoResponseModel.getServer();
        String SECRET = photoResponseModel.getSecret();
        String ID = photoResponseModel.getId();

        return "https://farm" + FARMID + ".staticflickr.com/" +
                SERVERID + "/" + ID + "_" +
                SECRET + "_m" + ".jpg";
    }
}