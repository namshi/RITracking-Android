package de.rocketinternet.android.tracking.containers.fragments;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import java.lang.annotation.Annotation;

import de.rocketinternet.android.tracking.annotations.RITrackingScreenAnnotation;
import de.rocketinternet.android.tracking.core.RITracking;

/**
 * @author alessandro.balocco
 *
 * This is an implementation of a BaseFragment that the user of the library can extend to
 * automatize these behaviours:
 *
 * - Sending screen name of the fragment when it is attached if screen annotation has been defined
 *
 * This class is extend Fragment class from the support library. Use this class if your minumum SDK
 * is smaller that API level 11.
 */
public class RITrackingFragmentSupport extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Annotation annotation = activity.getClass().getAnnotation(RITrackingScreenAnnotation.class);
        if (annotation != null) {
            String screenName = ((RITrackingScreenAnnotation) annotation).screenName();
            if (!TextUtils.isEmpty(screenName)) {
                RITracking.getInstance().trackScreenWithName(screenName);
            }
        }
    }
}
