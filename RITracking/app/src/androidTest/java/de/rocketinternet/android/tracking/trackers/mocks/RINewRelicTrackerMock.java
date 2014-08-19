package de.rocketinternet.android.tracking.trackers.mocks;

import android.content.Context;
import android.text.TextUtils;

import com.newrelic.agent.android.util.NetworkFailure;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.trackers.RINewRelicTracker;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;

/**
 * @author alessandro.balocco
 *
 * This class is a mock implementation of the RINewRelicTracker used for testing purposes
 */
public class RINewRelicTrackerMock extends RINewRelicTracker {

    private CountDownLatch mSignal;
    private String mLastInteractionName;
    private String mLastStartedInteractionName;
    private String mLastEndedInteractionId;
    private String mLastHttpTransactionUrl;
    private String mLastNetworkFailureUrl;
    private boolean mIsEventTracked;

    @Override
    public boolean initializeTracker(Context context) {
        String appToken = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.NEWRELIC_APP_TOKEN);
        return !TextUtils.isEmpty(appToken);
    }

    public void setSignal(CountDownLatch signal) {
        mSignal = signal;
    }

    @Override
    public void trackEvent(String event, long value, String action, String category, Map<String, Object> data) {
        mIsEventTracked = true;
        mSignal.countDown();
    }

    @Override
    public void trackScreenWithName(String name) {
        mLastInteractionName = name;
        mSignal.countDown();
    }

    @Override
    public String trackStartInteraction(String name) {
        mLastStartedInteractionName = name;
        mSignal.countDown();

        return "";
    }

    @Override
    public void trackEndInteraction(String id) {
        mLastEndedInteractionId = id;
        mSignal.countDown();
    }

    @Override
    public void trackHttpTransaction(String url, int statusCode, long startTime, long endTime,
                                     long bytesSent, long bytesReceived, String responseBody,
                                     Map<String, String> params) {
        mLastHttpTransactionUrl = url;
        mSignal.countDown();
    }

    @Override
    public void trackNetworkFailure(String url, long startTime, long endTime, Exception exception,
                                    NetworkFailure failure) {
        mLastNetworkFailureUrl = url;
        mSignal.countDown();
    }

    public boolean isEventTracked() {
        return mIsEventTracked;
    }

    public String getLastInteractionName() {
        return mLastInteractionName;
    }

    public String getLastStartedInteractionName() {
        return mLastStartedInteractionName;
    }

    public String getLastEndedInteractionId() {
        return mLastEndedInteractionId;
    }

    public String getLastHttpTransactionUrl() {
        return mLastHttpTransactionUrl;
    }

    public String getLastNetworkFailureUrl() {
        return mLastNetworkFailureUrl;
    }
}
