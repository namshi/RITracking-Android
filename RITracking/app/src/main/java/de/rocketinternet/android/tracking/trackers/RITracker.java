package de.rocketinternet.android.tracking.trackers;

import android.content.Context;

import java.util.concurrent.ExecutorService;

/**
 * @author alessandro.balocco
 *
 * This class provides basic functionalities to every considered tracker
 */
public abstract class RITracker {

    /**
     * Number of concurrent tasks that can be executed by a tracker
     */
    protected static final int NUMBER_OF_CONCURRENT_TASKS = 1;

    /**
     * String that identifies the tracker
     */
    protected String mIdentifier;

    /**
     * Queue to avoid different trackers to stall each other or the app flow.
     */
    protected ExecutorService mQueue;

    /**
     * Get the identifier of the tracker. Mostly used for debugging reasons
     *
     * @return the identifier name of the tracker
     */
    public abstract String getIdentifier();

    /**
     * Initialize the tracker providing it with a valid context
     *
     * @return true if the tracker was successfully initialized
     */
    public abstract boolean initializeTracker(Context context);

    /**
     * Execute a certain operation
     *
     * @param runnable A runnable containing next operation
     */
    public abstract void execute(Runnable runnable);
}
