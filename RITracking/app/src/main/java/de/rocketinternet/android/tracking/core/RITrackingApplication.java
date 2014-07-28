package de.rocketinternet.android.tracking.core;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;

import java.util.HashMap;

/**
 *  @author alessandro.balocco
 *
 *  The RITrackingApplication class provides an application class with preset tracking of user sessions
 *
 *  This class should be inherited by custom application class.
 */
public class RITrackingApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RITracking.getInstance().setDebug(true);
        RITracking.getInstance().startWithConfigurationFromPropertyList(getApplicationContext());
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}
