package de.rocketinternet.android.tracking.trackers.mocks;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.trackers.RIAd4PushTracker;

/**
 * @author alessandro.balocco
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
    private Location mLastLocation;
    private Map<String, Object> mDeviceInfo;

    public RIAd4PushTrackerMock() {
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
    }

    @Override
    public boolean initializeTracker(Context context) {
        String ad4PushIntegration = RITrackingConfiguration.getInstance().getValueFromKeyMap("RIAd4PushIntegration");
        boolean integrationNeeded = Boolean.valueOf(ad4PushIntegration);
        return integrationNeeded;
    }

    public void setSignal(CountDownLatch signal) {
        mSignal = signal;
    }

    @Override
    public void trackEvent(String event, int value, String action, String category, Map<String, Object> data) {
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
    public void updateDeviceInfo(Map<String, Object> map) {
        mDeviceInfo = map;
        mSignal.countDown();
    }

    @Override
    public void updateGeoLocation(Location location) {
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
}
