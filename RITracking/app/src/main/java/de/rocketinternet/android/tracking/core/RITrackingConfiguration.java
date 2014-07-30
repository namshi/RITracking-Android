package de.rocketinternet.android.tracking.core;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 *  @author alessandro.balocco
 *
 *  RITrackingConfiguration has the configuration for RITracking
 */
public class RITrackingConfiguration {

    private static RITrackingConfiguration sInstance;
    private Map<String, String> mProperties;

    private RITrackingConfiguration() {}

    /**
     *  Creates and initializes an 'RITrackingConfiguration' object
     *
     *  @return The newly-initialized object
     */
    public static RITrackingConfiguration getInstance() {
        if (sInstance == null) {
            sInstance = new RITrackingConfiguration();
        }
        return sInstance;
    }

    /**
     *  Loads a property map from a property file to read the contained configuration settings
     *
     *  @return true in case of success, false in case of error
     */
    public HashMap<String, String> loadPropertiesFromFile(Properties properties) {
        // Clear old values to avoid state information
        if (mProperties != null && !mProperties.isEmpty()) { mProperties = null; };

        HashMap<String, String> propertiesMap = new HashMap<String, String>();
        for (String name: properties.stringPropertyNames()) {
            if (!TextUtils.isEmpty(properties.getProperty(name))) {
                propertiesMap.put(name, properties.getProperty(name));
            }
        }

        mProperties = propertiesMap;

        return propertiesMap;
    }

    /**
     *  Lookup a configuration value, given it's key
     *
     *  @param key The key to search
     *
     *  @return the value for the given key
     */
    public String getValueFromKeyMap(String key) {
        return (mProperties != null && mProperties.containsKey(key)) ? mProperties.get(key) : null;
    }

    /**
     *  Hidden test helper
     */
    public void clear() {
        sInstance = null;
    }
}
