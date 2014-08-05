package de.rocketinternet.android.tracking.trackers;

import android.content.Context;
import android.location.Location;
import android.text.TextUtils;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import de.rocketinternet.android.tracking.R;
import de.rocketinternet.android.tracking.core.RITracking;
import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.interfaces.RIEcommerceEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIEventTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.interfaces.RIUserTracking;
import de.rocketinternet.android.tracking.models.RITrackingProduct;
import de.rocketinternet.android.tracking.models.RITrackingTotal;
import de.rocketinternet.android.tracking.trackers.gtm.RIContainerHolder;
import de.rocketinternet.android.tracking.trackers.gtm.RIContainerLoadedCallback;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 *         <p/>
 *         Convenience controller to proxy-pass tracking information to Google Tag Manager
 */
public class RIGoogleTagManagerTracker extends RITracker implements
        RIEventTracking,
        RIScreenTracking,
        RIUserTracking,
        RIEcommerceEventTracking {

    private static final String LOG_TAG = RIGoogleTagManagerTracker.class.getSimpleName();

    private static final String TRACKER_ID = "RIGoogleTagManagerTrackerID";
    private static final String CONTAINER_ID = "RIGoogleTagManagerContainerID";
    private static final String RESOURCE_NAME = "gtm_default_container";
    private static final String RESOURCE_TYPE = "raw";

    private DataLayer mDataLayer;

    public RIGoogleTagManagerTracker() {
    }

    @Override
    public void execute(Runnable runnable) {
        mQueue.execute(runnable);
    }

    @Override
    public String getIdentifier() {
        return mIdentifier;
    }

    @Override
    public boolean initializeTracker(Context context) {
        RILogUtils.logDebug("Initializing Google Tag Manager tracker");
        boolean isResourceAvailable = RITrackingConfiguration.getInstance().isResourceAvailable(context, RESOURCE_NAME, RESOURCE_TYPE);
        String containerId = RITrackingConfiguration.getInstance().getValueFromKeyMap(CONTAINER_ID);
        if (isResourceAvailable && !TextUtils.isEmpty(containerId)) {
            createTracker(context, containerId);
            return true;
        } else {
            RILogUtils.logError("GoogleTagManager tracker ISSUE: you are missing (1) container file " +
                    "in raw folder of your app or (2) container ID in tracking properties");
        }

        return false;
    }

    private void createTracker(final Context context, String containerId) {
        TagManager tagManager = TagManager.getInstance(context);

        // Modify the log level of the logger to print out not only
        // warning and error messages, but also verbose, debug, info messages.
        RILogUtils.logDebug("GoogleTagManager set verbose logging");
        tagManager.setVerboseLoggingEnabled(RITracking.getInstance().isDebug());

        PendingResult<ContainerHolder> pending =
                tagManager.loadContainerPreferNonDefault(containerId, R.raw.gtm_default_container);

        // The onResult method will be called as soon as one of the following happens:
        //  - 1. a saved container is loaded
        //  - 2. if there is no saved container, a network container is loaded
        //  - 3. the request times out. The example below uses a constant to manage the timeout period.
        pending.setResultCallback(new ResultCallback<ContainerHolder>() {
            @Override
            public void onResult(ContainerHolder containerHolder) {
                RILogUtils.logDebug("GoogleTagManager received result");
                RIContainerHolder.setContainerHolder(containerHolder);
                if (!containerHolder.getStatus().isSuccess()) {
                    RILogUtils.logError(LOG_TAG, "Failure loading container");
                    return;
                }
                RIContainerHolder.setContainerHolder(containerHolder);
                containerHolder.setContainerAvailableListener(new RIContainerLoadedCallback());
                mDataLayer = TagManager.getInstance(context).getDataLayer();
            }
        }, 2, TimeUnit.SECONDS);

        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
        mIdentifier = TRACKER_ID;
    }

    @Override
    public void trackEvent(String event, int value, String action, String category, Map<String, Object> data) {
        RILogUtils.logDebug("Google Tag Manager - Tracking event: " + event);

        if (mDataLayer == null) {
            RILogUtils.logError("Missing Google Tag Manager Data Layer reference");
            return;
        }

        mDataLayer.pushEvent(event, data);
    }

    @Override
    public void trackScreenWithName(String name) {
        RILogUtils.logDebug("Google Tag Manager - Tracking screen with name: " + name);

        if (mDataLayer == null) {
            RILogUtils.logError("Missing Google Tag Manager Data Layer reference");
            return;
        }

        String openScreen = RITrackersConstants.GTM_OPEN_SCREEN;
        String screenName = RITrackersConstants.GTM_SCREEN_NAME;
        mDataLayer.pushEvent(openScreen, DataLayer.mapOf(screenName, name));
    }

    @Override
    public void trackUserInfo(String userEvent, Map<String, Object> map) {
        RILogUtils.logDebug("Google Tag Manager - Tracking user event: " + userEvent);

        if (mDataLayer == null) {
            RILogUtils.logError("Missing Google Tag Manager Data Layer reference");
            return;
        }

        mDataLayer.pushEvent(userEvent, map);
    }

    @Override
    public void updateDeviceInfo(Map<String, Object> map) {
        // Not used by this tracker
    }

    @Override
    public void updateGeoLocation(Location location) {
        // Not used by this tracker
    }

    @Override
    public void trackCheckoutWithTransactionId(String idTransaction, RITrackingTotal total) {
        RILogUtils.logDebug("Google Tag Manager - Tracking checkout with transaction id: " + idTransaction);

        if (mDataLayer == null) {
            RILogUtils.logError("Missing Google Tag Manager Data Layer reference");
            return;
        }

        // FIXME: create map from total
    }

    @Override
    public void trackAddProductToCart(RITrackingProduct product) {

    }

    @Override
    public void trackRemoveProductFromCart(String idTransaction, int quantity) {

    }
}
