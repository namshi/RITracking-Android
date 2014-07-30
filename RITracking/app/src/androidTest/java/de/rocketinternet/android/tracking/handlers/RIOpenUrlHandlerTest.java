package de.rocketinternet.android.tracking.handlers;

import android.test.InstrumentationTestCase;

import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrlMockImpl;

/**
 * @author alessandro.balocco
 */
public class RIOpenUrlHandlerTest extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testWrongPatternFoundAndMissedListenerNotification() {
        RIOnHandledOpenUrlMockImpl listener = new RIOnHandledOpenUrlMockImpl();

        // For the moment left on hold while waiting how to parse info
    }

    public void testRightPatternFoundAndSuccessfulListenerNotification() {
        RIOnHandledOpenUrlMockImpl listener = new RIOnHandledOpenUrlMockImpl();

        // For the moment left on hold while waiting how to parse info
    }
}
