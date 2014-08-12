package de.rocketinternet.android.tracking.listeners;

import java.util.Map;

/**
 *  @author alessandro.balocco
 *
 *  API interface that is called when a URL respect the patterns previously defined and needs to be
 *  filter to provide specific behaviour
 */
public interface RIOnHandledOpenUrl {

    /**
     * Callback to the listener that provides information to allow deep-linking URL filtering
     *
     * @param identifier    the handler identifier
     * @param params        expected parameters coming back from the url filtering
     */
    void onHandledOpenUrl(String identifier, Map<String, String> params);
}
