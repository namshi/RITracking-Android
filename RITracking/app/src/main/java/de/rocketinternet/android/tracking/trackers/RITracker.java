package de.rocketinternet.android.tracking.trackers;

import android.content.Context;

/**
 * @author alessandro.balocco
 *
 * This class provides basic functionalities to every considered tracker
 */
public abstract class RITracker {

    /**
     * String that identifies the tracker
     */
    protected String mIdentifier;

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
}
