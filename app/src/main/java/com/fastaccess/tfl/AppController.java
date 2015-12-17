package com.fastaccess.tfl;


import com.activeandroid.app.Application;
import com.fastaccess.tfl.apps.IconCache;
import com.fastaccess.tfl.helper.TypeFaceHelper;

/**
 * Created by Kosh on 17/12/15 6:55 PM
 */
public class AppController extends Application {
    private static AppController app;
    private static IconCache iconCache;

    @Override public void onCreate() {
        super.onCreate();
        app = this;
        TypeFaceHelper.generateTypeface(this);
    }

    public synchronized static AppController getApp() {
        return app;
    }

    public IconCache getIconCache() {
        if (iconCache == null) iconCache = new IconCache(this);
        return iconCache;
    }
}
