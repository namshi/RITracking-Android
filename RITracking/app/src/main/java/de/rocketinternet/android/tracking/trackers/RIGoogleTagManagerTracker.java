package de.rocketinternet.android.tracking.trackers;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.rocketinternet.android.tracking.R;
import de.rocketinternet.android.tracking.core.RITracking;
import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.interfaces.RIEcommerceEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.interfaces.RIUserTracking;
import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.models.RITrackingTransaction;
import de.rocketinternet.android.tracking.trackers.gtm.RIContainerHolder;
import de.rocketinternet.android.tracking.trackers.gtm.RIContainerLoadedCallback;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 *         <p/>
 *         Convenience controller to proxy-pass tracking information to Google Tag Manager
 */
public class RIGoogleTagManagerTracker extends RITracker implements
        RIEventTracking,
        RIScreenTracking,
        RIUserTracking,
        RIEcommerceEventTracking {

    private static final String LOG_TAG = RIGoogleTagManagerTracker.class.getSimpleName();
    private static final String TRACKER_ID = "RIGoogleTagManagerTrackerID";

    private DataLayer mDataLayer;

    @Override
    public String getIdentifier() {
        return mIdentifier;
    }

    @Override
    public boolean initializeTracker(Context context) {
        RILogUtils.logDebug("Initializing Google Tag Manager tracker");
        String resourceName = RITrackersConstants.GTM_CONTAINER_RESOURCE_NAME;
        String resourceType = RITrackersConstants.GTM_CONTAINER_RESOURCE_TYPE;
        boolean isResourceAvailable = RITrackingConfiguration.getInstance().isResourceAvailable(context, resourceName, resourceType);
        String containerId = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.GTM_CONTAINER_ID);
        if (isResourceAvailable && !TextUtils.isEmpty(containerId)) {
            createTracker(context, containerId);
            return true;
        } else {
            RILogUtils.logError("GoogleTagManager tracker ISSUE: you are missing (1) container file " +
                    "in raw folder of your app or (2) container ID in tracking properties");
        }
        return false;
    }

    private void createTracker(final Context context, String containerId) {
        TagManager tagManager = TagManager.getInstance(context);

        // Modify the log level of the logger to print out not only
        // warning and error messages, but also verbose, debug, info messages.
        RILogUtils.logDebug("GoogleTagManager set verbose logging");
        tagManager.setVerboseLoggingEnabled(RITracking.getInstance().isDebug());

        PendingResult<ContainerHolder> pending =
                tagManager.loadContainerPreferNonDefault(containerId, R.raw.gtm_container);

        // The onResult method will be called as soon as one of the following happens:
        //  - 1. a saved container is loaded
        //  - 2. if there is no saved container, a network container is loaded
        //  - 3. the request times out. The example below uses a constant to manage the timeout period.
        pending.setResultCallback(new ResultCallback<ContainerHolder>() {
            @Override
            public void onResult(ContainerHolder containerHolder) {
                RILogUtils.logDebug("GoogleTagManager received result");
                RIContainerHolder.setContainerHolder(containerHolder);
                if (!containerHolder.getStatus().isSuccess()) {
                    RILogUtils.logError(LOG_TAG, "Failure loading container");
                    return;
                }
                RIContainerHolder.setContainerHolder(containerHolder);
                containerHolder.setContainerAvailableListener(new RIContainerLoadedCallback());
                mDataLayer = TagManager.getInstance(context).getDataLayer();
            }
        }, 2, TimeUnit.SECONDS);

        mIdentifier = TRACKER_ID;
    }

    @Override
    public void trackEvent(String event, int value, String action, String category, Map<String, Object> data) {
        RILogUtils.logDebug("Google Tag Manager - Tracking event: " + event);

        if (mDataLayer == null) {
            RILogUtils.logError("Missing Google Tag Manager Data Layer reference");
            return;
        }

        clearTransactionDependencies();
        mDataLayer.pushEvent(event, data);
    }

    @Override
    public void trackScreenWithName(String name) {
        RILogUtils.logDebug("Google Tag Manager - Tracking screen with name: " + name);

        if (mDataLayer == null) {
            RILogUtils.logError("Missing Google Tag Manager Data Layer reference");
            return;
        }

        clearTransactionDependencies();
        String openScreen = RITrackersConstants.GTM_OPEN_SCREEN;
        String screenName = RITrackersConstants.GTM_SCREEN_NAME;
        mDataLayer.pushEvent(openScreen, DataLayer.mapOf(screenName, name));
    }

    @Override
    public void trackUserInfo(String userEvent, Map<String, Object> map) {
        RILogUtils.logDebug("Google Tag Manager - Tracking user event: " + userEvent);

        if (mDataLayer == null) {
            RILogUtils.logError("Missing Google Tag Manager Data Layer reference");
            return;
        }

        clearTransactionDependencies();
        mDataLayer.pushEvent(userEvent, map);
    }

    @Override
    public void updateDeviceInfo(Map<String, Object> map) {
        // Not used by this tracker
    }

    @Override
    public void updateGeoLocation(Location location) {
        // Not used by this tracker
    }

    @Override
    public void trackCheckoutTransaction(RITrackingTransaction transaction) {
        RILogUtils.logDebug("Google Tag Manager - Tracking checkout with transaction id: " +
                transaction.getTransactionId());

        if (mDataLayer == null) {
            RILogUtils.logError("Missing Google Tag Manager Data Layer reference");
            return;
        }

        Map<String, Object> transactionMap = createTransactionMap(transaction);
        mDataLayer.pushEvent(RITrackersConstants.GTM_TRANSACTION, transactionMap);
    }

    @Override
    public void trackAddProductToCart(RITrackingProduct product, String cartId, String location) {
        RILogUtils.logDebug("Google Tag Manager - Tracking add product with id " +
                product.getIdentifier() + " to cart");

        if (mDataLayer == null) {
            RILogUtils.logError("Missing Google Tag Manager Data Layer reference");
            return;
        }

        clearTransactionDependencies();
        Map<String, Object> addToCartMap = createProductMapForAddingToCart(product, location);
        mDataLayer.pushEvent(RITrackersConstants.GTM_PRODUCT_ADD_TO_CART, addToCartMap);
    }

    @Override
    public void trackRemoveProductFromCart(RITrackingProduct product, int quantity, double cartValue) {
        RILogUtils.logDebug("Google Tag Manager - Tracking remove product with id " +
                product.getIdentifier() + " from cart");

        if (mDataLayer == null) {
            RILogUtils.logError("Missing Google Tag Manager Data Layer reference");
            return;
        }

        clearTransactionDependencies();
        Map<String, Object> removeFromCart = createProductMapToRemoveFromCart(product, quantity, cartValue);
        mDataLayer.pushEvent(RITrackersConstants.GTM_PRODUCT_REMOVE_FROM_CART, removeFromCart);
    }

    /**
     * This method return a map to be sent to the DataLayer for tracking checkout transaction.
     * This spreadsheet has been used as a reference to create transaction
     * https://docs.google.com/a/rocket-internet.de/spreadsheet/ccc?key=0AhBBVxg73HxhdG56RHVveEFad2ZrMHN2Q1lKcGpzbmc&usp=drive_web#gid=2
     *
     * @param transaction The transaction object containing the information
     * @return The map for the current transaction
     */
    private Map<String, Object> createTransactionMap(RITrackingTransaction transaction) {
        Map<String, Object> transactionMap = new HashMap<String, Object>();

        transactionMap.put(RITrackersConstants.GTM_TRANSACTION_ID, transaction.getTransactionId());
        transactionMap.put(RITrackersConstants.GTM_TRANSACTION_AFFILIATION, transaction.getAffiliation());
        transactionMap.put(RITrackersConstants.GTM_PAYMENT_METHOD, transaction.getPaymentMethod().getValue());
        transactionMap.put(RITrackersConstants.GTM_VOUCHER_AMOUNT, transaction.getVoucherAmount());
        transactionMap.put(RITrackersConstants.GTM_PREV_PURCHASES, transaction.getNumberOfPreviousPurchases());

        // Check for the RITrackingTotal object and add its values to the map
        RITrackingTotal transactionTotal = transaction.getTotal();
        if (transactionTotal != null) {
            transactionMap.put(RITrackersConstants.GTM_TRANSACTION_TOTAL, transactionTotal.getNet());
            transactionMap.put(RITrackersConstants.GTM_TRANSACTION_SHIPPING, transactionTotal.getShipping());
            transactionMap.put(RITrackersConstants.GTM_TRANSACTION_TAX, transactionTotal.getTax());
            transactionMap.put(RITrackersConstants.GTM_TRANSACTION_CURRENCY, transactionTotal.getCurrency());
        }

        // Loop through the products list of the checkout and add information to the map
        List<RITrackingProduct> productList = transaction.getProductsList();
        if (productList != null && productList.size() > 0) {
            List<Map<String, Object>> productsMaps = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < productList.size(); i++) {
                RITrackingProduct product = productList.get(i);
                Map<String, Object> productMap = new HashMap<String, Object>();

                productMap.put(RITrackersConstants.GTM_NAME, product.getName());
                productMap.put(RITrackersConstants.GTM_SKU, product.getIdentifier());
                productMap.put(RITrackersConstants.GTM_CATEGORY, product.getCategory());
                productMap.put(RITrackersConstants.GTM_PRICE, product.getPrice());
                productMap.put(RITrackersConstants.GTM_CURRENCY, product.getCurrency());
                productMap.put(RITrackersConstants.GTM_QUANTITY, product.getQuantity());
                productsMaps.add(productMap);
            }
            transactionMap.put(RITrackersConstants.GTM_TRANSACTION_PRODUCTS, productsMaps);
        }

        return transactionMap;
    }

    /**
     * This method return a map to be sent to the DataLayer representing an add a product to cart action.
     * This spreadsheet has been used as a reference to create transaction
     * https://docs.google.com/a/rocket-internet.de/spreadsheet/ccc?key=0AhBBVxg73HxhdG56RHVveEFad2ZrMHN2Q1lKcGpzbmc&usp=drive_web#gid=2
     *
     * @param product  The product that has been added
     * @param location Location in the app from where the product was added to the cart
     * @return The map for the added product
     */
    private Map<String, Object> createProductMapForAddingToCart(RITrackingProduct product, String location) {
        Map<String, Object> addToCartMap = new HashMap<String, Object>();

        addToCartMap.put(RITrackersConstants.GTM_PRODUCT_SKU, product.getIdentifier());
        addToCartMap.put(RITrackersConstants.GTM_PRODUCT_BRAND, product.getBrand());
        addToCartMap.put(RITrackersConstants.GTM_PRODUCT_CATEGORY, product.getCategory());
        addToCartMap.put(RITrackersConstants.GTM_PRODUCT_SUBCATEGORY, product.getSubCategory());
        addToCartMap.put(RITrackersConstants.GTM_PRODUCT_PRICE, product.getPrice());
        addToCartMap.put(RITrackersConstants.GTM_CURRENCY, product.getCurrency());
        addToCartMap.put(RITrackersConstants.GTM_DISCOUNT, product.getDiscount());
        addToCartMap.put(RITrackersConstants.GTM_PRODUCT_QUANTITY, product.getQuantity());
        addToCartMap.put(RITrackersConstants.GTM_LOCATION, location);
        addToCartMap.put(RITrackersConstants.GTM_AVERAGE_RATING_TOTAL, product.getAverageRating());

        return addToCartMap;
    }

    /**
     * This method return a map to be sent to the DataLayer representing a remove a product from cart action.
     * This spreadsheet has been used as a reference to create transaction
     * https://docs.google.com/a/rocket-internet.de/spreadsheet/ccc?key=0AhBBVxg73HxhdG56RHVveEFad2ZrMHN2Q1lKcGpzbmc&usp=drive_web#gid=2
     *
     * @param product   The product that has been added
     * @param quantity  The amount of products to be removed from the cart
     * @param cartValue The value of the cart before removing products
     * @return The map for the removed product
     */
    private Map<String, Object> createProductMapToRemoveFromCart(RITrackingProduct product, int quantity, double cartValue) {
        Map<String, Object> removeFromCartMap = new HashMap<String, Object>();

        removeFromCartMap.put(RITrackersConstants.GTM_PRODUCT_SKU, product.getIdentifier());
        removeFromCartMap.put(RITrackersConstants.GTM_PRODUCT_PRICE, product.getPrice());
        removeFromCartMap.put(RITrackersConstants.GTM_AVERAGE_RATING_TOTAL, product.getAverageRating());
        removeFromCartMap.put(RITrackersConstants.GTM_QUANTITY_CART, quantity);
        removeFromCartMap.put(RITrackersConstants.GTM_CART_VALUE, cartValue);

        return removeFromCartMap;
    }

    /**
     * Utility method that is used because the data layer is persistent. Transaction and item
     * variable values should be reset to null once a transaction has been pushed to the data layer.
     * Every time that an event different from a transaction is pushed to the data layer transaction
     * are cleaned.
     */
    private void clearTransactionDependencies() {
        if (mDataLayer != null) {
            mDataLayer.pushEvent(RITrackersConstants.GTM_TRANSACTION, null);
        }
    }
}
