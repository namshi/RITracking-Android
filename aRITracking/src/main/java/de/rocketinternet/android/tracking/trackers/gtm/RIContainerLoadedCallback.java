package de.rocketinternet.android.tracking.trackers.gtm;

import com.google.android.gms.tagmanager.Container;
import com.google.android.gms.tagmanager.ContainerHolder;

/**
 * @author alessandro.balocco
 */
public class RIContainerLoadedCallback implements ContainerHolder.ContainerAvailableListener {

    @Override
    public void onContainerAvailable(ContainerHolder containerHolder, String containerVersion) {
        // We load each container when it becomes available.
        Container container = containerHolder.getContainer();

        // It is possible to register callbacks when container became available
    }
}
