package de.rocketinternet.android.tracking.trackers.gtm;

import com.google.android.gms.tagmanager.ContainerHolder;

/**
 *  @author alessandro.balocco
 *
 *  Singleton to hold the GTM Container (since it should be only created once per run of the app).
 */
public class RIContainerHolder {
    private static ContainerHolder containerHolder;

    /**
     *  Utility class; don't instantiate.
     */
    private RIContainerHolder() {
    }

    public static ContainerHolder getContainerHolder() {
        return containerHolder;
    }

    public static void setContainerHolder(ContainerHolder c) {
        containerHolder = c;
    }
}