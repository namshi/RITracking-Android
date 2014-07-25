package de.rocketinternet.android.tracking.trackers;

import java.util.Map;
import java.util.concurrent.ExecutorService;

/**
 * @author alessandro.balocco
 */
public abstract class RITracker {

    /**
     * Number of concurrent tasks that can be executed by a tracker
     */
    protected static final int NUMBER_OF_CONCURRENT_TASKS = 1;

    /**
     * Queue to avoid different trackers to stall each other or the app flow.
     */
    protected ExecutorService mQueue;

    /**
     * Execute a certain operation
     *
     * @param runnable A runnable containing next operation
     */
    public abstract void execute(Runnable runnable);

    /**
     * Hook to recognise an app launch, given launch options
     *
     * @param options The launching options.
     */
    public abstract void trackApplicationLaunch(Map<String, String> options);
}
