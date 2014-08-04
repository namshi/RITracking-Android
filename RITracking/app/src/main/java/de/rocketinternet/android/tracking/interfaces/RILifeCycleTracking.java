package de.rocketinternet.android.tracking.interfaces;

import android.app.Activity;

/**
 * @author alessandro.balocco
 *         <p/>
 *         API interface for handling lifecycle events
 */
public interface RILifeCycleTracking {

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
