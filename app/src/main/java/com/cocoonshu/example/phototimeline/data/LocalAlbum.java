package com.cocoonshu.example.phototimeline.data;

import com.cocoonshu.example.phototimeline.GalleryApplication;

/**
 * @Author Cocoonshu
 * @Date   2017-04-26
 */
public class LocalAlbum extends MediaSet {

    private GalleryApplication mApplication = null;

    public LocalAlbum(GalleryApplication application, long version) {
        super(version);
        mApplication = application;
    }

    protected GalleryApplication getApplication() {
        return mApplication;
    }

    @Override
    public void reload() {

    }
}
