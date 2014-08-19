package de.rocketinternet.android.tracking.containers.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import de.rocketinternet.android.tracking.core.RITracking;
import de.rocketinternet.android.tracking.utils.RIAnnotationUtils;

/**
 * @author alessandro.balocco
 *
 * This is an implementation of a BaseListActivity that the user of the library can extend to
 * automatize these behaviours:
 *
 * - Sending screen name of the activity when launched if screen annotation has been defined
 * - Handling enabling notification in onCreate in case Ad4Push is enabled
 * - Handling resume and pause callbacks
 *
 */
public abstract class RITrackingListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RITracking.getInstance().trackActivityCreated(this, false);

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
