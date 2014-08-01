package de.rocketinternet.android.tracking.utils;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * @author alessandro.balocco
 *         <p/>
 *         Utility class that provides access to package information.
 */
public class RIPackageUtils {

    public static String getAppVersion(Context context) throws Exception {
        PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        if (packageInfo == null) {
            throw new Exception("Accessing package information failed.");
        }
        return packageInfo.versionName;
    }
}
