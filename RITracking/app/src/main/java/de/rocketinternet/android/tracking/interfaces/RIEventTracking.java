package de.rocketinternet.android.tracking.interfaces;

import java.util.Map;

/**
 * @author alessandro.balocco
 *
 * This interface implements tracking for events
 */
public interface RIEventTracking {

    /**
     * A method to track an event happening inside the application.
     * <p/>
     * The event may be triggered by the user and further information, such as category, action and
     * value are available.
     *
     * @param event    Name of the event
     * @param value    (optional) The value of the action
     * @param action   (optional) An identifier for the user action
     * @param category (optional) An identifier for the category of the app the user is in
     * @param data     (optional) Additional data about the event
     */
    void trackEvent(String event, long value, String action, String category, Map<String, Object> data);
}
