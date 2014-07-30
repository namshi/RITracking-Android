package de.rocketinternet.android.tracking.interfaces;

import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTotal;

/**
 * @author alessandro.balocco
 *
 * This interface implements tracking for e-commerce transactions
 *
 * The implementation to this interface should maintain a state machine to collect cart information.
 * Adding/Removing to/from cart is forwarded to Ad-X and A4S (http://goo.gl/iSjKut) instantly.
 * Both:
 *      A4S (http://www.ad4screen.com/DocSDK/doku.php?id=events)
 *      GA (https://developers.google.com/analytics/devguides/collection/android/v4/ecommerce)
 * receive information on checkout.
 */
public interface RIEcommerceEventTracking {

    /**
     * This method with include any previous calls to trackAddToCartForProductWithID and
     * trackRemoveFromCartForProductWithID.
     *
     * @param idTransaction The transaction ID
     * @param total         RITrackingProduct product
     */
    void trackCheckoutWithTransactionId(String idTransaction, RITrackingTotal total);

    /**
     * Track a product that was added to the cart
     *
     * @param product The product added
     */
    void trackProductAddToCart(RITrackingProduct product);

    /**
     * Track a product that was removed from the cart
     *
     * @param idTransaction The transaction ID
     * @param quantity      The quantity removed from the cart
     */
    void trackRemoveFromCartForProductWithID(String idTransaction, int quantity);
}
