package com.nfstech.sayeh_flickr_flicks.common;

import android.app.Application;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Uncomment below line if you want to use Stetho (https://github.com/facebook/stetho)
        //Stetho.initializeWithDefaults(this);
    }
}
