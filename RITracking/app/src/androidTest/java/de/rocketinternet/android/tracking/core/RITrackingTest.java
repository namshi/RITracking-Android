package de.rocketinternet.android.tracking.core;

import android.net.Uri;
import android.test.InstrumentationTestCase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import de.rocketinternet.android.tracking.handlers.RIOpenUrlHandler;
import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrlMockImpl;
import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.trackers.mocks.RIGoogleAnalyticsTrackerMock;
import de.rocketinternet.android.tracking.trackers.RITracker;
import de.rocketinternet.android.tracking.trackers.mocks.RIGoogleTagManagerTrackerMock;

/**
 * @author alessandro.balocco
 */
public class RITrackingTest extends InstrumentationTestCase {

    private RIGoogleAnalyticsTrackerMock mGoogleAnalyticsTrackerMock;
    private RIGoogleTagManagerTrackerMock mGoogleTagManagerTrackerMock;

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
        mGoogleAnalyticsTrackerMock = new RIGoogleAnalyticsTrackerMock();
        mGoogleTagManagerTrackerMock = new RIGoogleTagManagerTrackerMock();
        trackers.add(mGoogleAnalyticsTrackerMock);
        trackers.add(mGoogleTagManagerTrackerMock);
        RITracking.getInstance().addTrackers(trackers);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        RITracking.getInstance().reset();
    }

    public void testTrackEvent() throws InterruptedException {
        // Evaluate initial situation
        assertEquals(0, mGoogleAnalyticsTrackerMock.getNumberOfTrackedEvents());
        assertFalse(mGoogleAnalyticsTrackerMock.isEventTracked());
        assertEquals(0, mGoogleTagManagerTrackerMock.getNumberOfTrackedEvents());
        assertFalse(mGoogleTagManagerTrackerMock.isEventTracked());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(2);
        mGoogleAnalyticsTrackerMock.setSignal(latch);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        RITracking.getInstance().trackEvent("", 0, "", "", new HashMap<String, Object>());
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(1, mGoogleAnalyticsTrackerMock.getNumberOfTrackedEvents());
        assertTrue(mGoogleAnalyticsTrackerMock.isEventTracked());
        assertEquals(1, mGoogleTagManagerTrackerMock.getNumberOfTrackedEvents());
        assertTrue(mGoogleTagManagerTrackerMock.isEventTracked());
    }

    public void testTrackScreenName() throws InterruptedException {
        String screenName = "HomePage";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleAnalyticsTrackerMock.getLastTrackedScreenName()));
        assertTrue(TextUtils.isEmpty(mGoogleTagManagerTrackerMock.getLastTrackedScreenName()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(2);
        mGoogleAnalyticsTrackerMock.setSignal(latch);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        RITracking.getInstance().trackScreenWithName(screenName);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(screenName, mGoogleAnalyticsTrackerMock.getLastTrackedScreenName());
        assertEquals(screenName, mGoogleTagManagerTrackerMock.getLastTrackedScreenName());
    }

    public void testTrackUser() throws InterruptedException {
        String userEvent = "Today is my birthday";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleTagManagerTrackerMock.getLastUserEvent()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        RITracking.getInstance().trackUser(userEvent, new HashMap<String, Object>());
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(userEvent, mGoogleTagManagerTrackerMock.getLastUserEvent());

    }

    public void testTrackExceptionWithName() throws InterruptedException {
        String exception = "Something weird happened!";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleAnalyticsTrackerMock.getLastTrackedException()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mGoogleAnalyticsTrackerMock.setSignal(latch);
        RITracking.getInstance().trackExceptionWithName(exception);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());
        assertEquals(exception, mGoogleAnalyticsTrackerMock.getLastTrackedException());
    }

    public void testTrackCheckoutWithTransactionId() throws InterruptedException {
        String transactionId = "id56fca3drt5d";
        RITrackingTotal total = new RITrackingTotal(20, 0.2f, 3, "eur");

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleAnalyticsTrackerMock.getLastCheckoutTransaction()));
        assertTrue(TextUtils.isEmpty(mGoogleTagManagerTrackerMock.getLastCheckoutTransaction()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(2);
        mGoogleAnalyticsTrackerMock.setSignal(latch);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        RITracking.getInstance().trackCheckoutWithTransactionId(transactionId, total);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(transactionId, mGoogleAnalyticsTrackerMock.getLastCheckoutTransaction());
        assertEquals(transactionId, mGoogleTagManagerTrackerMock.getLastCheckoutTransaction());
    }

    public void testTrackUrl() throws InterruptedException {
        // Calling Uri
        Uri uri = Uri.parse("schema://testHost/testPath?handler2=value1&key2=value2");

        // Create listener
        RIOnHandledOpenUrlMockImpl listener = new RIOnHandledOpenUrlMockImpl();

        // Register handlers
        RITracking.getInstance().registerHandler("Handler1", "testHost1", "testPathOne", listener);
        RITracking.getInstance().registerHandler("Handler2", "testHost", "testPath", listener);
        RITracking.getInstance().registerHandler("Handler3", "testHost3", "testPathThree", listener);

        // Set count down to 0, here is used only for waiting notification
        CountDownLatch latch = new CountDownLatch(0);
        RITracking.getInstance().trackOpenUrl(uri);
        latch.await(4, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());
        assertTrue(listener.isHandlerCalled());
        assertEquals("Handler2", listener.getHandlerIdentifier());
        assertTrue(listener.getQueryParams().containsKey("handler2"));
    }
}
