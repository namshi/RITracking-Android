package de.rocketinternet.android.tracking;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Map;

import de.rocketinternet.android.tracking.core.RITracking;
import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrl;

/**
 * @author alessandro.balocco
 *
 * This activity is meant to be extended by clients. The ones which are interested in receiving and
 * intercepting deep-linking actions should extend this activity and implement RIOnHandledOpenUrl.
 * By doing that, they will be able to redirect their users to any specific activity.
 */
public class RIDeepLinkingActivity extends Activity implements RIOnHandledOpenUrl {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_linking);

        // Debug UI
        mTextView = (TextView) findViewById(R.id.text);
        registerHandlers();
        handleIntentReceived();

        // Finish this activity
        finish();
    }

    /**
     *  This method is meant to register the handlers for deep linking and saving them in
     *  RITraking singleton. Users of the library should provide their handlers.
     */
    private void registerHandlers() {
        // TODO: register handlers in RITracking
    }

    private void handleIntentReceived() {
        Intent intent = getIntent();
        if (intent == null || intent.getData() == null) {
            finish();
        }

        Uri data = intent.getData();
        RITracking.getInstance().queryHandlers(data);

        mTextView.setText("This is a deep linking activity and this is the url: " + data.toString());
    }

    @Override
    public void onHandledOpenUrl(String identifier, Map<String, String> params) {
        // TODO: handling custom behaviour for different clients needs depending on returned information
    }
}
