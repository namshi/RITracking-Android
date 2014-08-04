package de.rocketinternet.android.tracking.containers.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

import de.rocketinternet.android.tracking.R;
import de.rocketinternet.android.tracking.annotations.RITrackingScreenAnnotation;
import de.rocketinternet.android.tracking.core.RITracking;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 *
 * This is an implementation of a BaseActivity that the user of the library can extend to
 * automatize these behaviours:
 *
 * - Sending screen name of the activity when launched if screen annotation has been defined
 *
 */
public class RITrackingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Annotation annotation = getClass().getAnnotation(RITrackingScreenAnnotation.class);
        if (annotation != null) {
            String screenName = ((RITrackingScreenAnnotation) annotation).screenName();
            if (!TextUtils.isEmpty(screenName)) {
                RITracking.getInstance().trackScreenWithName(screenName);
            }
        }
    }
}
