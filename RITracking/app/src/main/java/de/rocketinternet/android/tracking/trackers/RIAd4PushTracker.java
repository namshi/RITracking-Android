package de.rocketinternet.android.tracking.trackers;

import android.app.Activity;
import android.content.Context;

import com.ad4screen.sdk.A4S;

import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.interfaces.RILifeCycleTracking;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 *         <p/>
 *         Convenience controller to proxy-pass tracking information to Ad4Push
 */
public class RIAd4PushTracker extends RITracker implements
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
