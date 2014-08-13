package de.rocketinternet.android.tracking.core;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import de.rocketinternet.android.tracking.handlers.RIOpenUrlHandler;
import de.rocketinternet.android.tracking.interfaces.RIEcommerceEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIExceptionTracking;
import de.rocketinternet.android.tracking.interfaces.RILifeCycleTracking;
import de.rocketinternet.android.tracking.interfaces.RIOpenUrlTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.interfaces.RIUserTracking;
import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrl;
import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTransaction;
import de.rocketinternet.android.tracking.trackers.RIAd4PushTracker;
import de.rocketinternet.android.tracking.trackers.RIAdjustTracker;
import de.rocketinternet.android.tracking.trackers.RIBugSenseTracker;
import de.rocketinternet.android.tracking.trackers.RIGoogleAnalyticsTracker;
import de.rocketinternet.android.tracking.trackers.RIGoogleTagManagerTracker;
import de.rocketinternet.android.tracking.trackers.RITracker;
import de.rocketinternet.android.tracking.utils.RILogUtils;
import de.rocketinternet.android.tracking.utils.RIResourceUtils;

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
        RIUserTracking,
        RIExceptionTracking,
        RIOpenUrlTracking,
        RIEcommerceEventTracking,
        RILifeCycleTracking {

    private static final String DEBUG_MODE = "DebugMode";
    private static final String PROPERTIES_FILE_NAME = "ri_tracking_config.properties";

    private static RITracking sInstance;
    private static boolean mIsDebug;
    private List<RITracker> mTrackers;
    private List<RIOpenUrlHandler> mHandlers;

    private RITracking() {
    }

    /**
     * Creates and initializes an 'RITracking' object
     *
     * @return The newly-initialized object
     */
    public static RITracking getInstance() {
        if (sInstance == null) {
            sInstance = new RITracking();
        }
        return sInstance;
    }

    /**
     * Give option to initialize tracker after application is launched
     *
     * @param trackers the list of supplied trackers
     */
    public void addTrackers(List<RITracker> trackers) {
        if (mTrackers == null) {
            mTrackers = new ArrayList<RITracker>();
        }

        String message = "### Trackers initialized manually ###";
        logTrackers(trackers, message);
        mTrackers.addAll(trackers);
    }

    /**
     * A method to check if debug mode is enabled
     */
    public boolean isDebug() {
        return mIsDebug;
    }

    /**
     * A flag to enable debug logging.
     */
    public void setDebug(boolean debugEnabled) {
        mIsDebug = debugEnabled;
        Log.d("RITracking", "Debug mode is enabled: " + mIsDebug);
    }

    /**
     * Load the needed configuration from the assets folder
     */
    public void startWithConfigurationFromPropertiesList(Context context) {
        RILogUtils.logDebug("Starting initialisation with property list");
        Properties properties = RIResourceUtils.getProperties(context, PROPERTIES_FILE_NAME);
        RITrackingConfiguration.getInstance().loadPropertiesFromFile(properties);
        mIsDebug = Boolean.valueOf(RITrackingConfiguration.getInstance().getValueFromKeyMap(DEBUG_MODE));
        initializeTrackers(context);
    }

    /**
     * Initialize all the needed trackers when app starts
     *
     * @param context a context needed for trackers initialization
     */
    private void initializeTrackers(Context context) {
        mTrackers = new ArrayList<RITracker>();
        // Google Analytics - actually deprecated
        RIGoogleAnalyticsTracker googleAnalyticsTracker = new RIGoogleAnalyticsTracker();
        if (googleAnalyticsTracker.initializeTracker(context)) {
            mTrackers.add(googleAnalyticsTracker);
        }
        // Google Tag Manager
        RIGoogleTagManagerTracker googleTagManagerTracker = new RIGoogleTagManagerTracker();
        if (googleTagManagerTracker.initializeTracker(context)) {
            mTrackers.add(googleTagManagerTracker);
        }
        // Ad4Push
        RIAd4PushTracker ad4PushTracker = new RIAd4PushTracker();
        if (ad4PushTracker.initializeTracker(context)) {
            mTrackers.add(ad4PushTracker);
        }
        // AdJust
        RIAdjustTracker adJustTracker = new RIAdjustTracker();
        if (adJustTracker.initializeTracker(context)) {
            mTrackers.add(adJustTracker);
        }
        // BugSense
        RIBugSenseTracker bugSenseTracker = new RIBugSenseTracker();
        if (bugSenseTracker.initializeTracker(context)) {
            mTrackers.add(bugSenseTracker);
        }

        String message = "## Trackers initialized onAppStart ##";
        logTrackers(mTrackers, message);
    }

    @Override
    public void trackEvent(String event, int value, String action, String category, Map<String, Object> data) {
        RILogUtils.logDebug("Tracking event: " + event + " with value: " + value + " with action: " + action +
                "with category: " + category + " and data: " + data);

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RIEventTracking) {
                ((RIEventTracking) tracker).trackEvent(event, value, action, category, data);
            }
        }
    }

    @Override
    public void trackScreenWithName(String name) {
        RILogUtils.logDebug("Tracking screen with name: " + name);

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RIScreenTracking) {
                ((RIScreenTracking) tracker).trackScreenWithName(name);
            }
        }
    }

    @Override
    public void trackUserInfo(String userEvent, Map<String, Object> map) {
        RILogUtils.logDebug("Tracking user event: " + userEvent);

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RIUserTracking) {
                ((RIUserTracking) tracker).trackUserInfo(userEvent, map);
            }
        }
    }

    @Override
    public void updateDeviceInfo(Map<String, Object> map) {
        RILogUtils.logDebug("Update Device Info");

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RIUserTracking) {
                ((RIUserTracking) tracker).updateDeviceInfo(map);
            }
        }
    }

    @Override
    public void updateGeoLocation(Location location) {
        RILogUtils.logDebug("Update Device Info");

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RIUserTracking) {
                ((RIUserTracking) tracker).updateGeoLocation(location);
            }
        }
    }

    @Override
    public void trackException(HashMap<String, String> params, Exception exception) {
        RILogUtils.logDebug("Tracking exception: " + exception.getMessage());

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RIExceptionTracking) {
                ((RIExceptionTracking) tracker).trackException(params, exception);
            }
        }
    }

    @Override
    public void trackOpenUrl(Uri uri) {
        RILogUtils.logDebug("Tracking opening URL: " + uri);

        // Looking for trackers that implement the interface
        if (mTrackers != null) {
            for (final RITracker tracker : mTrackers) {
                if (tracker instanceof RIOpenUrlTracking) {
                    ((RIOpenUrlTracking) tracker).trackOpenUrl(uri);
                }
            }
        }

        // Dispatch the uri to registered handlers
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
    public void registerHandler(String identifier, String host, String path, RIOnHandledOpenUrl listener) {
        RIOpenUrlHandler handler = new RIOpenUrlHandler(identifier, host, path, listener);
        if (mHandlers == null) {
            mHandlers = new ArrayList<RIOpenUrlHandler>();
        }
        mHandlers.add(handler);
    }

    @Override
    public void trackCheckoutTransaction(RITrackingTransaction transaction) {
        if (transaction != null) {
            RILogUtils.logDebug("Tracking checkout transaction with id " + transaction.getTransactionId());

            if (mTrackers == null) {
                RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
                return;
            }

            for (final RITracker tracker : mTrackers) {
                if (tracker instanceof RIEcommerceEventTracking) {
                    ((RIEcommerceEventTracking) tracker).trackCheckoutTransaction(transaction);
                }
            }
        }
    }

    @Override
    public void trackAddProductToCart(RITrackingProduct product, String cartId, String location) {
        if (product != null) {
            RILogUtils.logDebug("Tracking add product with id " + product.getIdentifier() + " to cart");

            if (mTrackers == null) {
                RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
                return;
            }

            for (final RITracker tracker : mTrackers) {
                if (tracker instanceof RIEcommerceEventTracking) {
                    ((RIEcommerceEventTracking) tracker).trackAddProductToCart(product, cartId, location);
                }
            }
        }
    }

    @Override
    public void trackRemoveProductFromCart(RITrackingProduct product, int quantity, double cartValue) {
        if (product != null) {
            RILogUtils.logDebug("Tracking remove " + quantity + " products with id " +
                    product.getIdentifier() + " from cart");

            if (mTrackers == null) {
                RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
                return;
            }

            for (final RITracker tracker : mTrackers) {
                if (tracker instanceof RIEcommerceEventTracking) {
                    ((RIEcommerceEventTracking) tracker).trackRemoveProductFromCart(product, quantity, cartValue);
                }
            }
        }
    }

    @Override
    public void trackActivityCreated(Activity activity, boolean isSplashScreen) {
        RILogUtils.logDebug("Activity: was created");

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RILifeCycleTracking) {
                ((RILifeCycleTracking) tracker).trackActivityCreated(activity, isSplashScreen);
            }
        }

    }

    @Override
    public void trackActivityResumed(Activity activity) {
        RILogUtils.logDebug("Activity: was resumed");

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RILifeCycleTracking) {
                ((RILifeCycleTracking) tracker).trackActivityResumed(activity);
            }
        }
    }

    @Override
    public void trackActivityPaused(Activity activity) {
        RILogUtils.logDebug("Activity: was paused");

        if (mTrackers == null) {
            RILogUtils.logError("Invalid call with non-existent trackers. Initialisation may have failed.");
            return;
        }

        for (final RITracker tracker : mTrackers) {
            if (tracker instanceof RILifeCycleTracking) {
                ((RILifeCycleTracking) tracker).trackActivityPaused(activity);
            }
        }
    }

    /**
     * Utility method that logs which trackers were initialized either manually or automatically
     * when application launches. These log are shown only when debug mode is set to TRUE.
     */
    private void logTrackers(List<RITracker> trackers, String message) {
        if (isDebug()) {
            RILogUtils.logDebug(message);
            for (int i = 0; i < trackers.size(); i++) {
                RILogUtils.logDebug("Tracker identifier: " + trackers.get(i).getIdentifier());
            }
            RILogUtils.logDebug("#####################################");
        }
    }

    /**
     * Hidden test helper
     */
    public void reset() {
        sInstance = null;
    }
}
