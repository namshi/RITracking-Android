package de.rocketinternet.android.tracking.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FileUtils {

    private static final String LOG_TAG = FileUtils.class.getSimpleName();

	public static Properties getProperties(Context context, String propertyFileName) {
        Properties properties = new Properties();
		try {
			AssetManager assetManager = context.getAssets();
			InputStream inputStream = assetManager.open(propertyFileName);
			properties.load(inputStream);
		} catch (IOException e) {
            RILogUtils.logError(LOG_TAG, "" + e.toString());
            e.printStackTrace();
		}

		return properties;
	}
}