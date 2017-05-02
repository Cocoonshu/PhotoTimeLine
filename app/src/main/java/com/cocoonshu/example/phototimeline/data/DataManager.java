package com.cocoonshu.example.phototimeline.data;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.cocoonshu.example.phototimeline.GalleryApplication;

import java.util.HashMap;

/**
 * @Author Cocoonshu
 * @Date   2017-04-26
 */
public class DataManager {

    private final GalleryApplication           mGalleryApplication;
    private final Context                      mApplicationContext;
    private final HashMap<Uri, NotifierBroker> mNotifierMap;
    private final Handler                      mNotifyDataChangeHandler;

    public DataManager(GalleryApplication application) {
        mGalleryApplication      = application;
        mApplicationContext      = application.getApplicationContext();
        mNotifierMap             = new HashMap<>();
        mNotifyDataChangeHandler = new Handler(application.getMainLooper());
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
