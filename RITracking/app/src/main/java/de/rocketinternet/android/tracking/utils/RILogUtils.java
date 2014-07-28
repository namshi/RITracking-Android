package de.rocketinternet.android.tracking.utils;

import de.rocketinternet.android.tracking.core.RITracking;
import android.util.Log;

public class RILogUtils {

    private static final String APP_LOG_TAG = "RITracking";

    public static void logVerbose(String message) {
        if (isDebug()) { Log.v(APP_LOG_TAG, message); }
    }
    
    public static void logVerbose(String tag, String message) {
        if (isDebug()) { Log.v(tag, message); }
    }

    public static void logDebug(String message) {
        if (isDebug()) { Log.d(APP_LOG_TAG, message); }
    }

    public static void logDebug(String tag, String message) {
        if (isDebug()) { Log.d(tag, message); }
    }

    public static void logInfo(String message) {
        if (isDebug()) { Log.i(APP_LOG_TAG, message); }
    }

    public static void logInfo(String tag, String message) {
        if (isDebug()) { Log.i(tag, message); }
    }

    public static void logError(String message) {
        if (isDebug()) { Log.e(APP_LOG_TAG, message); }
    }

    public static void logError(String tag, String message) {
        if (isDebug()) { Log.e(tag, message); }
    }

    public static void logWarning(String message) {
        if (isDebug()) { Log.w(APP_LOG_TAG, message); }
    }

    public static void logWarning(String tag, String message) {
        if (isDebug()) { Log.w(tag, message); }
    }

    public static void logWTF(String message) {
        if (isDebug()) { Log.wtf(APP_LOG_TAG, message); }
    }

    public static void logWTF(String tag, String message) {
        if (isDebug()) { Log.wtf(tag, message); }
    }

    private static boolean isDebug() {
        return RITracking.isDebug();
    }
}