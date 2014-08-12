package de.rocketinternet.android.tracking.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *  @author alessandro.balocco
 *
 *  Utility class that provide access to the assets folder of the app
 */
public class RIResourceUtils {

    /**
     *  Default log tag for this class
     */
    private static final String LOG_TAG = RIResourceUtils.class.getSimpleName();

    /**
     *  This method provides functionality for reading the properties file of the library containing
     *  configuration information for initializing trackers
     *
     *  @param context              A valid context to access folders
     *  @param propertyFileName     The name of the properties file
     *  @return                     A properties file
     */
    public static Properties getProperties(Context context, String propertyFileName) {
        Properties properties = new Properties();

        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(propertyFileName);
            properties.load(inputStream);
        } catch (IOException e) {
            RILogUtils.logError(LOG_TAG, "" + e.toString());
            e.printStackTrace();
            throw new RuntimeException("Error loading properties file. Check your assets folder " +
                    "for the properties file");
        }

        return properties;
    }

    /**
     *  Utility method that performs a check to verify the presence of a resource in the app
     *
     *  @param context          A valid context to access resources
     *  @param resourceName     The name of the resource
     *  @param resourceType     The type of the resource (ex. "raw")
     *  @return                 true if the resource was found
     */
    public static boolean isResourceAvailable(Context context, String resourceName, String resourceType) {
        int resourceExists =
                context.getResources().getIdentifier(resourceName, resourceType, context.getPackageName());

        return resourceExists != 0;
    }
}