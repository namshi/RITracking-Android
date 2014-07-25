package de.rocketinternet.android.tracking.interfaces;

/**
 *  @author alessandro.balocco
 *
 *  API protocol for exception tracking
 */
public interface RIExceptionTracking {

    /**
     * Track an exception occurrence by name
     *
     * @param name The exception that happed.
     */
    void trackExceptionWithName(String name);
}
