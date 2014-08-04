package de.rocketinternet.android.tracking.interfaces;

import java.util.Map;

import de.rocketinternet.android.tracking.trackers.ad4push.RIAd4PushUserEnum;

/**
 * @author alessandro.balocco
 *         <p/>
 *         This interface implements tracking user information
 */
public interface RIUserTracking {

    /**
     * Track user information based on a key and a value. These information are used from the library
     * to improve user experience, to allow user re-engagement and personalized messages
     *
     * @param userEvent    the key to identify the user information
     * @param map          the map of key value pairs
     * @param ad4PushValue value for ad4push user behaviours
     */
    void trackUser(String userEvent, Map<String, Object> map, RIAd4PushUserEnum ad4PushValue);
}
