package de.rocketinternet.android.tracking.core;

import de.rocketinternet.android.tracking.handlers.RIOpenUrlHandler;
import de.rocketinternet.android.tracking.interfaces.RIEcommerceEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIExceptionTracking;
import de.rocketinternet.android.tracking.interfaces.RIOpenUrlTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrl;
import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.trackers.RIGoogleAnalyticsTracker;
import de.rocketinternet.android.tracking.trackers.RITracker;
import de.rocketinternet.android.tracking.utils.RIAssetsUtils;
import de.rocketinternet.android.tracking.utils.RILogUtils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author alessandro.balocco
 *
 * This class allows users of this library to interact with different tracking systems. The class is
 * provides functionalities to track specific events and based on that it automatically spreads these
 * events to registered tracking libraries.
 */
public class RITracking implements
        RIEventTracking,
        RIScreenTracking,
        RIExceptionTracking,
        RIOpenUrlTracking,
        RIEcommerceEventTracking {

    private static final String PROPERTIES_FILE_NAME = "ri_tracking_config.properties";

    private static RITracking sInstance;
    private static boolean mIsDebug;
    private List<RITracker> mTrackers;
    private List<RIOpenUrlHandler> mHandlers;

    private RITracking() {}

    /**
     *  Creates and initializes an 'RITracking' object
     *
     *  @return The newly-initialized object
     */
    public static RITracking getInstance() {
        if (sInstance == null) {
            sInstance = new RITracking();
        }
        return sInstance;
    }

    // TODO: Give the option to initialize other trackers
    public void initTrackers(List<RITracker> trackers) {
        if (mTrackers == null) {
            mTrackers = new ArrayList<RITracker>();
        }
        mTrackers.addAll(trackers);
    }

    /**
     *  A method to check if debug mode is enabled
     */
    public boolean isDebug() {
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
     *  Load the needed configuration from the assets folder
     */
    public void startWithConfigurationFromPropertiesList(Context context) {
        RILogUtils.logDebug("Starting initialisation with property list");

        try {
            Properties properties = RIAssetsUtils.getProperties(context, PROPERTIES_FILE_NAME);
            RITrackingConfiguration.getInstance().loadPropertiesFromFile(properties);
        } catch (IOException e) {
            RILogUtils.logError("Unexpected error occurred when loading tracking configuration from " +
                    "property list file");
            return;
        }

        // TODO: Initialize trackers
        mTrackers = new ArrayList<RITracker>();
        // Google Analytics
        RIGoogleAnalyticsTracker googleAnalyticsTracker = new RIGoogleAnalyticsTracker();
        if (googleAnalyticsTracker.initializeTracker(context)) { mTrackers.add(googleAnalyticsTracker); }
    }

    @Override
    public void trackEvent(final String event, final int value, final String action, final String category, final Map<String, Object> data) {
        RILogUtils.logDebug("Tracking event: " + event + " with value: " + value + " with action: " + action +
                "with category: " + category + " and data: " + data);

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RIEventTracking) {
                tracker.execute(new Runnable() {
                    @Override
                    public void run() {
                        ((RIEventTracking) tracker).trackEvent(event, value, action, category, data);
                    }
                });
            }
        }
    }

    @Override
    public void trackScreenWithName(final String name) {
        RILogUtils.logDebug("Tracking screen with name: " + name);

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RIEventTracking) {
                tracker.execute(new Runnable() {
                    @Override
                    public void run() {
                        ((RIScreenTracking) tracker).trackScreenWithName(name);
                    }
                });
            }
        }
    }

    @Override
    public void trackExceptionWithName(final String name) {
        RILogUtils.logDebug("Tracking exception with name " + name);

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RIEventTracking) {
                tracker.execute(new Runnable() {
                    @Override
                    public void run() {
                        ((RIExceptionTracking) tracker).trackExceptionWithName(name);
                    }
                });
            }
        }
    }

    @Override
    public void trackOpenUrl(final Uri uri) {
        RILogUtils.logDebug("Tracking deepling with Uri " + uri);

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RIEventTracking) {
                tracker.execute(new Runnable() {
                    @Override
                    public void run() {
                        ((RIOpenUrlTracking) tracker).trackOpenUrl(uri);
                    }
                });
            }
        }
    }

    @Override
    public void registerHandler(String identifier, String pattern, RIOnHandledOpenUrl listener) {
        RIOpenUrlHandler handler = new RIOpenUrlHandler(identifier, pattern, listener);
        if (mHandlers == null || mHandlers.size() > 0) {
            mHandlers = new ArrayList<RIOpenUrlHandler>();
        }
        mHandlers.add(handler);
    }

    public void queryHandlers(Uri uri) {
        if (mHandlers != null) {
            boolean continueLooping = true;
            for (int i = 0; i < mHandlers.size() && continueLooping; i++) {
                if (mHandlers.get(i).handleOpenUrl(uri)) {
                    continueLooping = false;
                }
            }
        }
    }

    @Override
    public void trackCheckoutWithTransactionId(final String idTransaction, final RITrackingTotal total) {
        RILogUtils.logDebug("Tracking checkout transaction with id " + idTransaction);

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RIEventTracking) {
                tracker.execute(new Runnable() {
                    @Override
                    public void run() {
                        ((RIEcommerceEventTracking) tracker).trackCheckoutWithTransactionId(idTransaction, total);
                    }
                });
            }
        }
    }

    @Override
    public void trackProductAddToCart(RITrackingProduct product) {

    }

    @Override
    public void trackRemoveFromCartForProductWithID(String idTransaction, int quantity) {

    }

    /**
     *  Hidden test helper
     */
    public void reset() {
        sInstance = null;
    }
}
