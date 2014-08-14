package de.rocketinternet.android.tracking.interfaces;

import android.net.Uri;

import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrl;

/**
 * @author alessandro.balocco
 *
 * API interface for deeplink URL tracking
 */
public interface RIOpenUrlTracking {

    /**
     * Track a deep-link URL
     *
     * @param uri The URL opened.
     */
    void trackOpenUrl(Uri uri);

    /**
     * Register a handler block to be called when the given pattern matches a deep-link URL.
     *
     * The deep-link URL pattern may contain capture directives of the format `{<name>}` where '<name>'
     * is replaced with the actual property name to access the captured information.
     * The handler block receives a dictionary hash containing key-value properties obtained from pattern
     * capture directives and from the query string of the deep-link URL.
     *
     * @param identifier An identifier for filtering callbacks
     * @param host       The host part of the link
     * @param path       The path part of the link
     * @param listener   A listener for callbacks
     */
    void registerHandler(String identifier, String host, String path, RIOnHandledOpenUrl listener);

    // TODO: add registerHandler method to accept bundles as params
}
