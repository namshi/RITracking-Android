package de.rocketinternet.android.tracking.core;

import android.test.InstrumentationTestCase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.trackers.mocks.RIGoogleAnalyticsTrackerMock;
import de.rocketinternet.android.tracking.trackers.RITracker;

/**
 * @author alessandro.balocco
 */
public class RITrackingTest extends InstrumentationTestCase {

    private RIGoogleAnalyticsTrackerMock mGoogleAnalyticsTracker;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        RITracking.getInstance().reset();
        RITracking.getInstance().setDebug(true);

        // Needed when using Mokito to mock objects
//        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        createTrackersList();
    }

    private void createTrackersList() {
        List<RITracker> trackers = new ArrayList<RITracker>();
        mGoogleAnalyticsTracker = new RIGoogleAnalyticsTrackerMock();
        trackers.add(mGoogleAnalyticsTracker);
        RITracking.getInstance().initTrackers(trackers);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        RITracking.getInstance().reset();
    }

    public void testTrackEvent() throws InterruptedException {
        // Evaluate initial situation
        assertEquals(0, mGoogleAnalyticsTracker.getNumberOfTrackedEvents());
        assertFalse(mGoogleAnalyticsTracker.isEventTracked());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mGoogleAnalyticsTracker.setSignal(latch);
        RITracking.getInstance().trackEvent("", 0, "", "", new HashMap<String, Object>());
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());
        assertEquals(1, mGoogleAnalyticsTracker.getNumberOfTrackedEvents());
        assertTrue(mGoogleAnalyticsTracker.isEventTracked());
    }

    public void testTrackScreenName() throws InterruptedException {
        String screenName = "HomePage";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleAnalyticsTracker.getLastTrackedScreenName()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mGoogleAnalyticsTracker.setSignal(latch);
        RITracking.getInstance().trackScreenWithName(screenName);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());
        assertEquals(screenName, mGoogleAnalyticsTracker.getNumberOfTrackedEvents());
    }

    public void testTrackExceptionWithName() throws InterruptedException {
        String exception = "Something weird happened!";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleAnalyticsTracker.getLastTrackedException()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mGoogleAnalyticsTracker.setSignal(latch);
        RITracking.getInstance().trackScreenWithName(exception);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());
        assertEquals(exception, mGoogleAnalyticsTracker.getNumberOfTrackedEvents());
    }

    public void testTrackCheckoutWithTransactionId() throws InterruptedException {
        String transactionId = "id56fca3drt5d";
        RITrackingTotal total = new RITrackingTotal(20, 0.2f, 3, "eur");

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleAnalyticsTracker.getLastCheckoutTransaction()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mGoogleAnalyticsTracker.setSignal(latch);
        RITracking.getInstance().trackCheckoutWithTransactionId(transactionId, total);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());
        assertEquals(transactionId, mGoogleAnalyticsTracker.getLastCheckoutTransaction());
    }
}
