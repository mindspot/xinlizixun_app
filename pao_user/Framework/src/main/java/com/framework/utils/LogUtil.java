package com.framework.utils;

import android.util.Log;

public class LogUtil {

    /*************
     * 日志记录
     */
    public static boolean isDebug = false;

    public static void Log(String tag, String message) {
        if (!isDebug || tag == null || message == null) {
            return;
        }
        Log.e(tag, message);
    }
}
