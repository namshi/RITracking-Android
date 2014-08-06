package de.rocketinternet.android.tracking.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ad4screen.sdk.ReferrerHandler;
import com.google.android.gms.tagmanager.InstallReferrerReceiver;

/**
 * @author alessandro.balocco
 *
 * This receiver is shared among different libraries
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
