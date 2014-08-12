package de.rocketinternet.android.tracking.utils;

import java.lang.annotation.Annotation;

import de.rocketinternet.android.tracking.annotations.RITrackingScreenAnnotation;

/**
 * @author alessandro.balocco
 *
 * Utility class that provide access to declared annotations
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