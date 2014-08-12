package de.rocketinternet.android.tracking.trackers.mocks;

import android.content.Context;
import android.text.TextUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.trackers.RINewRelicTracker;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;

/**
 * @author alessandro.balocco
 *         <p/>
 *         This class is a mock implementation of the RINewRelicTracker used for testing purposes
 */
public class RINewRelicTrackerMock extends RINewRelicTracker {

    private CountDownLatch mSignal;
    private String mLastInteractionName;
    private String mLastStartedInteractionName;
    private String mLastEndedInteractionId;

    public RINewRelicTrackerMock() {
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
    }

    @Override
    public boolean initializeTracker(Context context) {
        String appToken = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.NEWRELIC_APP_TOKEN);
        return !TextUtils.isEmpty(appToken);
    }

    public void setSignal(CountDownLatch signal) {
        mSignal = signal;
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

    public String getLastInteractionName() {
        return mLastInteractionName;
    }

    public String getLastStartedInteractionName() {
        return mLastStartedInteractionName;
    }

    public String getLastEndedInteractionId() {
        return mLastEndedInteractionId;
    }
}
