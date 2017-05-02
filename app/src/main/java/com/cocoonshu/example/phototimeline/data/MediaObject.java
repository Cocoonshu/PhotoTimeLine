package com.cocoonshu.example.phototimeline.data;

/**
 * @Author Cocoonshu
 * @Date   2017-04-26
 */
public abstract class MediaObject {
    public    static final int    MEDIA_TYPE_DICTORY = 0;
    public    static final int    MEDIA_TYPE_IMAGE   = 1;
    public    static final int    MEDIA_TYPE_MUSIC   = 2;
    public    static final int    MEDIA_TYPE_VIDEO   = 3;
    public    static final String MIME_TYPE_IMAGE    = "image";
    public    static final String MIME_TYPE_AUDIO    = "audio";
    public    static final String MIME_TYPE_VIDEO    = "video";
    private   static       long   sVersionSerial     = 0;

    protected long mVersion = 0;

    public MediaObject(long version) {
        mVersion = version;
    }

    public static synchronized long nextVersion() {
        return sVersionSerial++;
    }

    public long getVersion() {
        return mVersion;
    }

    public abstract void reload();

    public abstract void notifyDateChanged();
}
