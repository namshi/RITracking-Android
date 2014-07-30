package de.rocketinternet.android.tracking.models;

import android.test.InstrumentationTestCase;

import java.util.Properties;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.trackers.RIGoogleAnalyticsTracker;
import de.rocketinternet.android.tracking.utils.FileUtils;

/**
 * @author alessandro.balocco
 */
public class RITrackingProductTest extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInitialization() {
        RITrackingProduct product = new RITrackingProduct("product5", "productName", 5, 5.0, "eur", "productCategory");

        assertEquals("product5", product.getIdentifier());
        assertEquals("productName", product.getName());
        assertEquals(5, product.getQuantity());
        assertEquals(5.0, product.getPrice());
        assertEquals("eur", product.getCurrency());
        assertEquals("productCategory", product.getCategory());
    }
}
