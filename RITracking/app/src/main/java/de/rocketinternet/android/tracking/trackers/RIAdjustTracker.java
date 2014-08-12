package de.rocketinternet.android.tracking.trackers;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.adjust.sdk.Adjust;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.interfaces.RIEcommerceEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIEventTracking;
import de.rocketinternet.android.tracking.interfaces.RILifeCycleTracking;
import de.rocketinternet.android.tracking.interfaces.RIOpenUrlTracking;
import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrl;
import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.models.RITrackingTransaction;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 *         Convenience controller to proxy-pass tracking information to AdJust
 */
public class RIAdjustTracker extends RITracker implements
        RIEventTracking,
        RIOpenUrlTracking,
        RILifeCycleTracking,
        RIEcommerceEventTracking {

    private static final String TRACKER_ID = "RIAdJustTrackerID";

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
        RILogUtils.logDebug("Initializing AdJust tracker");
        String adJustIntegration = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.ADJUST_INTEGRATION);
        boolean isAdJustIntegrationNeeded = Boolean.valueOf(adJustIntegration);
        if (isAdJustIntegrationNeeded) {
            createTracker();
            return true;
        } else {
            RILogUtils.logError("AdJust tracker is not needed and will not be initialized. Change the " +
                    "corresponding property value to 'true' if you want AdJust to be integrated");
        }
        return false;
    }

    private void createTracker() {
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
        mIdentifier = TRACKER_ID;
    }

    @Override
    public void trackEvent(String event, long value, String action, String category, Map<String, Object> data) {
        RILogUtils.logDebug("Adjust tracker - Tracking event with name: " + event);

        if (data == null) {
            Adjust.trackEvent(event);
        } else {
            Map<String, String> adjustMap = new HashMap<String, String>();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                adjustMap.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
            Adjust.trackEvent(event, adjustMap);
        }
    }

    @Override
    public void trackActivityCreated(Activity activity, boolean isSplashScreen) {
        // Not used by this tracker
    }

    @Override
    public void trackActivityResumed(Activity activity) {
        if (activity != null) {
            RILogUtils.logDebug("AdJust tracker - Activity: " + activity.getLocalClassName() + " was resumed");

            Adjust.onResume(activity);
        }
    }

    @Override
    public void trackActivityPaused(Activity activity) {
        if (activity != null) {
            RILogUtils.logDebug("AdJust tracker - Activity: " + activity.getLocalClassName() + " was paused");

            Adjust.onPause();
        }
    }

    @Override
    public void trackOpenUrl(Uri uri) {
        RILogUtils.logDebug("AdJust tracker - Intercepted Uri from deep-link: " + uri);

        Adjust.appWillOpenUrl(uri);
    }

    @Override
    public void registerHandler(String identifier, String host, String path, RIOnHandledOpenUrl listener) {
        // Not used by this tracker
    }

    @Override
    public void trackCheckoutTransaction(RITrackingTransaction transaction) {
        RILogUtils.logDebug("AdJust tracker - Tracking checkout with transaction id: " +
                transaction.getTransactionId());

        double transactionPrice = getTransactionCostInCents(transaction);
        Adjust.trackRevenue(transactionPrice);
    }

    @Override
    public void trackAddProductToCart(RITrackingProduct product, String cartId, String location) {
        // Not used by this tracker
    }

    @Override
    public void trackRemoveProductFromCart(RITrackingProduct product, int quantity, double cartValue) {
        // Not used by this tracker
    }

    /**
     * This method returns the value of the transaction in cents as accepted by AdJust library
     * To have more information this link points to the official documentation
     * http://www.ad4screen.com/DocSDK/doku.php?id=events
     *
     * @param transaction The transaction that has happened
     * @return The cost of the transaction in cents
     */
    private double getTransactionCostInCents(RITrackingTransaction transaction) {
        double transactionPrice = 0;
        RITrackingTotal transactionTotal = transaction.getTotal();
        if (transactionTotal != null) {
            transactionPrice = transactionTotal.getNet();
            transactionPrice = transactionPrice * 1000;  // Cents value is needed by the SDK
        }

        return transactionPrice;
    }
}
