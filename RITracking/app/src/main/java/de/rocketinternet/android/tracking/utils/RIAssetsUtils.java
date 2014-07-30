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
public class RIAssetsUtils {

    private static final String LOG_TAG = RIAssetsUtils.class.getSimpleName();

	public static Properties getProperties(Context context, String propertyFileName) throws IOException {
        Properties properties = new Properties();
		try {
			AssetManager assetManager = context.getAssets();
			InputStream inputStream = assetManager.open(propertyFileName);
			properties.load(inputStream);
		} catch (IOException e) {
            RILogUtils.logError(LOG_TAG, "" + e.toString());
            e.printStackTrace();
            throw new IOException("Error loading properties file. Check your assets folder for the properties file");
		}

		return properties;
	}
}