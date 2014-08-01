package de.rocketinternet.android.tracking.containers;

import android.app.Activity;
import android.os.Bundle;

import de.rocketinternet.android.tracking.R;
import de.rocketinternet.android.tracking.core.RITracking;
import de.rocketinternet.android.tracking.core.RITrackingApplication;

/**
 *  @author alessandro.balocco
 *
 *  This is an implementation of a BaseActivity that the user of the library can extend to
 *  automatize these behaviours:
 *
 *  - Sending screen name of the activity when launched
 *
 */
public class RITrackingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RITracking.getInstance().trackScreenWithName(getClass().getSimpleName());
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((RITrackingApplication) getApplication()).handleMainActivityResumed(this);
    }
}
