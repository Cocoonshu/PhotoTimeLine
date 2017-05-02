package com.cocoonshu.example.phototimeline.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;

import com.cocoonshu.example.phototimeline.GalleryApplication;
import com.cocoonshu.example.phototimeline.utils.DataUtils;
import com.cocoonshu.example.phototimeline.utils.Debugger;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * @Author Cocoonshu
 * @Date   2017-05-02
 */
public class BucketManager {
    private static final String              TAG             = "BucketManager";
    private static final long                RELOAD_INTERVAL = 500L;
    private static final Uri                 sImageUri       = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private static final Uri                 sVideoUri       = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private              Context             mContext        = null;
    private              MediaBucketObserver mBucketObserver = null;
    private              Handler             mReloadHandler  = null;
    private              Set<Long>           mDisplayBuckets = new HashSet<>();

    private static final String[] DisplayBucketNames = new String [] {
            "/DCIM/",
            "/Pictures/",
            "/DCIM/Screenshots/"
    };

    public BucketManager(GalleryApplication application) {
        mContext        = application.getApplicationContext();
        mReloadHandler  = new Handler(application.getSerialReloadHandlerThread().getLooper());
        mBucketObserver = new MediaBucketObserver(mContext, mReloadHandler);
    }

    public synchronized void setup() {
        Debugger.i(TAG, "[setup]");
        ContentResolver resolver = mContext.getContentResolver();
        resolver.registerContentObserver(sImageUri, false, mBucketObserver);
        resolver.registerContentObserver(sVideoUri, false, mBucketObserver);
        resolver.notifyChange(sImageUri, mBucketObserver);
    }

    public synchronized void unsetup() {
        Debugger.i(TAG, "[unsetup]");
        ContentResolver resolver = mContext.getContentResolver();
        resolver.unregisterContentObserver(mBucketObserver);
    }

    public Set<Long> obtainDisplayBuckets() {
        return new HashSet<>(mDisplayBuckets);
    }

    public Set<Long> getDisplayBuckets() {
        return mDisplayBuckets;
    }

    /**
     * Monitor media bucket changing and reload the displaying buckets.
     */
    private class MediaBucketObserver extends ContentObserver implements Runnable {
        private static final String   TAG         = "MediaBucketObserver";
        private        final String[] PROJECTIONS = new String[] {
                MediaStore.Video.VideoColumns.BUCKET_ID
        };

        private Context mContext       = null;
        private Handler mReloadHandler = null;

        public MediaBucketObserver(Context context, Handler handler) {
            super(null);
            mContext       = context;
            mReloadHandler = handler;
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            Debugger.i(TAG, "[onChange] uri = " + uri.toString());

            // This method will be invoke very frequently,
            // We should hold reload for a while in case the
            // next content changing notification coming such soon.
            if (mReloadHandler != null) {
                mReloadHandler.removeCallbacks(this);
                mReloadHandler.postDelayed(this, RELOAD_INTERVAL);
            }
        }

        @Override
        public void run() {
            GalleryApplication application = (GalleryApplication) mContext;
            application.getWorkExecutor().submit(new Runnable() {
                @Override
                public void run() {
                    synchronized (mDisplayBuckets) {
                        mDisplayBuckets = updateDisplayBuckets();
                    }
                }
            });
        }

        public Set<Long> updateDisplayBuckets() {
            Debugger.i(TAG, "[updateDisplayBuckets] Reload displaying buckets.");

            if (mContext != null) {
                StorageManager      manager      = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
                List<StorageVolume> volumes      = manager.getStorageVolumes();
                Set<String>         displayPaths = new HashSet<>();
                StringBuffer        whereClause  = new StringBuffer();
                Set<Long>           bucketIds    = new HashSet<>();
                long                startTime    = System.currentTimeMillis();

                Debugger.i(TAG, "[updateDisplayBuckets] ================ start query display buckets ================");
                for (String uniqueBucket : DisplayBucketNames) {
                    for (StorageVolume volume : volumes) {
                        String comboPath = volume.getPath() + uniqueBucket;
                        displayPaths.add(comboPath);
                        Debugger.i(TAG, "[updateDisplayBuckets] Bucket: " + comboPath);
                        whereClause.append(MediaStore.Files.FileColumns.DATA).append(" like '").append(comboPath).append("%'");
                        whereClause.append(" or ");
                    }
                }
                if (displayPaths.size() > 0) {
                    int lastPosition = whereClause.lastIndexOf("or");
                    int strLength    = whereClause.length();
                    if (lastPosition >= 0 && lastPosition < strLength) {
                        whereClause.delete(lastPosition, strLength);
                    }
                }
                whereClause.append(" and ").append(MediaStore.Files.FileColumns.MEDIA_TYPE).append(" in (0)");

                Debugger.i(TAG, "[updateDisplayBuckets] Querying buckets from MediaProvider");
                Uri uri = MediaStore.Files.getContentUri("external").buildUpon().appendQueryParameter("invalid", "0").build();
                ContentResolver resolver = mContext.getContentResolver();
                Cursor cursor = resolver.query(uri, PROJECTIONS, whereClause.toString(), null, null);
                try {
                    int indexBucketId = cursor.getColumnIndex(MediaStore.Video.VideoColumns.BUCKET_ID);
                    while (cursor.moveToNext()) {
                        long bucketId = cursor.getLong(indexBucketId);
                        bucketIds.add(bucketId);
                    }
                } catch (Throwable thr) {
                    Debugger.e(TAG, "[updateDisplayBuckets] Query display buckets failed", thr);
                } finally {
                    DataUtils.closeSafely(cursor);
                }

                Debugger.i(TAG, String.format(Locale.ENGLISH, "[updateDisplayBuckets] ===== end display buckets querying, duration %dms =====",
                        (System.currentTimeMillis() - startTime)));
                return bucketIds;
            } else {
                return null;
            }
        }
    }
}
