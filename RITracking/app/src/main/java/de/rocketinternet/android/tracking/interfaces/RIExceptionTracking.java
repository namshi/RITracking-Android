package de.rocketinternet.android.tracking.interfaces;

import java.util.HashMap;

/**
 * @author alessandro.balocco
 *         API interface for exception tracking
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
