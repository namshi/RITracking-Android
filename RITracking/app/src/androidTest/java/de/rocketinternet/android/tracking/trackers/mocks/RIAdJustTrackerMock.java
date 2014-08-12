package de.rocketinternet.android.tracking.trackers.mocks;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.Uri;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.trackers.RIAd4PushTracker;
import de.rocketinternet.android.tracking.trackers.RIAdjustTracker;

/**
 * @author alessandro.balocco
 *
 * This class is a mock implementation of the RIAdjustTracker used for testing purposes
 */
public class RIAdJustTrackerMock extends RIAdjustTracker {

    private CountDownLatch mSignal;
    private boolean mActivityWasResumed;
    private boolean mActivityWasPaused;
    private boolean mIsEventTracked;
    private int mNumberOfSentEvents = 0;
    private Uri mLastTrackedUri;

    public RIAdJustTrackerMock() {
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
    }

    @Override
    public boolean initializeTracker(Context context) {
        String adJustIntegration = RITrackingConfiguration.getInstance().getValueFromKeyMap("RIAdJustIntegration");
        boolean integrationNeeded = Boolean.valueOf(adJustIntegration);
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
    public void trackOpenUrl(Uri uri) {
        mLastTrackedUri = uri;
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

    public Uri getLastTrackerUri() {
        return mLastTrackedUri;
    }

    public boolean wasActivityResumed() {
        return mActivityWasResumed;
    }

    public boolean wasActivityPaused() {
        return mActivityWasPaused;
    }
}
