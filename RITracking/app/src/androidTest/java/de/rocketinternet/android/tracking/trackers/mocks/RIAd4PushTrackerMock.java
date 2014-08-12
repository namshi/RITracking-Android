package de.rocketinternet.android.tracking.trackers.mocks;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTransaction;
import de.rocketinternet.android.tracking.trackers.RIAd4PushTracker;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;

/**
 * @author alessandro.balocco
 *         <p/>
 *         This class is a mock implementation of the RIAd4PushTracker used for testing purposes
 */
public class RIAd4PushTrackerMock extends RIAd4PushTracker {

    private CountDownLatch mSignal;
    private boolean mActivityWasCreated;
    private boolean mActivityWasSplashScreen;
    private boolean mActivityWasResumed;
    private boolean mActivityWasPaused;
    private boolean mIsEventTracked;
    private int mNumberOfSentEvents = 0;
    private String mLastTrackedScreenName;
    private String mLastTrackedCheckoutTransaction;
    private String mLastAddedProduct;
    private String mCartIdOfLastAddedProduct;
    private Location mLastLocation;
    private Map<String, Object> mDeviceInfo;

    public RIAd4PushTrackerMock() {
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
    }

    @Override
    public boolean initializeTracker(Context context) {
        String ad4PushIntegration = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.AD4PUSH_INTEGRATION);
        boolean integrationNeeded = Boolean.valueOf(ad4PushIntegration);
        return integrationNeeded;
    }

    public void setSignal(CountDownLatch signal) {
        mSignal = signal;
    }

    @Override
    public void trackEvent(String event, long value, String action, String category, Map<String, Object> data) {
        mIsEventTracked = true;
        mNumberOfSentEvents++;
        mSignal.countDown();
    }

    @Override
    public void trackScreenWithName(String name) {
        mLastTrackedScreenName = name;
        mSignal.countDown();
    }

    @Override
    public void trackUpdateDeviceInfo(Map<String, Object> map) {
        mDeviceInfo = map;
        mSignal.countDown();
    }

    @Override
    public void trackUpdateGeoLocation(Location location) {
        mLastLocation = location;
        mSignal.countDown();
    }


    @Override
    public void trackActivityCreated(Activity activity, boolean isSplashScreen) {
        mActivityWasCreated = true;
        mActivityWasSplashScreen = isSplashScreen;
        mSignal.countDown();
    }

    @Override
    public void trackActivityResumed(Activity activity) {
        mActivityWasResumed = true;
        mSignal.countDown();
    }

    @Override
    public void trackActivityPaused(Activity activity) {
        mActivityWasPaused = true;
        mSignal.countDown();
    }

    @Override
    public void trackCheckoutTransaction(RITrackingTransaction transaction) {
        mLastTrackedCheckoutTransaction = transaction.getTransactionId();
        mSignal.countDown();
    }

    @Override
    public void trackAddProductToCart(RITrackingProduct product, String cartId, String location) {
        mLastAddedProduct = product.getName();
        mCartIdOfLastAddedProduct = cartId;
        mSignal.countDown();
    }

    public boolean isEventTracked() {
        return mIsEventTracked;
    }

    public int getNumberOfTrackedEvents() {
        return mNumberOfSentEvents;
    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    public Map<String, Object> getDeviceInfo() {
        return mDeviceInfo;
    }

    public String getLastTrackedScreenName() {
        return mLastTrackedScreenName;
    }

    public boolean wasActivityCreated() {
        return mActivityWasCreated;
    }

    public boolean wasActivitySplashScreen() {
        return mActivityWasSplashScreen;
    }

    public boolean wasActivityResumed() {
        return mActivityWasResumed;
    }

    public boolean wasActivityPaused() {
        return mActivityWasPaused;
    }

    public String getLastCheckoutTransaction() {
        return mLastTrackedCheckoutTransaction;
    }

    public String getLastAddedProduct() {
        return mLastAddedProduct;
    }

    public String getCartIdOfLastAddedProduct() {
        return mCartIdOfLastAddedProduct;
    }
}
