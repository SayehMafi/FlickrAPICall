package com.nfstech.sayeh_flickr_flicks.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PhotoDao {

    //This query fetches the most recently inserted rows in the db
    @Query("Select * from Photo limit 50 Offset (Select Count(*) from Photo) - 50")
    LiveData<List<Photo>> getLastAddedPhotos();

    @Query("Select * from Photo limit :startIndex,:rangeCount")
    List<Photo> getPhotosInRange(int startIndex, int rangeCount);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    List<Long> insertPhoto(List<Photo> photos);

    @Query("Delete from Photo")
    void deleteAllPhotos();
}
