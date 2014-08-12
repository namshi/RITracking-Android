package de.rocketinternet.android.tracking.interfaces;

import java.util.HashMap;

/**
 * @author alessandro.balocco
 *         <p/>
 *         This interface implements tracking for exceptions
 */
public interface RIExceptionTracking {

    /**
     * Track an exception occurrence by name
     *
     * @param params    Parameters associated with the exception (ex. exception level) (optional)
     * @param exception The exception that happened.
     */
    void trackException(HashMap<String, String> params, Exception exception);
}
