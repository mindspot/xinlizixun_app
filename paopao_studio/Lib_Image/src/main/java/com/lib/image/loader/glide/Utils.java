package com.lib.image.loader.glide;

import android.util.Log;

public class Utils {

    static final String LOG_TAG = "PullToRefresh";

    public static void warnDeprecation(String depreacted, String replacement) {
        Log.w(LOG_TAG, "You're using the deprecated " + depreacted + " attr, please switch over to " + replacement);
    }

    /**
     * 判断是否是gif
     */
    public static boolean isGifUrl(String url) {
        try {
            String[] splits = url.split(".");
            String type = splits[splits.length - 1].toUpperCase();
            return "GIF".equals(type);
        } catch (Exception e) {
            return false;
        }
    }

}
