package de.rocketinternet.android.tracking.trackers.mocks;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.models.RITrackingTransaction;
import de.rocketinternet.android.tracking.trackers.RIGoogleAnalyticsTracker;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;

/**
 * @author alessandro.balocco
 *         This class is a mock implementation of the RIGoogleAnalyticsTracker used for testing purposes
 */
public class RIGoogleAnalyticsTrackerMock extends RIGoogleAnalyticsTracker {

    private CountDownLatch mSignal;
    private boolean mIsEventTracked;
    private int mNumberOfSentEvents = 0;
    private String mLastTrackedScreenName;
    private String mLastCheckoutTransaction;
    private Exception mLastTrackedException;

    public RIGoogleAnalyticsTrackerMock() {
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
    }

    @Override
    public boolean initializeTracker(Context context) {
        String trackingId = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.GA_TRACKING_ID);
        return !TextUtils.isEmpty(trackingId);
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
    public void trackException(HashMap<String, String> params, Exception exception) {
        mLastTrackedException = exception;
        mSignal.countDown();
    }

    @Override
    public void trackCheckoutTransaction(RITrackingTransaction transaction) {
        mLastCheckoutTransaction = transaction.getTransactionId();
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

    public Exception getLastTrackedException() {
        return mLastTrackedException;
    }

    public String getLastCheckoutTransaction() {
        return mLastCheckoutTransaction;
    }
}
