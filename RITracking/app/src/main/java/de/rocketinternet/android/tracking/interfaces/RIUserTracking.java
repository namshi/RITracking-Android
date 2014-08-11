package de.rocketinternet.android.tracking.interfaces;

import android.location.Location;

import java.util.Map;

/**
 * @author alessandro.balocco
 *         This interface implements tracking user information
 */
public interface RIUserTracking {

    /**
     * Track user information based on a key and a value. These information are used from the library
     * to improve user experience, to allow user re-engagement and personalized messages
     *
     * @param key the key to identify the user information
     * @param map the map of key value pairs
     */
    void trackUserInfo(String key, Map<String, Object> map);

    /**
     * Update device information
     *
     * @param map The key values pair to update
     */
    void trackUpdateDeviceInfo(Map<String, Object> map);

    /**
     * Update device geolocation
     *
     * @param location the location to be updated
     */
    void trackUpdateGeoLocation(Location location);
}
