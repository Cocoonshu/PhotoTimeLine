package com.cocoonshu.example.phototimeline.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileFilter;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @Author Cocoonshu
 * @Date   2017-05-02
 */
public class DataUtils {

    public static int genBucketId(String path) {
        return path.toLowerCase(Locale.US).hashCode();
    }

    public static int genFileHashCode(File file) {
        return file.toString().toLowerCase(Locale.US).hashCode();
    }

    public static int genFileHashCode(String file) {
        return file.toLowerCase(Locale.US).hashCode();
    }

    public static void closeSafely(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable thr) {
                // Ignore this
            }
        }
    }

    public static void closeSafely(SQLiteDatabase database) {
        if (database != null) {
            try {
                database.close();
            } catch (Throwable thr) {
                // Ignore this
            }
        }
    }

    public static int getCpuCoreSize() {
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            File   dir   = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new CpuFilter());
            return files.length;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getSuggestionThreadSize(int defaultSize) {
        int cpuCoreSize = getCpuCoreSize();
        if (cpuCoreSize >= 1 && cpuCoreSize < 4) {
            return 4;
        } else if (cpuCoreSize >= 4) {
            return 2 * cpuCoreSize;
        } else {
            return defaultSize;
        }
    }
}
