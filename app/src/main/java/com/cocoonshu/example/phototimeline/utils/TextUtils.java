package com.cocoonshu.example.phototimeline.utils;

import java.util.Calendar;
import java.util.Locale;

/**
 * @Author Cocoonshu
 * @Date   2017-05-03
 */
public class TextUtils {

    public static String getFullDateTimeString(Calendar calendar) {
        return String.format(Locale.ENGLISH, "%04d-%02d-%02d %02d:%02d:%02d:%04d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
                calendar.get(Calendar.MILLISECOND));
    }

    public static String getDateTimeString(Calendar calendar) {
        return String.format(Locale.ENGLISH, "%04d-%02d-%02d %02d:%02d:%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }

    public static String getDateString(Calendar calendar) {
        return String.format(Locale.ENGLISH, "%04d-%02d-%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY));
    }

    public static String getTimeString(Calendar calendar) {
        return String.format(Locale.ENGLISH, "%02d:%02d:%02d",
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }

}
