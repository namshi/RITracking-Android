package de.rocketinternet.android.tracking.trackers.mocks;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.trackers.RIBugSenseTracker;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;

/**
 * @author alessandro.balocco
 *
 * This class is a mock implementation of the RIBugSenseTracker used for testing purposes
 */
public class RIBugSenseTrackerMock extends RIBugSenseTracker {

    private CountDownLatch mSignal;
    private boolean mActivityWasResumed;
    private boolean mActivityWasPaused;
    private String mLastLeftBreadCrumb;
    private Exception mLastCaughtException;

    @Override
    public boolean initializeTracker(Context context) {
        String bugSenseApiKey = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.BUGSENSE_API_KEY);
        return !TextUtils.isEmpty(bugSenseApiKey);
    }

    public void setSignal(CountDownLatch signal) {
        mSignal = signal;
    }

    @Override
    public void trackScreenWithName(String name) {
        mLastLeftBreadCrumb = name;
        mSignal.countDown();
    }

    @Override
    public void trackException(HashMap<String, String> params, Exception exception) {
        mLastCaughtException = exception;
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

    public String getLastLeftBreadCrumb() {
        return mLastLeftBreadCrumb;
    }

    public Exception getLastCaughtException() {
        return mLastCaughtException;
    }

    public boolean wasActivityResumed() {
        return mActivityWasResumed;
    }

    public boolean wasActivityPaused() {
        return mActivityWasPaused;
    }
}
