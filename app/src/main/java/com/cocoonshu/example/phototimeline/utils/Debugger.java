package com.cocoonshu.example.phototimeline.utils;

import android.util.Log;

/**
 * @Author Cocoonshu
 * @Date   2017-05-02
 */
public class Debugger {

    private static final boolean ENABLE         = true;
    private static final boolean VERBOSE_ENABLE = ENABLE & true;
    private static final boolean DEBUG_ENABLE   = ENABLE & true;
    private static final boolean INFO_ENABLE    = ENABLE & true;
    private static final boolean WARNING_ENABLE = ENABLE & true;
    private static final boolean ERROR_ENABLE   = ENABLE & true;
    private static final boolean STACK_ENABLE   = ENABLE & true;

    public static void v(String tag, String msg) {
        if (VERBOSE_ENABLE) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG_ENABLE) {
            Log.v(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (INFO_ENABLE) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (WARNING_ENABLE) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (ERROR_ENABLE) {
            Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (ERROR_ENABLE) {
            Log.e(tag, msg, throwable);
        }
    }

    public static void stack(String tag, String msg) {
        if (STACK_ENABLE) {
            Log.e(tag, msg, new Throwable());
        }
    }
}
