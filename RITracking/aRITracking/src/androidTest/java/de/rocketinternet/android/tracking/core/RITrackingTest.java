package de.rocketinternet.android.tracking.core;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.test.InstrumentationTestCase;
import android.text.TextUtils;

import com.newrelic.agent.android.util.NetworkFailure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrlMockImpl;
import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTransaction;
import de.rocketinternet.android.tracking.trackers.RITracker;
import de.rocketinternet.android.tracking.trackers.mocks.RIAd4PushTrackerMock;
import de.rocketinternet.android.tracking.trackers.mocks.RIAdJustTrackerMock;
import de.rocketinternet.android.tracking.trackers.mocks.RIBugSenseTrackerMock;
import de.rocketinternet.android.tracking.trackers.mocks.RIGoogleAnalyticsTrackerMock;
import de.rocketinternet.android.tracking.trackers.mocks.RIGoogleTagManagerTrackerMock;
import de.rocketinternet.android.tracking.trackers.mocks.RINewRelicTrackerMock;

/**
 * @author alessandro.balocco
 */
public class RITrackingTest extends InstrumentationTestCase {

    private RIGoogleAnalyticsTrackerMock mGoogleAnalyticsTrackerMock;
    private RIGoogleTagManagerTrackerMock mGoogleTagManagerTrackerMock;
    private RIAd4PushTrackerMock mAd4PushTrackerMock;
    private RIAdJustTrackerMock mAdJustTrackerMock;
    private RIBugSenseTrackerMock mBugSenseTrackerMock;
    private RINewRelicTrackerMock mNewRelicTrackerMock;

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
        mNewRelicTrackerMock = new RINewRelicTrackerMock();

