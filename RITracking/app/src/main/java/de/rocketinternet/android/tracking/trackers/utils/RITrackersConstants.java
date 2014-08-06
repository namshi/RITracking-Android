package de.rocketinternet.android.tracking.trackers.utils;

/**
 * @author alessandro.balocco
 *         <p/>
 *         This utility class contains all static constant for all trackers. It is possible to store here
 *         constants that remain static among different apps.
 */
public class RITrackersConstants {

    /* Google Analytics - Deprecated */
    public static final String GA_TRACKING_ID = "RIGoogleAnalyticsTrackingID";

    /* Google Tag Manager */
    public static final String GTM_CONTAINER_ID = "RIGoogleTagManagerContainerID";
    public static final String GTM_CONTAINER_RESOURCE_NAME = "gtm_container";
    public static final String GTM_CONTAINER_RESOURCE_TYPE = "raw";
    public static final String GTM_SCREEN_NAME = "screenName";
    public static final String GTM_OPEN_APP = "openApp";
    public static final String GTM_OPEN_SCREEN = "openScreen";

    /* Ad4Push */
    public static final String AD4PUSH_INTEGRATION = "RIAd4PushIntegration";
    public static final String AD4PUSH_VIEW = "view";

    /* Ad4Just */
    public static final String ADJUST_INTEGRATION = "RIAdJustIntegration";
}
