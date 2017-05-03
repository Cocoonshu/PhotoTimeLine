package com.cocoonshu.example.phototimeline.data;

import android.content.Context;
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
    private static final String    TAG             = "BucketManager";
    private              Context   mContext        = null;
    private              Set<Long> mDisplayBuckets = new HashSet<>();

    private static final String[] DisplayBucketNames = new String [] {
            "/DCIM",
            "/DCIM/Camera",
            "/Pictures",
            "/DCIM/Screenshots"
    };
    private static final String[] PROJECTIONS = new String[] {
            MediaStore.Video.VideoColumns.BUCKET_ID
    };

    public BucketManager(GalleryApplication application) {
        mContext = application.getApplicationContext();
    }

    public synchronized void setup() {
        Debugger.i(TAG, "[setup]");
    }

    public synchronized void unsetup() {
        Debugger.i(TAG, "[unsetup]");
    }

    public synchronized Set<Long> obtainDisplayBuckets() {
        if (mDisplayBuckets.isEmpty()) {
            mDisplayBuckets = updateDisplayBuckets();
        }
        return new HashSet<>(mDisplayBuckets);
    }

    public synchronized Set<Long> getDisplayBuckets() {
        if (mDisplayBuckets.isEmpty()) {
            mDisplayBuckets = updateDisplayBuckets();
        }
        return mDisplayBuckets;
    }

    public Set<Long> updateDisplayBuckets() {
        Debugger.i(TAG, "[updateDisplayBuckets] Reload displaying buckets.");

        if (mContext != null) {
            StorageManager      manager   = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
            List<StorageVolume> volumes   = manager.getStorageVolumes();
            Set<Long>           bucketIds = new HashSet<>();
            long                startTime = System.currentTimeMillis();

            Debugger.i(TAG, "[updateDisplayBuckets] ================ start query display buckets ================");
            StringBuilder pathBuffer = new StringBuilder();
            for (String uniqueBucket : DisplayBucketNames) {
                for (StorageVolume volume : volumes) {
                    pathBuffer.delete(0, pathBuffer.length());
                    pathBuffer.append(volume.getPath()).append(uniqueBucket);
                    String comboPath = pathBuffer.toString();
                    long   bucketId  = DataUtils.genBucketId(comboPath);
                    bucketIds.add(bucketId);
                    Debugger.i(TAG, "[updateDisplayBuckets] Bucket: " + comboPath + ", bucket id: " + bucketId);
                }
            }
            Debugger.i(TAG, String.format(Locale.ENGLISH, "[updateDisplayBuckets] ======== end display buckets querying, duration %dms ========",
                    (System.currentTimeMillis() - startTime)));
            return bucketIds;
        } else {
            return null;
        }
    }

}
