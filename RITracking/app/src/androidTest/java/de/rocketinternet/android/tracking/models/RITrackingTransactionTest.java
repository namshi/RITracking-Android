package de.rocketinternet.android.tracking.models;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;

import de.rocketinternet.android.tracking.models.enums.RITrackingPaymentMethod;

/**
 * @author alessandro.balocco
 */
public class RITrackingTransactionTest extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInitializationWithValues() {
        // Create testing total
        RITrackingTotal total = new RITrackingTotal();
        total.setNet(100.50);

        // Create testing list of products
        List<RITrackingProduct> productsList = new ArrayList<RITrackingProduct>();
        productsList.add(new RITrackingProduct());
        productsList.add(new RITrackingProduct());

        RITrackingTransaction transaction = new RITrackingTransaction("transactionId", "transactionAffiliation",
                RITrackingPaymentMethod.CREDIT_CARD, 30.5f, 5, total, productsList);

        assertEquals("transactionId", transaction.getTransactionId());
        assertEquals("transactionAffiliation", transaction.getAffiliation());
        assertEquals(RITrackingPaymentMethod.CREDIT_CARD, transaction.getPaymentMethod());
        assertEquals(30.5f, transaction.getVoucherAmount());
        assertEquals(5, transaction.getNumberOfPreviousPurchases());
        assertEquals(100.50, transaction.getTotal().getNet());
        assertEquals(2, transaction.getProductsList().size());
    }

    public void testSetters() {
        RITrackingTransaction transaction = new RITrackingTransaction();

        transaction.setTransactionId("transactionId");
        assertEquals("transactionId", transaction.getTransactionId());

        transaction.setAffiliation("transactionAffiliation");
        assertEquals("transactionAffiliation", transaction.getAffiliation());

        transaction.setPaymentMethod(RITrackingPaymentMethod.CREDIT_CARD);
        assertEquals(RITrackingPaymentMethod.CREDIT_CARD, transaction.getPaymentMethod());

        transaction.setVoucherAmount(30.5f);
        assertEquals(30.5f, transaction.getVoucherAmount());

        transaction.setNumberOfPreviousPurchases(5);
        assertEquals(5, transaction.getNumberOfPreviousPurchases());

        RITrackingTotal total = new RITrackingTotal();
        total.setNet(100.50);
        transaction.setTrackingTotal(total);
        assertEquals(100.50, transaction.getTotal().getNet());

        List<RITrackingProduct> productsList = new ArrayList<RITrackingProduct>();
        productsList.add(new RITrackingProduct());
        productsList.add(new RITrackingProduct());
        transaction.setProductsList(productsList);
        assertEquals(2, transaction.getProductsList().size());
    }
}
