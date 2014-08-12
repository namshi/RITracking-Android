package de.rocketinternet.android.tracking.interfaces;

/**
 * @author alessandro.balocco
 *         <p/>
 *         This interface implements tracking for screen names
 */
public interface RIScreenTracking {

    /**
     * Track the display of a presented screen view to the user, given its name
     *
     * @param name The screen name.
     */
    void trackScreenWithName(String name);
}
