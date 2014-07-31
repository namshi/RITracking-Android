package de.rocketinternet.android.tracking.interfaces;

import java.util.Map;

/**
 *  @author alessandro.balocco
 *
 *  This interface implements tracking user information
 */
public interface RIUserTracking {

    /**
     *  Track user information based on a key and a value. These information are used from the library
     *  to improve user experience, to allow user re-engagement and personalized messages
     *
     *  @param userEvent    the key to identify the user information
     *  @param map          the map of key value pairs
     */
    void trackUser(String userEvent, Map<String, Object> map);
}
