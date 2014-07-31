package de.rocketinternet.android.tracking.containers;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import de.rocketinternet.android.tracking.R;
import de.rocketinternet.android.tracking.core.RITracking;

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
}
