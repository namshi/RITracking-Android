package de.rocketinternet.android.tracking.interfaces;

import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTransaction;

/**
 * @author alessandro.balocco
 *
 * This interface implements tracking for e-commerce transactions
 *
 * The implementation to this interface should maintain a state machine to collect cart information.
 * Adding/Removing to/from cart is forwarded to A4S (http://goo.gl/iSjKut) instantly.
 */
public interface RIEcommerceEventTracking {

    /**
     * This method with include any previous calls to trackAddToCartForProductWithID and
     * trackRemoveProductFromCart.
     *
     * @param transaction The checkout transaction
     */
    void trackCheckoutTransaction(RITrackingTransaction transaction);

    /**
     * Track a product that was added to the cart
     *
     * @param product  The product added to the cart
     * @param cartId   The id of the current cart
     * @param location The location in the app from where the product was added to the cart
     */
    void trackAddProductToCart(RITrackingProduct product, String cartId, String location);

    /**
     * Track a product that was removed from the cart
     *
     * @param product   The product removed from the cart
     * @param quantity  The amount of products removed from the cart
     * @param cartValue The total value of the cart before product removal
     */
    void trackRemoveProductFromCart(RITrackingProduct product, int quantity, double cartValue);
}
