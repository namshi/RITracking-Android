package de.rocketinternet.android.tracking.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Properties;

import de.rocketinternet.android.tracking.annotations.RITrackingScreenAnnotation;
import de.rocketinternet.android.tracking.core.RITracking;

/**
 *  @author alessandro.balocco
 *
 *  Utility class that provide access to declared annotations
 */
public class RIAnnotationUtils {

    /**
     *  Default log tag for this class
     */
    public static String getScreenNameFromAnnotation(Class<?> callingClass) {
        Annotation annotation = callingClass.getAnnotation(RITrackingScreenAnnotation.class);
        if (annotation != null) {
            return ((RITrackingScreenAnnotation) annotation).screenName();
        }
        return null;
    }
}