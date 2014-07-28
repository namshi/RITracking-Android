package de.rocketinternet.android.tracking.core;

import de.rocketinternet.android.tracking.handlers.RIOpenUrlHandler;
import de.rocketinternet.android.tracking.interfaces.RIEcommerceEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIExceptionTracking;
import de.rocketinternet.android.tracking.interfaces.RIOpenUrlTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.trackers.RIGoogleAnalyticsTracker;
import de.rocketinternet.android.tracking.trackers.RITracker;
import de.rocketinternet.android.tracking.utils.RILogUtils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author alessandro.balocco
 */
public class RITracking implements
        RIEventTracking,
        RIScreenTracking,
        RIExceptionTracking,
        RIOpenUrlTracking,
        RIEcommerceEventTracking {

    private static RITracking sInstance;
    private static boolean mIsDebug;
    private List<RITracker> mTrackers;
    private List<RIOpenUrlHandler> mHandlers;

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

    // TODO: Give the option to initialize other trackers
    public void initTrackers(RITracker[] trackers) {
    }

    // TODO: Pass in some handlers
    public void initHandlers(List<RIOpenUrlHandler> handlers) {
        if (mHandlers == null) {
            mHandlers = new ArrayList<RIOpenUrlHandler>();
        }
        mHandlers.addAll(handlers);
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
     *  Load the configuration needed from the assets folder
     */
    public void startWithConfigurationFromPropertyList(Context context) {
        RILogUtils.logDebug("Starting initialisation with property list");

        boolean loaded = RITrackingConfiguration.getInstance().loadFromPropertyList(context);

        if (!loaded) {
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
    public void trackOpenUrl(final URL url) {
        RILogUtils.logDebug("Tracking deepling with URL " + url);

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RIEventTracking) {
                tracker.execute(new Runnable() {
                    @Override
                    public void run() {
                        ((RIOpenUrlTracking) tracker).trackOpenUrl(url);
                    }
                });
            }
        }
    }

    @Override
    public void registerHandler(List<String> params, String pattern) {
        // TODO: write code for handler
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
    public static void reset() {
        sInstance = null;
    }
}
