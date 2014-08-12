package de.rocketinternet.android.tracking.trackers;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.ecommerce.ProductAction;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.interfaces.RIEcommerceEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIExceptionTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.models.RITrackingTransaction;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 * @deprecated this tracker is actually deprecated in favor of GTMTracker
 * Convenience controller to proxy-pass tracking information to Google Analytics
 */
public class RIGoogleAnalyticsTracker extends RITracker implements
        RIEventTracking,
        RIScreenTracking,
        RIExceptionTracking,
        RIEcommerceEventTracking {

    private static final String TRACKER_ID = "RIGoogleAnalyticsTrackerID";
    private Tracker mTracker;

    public RIGoogleAnalyticsTracker() {
    }

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
            return true;
        } else {
            RILogUtils.logError("Missing Google Analytics Tracking ID in tracking properties");
        }
        return false;
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
    public void trackEvent(String event, long value, String action, String category, Map<String, Object> data) {
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
    public void trackException(HashMap<String, String> params, Exception exception) {
        RILogUtils.logDebug("Google Analytics tracker tracks exception with name " + exception.getMessage());

        if (mTracker == null) {
            RILogUtils.logError("Missing default Google Analytics tracker");
            return;
        }

        // Build and send exception.
        Map<String, String> exceptionParams =
                new HitBuilders.ExceptionBuilder()
                        .setDescription(exception.getMessage())
                        .setFatal(false)
                        .build();

        mTracker.send(exceptionParams);
    }

    @Override
    public void trackCheckoutTransaction(RITrackingTransaction transaction) {
        RILogUtils.logDebug("Google Analytics - Tracking checkout with transaction id: " + transaction.getTransactionId());

        if (mTracker == null) {
            RILogUtils.logError("Missing default Google Analytics tracker");
            return;
        }

        RITrackingTotal total = transaction.getTotal();
        float tax = 0;
        int shipping = 0;
        String currency = "";
        if (total != null) {
            tax = total.getTax();
            shipping = total.getShipping();
            currency = total.getCurrency();
        }

        ProductAction analyticsProductAction = new ProductAction(ProductAction.ACTION_PURCHASE)
                .setTransactionId(transaction.getTransactionId())
                .setTransactionTax(tax)
                .setTransactionShipping(shipping);
        HitBuilders.ScreenViewBuilder builder = new HitBuilders.ScreenViewBuilder()
                .setProductAction(analyticsProductAction);
        mTracker.setScreenName(RITrackersConstants.GA_CHECKOUT_SCREEN_NAME);
        mTracker.set(RITrackersConstants.GA_CURRENCY_KEY, currency);
        mTracker.send(builder.build());
    }

    @Override
    public void trackAddProductToCart(RITrackingProduct product, String cartId, String location) {
        // TODO: add implementation
    }

    @Override
    public void trackRemoveProductFromCart(RITrackingProduct product, int quantity, double cartValue) {
        // TODO: add implementation
    }
}
