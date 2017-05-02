package com.cocoonshu.example.phototimeline.data;

import android.database.ContentObserver;
import android.os.Handler;

import java.util.WeakHashMap;

/**
 * @Author Cocoonshu
 * @Date   2017-04-26
 */
public class NotifierBroker extends ContentObserver {

    private WeakHashMap<ChangeNotifier, Void> mNotifiers = new WeakHashMap<>();

    /**
     * Creates a content observer.
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public NotifierBroker(Handler handler) {
        super(handler);
    }

    public synchronized void registerNotifier(ChangeNotifier notifier) {
        mNotifiers.put(notifier, null);
    }

    @Override
    public void onChange(boolean selfChange) {
        for (ChangeNotifier notifier : mNotifiers.keySet()) {
            notifier.onChange(selfChange);
        }
    }
}
