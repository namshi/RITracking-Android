package de.rocketinternet.android.tracking.utils.serializers;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import de.rocketinternet.android.tracking.utils.RIDisplayUtils;
import de.rocketinternet.android.tracking.utils.RILogUtils;
import de.rocketinternet.android.tracking.utils.RIPackageUtils;

/**
 * @author alessandro.balocco
 *         <p/>
 *         Utility class in charge of serializing information when app launches
 */
public class RIAppLaunchJsonSerializer {

    private static final String JSON_KEY_DISPLAY_SIZE = "display_size";
    private static final String JSON_KEY_APP_VERSION = "app_version";
    private static final String JSON_KEY_DURATION = "duration";

    public static JSONObject getAppLaunchInformationJson(Activity activity, long launchTime) {
        JSONObject appInfoJson = new JSONObject();
        try {
            // Stringify screen size float value to avoidJSON value 4.3000001928 instead of 4.3
            appInfoJson.put(JSON_KEY_DISPLAY_SIZE, String.valueOf(RIDisplayUtils.getScreenSizeInches(activity)));
            try {
                String appVersion = RIPackageUtils.getAppVersion(activity);
                if (appVersion == null) {
                    RILogUtils.logDebug("Unexpected empty app version");
                } else {
                    appInfoJson.put(JSON_KEY_APP_VERSION, appVersion);
                }
            } catch (Exception e) {
                RILogUtils.logError("Unexpected exception when accessing package version: " + e);
            }
            appInfoJson.put(JSON_KEY_DURATION, (System.currentTimeMillis() - launchTime));
        } catch (JSONException e) {
            RILogUtils.logDebug("Unexpected exception when adding information to JSON object: " + e);
        }

        return appInfoJson;
    }
}
