package de.rocketinternet.android.tracking;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.Container;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;

import java.util.concurrent.TimeUnit;

import de.rocketinternet.android.tracking.annotations.RITrackingScreenAnnotation;
import de.rocketinternet.android.tracking.containers.activities.RITrackingActivity;
import de.rocketinternet.android.tracking.trackers.gtm.RIContainerHolder;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 */
@RITrackingScreenAnnotation(screenName = "Homepage")
public class MainActivity extends RITrackingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Actual class is: " + getClass().getName(), Toast.LENGTH_LONG).show();

        RILogUtils.logDebug("First activity app launched");
    }
}
