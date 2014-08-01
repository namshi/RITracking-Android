package de.rocketinternet.android.tracking.trackers.mocks;

import android.app.Activity;
import android.content.Context;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.trackers.RIAd4PushTracker;

/**
 * @author alessandro.balocco
 */
public class RIAd4PushTrackerMock extends RIAd4PushTracker {

    private CountDownLatch mSignal;
    private boolean mActivityWasResumed, mActivityWasPaused;

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
    public void onActivityResumed(Activity activity) {
        mActivityWasResumed = true;
        mSignal.countDown();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        mActivityWasPaused = true;
        mSignal.countDown();
    }

    public boolean wasActivityResumed() {
        return mActivityWasResumed;
    }

    public boolean wasActivityPaused() {
        return mActivityWasPaused;
    }
}
