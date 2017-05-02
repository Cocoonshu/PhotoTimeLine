package com.cocoonshu.example.phototimeline;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.HandlerThread;

import com.cocoonshu.example.phototimeline.config.Config;
import com.cocoonshu.example.phototimeline.data.DataManager;
import com.cocoonshu.example.phototimeline.loader.ThreadPool;
import com.cocoonshu.example.phototimeline.utils.Debugger;

import java.util.concurrent.ExecutorService;

/**
 * @Author Cocoonshu
 * @Date   2017-04-13
 */
public class GalleryApplication extends Application {
    private static final String TAG = "GalleryApplication";

    private DataManager mDataManager       = null;
    private ThreadPool  mThreadPool        = null;
    private Config      mApplicationConfig = null;

    @Override
    protected void attachBaseContext(Context base) {
        Debugger.i(TAG, "[attachBaseContext]");
        super.attachBaseContext(base);
        mApplicationConfig = new Config(this);
    }

    @Override
    public void onCreate() {
        Debugger.i(TAG, "[onCreate]");
        super.onCreate();

        mThreadPool = new ThreadPool();
        mThreadPool.setup();

        mDataManager = new DataManager(this);
        mDataManager.setup();
    }

    @Override
    public void onTerminate() {
        Debugger.i(TAG, "[onTerminate]");
        super.onTerminate();

        mThreadPool.terminal();
        mDataManager.terminal();
    }

    /********************************
     * Application level interfaces *
     ********************************/

    public ThreadPool getThreadPool() {
        return mThreadPool;
    }

    public ExecutorService getWorkExecutor() {
        return mThreadPool.getWorkExecutor();
    }

    public HandlerThread getSerialReloadHandlerThread() {
        return mThreadPool.getSerialReloadHandlerThread();
    }

    public ExecutorService getBitmapExecutor() {
        return null;
    }

    public Config getConfig() {
        return mApplicationConfig;
    }

    public Config getThemedConfig(Activity activity) {
        if (!mApplicationConfig.isSame(activity)) {
            mApplicationConfig.updateActivityConfig(activity);
        }
        return mApplicationConfig;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }
}
