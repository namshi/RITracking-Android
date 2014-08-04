package de.rocketinternet.android.tracking.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author alessandro.balocco
 *
 * This annotation allows users of the library to define a screen name. By doing that in Activities or
 * Fragments that extend the base ones, the screen name will be automatically sent to the registered
 * trackers.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RITrackingScreenAnnotation {
    public String screenName();
}
