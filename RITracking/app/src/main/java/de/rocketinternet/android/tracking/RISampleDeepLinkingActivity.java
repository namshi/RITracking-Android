package de.rocketinternet.android.tracking;

import android.os.Bundle;

import java.util.Map;

import de.rocketinternet.android.tracking.containers.deeplinks.RITrackingDeepLinkingActivity;
import de.rocketinternet.android.tracking.core.RITracking;
import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrl;

/**
 * @author alessandro.balocco
 *
 * This activity is meant to be an example implementation for filtering deep-links
 *
 */
public class RISampleDeepLinkingActivity extends RITrackingDeepLinkingActivity implements RIOnHandledOpenUrl {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void registerHandlers() {
        // Register Handlers for intercepting deep-links
        RITracking.getInstance().registerHandler("IDENTIFIER", "HOST", "PATH", this);
    }

    @Override
    public void onHandledOpenUrl(String identifier, Map<String, String> params) {
        // Handle the eventual callback
        if (identifier.equals("CATALOG")) {

        }
    }
}
