package de.rocketinternet.android.tracking.trackers.mocks;

import android.app.Activity;
import android.content.Context;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.trackers.RIAd4PushTracker;
import de.rocketinternet.android.tracking.trackers.ad4push.RIAd4PushUserEnum;

/**
 * @author alessandro.balocco
 */
public class RIAd4PushTrackerMock extends RIAd4PushTracker {

    private CountDownLatch mSignal;
    private boolean mActivityWasResumed, mActivityWasPaused, mIsEventTracked;
    private int mNumberOfSentEvents = 0;
    private String mLastTrackedScreenName, mLastTrackedCheckoutTransaction, mLastTrackedUserEvent;

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
    public void trackUser(String userEvent, Map<String, Object> map, RIAd4PushUserEnum ad4PushUserEnum) {
        mLastTrackedUserEvent = userEvent;
        mSignal.countDown();
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mActivityWasResumed = true;
        mSignal.countDown();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mActivityWasPaused = true;
        mSignal.countDown();
    }

    public boolean isEventTracked() {
        return mIsEventTracked;
    }

    public int getNumberOfTrackedEvents() {
        return mNumberOfSentEvents;
    }

    public String getLastUserEvent() {
        return mLastTrackedUserEvent;
    }

    public String getLastTrackedScreenName() {
        return mLastTrackedScreenName;
    }

    public boolean wasActivityResumed() {
        return mActivityWasResumed;
    }

    public boolean wasActivityPaused() {
        return mActivityWasPaused;
    }
}
