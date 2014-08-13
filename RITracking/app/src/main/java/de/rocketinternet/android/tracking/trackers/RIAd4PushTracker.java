package de.rocketinternet.android.tracking.trackers;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.ad4screen.sdk.A4S;
import com.ad4screen.sdk.analytics.Cart;
import com.ad4screen.sdk.analytics.Item;
import com.ad4screen.sdk.analytics.Purchase;

import java.util.List;
import java.util.Map;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.interfaces.RIEcommerceEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIEventTracking;
import de.rocketinternet.android.tracking.interfaces.RILifeCycleTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.interfaces.RIUserTracking;
import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.models.RITrackingTransaction;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 *
 * Convenience controller to proxy-pass tracking information to Ad4Push
 */
public class RIAd4PushTracker extends RITracker implements
        RIScreenTracking,
        RIUserTracking,
        RIEventTracking,
        RILifeCycleTracking,
        RIEcommerceEventTracking {

    private static final String TRACKER_ID = "RIAd4PushTrackerID";

    private A4S mA4S;
    private boolean mNotificationEnabled;

    @Override
    public String getIdentifier() {
        return mIdentifier;
    }

    @Override
    public boolean initializeTracker(Context context) {
        RILogUtils.logDebug("Initializing Ad4Push tracker");
        String ad4PushIntegration = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.AD4PUSH_INTEGRATION);
        boolean isAd4PushIntegrationNeeded = Boolean.valueOf(ad4PushIntegration);
        if (isAd4PushIntegrationNeeded) {
            createTracker(context);
            return true;
        } else {
            RILogUtils.logError("Ad4Push tracker is not needed and will not be initialized. Change the " +
                    "corresponding property value to 'true' if you want Ad4Push to be integrated");
        }
        return false;
    }

    private void createTracker(Context context) {
        mA4S = A4S.get(context);
        mIdentifier = TRACKER_ID;
    }


    @Override
    public void trackScreenWithName(String name) {
        RILogUtils.logDebug("Ad4Push tracker - Tracking screen with name: " + name);

        if (mA4S == null) {
            RILogUtils.logError("Missing Ad4Push singleton reference");
            return;
        }

        mA4S.putState(RITrackersConstants.AD4PUSH_VIEW, name);
    }

    @Override
    public void trackUserInfo(String userEvent, Map<String, Object> map) {
        // Not used by this tracker
    }

    @Override
    public void updateDeviceInfo(Map<String, Object> map) {
        RILogUtils.logDebug("Ad4Push tracker - update device info");

        if (mA4S == null) {
            RILogUtils.logError("Missing Ad4Push singleton reference");
            return;
        }

        Bundle bundle = new Bundle();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            bundle.putString(entry.getKey(), (String) entry.getValue());
        }
        mA4S.updateDeviceInfo(bundle);
    }

    @Override
    public void updateGeoLocation(Location location) {
        if (location != null) {
            RILogUtils.logDebug("Ad4Push tracker - update geolocation with: lng = " + location.getLongitude() +
                    "and lat = " + location.getLatitude());
        }

        if (mA4S == null) {
            RILogUtils.logError("Missing Ad4Push singleton reference");
            return;
        }

        mA4S.updateGeolocation(location);
    }

    @Override
    public void trackEvent(String event, int value, String action, String category, Map<String, Object> data) {
        RILogUtils.logDebug("Ad4Push tracker - Tracking event with name: " + event);

        if (mA4S == null) {
            RILogUtils.logError("Missing Ad4Push singleton reference");
            return;
        }

        mA4S.trackEvent(value, event);
    }

    @Override
    public void trackActivityCreated(Activity activity, boolean isSplashScreen) {
        if (activity != null) {
            RILogUtils.logDebug("Ad4Push tracker - Activity: " + activity.getLocalClassName() + " was created");

            if (mA4S == null) {
                RILogUtils.logError("Missing Ad4Push singleton reference");
                return;
            }

            if (isSplashScreen) {
                mA4S.setPushNotificationLocked(true);
            } else if (!mNotificationEnabled) {
                mA4S.setPushNotificationLocked(false);
                mNotificationEnabled = true;
            }
        }
    }

    @Override
    public void trackActivityResumed(Activity activity) {
        if (activity != null) {
            RILogUtils.logDebug("Ad4Push tracker - Activity: " + activity.getLocalClassName() + " was resumed");

            if (mA4S == null) {
                RILogUtils.logError("Missing Ad4Push singleton reference");
                return;
            }

            mA4S.startActivity(activity);
        }
    }

    @Override
    public void trackActivityPaused(Activity activity) {
        RILogUtils.logDebug("Ad4Push tracker - Activity: " + activity.getLocalClassName() + " was paused");

        if (mA4S == null) {
            RILogUtils.logError("Missing Ad4Push singleton reference");
            return;
        }

        mA4S.stopActivity(activity);
    }

    @Override
    public void trackCheckoutTransaction(RITrackingTransaction transaction) {
        RILogUtils.logDebug("Ad4Push tracker - Tracking checkout with transaction id: " +
                transaction.getTransactionId());

        if (mA4S == null) {
            RILogUtils.logError("Missing Ad4Push singleton reference");
            return;
        }

        Purchase purchase = createPurchaseFromTransaction(transaction);
        mA4S.trackPurchase(purchase);
    }

    @Override
    public void trackAddProductToCart(RITrackingProduct product, String cartId, String location) {
        RILogUtils.logDebug("Ad4Push tracker - Tracking add product with id " +
                product.getIdentifier() + " to cart");

        if (mA4S == null) {
            RILogUtils.logError("Missing Ad4Push singleton reference");
            return;
        }

        Item itemToAdd = createItemFromProduct(product);
        Cart cart = new Cart(cartId, itemToAdd);
        mA4S.trackAddToCart(cart);
    }

    @Override
    public void trackRemoveProductFromCart(RITrackingProduct product, int quantity, double cartValue) {
        // Not used by this tracker
    }

    /**
     * This method maps a RITrackingProduct to an Item object required by Ad4Push library
     * To have more information this link points to the official documentation
     * http://www.ad4screen.com/DocSDK/doku.php?id=events
     *
     * @param product The product to be mapped
     * @return The Item resulting from the mapping
     */
    private Item createItemFromProduct(RITrackingProduct product) {
        String id = product.getIdentifier();
        String currency = product.getCurrency();
        String name = product.getName();
        String category = product.getCategory();
        double price = product.getPrice();
        int quantity = product.getQuantity();

        return new Item(id, currency, name, category, price, quantity);
    }

    /**
     * This method maps a RITrackingTransaction to a Purchase object required by Ad4Push library
     * To have more information this link points to the official documentation
     * http://www.ad4screen.com/DocSDK/doku.php?id=events
     *
     * @param transaction The transaction to be converted into a Purchase
     * @return The purchase resulting from the mapping
     */
    private Purchase createPurchaseFromTransaction(RITrackingTransaction transaction) {
        String transactionId = transaction.getTransactionId();
        String transactionCurrency = "";
        double transactionPrice = 0;
        RITrackingTotal transactionTotal = transaction.getTotal();
        if (transactionTotal != null) {
            transactionCurrency = transactionTotal.getCurrency();
            transactionPrice = transactionTotal.getNet();
        }
        List<RITrackingProduct> productList = transaction.getProductsList();
        if (productList != null && productList.size() > 0) {
            Item[] items = new Item[productList.size()];
            for (int i = 0; i < productList.size(); i++) {
                items[i] = createItemFromProduct(productList.get(i));
            }
            return new Purchase(transactionId, transactionCurrency, transactionPrice, items);
        }

        return new Purchase(transactionId, transactionCurrency, transactionPrice);
    }
}
