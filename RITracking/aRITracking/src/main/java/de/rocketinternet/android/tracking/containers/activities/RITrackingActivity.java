package de.rocketinternet.android.tracking.containers.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.dexmaker.dx.dex.file.AnnotationUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import de.rocketinternet.android.tracking.R;
import de.rocketinternet.android.tracking.annotations.RITrackingScreenAnnotation;
import de.rocketinternet.android.tracking.core.RITracking;
import de.rocketinternet.android.tracking.utils.RIAnnotationUtils;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 *
 * This is an implementation of a BaseActivity that the user of the library can extend to
 * automatize these behaviours:
 *
 * - Sending screen name of the activity when launched if screen annotation has been defined
 * - Handling enabling notification in onCreate in case Ad4Push is enabled
 * - Handling resume and pause callbacks
 *
 */
public abstract class RITrackingActivity extends Activity {

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
