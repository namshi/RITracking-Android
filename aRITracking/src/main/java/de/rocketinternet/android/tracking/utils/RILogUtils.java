package de.rocketinternet.android.tracking.utils;

import de.rocketinternet.android.tracking.core.RITracking;
import android.util.Log;

/**
 * @author alessandro.balocco
 *
 * Utility class that provides log functionalities. It makes easier to track logs based on default tag.
 * It also provides the option to create logs with specific tag for specific needs.
 *
 * It will log information only if library is in debug mode.
 */
public class RILogUtils {

    private static final String LOG_TAG = "RITracking";

    public static void logVerbose(String message) {
        if (isDebug()) { Log.v(LOG_TAG, message); }
    }
    
    public static void logVerbose(String tag, String message) {
        if (isDebug()) { Log.v(tag, message); }
    }

    public static void logDebug(String message) {
        if (isDebug()) { Log.d(LOG_TAG, message); }
    }

    public static void logDebug(String tag, String message) {
        if (isDebug()) { Log.d(tag, message); }
    }

    public static void logInfo(String message) {
        if (isDebug()) { Log.i(LOG_TAG, message); }
    }

    public static void logInfo(String tag, String message) {
        if (isDebug()) { Log.i(tag, message); }
    }

    public static void logError(String message) {
        if (isDebug()) { Log.e(LOG_TAG, message); }
    }

    public static void logError(String tag, String message) {
        if (isDebug()) { Log.e(tag, message); }
    }

    public static void logWarning(String message) {
        if (isDebug()) { Log.w(LOG_TAG, message); }
    }

    public static void logWarning(String tag, String message) {
        if (isDebug()) { Log.w(tag, message); }
    }

    public static void logWTF(String message) {
        if (isDebug()) { Log.wtf(LOG_TAG, message); }
    }

    public static void logWTF(String tag, String message) {
        if (isDebug()) { Log.wtf(tag, message); }
    }

    private static boolean isDebug() {
        return RITracking.getInstance().isDebug();
    }
}