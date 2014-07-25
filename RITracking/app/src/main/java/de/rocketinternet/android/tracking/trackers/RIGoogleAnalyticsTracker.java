package de.rocketinternet.android.tracking.trackers;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.interfaces.RIEcommerceEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIExceptionTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.utils.LogUtils;
import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.Executors;

/**
 *  @author alessandro.balocco
 *
 *  Convenience controller to proxy-pass tracking information to Google Analytics
 */
public class RIGoogleAnalyticsTracker extends RITracker implements
        RIEventTracking,
        RIScreenTracking,
        RIExceptionTracking,
        RIEcommerceEventTracking {

    private static final String RI_GOOGLE_ANALYTICS_TRACKING_ID = "RIGoogleAnalyticsTrackingID";

    public RIGoogleAnalyticsTracker() {
        init();
    }

    private void init() {
        LogUtils.logDebug("Initializing Google Analytics tracker");
        // Initialize the queue
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
    }

    @Override
    public void execute(Runnable runnable) {
        mQueue.execute(runnable);
    }

    @Override
    public void trackApplicationLaunch(Map<String, String> options) {
        LogUtils.logDebug("Google Analytics tracker tracks application launch");

        String trackingId = RITrackingConfiguration.getInstance().getValueFromKeyMap(RI_GOOGLE_ANALYTICS_TRACKING_ID);

        if (TextUtils.isEmpty(trackingId)) {
            LogUtils.logError("Missing Google Analytics Tracking ID in tracking properties");
            return;
        }

        // TODO: Check GA code for tracking same ios stuff
//        // Automatically send uncaught exceptions to Google Analytics.
//        [GAI sharedInstance].trackUncaughtExceptions = YES;
//
//        // Dispatch tracking information every 5 seconds (default: 120)
//        [GAI sharedInstance].dispatchInterval = 5;
//
//        // Create tracker instance.
//        [[GAI sharedInstance] trackerWithTrackingId:trackingId];
//
//        LogUtils.logDebug("Initialized Google Analytics: ", [GAI sharedInstance].trackUncaughtExceptions);
    }

    @Override
    public void trackEvent(String event, int value, String action, String category, Map<String, Object> data) {
        LogUtils.logDebug("Google Analytics - Tracking event: " + event);

        // TODO Check GA code for tracking events in Android
//        id tracker = [[GAI sharedInstance] defaultTracker];
//
//        if (!tracker == null) {
//            LogUtils.logError("Missing default Google Analytics tracker");
//            return;
//        }
//
//        NSDictionary *dict = [[GAIDictionaryBuilder createEventWithCategory:category
//        action:action
//        label:event
//        value:value] build];
//
//        [tracker send:dict];
    }

    @Override
    public void trackScreenWithName(String name) {
        LogUtils.logDebug("Google Analytics - Tracking screen with name: " + name);

        // TODO Check GA code for tracking screen name in Android
//        id tracker = [[GAI sharedInstance] defaultTracker];
//
//        if (!tracker) {
//            LogUtils.logError("Missing default Google Analytics tracker");
//            return;
//        }
//
//        [tracker set:kGAIScreenName value:name];
//        [tracker send:[[GAIDictionaryBuilder createAppView] build]];
    }

    @Override
    public void trackExceptionWithName(String name) {
        LogUtils.logDebug("Google Analytics tracker tracks exception with name " + name);

        // TODO Check GA code for tracking exception in Android
//        id tracker = [[GAI sharedInstance] defaultTracker];
//
//        if (!tracker) {
//            LogUtils.logError("Missing default Google Analytics tracker");
//            return;
//        }
//
//        NSDictionary *dict = [[GAIDictionaryBuilder createExceptionWithDescription:name
//        withFatal:NO] build];
//
//        [tracker send:dict];
    }

    @Override
    public void trackCheckoutWithTransactionId(String idTransaction, RITrackingTotal total) {
        LogUtils.logDebug("Google Analytics - Tracking checkout with transaction id: %@", idTransaction);

        // TODO Check GA code for tracking checkout in Android
//        id tracker = [[GAI sharedInstance] defaultTracker];
//
//        if (!tracker) {
//            LogUtils.logError("Missing default Google Analytics tracker");
//            return;
//        }
//
//        NSDictionary *dict = [[GAIDictionaryBuilder createTransactionWithId:idTransaction
//        affiliation:nil
//        revenue:nil
//        tax:total.tax
//        shipping:total.shipping
//        currencyCode:total.currency] build];
//
//        [tracker send:dict];
    }

    @Override
    public void trackProductAddToCart(RITrackingProduct product) {

    }

    @Override
    public void trackRemoveFromCartForProductWithID(String idTransaction, int quantity) {

    }
}
