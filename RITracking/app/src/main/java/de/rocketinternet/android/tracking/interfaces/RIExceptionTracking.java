package de.rocketinternet.android.tracking.interfaces;

/**
 *  @author alessandro.balocco
 *
 *  API interface for exception tracking
 */
public interface RIExceptionTracking {

    /**
     * Track an exception occurrence by name
     *
     * @param name The exception that happened.
     */
    void trackExceptionWithName(String name);
}
