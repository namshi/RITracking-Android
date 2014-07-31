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

    private static final String LOG_TAG = RIResourceUtils.class.getSimpleName();

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

    public static boolean isResourceAvailable(Context context, String resourceName, String resourceType) {
        int resourceExists =
                context.getResources().getIdentifier(resourceName, resourceType, context.getPackageName());

        return resourceExists != 0;
    }
}