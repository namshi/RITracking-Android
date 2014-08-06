package de.rocketinternet.android.tracking.trackers.mocks;

import android.content.Context;
import android.text.TextUtils;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.trackers.RIGoogleTagManagerTracker;

/**
 * @author alessandro.balocco
 *
 * This class is a mock implementation of the RIGoogleTagManagerTracker used for testing purposes
 */
public class RIGoogleTagManagerTrackerMock extends RIGoogleTagManagerTracker {

    private CountDownLatch mSignal;
    private boolean mIsEventTracked;
    private int mNumberOfSentEvents = 0;
    private String mLastTrackedScreenName;
    private String mLastTrackedCheckoutTransaction;
    private String mLastTrackedUserEvent;

    public RIGoogleTagManagerTrackerMock() {
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
    }

    @Override
    public boolean initializeTracker(Context context) {
        String containerId = RITrackingConfiguration.getInstance().getValueFromKeyMap("RIGoogleTagManagerContainerID");
        return !TextUtils.isEmpty(containerId);
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
    public void trackUserInfo(String userEvent, Map<String, Object> map) {
        mLastTrackedUserEvent = userEvent;
        mSignal.countDown();
    }

    @Override
    public void trackCheckoutWithTransactionId(String idTransaction, RITrackingTotal total) {
        mLastTrackedCheckoutTransaction = idTransaction;
        mSignal.countDown();
    }

    public boolean isEventTracked() {
        return mIsEventTracked;
    }

    public int getNumberOfTrackedEvents() {
        return mNumberOfSentEvents;
    }

    public String getLastTrackedScreenName() {
        return mLastTrackedScreenName;
    }

    public String getLastUserEvent() {
        return mLastTrackedUserEvent;
    }

    public String getLastCheckoutTransaction() {
        return mLastTrackedCheckoutTransaction;
    }
}
