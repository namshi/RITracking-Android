package de.rocketinternet.android.tracking.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * @author alessandro.balocco
 *
 * Utility class that provides methods to get information from the device display
 */
public class RIDisplayUtils {

    public static float getScreenSizeInches(Activity activity) {
        if (activity != null) {
            DisplayMetrics dm = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
            double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
            double screenInches = Math.sqrt(x + y);
            return (float) Math.round(screenInches * 10) / 10;
        }
        return 0f;
    }
}
