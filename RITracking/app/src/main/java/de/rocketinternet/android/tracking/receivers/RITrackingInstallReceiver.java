package de.rocketinternet.android.tracking.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ad4screen.sdk.ReferrerHandler;
import com.google.android.gms.tagmanager.InstallReferrerReceiver;
import com.adjust.sdk.ReferrerReceiver;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;

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

        /**
         * Google Tag Manager
         *
         * Check if the tracker is initialized and in this case send the broadcast to it
         */
        String resourceName = RITrackersConstants.GTM_CONTAINER_RESOURCE_NAME;
        String resourceType = RITrackersConstants.GTM_CONTAINER_RESOURCE_TYPE;
        boolean isResourceAvailable = RITrackingConfiguration.getInstance().isResourceAvailable(context, resourceName, resourceType);
        String containerId = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.GTM_CONTAINER_ID);
        if (isResourceAvailable && !TextUtils.isEmpty(containerId)) {
            new InstallReferrerReceiver().onReceive(context, intent);
        }

        /**
         * Ad4Push
         *
         * Check if the tracker has information for initialization
         */
        String ad4PushIntegration = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.AD4PUSH_INTEGRATION);
        boolean isAd4PushIntegrationNeeded = Boolean.valueOf(ad4PushIntegration);
        if (isAd4PushIntegrationNeeded) {
            new ReferrerHandler().onReceive(context, intent);
        }

        // AdJust
        new ReferrerReceiver().onReceive(context, intent);
    }
}
