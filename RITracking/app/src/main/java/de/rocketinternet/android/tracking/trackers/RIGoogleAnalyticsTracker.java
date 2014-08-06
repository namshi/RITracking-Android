package de.rocketinternet.android.tracking.trackers;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.interfaces.RIEcommerceEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIExceptionTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;
import de.rocketinternet.android.tracking.utils.RILogUtils;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.ProductAction;

import java.util.Map;
import java.util.concurrent.Executors;

/**
 *  @author alessandro.balocco
 *
 *  @deprecated this tracker is actually deprecated in favor of GTMTracker
 *  Convenience controller to proxy-pass tracking information to Google Analytics
 */
public class RIGoogleAnalyticsTracker extends RITracker implements
        RIEventTracking,
        RIScreenTracking,
        RIExceptionTracking,
        RIEcommerceEventTracking {

    private static final String TRACKER_ID = "RIGoogleAnalyticsTrackerID";
    private Tracker mTracker;

    public RIGoogleAnalyticsTracker() {}

    @Override
    public void execute(Runnable runnable) {
        mQueue.execute(runnable);
    }

    @Override
    public String getIdentifier() {
        return mIdentifier;
    }

    @Override
    public boolean initializeTracker(Context context) {
        RILogUtils.logDebug("Initializing Google Analytics tracker");
        String trackingId = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.GA_TRACKING_ID);
        if (!TextUtils.isEmpty(trackingId)) {
            createTracker(context, trackingId);
        } else {
            RILogUtils.logError("Missing Google Analytics Tracking ID in tracking properties");
            return false;
        }
        return true;
    }

    private void createTracker(Context context, String trackingId) {
        // Dispatch tracking information every 5 seconds (default: 1800)
        GoogleAnalytics.getInstance(context).setLocalDispatchPeriod(5);
        mTracker = GoogleAnalytics.getInstance(context).newTracker(trackingId);
        mTracker.enableExceptionReporting(true);
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
        mIdentifier = TRACKER_ID;
    }

    @Override
    public void trackEvent(String event, int value, String action, String category, Map<String, Object> data) {
        RILogUtils.logDebug("Google Analytics - Tracking event: " + event);

        if (mTracker == null) {
            RILogUtils.logError("Missing default Google Analytics tracker");
            return;
        }

        // Build and send an event
        Map<String, String> eventsParams =
                new HitBuilders.EventBuilder()
                        .setLabel(event)
                        .setValue(value)
                        .setAction(action)
                        .setCategory(category)
                        .build();

        mTracker.send(eventsParams);
    }

    @Override
    public void trackScreenWithName(String name) {
        RILogUtils.logDebug("Google Analytics - Tracking screen with name: " + name);

        if (mTracker == null) {
            RILogUtils.logError("Missing default Google Analytics tracker");
            return;
        }

        // Set screen name.
        mTracker.setScreenName(name);

        // Send a screen view.
        mTracker.send(new HitBuilders.AppViewBuilder().build());
    }

    @Override
    public void trackExceptionWithName(String name) {
        RILogUtils.logDebug("Google Analytics tracker tracks exception with name " + name);

        if (mTracker == null) {
            RILogUtils.logError("Missing default Google Analytics tracker");
            return;
        }

        // Build and send exception.
        Map<String, String> exceptionParams =
                new HitBuilders.ExceptionBuilder()
                        .setDescription(name)
                        .setFatal(false)
                        .build();

        mTracker.send(exceptionParams);
    }

    @Override
    public void trackCheckoutWithTransactionId(String idTransaction, RITrackingTotal total) {
        RILogUtils.logDebug("Google Analytics - Tracking checkout with transaction id: " + idTransaction);

        if (mTracker == null) {
            RILogUtils.logError("Missing default Google Analytics tracker");
            return;
        }

        ProductAction analyticsProductAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                .setTransactionId(idTransaction)
                .setTransactionTax(total.getTax())
                .setTransactionShipping(total.getShipping());
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .setProductAction(analyticsProductAction);
        mTracker.setScreenName("transaction");
        mTracker.set("&cu", total.getCurrency());
        mTracker.send(builder.build());
    }

    @Override
    public void trackAddProductToCart(RITrackingProduct product) {
        // TODO: add implementation
    }

    @Override
    public void trackRemoveProductFromCart(String idTransaction, int quantity) {
        // TODO: add implementation
    }
}
