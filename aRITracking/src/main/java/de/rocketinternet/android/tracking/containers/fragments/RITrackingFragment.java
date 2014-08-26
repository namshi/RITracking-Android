package de.rocketinternet.android.tracking.containers.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.text.TextUtils;

import java.lang.annotation.Annotation;

import de.rocketinternet.android.tracking.annotations.RITrackingScreenAnnotation;
import de.rocketinternet.android.tracking.core.RITracking;
import de.rocketinternet.android.tracking.utils.RIAnnotationUtils;

/**
 * @author alessandro.balocco
 *
 * This is an implementation of a BaseFragment that the user of the library can extend to
 * automatize these behaviours:
 *
 * - Sending screen name of the fragment when it is attached if screen annotation has been defined
 *
 * This class is supported from API level 11. If you are targeting something smaller as minumum SDK
 * you should use {@link de.rocketinternet.android.tracking.containers.fragments.RITrackingFragmentSupport}
 * instead
 */
@SuppressLint("NewApi")
public abstract class RITrackingFragment extends Fragment {

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        String screenName = RIAnnotationUtils.getScreenNameFromAnnotation(getClass());
        if (!TextUtils.isEmpty(screenName)) {
            RITracking.getInstance().trackScreenWithName(screenName);
        }
    }
}
