package de.rocketinternet.android.tracking.core;

import de.rocketinternet.android.tracking.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 *  @author alessandro.balocco
 *
 *  RITrackingConfiguration has the configuration for RITracking
 */
public class RITrackingConfiguration {

    private static RITrackingConfiguration sInstance;
    private Map<String, String> mProperties;

    private RITrackingConfiguration() {}

    public static RITrackingConfiguration getInstance() {
        if (sInstance == null) {
            sInstance = new RITrackingConfiguration();
        }
        return sInstance;
    }

    /**
     *  Loads a property list located in the given path to read the contained configuration settings
     *
     *  @param path The path where is the configuration file
     *
     *  @return True in case of success, false in case of error
     */
    public boolean loadFromPropertyListAtPath(String path) {
        // Clear old values to avoid state information
        if (mProperties != null) { mProperties = null; };

        Map<String, String> properties = loadPropertiesFromFile(path);

        if (properties == null) {
            LogUtils.logError("Missing properties when loading property file at path '%@'", path);
            return false;
        }

        mProperties = properties;

        return true;
    }

    private HashMap<String, String> loadPropertiesFromFile(String path) {
        HashMap<String, String> propertiesFromFile = new HashMap<String, String>();
        // TODO: read properties from file and fill the map
        return propertiesFromFile;
    }

    /**
     *  Lookup a configuration value, given it's key
     *
     *  @param key The key to search
     *
     *  @return Returns the value for the given key
     */
    public String getValueFromKeyMap(String key) {
        return mProperties.containsKey(key) ? mProperties.get(key) : null;
    }

    /**
     *  Hidden test helper
     */
    public void clear() {
        sInstance = null;
    }
}
