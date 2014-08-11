package de.rocketinternet.android.tracking.interfaces;

/**
 * @author alessandro.balocco
 *         This interface implements tracking to a given screen
 */
public interface RIScreenTracking {

    /**
     * Track the display of a presented screen view to the user, given its name
     *
     * @param name The screen name.
     */
    void trackScreenWithName(String name);
}