        trackers.add(mGoogleAnalyticsTrackerMock);
        trackers.add(mGoogleTagManagerTrackerMock);
        trackers.add(mAd4PushTrackerMock);
        trackers.add(mAdJustTrackerMock);
        trackers.add(mBugSenseTrackerMock);
        trackers.add(mNewRelicTrackerMock);
        RITracking.getInstance().addTrackers(trackers);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        RITracking.getInstance().reset();
    }

    public void testTrackEvent() {
        // Evaluate initial situation
        assertEquals(0, mGoogleAnalyticsTrackerMock.getNumberOfTrackedEvents());
        assertFalse(mGoogleAnalyticsTrackerMock.isEventTracked());
        assertEquals(0, mGoogleTagManagerTrackerMock.getNumberOfTrackedEvents());
        assertFalse(mGoogleTagManagerTrackerMock.isEventTracked());
        assertEquals(0, mAd4PushTrackerMock.getNumberOfTrackedEvents());
        assertFalse(mAd4PushTrackerMock.isEventTracked());
        assertEquals(0, mAdJustTrackerMock.getNumberOfTrackedEvents());
        assertFalse(mAdJustTrackerMock.isEventTracked());
        assertFalse(mNewRelicTrackerMock.isEventTracked());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(5);
        mGoogleAnalyticsTrackerMock.setSignal(latch);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        mAd4PushTrackerMock.setSignal(latch);
        mAdJustTrackerMock.setSignal(latch);
        mNewRelicTrackerMock.setSignal(latch);
        RITracking.getInstance().trackEvent("", 0, "", "", new HashMap<String, Object>());

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
        assertTrue(mNewRelicTrackerMock.isEventTracked());
    }

    public void testTrackScreenName() {
        String screenName = "HomePage";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleAnalyticsTrackerMock.getLastTrackedScreenName()));
        assertTrue(TextUtils.isEmpty(mGoogleTagManagerTrackerMock.getLastTrackedScreenName()));
        assertTrue(TextUtils.isEmpty(mAd4PushTrackerMock.getLastTrackedScreenName()));
        assertTrue(TextUtils.isEmpty(mBugSenseTrackerMock.getLastLeftBreadCrumb()));
        assertTrue(TextUtils.isEmpty(mNewRelicTrackerMock.getLastInteractionName()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(5);
        mGoogleAnalyticsTrackerMock.setSignal(latch);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        mAd4PushTrackerMock.setSignal(latch);
        mBugSenseTrackerMock.setSignal(latch);
        mNewRelicTrackerMock.setSignal(latch);
        RITracking.getInstance().trackScreenWithName(screenName);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(screenName, mGoogleAnalyticsTrackerMock.getLastTrackedScreenName());
        assertEquals(screenName, mGoogleTagManagerTrackerMock.getLastTrackedScreenName());
        assertEquals(screenName, mAd4PushTrackerMock.getLastTrackedScreenName());
        assertEquals(screenName, mBugSenseTrackerMock.getLastLeftBreadCrumb());
        assertEquals(screenName, mNewRelicTrackerMock.getLastInteractionName());
    }

    public void testTrackUserInfo() {
        String userEvent = "Today is my birthday";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleTagManagerTrackerMock.getLastUserEvent()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        RITracking.getInstance().trackUserInfo(userEvent, new HashMap<String, Object>());

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(userEvent, mGoogleTagManagerTrackerMock.getLastUserEvent());
    }

    public void testUpdateDeviceInfo() {
        Map<String, Object> deviceInfo = new HashMap<String, Object>();
        deviceInfo.put("userId", "0000000000");

        // Evaluate initial situation
        assertNull(mAd4PushTrackerMock.getDeviceInfo());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mAd4PushTrackerMock.setSignal(latch);
        RITracking.getInstance().trackUpdateDeviceInfo(deviceInfo);

        // Validate results
        assertEquals(0, latch.getCount());

        assertTrue(mAd4PushTrackerMock.getDeviceInfo().containsKey("userId"));
    }

    public void testUpdateGeolocation() {
        Location location = new Location("testLocation");
        location.setLatitude(50.0);
        location.setLongitude(20.0);

        // Evaluate initial situation
        assertNull(mAd4PushTrackerMock.getLastLocation());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mAd4PushTrackerMock.setSignal(latch);
        RITracking.getInstance().trackUpdateGeoLocation(location);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(50.0, mAd4PushTrackerMock.getLastLocation().getLatitude());
        assertEquals(20.0, mAd4PushTrackerMock.getLastLocation().getLongitude());
    }

    public void testTrackException() {
        Exception exception = new Exception("Something weird happened!");

        // Evaluate initial situation
        assertNull(mGoogleAnalyticsTrackerMock.getLastTrackedException());
        assertNull(mBugSenseTrackerMock.getLastCaughtException());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(2);
        mGoogleAnalyticsTrackerMock.setSignal(latch);
        mBugSenseTrackerMock.setSignal(latch);
        RITracking.getInstance().trackException(new HashMap<String, String>(), exception);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(exception, mGoogleAnalyticsTrackerMock.getLastTrackedException());
        assertEquals(exception, mBugSenseTrackerMock.getLastCaughtException());
    }

    public void testTrackCheckoutTransaction() {
        String transactionId = "id56fca3drt5d";
        RITrackingTransaction transaction = new RITrackingTransaction();
        transaction.setTransactionId(transactionId);

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleAnalyticsTrackerMock.getLastCheckoutTransaction()));
        assertTrue(TextUtils.isEmpty(mGoogleTagManagerTrackerMock.getLastCheckoutTransaction()));
        assertTrue(TextUtils.isEmpty(mAd4PushTrackerMock.getLastCheckoutTransaction()));
        assertTrue(TextUtils.isEmpty(mAdJustTrackerMock.getLastCheckoutTransaction()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(4);
        mGoogleAnalyticsTrackerMock.setSignal(latch);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        mAd4PushTrackerMock.setSignal(latch);
        mAdJustTrackerMock.setSignal(latch);
        RITracking.getInstance().trackCheckoutTransaction(transaction);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(transactionId, mGoogleAnalyticsTrackerMock.getLastCheckoutTransaction());
        assertEquals(transactionId, mGoogleTagManagerTrackerMock.getLastCheckoutTransaction());
        assertEquals(transactionId, mAd4PushTrackerMock.getLastCheckoutTransaction());
        assertEquals(transactionId, mAdJustTrackerMock.getLastCheckoutTransaction());
    }

    public void testTrackAddProductToCart() {
        String location = "Product detail";
        String productName = "productName";
        String cartId = "cartId";
        RITrackingProduct product = new RITrackingProduct();
        product.setName(productName);

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleTagManagerTrackerMock.getLastAddedProduct()));
        assertTrue(TextUtils.isEmpty(mGoogleTagManagerTrackerMock.getLocationOfProductBeforeAddingToCart()));
        assertTrue(TextUtils.isEmpty(mAd4PushTrackerMock.getLastAddedProduct()));
        assertTrue(TextUtils.isEmpty(mAd4PushTrackerMock.getCartIdOfLastAddedProduct()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(2);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        mAd4PushTrackerMock.setSignal(latch);
        RITracking.getInstance().trackAddProductToCart(product, cartId, location);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(productName, mGoogleTagManagerTrackerMock.getLastAddedProduct());
        assertEquals(location, mGoogleTagManagerTrackerMock.getLocationOfProductBeforeAddingToCart());
        assertEquals(productName, mAd4PushTrackerMock.getLastAddedProduct());
        assertEquals(cartId, mAd4PushTrackerMock.getCartIdOfLastAddedProduct());
    }

    public void testTrackRemoveProductFromCart() {
        int quantity = 3;
        double cartValue = 50.50;
        String productName = "productName";
        RITrackingProduct product = new RITrackingProduct();
        product.setName(productName);

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mGoogleTagManagerTrackerMock.getLastRemovedProduct()));
        assertEquals(0, mGoogleTagManagerTrackerMock.getNumberOfRemovedProduct());
        assertEquals(0.0, mGoogleTagManagerTrackerMock.getValueOfCartBeforeRemoval());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mGoogleTagManagerTrackerMock.setSignal(latch);
        RITracking.getInstance().trackRemoveProductFromCart(product, quantity, cartValue);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(productName, mGoogleTagManagerTrackerMock.getLastRemovedProduct());
        assertEquals(quantity, mGoogleTagManagerTrackerMock.getNumberOfRemovedProduct());
        assertEquals(cartValue, mGoogleTagManagerTrackerMock.getValueOfCartBeforeRemoval());
    }

    public void testTrackUrl() {
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

        // Validate results
        assertEquals(0, latch.getCount());
        assertTrue(listener.isHandlerCalled());
        assertEquals("Handler2", listener.getHandlerIdentifier());
        assertTrue(listener.getQueryParams().containsKey("handler2"));
        assertEquals(uri, mAdJustTrackerMock.getLastTrackerUri());
    }

    public void testTrackActivityCreatedAsSplashScreen() {
        Activity mockActivity = new Activity();

        // Evaluate initial situation
        assertFalse(mAd4PushTrackerMock.wasActivityCreated());
        assertFalse(mAd4PushTrackerMock.wasActivitySplashScreen());

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mAd4PushTrackerMock.setSignal(latch);
        RITracking.getInstance().trackActivityCreated(mockActivity, true);

        // Validate results
        assertEquals(0, latch.getCount());

        assertTrue(mAd4PushTrackerMock.wasActivityCreated());
        assertTrue(mAd4PushTrackerMock.wasActivitySplashScreen());
    }

    public void testTrackActivityResumed() {
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

        // Validate results
        assertEquals(0, latch.getCount());

        assertTrue(mAd4PushTrackerMock.wasActivityResumed());
        assertTrue(mAdJustTrackerMock.wasActivityResumed());
        assertTrue(mBugSenseTrackerMock.wasActivityResumed());
    }

    public void testTrackActivityPaused() {
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

        // Validate results
        assertEquals(0, latch.getCount());

        assertTrue(mAd4PushTrackerMock.wasActivityPaused());
        assertTrue(mAdJustTrackerMock.wasActivityPaused());
        assertTrue(mBugSenseTrackerMock.wasActivityPaused());
    }

    public void testTrackStartInteraction() {
        String interactionName = "Interaction name";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mNewRelicTrackerMock.getLastStartedInteractionName()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mNewRelicTrackerMock.setSignal(latch);
        RITracking.getInstance().trackStartInteraction(interactionName);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(interactionName, mNewRelicTrackerMock.getLastStartedInteractionName());
    }

    public void testTrackEndInteraction() {
        String interactionId = "InteractionId";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mNewRelicTrackerMock.getLastEndedInteractionId()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mNewRelicTrackerMock.setSignal(latch);
        RITracking.getInstance().trackEndInteraction(interactionId);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(interactionId, mNewRelicTrackerMock.getLastEndedInteractionId());
    }

    public void testTrackHttpTransaction() {
        String url = "http://www.test.com";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mNewRelicTrackerMock.getLastHttpTransactionUrl()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mNewRelicTrackerMock.setSignal(latch);
        RITracking.getInstance().trackHttpTransaction(url, 200, 11111, 22222, 33333, 44444, "", null);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(url, mNewRelicTrackerMock.getLastHttpTransactionUrl());
    }

    public void testTrackNetworkFailure() {
        String url = "http://www.test.com";

        // Evaluate initial situation
        assertTrue(TextUtils.isEmpty(mNewRelicTrackerMock.getLastNetworkFailureUrl()));

        // Set count down depending on how many tracker we expect the callback from
        CountDownLatch latch = new CountDownLatch(1);
        mNewRelicTrackerMock.setSignal(latch);
        RITracking.getInstance().trackNetworkFailure(url, 11111, 22222, new Exception("Exception"), NetworkFailure.Unknown);

        // Validate results
        assertEquals(0, latch.getCount());

        assertEquals(url, mNewRelicTrackerMock.getLastNetworkFailureUrl());
    }
}