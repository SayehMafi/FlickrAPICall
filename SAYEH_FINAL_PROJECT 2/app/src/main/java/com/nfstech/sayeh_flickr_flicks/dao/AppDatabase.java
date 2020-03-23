package com.nfstech.sayeh_flickr_flicks.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {Photo.class}, version = 1,exportSchema=false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PhotoDao photoDao();
}
