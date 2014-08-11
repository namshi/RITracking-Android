package de.rocketinternet.android.tracking.interfaces;

/**
 * @author alessandro.balocco
 *
 * This interface implements tracking of interactions mostly provided by NewRelic
 */
public interface RIInteractionTracking {

    /**
     * Track an interaction defined by a name
     *
     * @param name The name of the interaction
     * @return The String Id of the interaction
     */
    String trackStartInteraction(String name);

    /**
     * Stop the previously started interaction. This call has no effect if the interaction has already
     * ended
     *
     * @param id The id of the interaction
     */
    void trackEndInteraction(String id);
}
