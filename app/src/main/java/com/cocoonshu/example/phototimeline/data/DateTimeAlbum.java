package com.cocoonshu.example.phototimeline.data;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * @Author Cocoonshu
 * @Date   2017-04-26
 */
public class DateTimeAlbum extends LocalAlbum {
    private static final String TAG = "DateTimeAlbum";
    private static final String COLUMN_CSHOT_ID = "cshot_id";
    private Context mContext = null;
    public DateTimeAlbum(Context context) {
        super(nextVersion());
        mContext = context;
    }

    public ArrayList<MediaItem> getMediaItem(int start, int count) {
        return null;
    }

    /**
     * Update all time information from MediaProvider
     */
    public void updateTimeInfo() {
        Uri uri = MediaStore.Files.getContentUri("external");//content://media/external/file
        Log.d(TAG, "updateDaysInfo uri 11 = " + uri);
        ContentResolver cr = mContext.getContentResolver();
        uri = uri.buildUpon().appendQueryParameter("invalid", "0").build();//content://media/external/file?invalid=0
        Log.d(TAG, "updateDaysInfo uri 22 = " + uri);
        long dateTaken = 0;
        long lastDayTime = Long.MAX_VALUE;
        long lastMonthTime = Long.MAX_VALUE;
        long lastYearTime = Long.MAX_VALUE;
        int index = 0;
        Range range = null;
        Cursor cursor = null;
        try {
            //String whereClause = getWhereClause();
            String whereClause = "(((cshot_id == 0 AND bucket_id IN (-1739773001,-508660256))  OR (_id in (SELECT _id FROM images WHERE cshot_id > 1 GROUP BY cshot_id HAVING _data = MIN(_data)))) AND media_type IN (1,3))";
            cursor = cr.query(uri, PROJECTION_BUCKET, whereClause, null, BUCKET_ORDER_BY);

            if (cursor == null) {
                Log.d(TAG, "updateDaysInfo cursor = null");
                return;
            }
            while (cursor.moveToNext()) {
                dateTaken = cursor.getLong(0);
                if (dateTaken <= 0) {
                    dateTaken = cursor.getLong(1) * 1000;
                }
                lastDayTime = addElementToRange(dateTaken, lastDayTime, mDaysInfo, index, ADD_TO_DAY_RANGE);
                lastMonthTime = addElementToRange(dateTaken, lastMonthTime, mMonthsInfo, index, ADD_TO_MONTH_RANGE);
                lastYearTime = addElementToRange(dateTaken, lastYearTime, mYearsInfo, index, ADD_TO_YEAR_RANGE);
                index++;
            }
//            mCachedCount = cursor.getCount();
        } catch (Exception e) {
            Log.w(TAG, "query Exception: ", e);
        } finally {
            if (null != cursor) {
                cursor.close();
                cursor = null;
            }
        }
        if (mDaysInfo != null) {
            for (Range ran : mDaysInfo) {
                Log.d(TAG, "updateDaysInfo range = " + ran);
            }
        }
        if (mMonthsInfo != null) {
            for (Range ran : mMonthsInfo) {
                Log.d(TAG, "updateMonthInfo range = " + ran);
            }
        }
        if (mYearsInfo != null) {
            for (Range ran : mYearsInfo) {
                Log.d(TAG, "updateYearInfo range = " + ran);
            }
        }
    }

    /**
     * Get sql where clause sentence
     * @return
     */
    public String getWhereClause() {
        /**
         * SELECT * FROM files
         * WHERE cshot_id == 0 AND
         */
        return "cshot_id";
    }


    private ArrayList<Range> mDaysInfo = new ArrayList<Range>();
    private ArrayList<Range> mMonthsInfo = new ArrayList<Range>();
    private ArrayList<Range> mYearsInfo = new ArrayList<Range>();
    private static final String[] PROJECTION_BUCKET = { MediaStore.Images.ImageColumns.DATE_TAKEN,
            MediaStore.Images.ImageColumns.DATE_MODIFIED };
    private static final String BUCKET_ORDER_BY = "datetaken DESC";
    private static final long ONE_HOUR = 1000 * 60 * 60;
    private static final long ONE_DAY = 24 * ONE_HOUR;

    private static final int ADD_TO_DAY_RANGE = 0;
    private static final int ADD_TO_MONTH_RANGE = 1;
    private static final int ADD_TO_YEAR_RANGE = 2;

    private long addElementToRange(long dateTaken, long lastTime, ArrayList<Range> mInfo, int index, int type) {
        boolean isDiffRange = dateTaken < lastTime;
        Range range = null;
        if (isDiffRange) {
            lastTime = cutTimeTail(dateTaken, type);
            range = new Range(index, index, lastTime, false, false, false);
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
            date.set(Calendar.HOUR_OF_DAY, 0);//Tue May 02 00:00:00 GMT+08:00 2017
            Log.d(TAG, "cutTimeTail day2 = " + date.getTime());
        } else if (type == ADD_TO_MONTH_RANGE) {
            date.setTimeInMillis(time / ONE_DAY * ONE_DAY);
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.DAY_OF_MONTH, 1);//Mon May 01 00:00:00 GMT+08:00 2017
            Log.d(TAG, "cutTimeTail month2 = " + date.getTime());
        } else if (type == ADD_TO_YEAR_RANGE) {
            date.setTimeInMillis(time / ONE_DAY * ONE_DAY);
            date.set(Calendar.HOUR_OF_DAY, 0);
            date.set(Calendar.DAY_OF_MONTH, 1);
            date.set(Calendar.MONTH, 0);//Sun Jan 01 00:00:00 GMT+08:00 2017
            Log.d(TAG, "cutTimeTail year2 = " + date.getTime());
        }

        return date.getTimeInMillis();
    }
	
    public static class Range{
        public int mStart;
        public int mEnd;
        public long mTime;
        public boolean mIsPressed;
        public boolean mIsLongPressed;
        public boolean mIsSelectedAll;
        public Range(int start, int end, long time, boolean isPressed, boolean isLongPressed, boolean isSelectedAll) {
            mStart = start;
            mEnd = end;
            mTime = time;
            mIsPressed = isPressed;
            mIsLongPressed = isLongPressed;
            mIsSelectedAll = isSelectedAll;
        }
        public String toString() {
            return " mStart:" + mStart + " mEnd:" + mEnd + " mTime:" + mTime;
        }
    }
}
