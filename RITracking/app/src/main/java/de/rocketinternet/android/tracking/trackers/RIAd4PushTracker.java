package de.rocketinternet.android.tracking.trackers;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.ad4screen.sdk.A4S;

import java.util.Map;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.interfaces.RIEventTracking;
import de.rocketinternet.android.tracking.interfaces.RILifeCycleTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.interfaces.RIUserTracking;
import de.rocketinternet.android.tracking.trackers.ad4push.RIAd4PushUserEnum;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 *         <p/>
 *         Convenience controller to proxy-pass tracking information to Ad4Push
 */
public class RIAd4PushTracker extends RITracker implements
        RIScreenTracking,
        RIUserTracking,
        RIEventTracking,
        RILifeCycleTracking {

    private static final String TRACKER_ID = "RIAd4PushTrackerID";
    private static final String INTEGRATION_NEEDED = "RIAd4PushIntegration";

    private A4S mA4S;

    @Override
    public void execute(Runnable runnable) {
        mQueue.execute(runnable);
    }

    @Override
    public String getIdentifier() {
        return mIdentifier;
    }

    @Override
    public boolean initializeTracker(Context context) {
        RILogUtils.logDebug("Initializing Ad4Push tracker");
        String ad4PushIntegration = RITrackingConfiguration.getInstance().getValueFromKeyMap(INTEGRATION_NEEDED);
        boolean isAd4PushIntegrationNeeded = Boolean.valueOf(ad4PushIntegration);
        if (isAd4PushIntegrationNeeded) {
            createTracker(context);
            return true;
        } else {
            RILogUtils.logError("Ad4Push tracker is not needed and will not be initialized. Change the " +
                    "corresponding property value to 'true' if you want Ad4Push to be integrated");
        }
        return false;
    }

    private void createTracker(Context context) {
        mA4S = A4S.get(context);
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
        mIdentifier = TRACKER_ID;
    }


    @Override
    public void trackScreenWithName(String name) {
        RILogUtils.logDebug("Ad4Push tracker - Tracking screen with name: " + name);

        if (mA4S == null) {
            RILogUtils.logError("Missing Ad4Push singleton reference");
            return;
        }

        mA4S.putState(RITrackersConstants.AD4PUSH_VIEW, name);
    }

    @Override
    public void trackUser(String userEvent, Map<String, Object> map, RIAd4PushUserEnum ad4PushValue) {
        RILogUtils.logDebug("Ad4Push tracker - Tracking user event: " + userEvent);

        if (mA4S == null) {
            RILogUtils.logError("Missing Ad4Push singleton reference");
            return;
        }

        Bundle bundle = new Bundle();
        switch (ad4PushValue) {
            case DEVICE_INFO:
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    bundle.putString(entry.getKey(), (String) entry.getValue());
                }
                mA4S.updateDeviceInfo(bundle);
            case IGNORE:
            default:
                break;
        }
    }

    @Override
    public void trackEvent(String event, int value, String action, String category, Map<String, Object> data) {
        RILogUtils.logDebug("Ad4Push tracker - Tracking event with name: " + event);

        if (mA4S == null) {
            RILogUtils.logError("Missing Ad4Push singleton reference");
            return;
        }

        mA4S.trackEvent(value, event);
    }

    @Override
    public void onActivityResumed(Activity activity) {
        RILogUtils.logDebug("Ad4Push tracker - Activity: " + activity.getLocalClassName() + " was resumed");

        if (mA4S == null) {
            RILogUtils.logError("Missing Ad4Push singleton reference");
            return;
        }

        mA4S.startActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        RILogUtils.logDebug("Ad4Push tracker - Activity: " + activity.getLocalClassName() + " was paused");

        if (mA4S == null) {
            RILogUtils.logError("Missing Ad4Push singleton reference");
            return;
        }

        mA4S.stopActivity(activity);
    }
}
