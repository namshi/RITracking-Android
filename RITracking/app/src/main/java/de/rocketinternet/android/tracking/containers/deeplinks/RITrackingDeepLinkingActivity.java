package de.rocketinternet.android.tracking.containers.deeplinks;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Map;

import de.rocketinternet.android.tracking.R;
import de.rocketinternet.android.tracking.core.RITracking;
import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrl;

/**
 * @author alessandro.balocco
 *
 * This activity is meant to be extended by clients. The ones which are interested in receiving and
 * intercepting deep-linking actions should extend this activity and implement RIOnHandledOpenUrl.
 * By doing that, they will be able to redirect their users to any specific activity.
 */
public abstract class RITrackingDeepLinkingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerHandlers();
        handleIntentReceived();

        // Finish this activity
        finish();
    }

    /**
     *  This method is meant to register the handlers for deep linking and saving them in
     *  RITraking singleton. Users of the library should provide their handlers.
     */
    protected abstract void registerHandlers();

    private void handleIntentReceived() {
        Intent intent = getIntent();
        if (intent == null || intent.getData() == null) {
            finish();
            return;
        }

        Uri data = intent.getData();
        RITracking.getInstance().trackOpenUrl(data);
    }
}
