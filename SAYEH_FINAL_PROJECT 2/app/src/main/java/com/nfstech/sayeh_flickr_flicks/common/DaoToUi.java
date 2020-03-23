package com.nfstech.sayeh_flickr_flicks.common;

import android.support.annotation.Nullable;

import com.nfstech.sayeh_flickr_flicks.dao.Photo;
import com.nfstech.sayeh_flickr_flicks.view.model.PhotoListModel;

public class DaoToUi {

    @Nullable
    public static PhotoListModel toUi(Photo photo) {
        PhotoListModel photoListModel = null;
        if (photo != null) {
            photoListModel = new PhotoListModel();
            photoListModel.setUrl(photo.getPhotoUrl());
            photoListModel.setId(photo.getPhotoId());
        }

        return photoListModel;
    }
}
