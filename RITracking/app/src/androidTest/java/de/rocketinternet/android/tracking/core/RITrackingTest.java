package de.rocketinternet.android.tracking.core;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.test.InstrumentationTestCase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrlMockImpl;
import de.rocketinternet.android.tracking.models.RITrackingTransaction;
import de.rocketinternet.android.tracking.trackers.RITracker;
import de.rocketinternet.android.tracking.trackers.mocks.RIAd4PushTrackerMock;
import de.rocketinternet.android.tracking.trackers.mocks.RIAdJustTrackerMock;
import de.rocketinternet.android.tracking.trackers.mocks.RIBugSenseTrackerMock;
import de.rocketinternet.android.tracking.trackers.mocks.RIGoogleAnalyticsTrackerMock;
import de.rocketinternet.android.tracking.trackers.mocks.RIGoogleTagManagerTrackerMock;

/**
 * @author alessandro.balocco
 */
public class RITrackingTest extends InstrumentationTestCase {

    private RIGoogleAnalyticsTrackerMock mGoogleAnalyticsTrackerMock;
    private RIGoogleTagManagerTrackerMock mGoogleTagManagerTrackerMock;
    private RIAd4PushTrackerMock mAd4PushTrackerMock;
    private RIAdJustTrackerMock mAdJustTrackerMock;
    private RIBugSenseTrackerMock mBugSenseTrackerMock;

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
        mAd4PushTrackerMock = new RIAd4PushTrackerMock();
        mAdJustTrackerMock = new RIAdJustTrackerMock();
        mBugSenseTrackerMock = new RIBugSenseTrackerMock();
        trackers.add(mGoogleAnalyticsTrackerMock);
        trackers.add(mGoogleTagManagerTrackerMock);
        trackers.add(mAd4PushTrackerMock);
        trackers.add(mAdJustTrackerMock);
        trackers.add(mBugSenseTrackerMock);
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
        assertEquals(0, mAd4PushTrackerMock.getNumberOfTrackedEvents());
        assertFalse(mAd4PushTrackerMock.isEventTracked());
        assertEquals(0, mAdJustTrackerMock.getNumberOfTrackedEvents());
        assertFalse(mAdJustTrackerMock.isEventTracked());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(4);
        mGoogleAnalyticsTrackerMock.setSignal(latch);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        mAd4PushTrackerMock.setSignal(latch);
        mAdJustTrackerMock.setSignal(latch);
        RITracking.getInstance().trackEvent("", 0, "", "", new HashMap<String, Object>());
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(1, mGoogleAnalyticsTrackerMock.getNumberOfTrackedEvents());
        assertTrue(mGoogleAnalyticsTrackerMock.isEventTracked());
        assertEquals(1, mGoogleTagManagerTrackerMock.getNumberOfTrackedEvents());
        assertTrue(mGoogleTagManagerTrackerMock.isEventTracked());
        assertEquals(1, mAd4PushTrackerMock.getNumberOfTrackedEvents());
        assertTrue(mAd4PushTrackerMock.isEventTracked());
        assertEquals(1, mAdJustTrackerMock.getNumberOfTrackedEvents());
        assertTrue(mAdJustTrackerMock.isEventTracked());
    }

    public void testTrackScreenName() throws InterruptedException {
        String screenName = "HomePage";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleAnalyticsTrackerMock.getLastTrackedScreenName()));
        assertTrue(TextUtils.isEmpty(mGoogleTagManagerTrackerMock.getLastTrackedScreenName()));
        assertTrue(TextUtils.isEmpty(mAd4PushTrackerMock.getLastTrackedScreenName()));
        assertTrue(TextUtils.isEmpty(mBugSenseTrackerMock.getLastLeftBreadCrumb()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(4);
        mGoogleAnalyticsTrackerMock.setSignal(latch);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        mAd4PushTrackerMock.setSignal(latch);
        mBugSenseTrackerMock.setSignal(latch);
        RITracking.getInstance().trackScreenWithName(screenName);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(screenName, mGoogleAnalyticsTrackerMock.getLastTrackedScreenName());
        assertEquals(screenName, mGoogleTagManagerTrackerMock.getLastTrackedScreenName());
        assertEquals(screenName, mAd4PushTrackerMock.getLastTrackedScreenName());
        assertEquals(screenName, mBugSenseTrackerMock.getLastLeftBreadCrumb());
    }

    public void testTrackUserInfo() throws InterruptedException {
        String userEvent = "Today is my birthday";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleTagManagerTrackerMock.getLastUserEvent()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        RITracking.getInstance().trackUserInfo(userEvent, new HashMap<String, Object>());
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(userEvent, mGoogleTagManagerTrackerMock.getLastUserEvent());
    }

    public void testUpdateDeviceInfo() throws InterruptedException {
        Map<String, Object> deviceInfo = new HashMap<String, Object>();
        deviceInfo.put("userId", "0000000000");

        // Evaluate initial situation
        assertNull(mAd4PushTrackerMock.getDeviceInfo());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mAd4PushTrackerMock.setSignal(latch);
        RITracking.getInstance().updateDeviceInfo(deviceInfo);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertTrue(mAd4PushTrackerMock.getDeviceInfo().containsKey("userId"));
    }

    public void testUpdateGeolocation() throws InterruptedException {
        Location location = new Location("testLocation");
        location.setLatitude(50.0);
        location.setLongitude(20.0);

        // Evaluate initial situation
        assertNull(mAd4PushTrackerMock.getLastLocation());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mAd4PushTrackerMock.setSignal(latch);
        RITracking.getInstance().updateGeoLocation(location);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(50.0, mAd4PushTrackerMock.getLastLocation().getLatitude());
        assertEquals(20.0, mAd4PushTrackerMock.getLastLocation().getLongitude());
    }

    public void testTrackException() throws InterruptedException {
        Exception exception = new Exception("Something weird happened!");

        // Evaluate initial situation
        assertNull(mGoogleAnalyticsTrackerMock.getLastTrackedException());
        assertNull(mBugSenseTrackerMock.getLastCaughtException());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(2);
        mGoogleAnalyticsTrackerMock.setSignal(latch);
        mBugSenseTrackerMock.setSignal(latch);
        RITracking.getInstance().trackException(new HashMap<String, String>(), exception);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(exception, mGoogleAnalyticsTrackerMock.getLastTrackedException());
        assertEquals(exception, mBugSenseTrackerMock.getLastCaughtException());
    }

    public void testTrackCheckoutWithTransactionId() throws InterruptedException {
        String transactionId = "id56fca3drt5d";
        RITrackingTransaction transaction = new RITrackingTransaction();
        transaction.setTransactionId(transactionId);

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleAnalyticsTrackerMock.getLastCheckoutTransaction()));
        assertTrue(TextUtils.isEmpty(mGoogleTagManagerTrackerMock.getLastCheckoutTransaction()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(2);
        mGoogleAnalyticsTrackerMock.setSignal(latch);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        RITracking.getInstance().trackCheckoutTransaction(transaction);
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

        // Evaluate initial situation
        assertNull(mAdJustTrackerMock.getLastTrackerUri());

        // Set count down to 0, here is used only for waiting notification
        CountDownLatch latch = new CountDownLatch(1);
        mAdJustTrackerMock.setSignal(latch);
        RITracking.getInstance().trackOpenUrl(uri);
        latch.await(3, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());
        assertTrue(listener.isHandlerCalled());
        assertEquals("Handler2", listener.getHandlerIdentifier());
        assertTrue(listener.getQueryParams().containsKey("handler2"));
        assertEquals(uri, mAdJustTrackerMock.getLastTrackerUri());
    }

    public void testTrackActivityCreatedAsSplashScreen() throws InterruptedException {
        Activity mockActivity = new Activity();

        // Evaluate initial situation
        assertFalse(mAd4PushTrackerMock.wasActivityCreated());
        assertFalse(mAd4PushTrackerMock.wasActivitySplashScreen());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mAd4PushTrackerMock.setSignal(latch);
        RITracking.getInstance().trackActivityCreated(mockActivity, true);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertTrue(mAd4PushTrackerMock.wasActivityCreated());
        assertTrue(mAd4PushTrackerMock.wasActivitySplashScreen());
    }

    public void testTrackActivityResumed() throws InterruptedException {
        Activity mockActivity = new Activity();

        // Evaluate initial situation
        assertFalse(mAd4PushTrackerMock.wasActivityResumed());
        assertFalse(mAdJustTrackerMock.wasActivityResumed());
        assertFalse(mBugSenseTrackerMock.wasActivityResumed());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(3);
        mAd4PushTrackerMock.setSignal(latch);
        mAdJustTrackerMock.setSignal(latch);
        mBugSenseTrackerMock.setSignal(latch);
        RITracking.getInstance().trackActivityResumed(mockActivity);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertTrue(mAd4PushTrackerMock.wasActivityResumed());
        assertTrue(mAdJustTrackerMock.wasActivityResumed());
        assertTrue(mBugSenseTrackerMock.wasActivityResumed());
    }

    public void testTrackActivityPaused() throws InterruptedException {
        Activity mockActivity = new Activity();

        // Evaluate initial situation
        assertFalse(mAd4PushTrackerMock.wasActivityPaused());
        assertFalse(mAdJustTrackerMock.wasActivityPaused());
        assertFalse(mBugSenseTrackerMock.wasActivityPaused());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(3);
        mAd4PushTrackerMock.setSignal(latch);
        mAdJustTrackerMock.setSignal(latch);
        mBugSenseTrackerMock.setSignal(latch);
        RITracking.getInstance().trackActivityPaused(mockActivity);
        latch.await(2, TimeUnit.SECONDS);

        // Validate results
        assertEquals(0, latch.getCount());

        assertTrue(mAd4PushTrackerMock.wasActivityPaused());
        assertTrue(mAdJustTrackerMock.wasActivityPaused());
        assertTrue(mBugSenseTrackerMock.wasActivityPaused());
    }
}
