package de.rocketinternet.android.tracking.trackers.mocks;

import android.content.Context;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.trackers.RIGoogleAnalyticsTracker;

/**
 * @author alessandro.balocco
 */
public class RIGoogleAnalyticsTrackerMock extends RIGoogleAnalyticsTracker {

    private CountDownLatch mSignal;
    private boolean mIsEventTracked;
    private int mNumberOfSentEvent = 0;
    private String mLastTrackedScreenName, mLastTrackedException, mLastCheckoutTransaction;

    public RIGoogleAnalyticsTrackerMock() {
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
    }

    public void setSignal(CountDownLatch signal) {
        mSignal = signal;
    }

    @Override
    public void trackEvent(String event, int value, String action, String category, Map<String, Object> data) {
        mIsEventTracked = true;
        mNumberOfSentEvent++;
        mSignal.countDown();
    }

    @Override
    public void trackScreenWithName(String name) {
        mLastTrackedScreenName = name;
        mSignal.countDown();
    }

    @Override
    public void trackExceptionWithName(String name) {
        mLastTrackedException = name;
        mSignal.countDown();
    }

    @Override
    public void trackCheckoutWithTransactionId(String idTransaction, RITrackingTotal total) {
        mLastCheckoutTransaction = idTransaction;
        mSignal.countDown();
    }

    public boolean isEventTracked() {
        return mIsEventTracked;
    }

    public int getNumberOfTrackedEvents() {
        return mNumberOfSentEvent;
    }

    public String getLastTrackedScreenName() {
        return mLastTrackedScreenName;
    }

    public String getLastTrackedException() {
        return mLastTrackedException;
    }

    public String getLastCheckoutTransaction() {
        return mLastCheckoutTransaction;
    }
}
