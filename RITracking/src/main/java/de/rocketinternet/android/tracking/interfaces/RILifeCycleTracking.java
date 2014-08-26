package de.rocketinternet.android.tracking.interfaces;

import android.app.Activity;

/**
 * @author alessandro.balocco
 *
 * This interface implements tracking for life cycle events
 */
public interface RILifeCycleTracking {

    /**
     * Track the onCreate callback of the Activity
     *
     * @param activity       The activity that is actually running
     * @param isSplashScreen flag that indicates if the activity is a splash screen
     */
    void trackActivityCreated(Activity activity, boolean isSplashScreen);

    /**
     * Track the onResume callback of the Activity
     *
     * @param activity The activity that is actually running
     */
    void trackActivityResumed(Activity activity);

    /**
     * Track the onPause callback of the Activity
     *
     * @param activity The activity that is actually going in background
     */
    void trackActivityPaused(Activity activity);
}
