package de.rocketinternet.android.tracking.interfaces;

import java.net.URL;

/**
 *  @author alessandro.balocco
 *
 *  API protocol for deeplink URL tracking
 */
public interface RIOpenUrlTracking {

    /**
     * Track a deeplink URL
     *
     * @param url The URL opened.
     */
    void trackOpenUrl(URL url);

/**
 *  Register a handler block to be called when the given pattern matches a deeplink URL.
 *
 *  The deeplink URL pattern may contain capture directives of the format `{<name>}` where '<name>'
 *  is replaced with the actual property name to access the captured information.
 *  The handler block receives a dictionary hash containing key-value properties obtained from pattern
 *  capture directives and from the query string of the deeplink URL.
 *
 *  @param handler A handler to be called on matching a deeplink URL.
 *  @param pattern A pattern of regex extended with capture directive syntax.
 */
//    void registerHandler(void(^)(NSDictionary *))handler forOpenURLPattern:(NSString *)pattern;
}
