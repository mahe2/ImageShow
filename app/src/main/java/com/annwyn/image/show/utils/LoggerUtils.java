package com.annwyn.image.show.utils;

import android.util.Log;

public final class LoggerUtils {

    private static final String TAG = "com.annwyn.image.show";

    public static void log(String msg) {
        Log.i(TAG, msg);
    }

    public static void log(String msg, Exception e) {
        Log.i(TAG, msg, e);
    }

}
