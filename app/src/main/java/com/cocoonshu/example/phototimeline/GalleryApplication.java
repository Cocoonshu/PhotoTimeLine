package com.cocoonshu.example.phototimeline;

import android.app.Activity;
import android.app.Application;
import android.media.MediaScannerConnection;

import com.cocoonshu.example.phototimeline.config.Config;
import com.cocoonshu.example.phototimeline.data.DataManager;

import java.util.concurrent.ExecutorService;

/**
 * @Author Cocoonshu
 * @Date   2017-04-13
 */
public class GalleryApplication extends Application {

    private DataManager mDataManager = null;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeResources();
    }

    private void initializeResources() {
        mDataManager = new DataManager(this);
    }

    /********************************
     * Application level interfaces *
     ********************************/

    private Config mApplicationConfig;

    public ExecutorService getWorkExecutor() {
        return null;
    }

    public ExecutorService getBitmapExecutor() {
        return null;
    }

    public Config getConfig() {
        return mApplicationConfig;
    }

    public Config getThemedConfig(Activity activity) {
        throw new UnsupportedOperationException();
    }

    public DataManager getDataManager() {
        return mDataManager;
    }
}
