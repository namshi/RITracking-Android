package de.rocketinternet.android.tracking.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ad4screen.sdk.ReferrerHandler;
import com.google.android.gms.tagmanager.InstallReferrerReceiver;

/**
 * @author alessandro.balocco
 *
 * This broadcast receiver has been implemented because more than one SDK is interested in intercepting
 * the "com.android.vending.INSTALL_REFERRER" intent. For this reason is not possible to use the
 * default manifest declaration but the library needs to intercept the intent and dispatch it to the
 * registered trackers.
 */
public class RITrackingInstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        // Google Tag Manager
        new InstallReferrerReceiver().onReceive(context, intent);

        // Ad4Push
        new ReferrerHandler().onReceive(context, intent);
    }
}
