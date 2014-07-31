package de.rocketinternet.android.tracking.models;

import android.test.InstrumentationTestCase;

/**
 * @author alessandro.balocco
 */
public class RITrackingTotalTest extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInitializationWithValues() {
        RITrackingTotal total = new RITrackingTotal(5, 0.8f, 15, "dol");

        assertEquals(5, total.getNet());
        assertEquals(0.8f, total.getTax());
        assertEquals(15, total.getShipping());
        assertEquals("dol", total.getCurrency());
    }

    public void testSetters() {
        RITrackingTotal total = new RITrackingTotal();

        total.setNet(5);
        assertEquals(5, total.getNet());

        total.setTax(0.8f);
        assertEquals(0.8f, total.getTax());

        total.setShipping(15);
        assertEquals(15, total.getShipping());

        total.setCurrency("dol");
        assertEquals("dol", total.getCurrency());
    }
}
