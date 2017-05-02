package com.cocoonshu.example.phototimeline.data;


import android.net.Uri;

import com.cocoonshu.example.phototimeline.GalleryApplication;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author Cocoonshu
 * @Date   2017-04-26
 */
public class ChangeNotifier {

    private MediaSet      mMediaSet         = null;
    private AtomicBoolean mIsContentChanged = new AtomicBoolean(true);

    public ChangeNotifier(MediaSet data, Uri uri, GalleryApplication application) {
        mMediaSet = data;
    }

    public boolean isDirty() {
        return mIsContentChanged.compareAndSet(true, false);
    }

    public void onChange(boolean selfChange) {
        if (mIsContentChanged.compareAndSet(false, true)) {
            mMediaSet.notifyDateChanged();
        }
    }
}
