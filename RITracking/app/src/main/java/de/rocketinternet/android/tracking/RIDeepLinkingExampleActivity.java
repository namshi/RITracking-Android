package de.rocketinternet.android.tracking;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Map;

import de.rocketinternet.android.tracking.core.RITracking;
import de.rocketinternet.android.tracking.handlers.RIOpenUrlHandler;
import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrl;

/**
 * @author alessandro.balocco
 *
 * This activity is meant to be an example implementation for filtering deep-links
 */
public class RIDeepLinkingExampleActivity extends RIDeepLinkingActivity implements RIOnHandledOpenUrl {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    void registerHandlers() {
        RITracking.getInstance().registerHandler("CATALOG", "mxc", "c", this);
    }

    @Override
    public void onHandledOpenUrl(String identifier, Map<String, String> params) {
        if (identifier.equals("CATALOG")) {

        }
    }
}
