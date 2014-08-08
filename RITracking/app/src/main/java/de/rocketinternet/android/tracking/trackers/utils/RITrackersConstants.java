package de.rocketinternet.android.tracking.trackers.utils;

/**
 * @author alessandro.balocco
 *         <p/>
 *         This utility class contains all static constant for all trackers. It is possible to store here
 *         constants that remain static among different apps.
 */
public class RITrackersConstants {

    /**
     * ******************
     */
    /* Google Analytics - Deprecated */
    public static final String GA_TRACKING_ID = "RIGoogleAnalyticsTrackingID";
    public static final String GA_CHECKOUT_SCREEN_NAME = "transaction";
    public static final String GA_CURRENCY_KEY = "&cu";

    /**
     * ******************
     */
    /* Google Tag Manager */
    public static final String GTM_CONTAINER_ID = "RIGoogleTagManagerContainerID";
    public static final String GTM_CONTAINER_RESOURCE_NAME = "gtm_container";
    public static final String GTM_CONTAINER_RESOURCE_TYPE = "raw";
    public static final String GTM_SCREEN_NAME = "screenName";
    public static final String GTM_OPEN_APP = "openApp";
    public static final String GTM_OPEN_SCREEN = "openScreen";
    // Checkout constant
    public static final String GTM_TRANSACTION = "transaction";
    public static final String GTM_PAYMENT_METHOD = "paymentMethod";
    public static final String GTM_VOUCHER_AMOUNT = "voucherAmount";
    public static final String GTM_PREV_PURCHASES = "previousPurchases";
    public static final String GTM_TRANSACTION_ID = "transactionId";
    public static final String GTM_TRANSACTION_AFFILIATION = "transactionAffiliation";
    public static final String GTM_TRANSACTION_TOTAL = "transactionTotal";
    public static final String GTM_TRANSACTION_SHIPPING = "transactionShipping";
    public static final String GTM_TRANSACTION_TAX = "transactionTax";
    public static final String GTM_TRANSACTION_CURRENCY = "transactionCurrency";
    public static final String GTM_TRANSACTION_PRODUCTS = "transactionProducts";
    public static final String GTM_NAME = "name";
    public static final String GTM_SKU = "sku";
    public static final String GTM_CATEGORY = "category";
    public static final String GTM_PRICE = "price";
    public static final String GTM_CURRENCY = "currency";
    public static final String GTM_QUANTITY = "quantity";
    public static final String GTM_DISCOUNT = "discount";
    public static final String GTM_LOCATION = "location";
    public static final String GTM_AVERAGE_RATING_TOTAL = "productQuantity";
    public static final String GTM_QUANTITY_CART = "quantityCart";
    public static final String GTM_CART_VALUE = "cartValue";
    // Product constant
    public static final String GTM_PRODUCT_ADD_TO_CART = "addToCart";
    public static final String GTM_PRODUCT_REMOVE_FROM_CART = "removeFromCart";
    public static final String GTM_PRODUCT_SKU = "productSKU";
    public static final String GTM_PRODUCT_BRAND = "productBrand";
    public static final String GTM_PRODUCT_CATEGORY = "productCategory";
    public static final String GTM_PRODUCT_SUBCATEGORY = "productSubcategory";
    public static final String GTM_PRODUCT_PRICE = "productPrice";
    public static final String GTM_PRODUCT_QUANTITY = "productQuantity";

    /**********************/
    /* Ad4Push */
    public static final String AD4PUSH_INTEGRATION = "RIAd4PushIntegration";
    public static final String AD4PUSH_VIEW = "view";

    /**********************/
    /* Ad4Just */
    public static final String ADJUST_INTEGRATION = "RIAdJustIntegration";

    /**********************/
    /* BugSense */
    public static final String BUGSENSE_API_KEY = "RIBugSenseApiKey";
}
