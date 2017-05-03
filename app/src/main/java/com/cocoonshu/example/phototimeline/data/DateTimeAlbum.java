package com.cocoonshu.example.phototimeline.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Files.FileColumns;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Video.VideoColumns;
import android.util.Log;

import com.cocoonshu.example.phototimeline.GalleryApplication;
import com.cocoonshu.example.phototimeline.utils.DataUtils;
import com.cocoonshu.example.phototimeline.utils.Debugger;
import com.cocoonshu.example.phototimeline.utils.TextUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

/**
 * @Author Cocoonshu
 * @Date   2017-04-26
 */
public class DateTimeAlbum extends LocalAlbum {

    private static final String TAG                = "DateTimeAlbum";
    private static final String COLUMN_CSHOT_ID    = "cshot_id";
    private static final long   ONE_HOUR           = 1000 * 60 * 60;
    private static final long   ONE_DAY            = 24 * ONE_HOUR;
    private static final int    ADD_TO_DAY_RANGE   = 0;
    private static final int    ADD_TO_MONTH_RANGE = 1;
    private static final int    ADD_TO_YEAR_RANGE  = 2;

    private ArrayList<Range> mDaysInfo    = new ArrayList<Range>();
    private ArrayList<Range> mMonthsInfo  = new ArrayList<Range>();
    private ArrayList<Range> mYearsInfo   = new ArrayList<Range>();
    private String           mWhereClause = null;

    public DateTimeAlbum(GalleryApplication application) {
        super(application, nextVersion());
    }

    /**
     * Update all time information from MediaProvider
     */
    public void updateTimeInfo() {
        final String[] PROJECTION = new String[] {
                ImageColumns.DATE_TAKEN,
                ImageColumns.DATE_MODIFIED
        };

        long               startTime   = System.currentTimeMillis();
        GalleryApplication application = getApplication();
        ContentResolver    resolver    = application.getContentResolver();
        Uri                uri         = MediaStore.Files.getContentUri("external").buildUpon().appendQueryParameter("invalid", "0").build();
        String             whereClause = mWhereClause == null ? getWhereClause() : mWhereClause;
        String             orderClause = MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC";
        Cursor             cursor      = resolver.query(uri, PROJECTION, whereClause, null, orderClause);
        if (cursor != null) {
            try {
                long  dateTaken         = 0;
                long  lastDayTime       = Long.MAX_VALUE;
                long  lastMonthTime     = Long.MAX_VALUE;
                long  lastYearTime      = Long.MAX_VALUE;
                int   index             = 0;
                int   indexDateTaken    = cursor.getColumnIndex(ImageColumns.DATE_TAKEN);
                int   indexDateModified = cursor.getColumnIndex(ImageColumns.DATE_MODIFIED);

                while (cursor.moveToNext()) {
                    dateTaken     = cursor.getLong(indexDateTaken);
                    dateTaken     = dateTaken <= 0 ? (cursor.getLong(indexDateModified) * 1000) : dateTaken;
                    lastDayTime   = addElementToRange(dateTaken, lastDayTime, mDaysInfo, index, ADD_TO_DAY_RANGE);
                    lastMonthTime = addElementToRange(dateTaken, lastMonthTime, mMonthsInfo, index, ADD_TO_MONTH_RANGE);
                    lastYearTime  = addElementToRange(dateTaken, lastYearTime, mYearsInfo, index, ADD_TO_YEAR_RANGE);
                    index++;
                }
            } catch (Throwable thr) {
                Debugger.e(TAG, "[updateTimeInfo] Querying time information failed", thr);
            } finally {
                DataUtils.closeSafely(cursor);
            }

            {// Debug
                Calendar calendar = Calendar.getInstance();
                if (mDaysInfo != null) {
                    Debugger.i(TAG, "[updateTimeInfo] ======== Days information ========");
                    for (Range range : mDaysInfo) {
                        calendar.setTimeInMillis(range.mTime);
                        String time = TextUtils.getFullDateTimeString(calendar);
                        Debugger.i(TAG, "[updateTimeInfo] Range: " + time + ", index: " + range.mStart + " - " + range.mEnd);
                    }
                }
                if (mMonthsInfo != null) {
                    Debugger.i(TAG, "[updateTimeInfo] ======== Months information ========");
                    for (Range range : mMonthsInfo) {
                        calendar.setTimeInMillis(range.mTime);
                        String time = TextUtils.getFullDateTimeString(calendar);
                        Debugger.i(TAG, "[updateTimeInfo] Range: " + time + ", index: " + range.mStart + " - " + range.mEnd);
                    }
                }
                if (mYearsInfo != null) {
                    Debugger.i(TAG, "[updateTimeInfo] ======== Years information ========");
                    for (Range range : mYearsInfo) {
                        calendar.setTimeInMillis(range.mTime);
                        String time = TextUtils.getFullDateTimeString(calendar);
                        Debugger.i(TAG, "[updateTimeInfo] Range: " + time + ", index: " + range.mStart + " - " + range.mEnd);
                    }
                }
            }
        }
        Debugger.i(TAG, String.format(Locale.ENGLISH, "[updateTimeInfo] ========== Duration: %dms ===========",
                (System.currentTimeMillis() - startTime)));
    }

