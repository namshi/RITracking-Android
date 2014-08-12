package de.rocketinternet.android.tracking.containers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import de.rocketinternet.android.tracking.R;
import de.rocketinternet.android.tracking.core.RITracking;
import de.rocketinternet.android.tracking.utils.RIAnnotationUtils;

/**
 * @author alessandro.balocco
 *
 * This is an implementation of a BaseActivity that the user of the library can extend if the application
 * needs a splash screen
 *
 * - Sending screen name of the activity when launched if screen annotation has been defined
 * - Handling blocking notification in case Ad4Push is enabled
 *
 */
public abstract class RITrackingSplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RITracking.getInstance().trackActivityCreated(this, true);

        String screenName = RIAnnotationUtils.getScreenNameFromAnnotation(getClass());
        if (!TextUtils.isEmpty(screenName)) {
            RITracking.getInstance().trackScreenWithName(screenName);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        RITracking.getInstance().trackActivityResumed(this);
    }

    @Override
    protected void onNewIntent(Intent newIntent) {
        this.setIntent(newIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        RITracking.getInstance().trackActivityPaused(this);
    }
}
