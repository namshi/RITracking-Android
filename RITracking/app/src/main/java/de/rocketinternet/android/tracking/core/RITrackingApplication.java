package de.rocketinternet.android.tracking.core;

import android.app.Activity;
import android.app.Application;

import org.json.JSONObject;

import de.rocketinternet.android.tracking.utils.RILogUtils;
import de.rocketinternet.android.tracking.utils.serializers.RIAppLaunchJsonSerializer;

/**
 *  @author alessandro.balocco
 *
 *  The RITrackingApplication class provides an application class with preset tracking of user sessions
 *
 *  This class should be inherited by custom application class.
 */
public class RITrackingApplication extends Application {

    private long mLaunchTime;

    @Override
    public void onCreate() {
        super.onCreate();
        RILogUtils.logDebug("RITracking App launched");

        RITracking.getInstance().setDebug(true);
        RITracking.getInstance().startWithConfigurationFromPropertiesList(getApplicationContext());

        // Keep launch time to compare with newer timestamp later
        mLaunchTime = System.currentTimeMillis();
    }

    public void handleMainActivityResumed(Activity activity) {
        // Only run the following for the launch of the app
        if (mLaunchTime == 0) {
            return;
        }

        // Collect launching information and print them out
        JSONObject appLaunchInfoJson = RIAppLaunchJsonSerializer.getAppLaunchInformationJson(activity, mLaunchTime);
        RILogUtils.logDebug("Launch JSON data: " + appLaunchInfoJson.toString());

        // Reset the launch time to identify the launch was handled
        mLaunchTime = 0;
    }
}
