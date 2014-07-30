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

    public void testInitialization() {
        RITrackingTotal total = new RITrackingTotal(5, 0.8f, 15, "dol");

        assertEquals(5, total.getNet());
        assertEquals(0.8f, total.getTax());
        assertEquals(15, total.getShipping());
        assertEquals("dol", total.getCurrency());
    }
}
