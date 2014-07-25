package de.rocketinternet.android.tracking.core;

import de.rocketinternet.android.tracking.trackers.RITracker;
import android.util.Log;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author alessandro.balocco
 */
public class RITracking {

    private static RITracking sInstance;
    private static boolean mIsDebug;
    private RITracker[] mTrackers;
    private List<RITracker> mHandlers;

    private RITracking() {}

    /**
     *  Creates and initializes an `RITracking`object
     *
     *  @return The newly-initialized object
     */
    public static RITracking getInstance() {
        if (sInstance == null) {
            sInstance = new RITracking();
        }
        return sInstance;
    }

    // TODO: What is the purpose?
    public void initWithTrackers(RITracker[] trackers) {
        getInstance();
        mHandlers = new ArrayList<RITracker>();
    }

    public static boolean isDebug() {
        return mIsDebug;
    }

    /**
     *  A flag to enable debug logging.
     */
    public void setDebug(boolean debugEnabled) {
        mIsDebug = debugEnabled;
        Log.d("RITracking", "Debug mode is enabled: " + mIsDebug);
    }

    /**
     *  Load the configuration needed from a plist file in the given path and launching options
     *
     *  @param path Path to the configuration file (plist file).
     *  @param launchOptions The launching options.
     */
    public void startWithConfigurationFromPropertyListAtPath(String path,
                                                             final Map<String, String> launchOptions) {

    }


    /**
     *  Hidden test helper
     */
    public static void reset() {
        sInstance = null;
    }
}
