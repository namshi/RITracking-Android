package de.rocketinternet.android.tracking.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class RIAssetsPropertiesUtils {

    private static final String LOG_TAG = RIAssetsPropertiesUtils.class.getSimpleName();
	private static final String PROPERTIES_FILE_NAME = "ri_tracking_config.properties";

	public static Properties getProperties(Context context) throws IOException {
        Properties properties = new Properties();
		try {
			AssetManager assetManager = context.getAssets();
			InputStream inputStream = assetManager.open(PROPERTIES_FILE_NAME);
			properties.load(inputStream);
		} catch (IOException e) {
            RILogUtils.logError(LOG_TAG, "" + e.toString());
            e.printStackTrace();
            throw new IOException("Error loading properties file. Check your assets folder for the properties file");
		}

		return properties;
	}
}