    /**
     * Get sql where clause sentence
     * @return
     */
    public String getWhereClause() {
        /**
         * SELECT * FROM files
         * WHERE
         *     ( media_type IN (1, 3) )
         *  AND
         *     ( cshot_id == 0 AND bucket_id IN (xxx) )
         *  OR
         *     ( _id IN (SELECT _id FROM images WHERE cshot_id > 1 GROUP BY cshot_id HAVING _data = MIN(_data)) )
         */
        Long[]        bucketIds = getDisplayBucketIds();
        StringBuilder buffer    = new StringBuilder();
        buffer.append(" (").append(FileColumns.MEDIA_TYPE).append(" IN (1, 3) )) ")
              .append(" AND ")
              .append(" (").append(COLUMN_CSHOT_ID).append(" == 0 AND ").append(VideoColumns.BUCKET_ID).append(" IN (");
        {// Looping bucket ids
            for (Long bucketId : bucketIds) {
                if (bucketId != null) {
                    buffer.append(bucketId).append(",");
                }
            }
            if (bucketIds.length > 1) {
                int bufferLength = buffer.length();
                buffer.delete(bufferLength - 1, bufferLength);
            }
        }
        buffer.append(")")
              .append(" OR ")
              .append(" (").append(FileColumns._ID).append(" IN ( SELECT ").append(FileColumns._ID)
              .append(" FROM ").append(" images ")
              .append(" WHERE ").append(COLUMN_CSHOT_ID).append(" > 1 GROUP BY ").append(COLUMN_CSHOT_ID)
              .append(" HAVING ").append(FileColumns.DATA).append(" = MIN(").append(FileColumns.DATA).append(") ")
              .append(" )")
              .append(" )");

        mWhereClause = buffer.toString();
        return mWhereClause;
    }

    /**
     * Get bucket id array for album to collecting
     * @return
     */
    public Long[] getDisplayBucketIds() {
        BucketManager bucketManager = getApplication().getDataManager().getBucketManager();
        if (bucketManager != null) {
            Set<Long> bucketSet = bucketManager.getDisplayBuckets();
            if (bucketSet != null) {
                return bucketSet.toArray(new Long[0]);
            }
        }
        return new Long[0];
    }

    private long addElementToRange(long dateTaken, long lastTime, ArrayList<Range> mInfo, int index, int type) {
        boolean isDiffRange = dateTaken < lastTime;
        Range   range       = null;
        if (isDiffRange) {
            lastTime = cutTimeTail(dateTaken, type);
            range    = new Range(index, index, lastTime);
            mInfo.add(range);
        } else {
            range = mInfo.get(mInfo.size() - 1);
            range.mEnd += 1;
        }

        return lastTime;
    }

    public static long cutTimeTail(long time, int type) {
        Calendar date = Calendar.getInstance();
        if (type == ADD_TO_DAY_RANGE) {
            date.setTimeInMillis(time / ONE_HOUR * ONE_HOUR);
            date.set(Calendar.HOUR_OF_DAY, 0);
        } else if (type == ADD_TO_MONTH_RANGE) {
            date.setTimeInMillis(time / ONE_DAY * ONE_DAY);
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.DAY_OF_MONTH, 1);
        } else if (type == ADD_TO_YEAR_RANGE) {
            date.setTimeInMillis(time / ONE_DAY * ONE_DAY);
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.DAY_OF_MONTH, 1);
            date.set(Calendar.MONTH, 0);
        }
        return date.getTimeInMillis();
    }

    /**
     * Time range
     */
    public static class Range {
        public int     mStart;
        public int     mEnd;
        public long    mTime;

        public Range(int start, int end, long time) {
            mStart = start;
            mEnd = end;
            mTime = time;
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append("mStart: ").append(mStart).append(", mEnd: ").append(mEnd).append(", mTime: ").append(mTime);
            return buffer.toString();
        }
    }

}
