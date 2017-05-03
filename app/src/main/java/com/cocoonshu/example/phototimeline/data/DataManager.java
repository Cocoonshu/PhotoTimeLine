package com.cocoonshu.example.phototimeline.data;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.cocoonshu.example.phototimeline.GalleryApplication;
import com.cocoonshu.example.phototimeline.utils.Debugger;

import java.util.HashMap;

/**
 * @Author Cocoonshu
 * @Date   2017-04-26
 */
public class DataManager {
    private static final String TAG = "DataManager";

    private final GalleryApplication           mGalleryApplication;
    private final Context                      mApplicationContext;
    private final HashMap<Uri, NotifierBroker> mNotifierMap;
    private final Handler                      mNotifyDataChangeHandler;
    private final BucketManager                mBucketManager;

    public DataManager(GalleryApplication application) {
        mGalleryApplication      = application;
        mApplicationContext      = application.getApplicationContext();
        mBucketManager           = new BucketManager(application);
        mNotifierMap             = new HashMap<>();
        mNotifyDataChangeHandler = new Handler(application.getSerialReloadHandlerThread().getLooper());
    }

    public void setup() {
        Debugger.i(TAG, "[setup]");
    }

    public void resume() {
        Debugger.i(TAG, "[resume]");
        mBucketManager.setup();
    }

    public void pause() {
        Debugger.i(TAG, "[pause]");
        mBucketManager.unsetup();
    }

    public void terminal() {
        Debugger.i(TAG, "[terminal]");
        // TODO
    }

    /********************************
     * Public interface for invoker
     ********************************/

    public BucketManager getBucketManager() {
        return mBucketManager;
    }

    public void registerChangeNotifier(Uri uri, ChangeNotifier notifier) {
        NotifierBroker broker = null;
        synchronized (mNotifierMap) {
            broker = mNotifierMap.get(uri);
            if (broker == null) {
                broker = new NotifierBroker(mNotifyDataChangeHandler);
                mApplicationContext.getContentResolver().registerContentObserver(uri, true, broker);
                mNotifierMap.put(uri, broker);
            }
        }
        broker.registerNotifier(notifier);
    }
}
