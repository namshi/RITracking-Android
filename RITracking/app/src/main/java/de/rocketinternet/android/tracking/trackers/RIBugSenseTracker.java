package de.rocketinternet.android.tracking.trackers;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.bugsense.trace.BugSenseHandler;

import java.util.HashMap;
import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.interfaces.RIExceptionTracking;
import de.rocketinternet.android.tracking.interfaces.RILifeCycleTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 *
 * Convenience controller to proxy-pass tracking information to BugSense
 */
public class RIBugSenseTracker extends RITracker implements
        RIScreenTracking,
        RIExceptionTracking,
        RILifeCycleTracking {

    private static final String TRACKER_ID = "RIBugSenseTrackerID";

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
        RILogUtils.logDebug("Initializing BugSense tracker");
        String bugSenseApiKey = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.BUGSENSE_API_KEY);
        if (!TextUtils.isEmpty(bugSenseApiKey)) {
            createTracker(context, bugSenseApiKey);
            return true;
        } else {
            RILogUtils.logError("BugSense tracker is not needed and will not be initialized. In case " +
                    "you need it please insert the ApiKey in the properties file");
        }
        return false;
    }

    private void createTracker(Context context, String apiKey) {
        BugSenseHandler.initAndStartSession(context, apiKey);
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
        mIdentifier = TRACKER_ID;
    }


    @Override
    public void trackScreenWithName(String name) {
        RILogUtils.logDebug("BugSense tracker - tracking screen with name " + name + " for leaving breadcrumbs");

        BugSenseHandler.leaveBreadcrumb(name);
    }

    @Override
    public void trackExceptionWithName(HashMap<String, String> params, Exception exception) {
        if (exception != null) {
            RILogUtils.logDebug("BugSense tracker - tracking exception: " + exception.getMessage());

            if (params != null && !params.isEmpty()) {
                BugSenseHandler.sendExceptionMap(params, exception);
            } else {
                BugSenseHandler.sendException(exception);
            }
        }
    }

    @Override
    public void trackActivityCreated(Activity activity, boolean isSplashScreen) {
        // Not used by this tracker
    }

    @Override
    public void trackActivityResumed(Activity activity) {
        if (activity != null) {
            RILogUtils.logDebug("BugSense tracker - Activity: " + activity.getLocalClassName() +
                    " was resumed, and it tries to start a session");

            BugSenseHandler.startSession(activity);
        }
    }

    @Override
    public void trackActivityPaused(Activity activity) {
        if (activity != null) {
            RILogUtils.logDebug("BugSense tracker - Activity: " + activity.getLocalClassName() +
                    " was paused, and it tries to stop the session");

            BugSenseHandler.startSession(activity);
        }
    }
}
