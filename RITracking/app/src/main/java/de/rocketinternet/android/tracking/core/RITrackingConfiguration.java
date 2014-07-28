package de.rocketinternet.android.tracking.core;

import android.content.Context;

import de.rocketinternet.android.tracking.utils.RIAssetsPropertiesUtils;
import de.rocketinternet.android.tracking.utils.RILogUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
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

    public static RITrackingConfiguration getInstance() {
        if (sInstance == null) {
            sInstance = new RITrackingConfiguration();
        }
        return sInstance;
    }

    /**
     *  Loads a property list located in the given path to read the contained configuration settings
     *
     *  @return True in case of success, false in case of error
     */
    public boolean loadFromPropertyList(Context context) {
        // Clear old values to avoid state information
        if (mProperties != null) { mProperties = null; };

        Map<String, String> properties = loadPropertiesFromFile(context);

        if (properties == null) {
            RILogUtils.logError("Missing properties when loading properties file");
            return false;
        }

        mProperties = properties;

        return true;
    }

    private HashMap<String, String> loadPropertiesFromFile(Context context) {
        HashMap<String, String> propertiesMap = null;
        try {
            Properties properties = RIAssetsPropertiesUtils.getProperties(context);
            propertiesMap = new HashMap<String, String>();
            for (String name: properties.stringPropertyNames()) {
                propertiesMap.put(name, properties.getProperty(name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return propertiesMap;
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
