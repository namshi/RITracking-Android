package de.rocketinternet.android.tracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * @author alessandro.balocco
 */
public class RIPushNotificationExampleReceiver extends BroadcastReceiver {

    public static final String INTENT_ACTION_DISPLAYED = "com.ad4screen.sdk.intent.action.DISPLAYED";
    public static final String INTENT_ACTION_CLICKED = "com.ad4screen.sdk.intent.action.CLICKED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle bundle = intent.getExtras();
        handleEvent(context, action, bundle);
    }

    private void handleEvent(Context context, String action, Bundle extras) {
        if (extras != null && !TextUtils.isEmpty(action)) {
            if (action.equals(INTENT_ACTION_DISPLAYED)) {

            } else if (action.equals(INTENT_ACTION_CLICKED)) {

            }
        }
    }
}
