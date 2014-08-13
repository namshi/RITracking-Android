package de.rocketinternet.android.tracking.trackers;

import android.content.Context;
import android.text.TextUtils;

import com.newrelic.agent.android.NewRelic;
import com.newrelic.agent.android.util.NetworkFailure;

import java.util.Map;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.interfaces.RIEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIInteractionTracking;
import de.rocketinternet.android.tracking.interfaces.RINetworkTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 *
 * Convenience controller to proxy-pass tracking information to NewRelic
 */
public class RINewRelicTracker extends RITracker implements
        RIEventTracking,
        RIScreenTracking,
        RIInteractionTracking,
        RINetworkTracking {

    private static final String TRACKER_ID = "RINewRelicTrackerID";

    @Override
    public String getIdentifier() {
        return mIdentifier;
    }

    @Override
    public boolean initializeTracker(Context context) {
        RILogUtils.logDebug("Initializing New Relic tracker");
        String appToken = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.NEWRELIC_APP_TOKEN);
        if (!TextUtils.isEmpty(appToken)) {
            createTracker(context, appToken);
            return true;
        } else {
            RILogUtils.logError("New Relic tracker is not needed and will not be initialized. Add the " +
                    "App Token in properties file if you want New Relic to be integrated");
        }
        return false;
    }

    private void createTracker(Context context, String appToken) {
        NewRelic.withApplicationToken(appToken).start(context);
        mIdentifier = TRACKER_ID;
    }

    @Override
    public void trackEvent(String event, long value, String action, String category, Map<String, Object> data) {
        RILogUtils.logDebug("New Relic tracker - Tracking event: " + event);

        NewRelic.recordMetric(event, category, value);
    }

    @Override
    public void trackScreenWithName(String name) {
        RILogUtils.logDebug("New Relic tracker - Give interactions name: " + name);

        NewRelic.setInteractionName(name);
    }

    @Override
    public String trackStartInteraction(String name) {
        RILogUtils.logDebug("New Relic tracker - Starting interaction with name: " + name);

        return NewRelic.startInteraction(name);
    }

    @Override
    public void trackEndInteraction(String id) {
        RILogUtils.logDebug("New Relic tracker - Stopping interaction with id: " + id);

        NewRelic.endInteraction(id);
    }

    @Override
    public void trackHttpTransaction(String url, int statusCode, long startTime, long endTime,
                                     long bytesSent, long bytesReceived, String responseBody,
                                     Map<String, String> params) {
        RILogUtils.logDebug("New Relic tracker - Track http transaction with url: " + url);

        if ((TextUtils.isEmpty(responseBody)) && (params == null || params.isEmpty())) {
            NewRelic.noticeHttpTransaction(url, statusCode, startTime, endTime, bytesSent, bytesReceived);
        } else {
            NewRelic.noticeHttpTransaction(url, statusCode, startTime, endTime, bytesSent, bytesReceived,
                    responseBody, params);
        }
    }

    @Override
    public void trackNetworkFailure(String url, long startTime, long endTime, Exception exception,
                                    NetworkFailure failure) {
        RILogUtils.logDebug("New Relic tracker - Track network failure with url: " + url);

        if (exception != null) {
            NewRelic.noticeNetworkFailure(url, startTime, endTime, exception);
        } else {
            NewRelic.noticeNetworkFailure(url, startTime, endTime, failure);
        }

    }
}
