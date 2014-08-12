package de.rocketinternet.android.tracking.interfaces;

import com.newrelic.agent.android.util.NetworkFailure;

import java.util.Map;

/**
 * @author alessandro.balocco
 *         <p/>
 *         This interface implements tracking for network requests and network failures
 */
public interface RINetworkTracking {

    /**
     * A method to record HTTP transactions with several available levels of detail.
     *
     * @param url           The url of the request
     * @param statusCode    The status code of the HTTP response such as 200 for OK
     * @param startTime     The start time of the request in milliseconds
     * @param endTime       The end time of the request in milliseconds
     * @param bytesSent     The number of bytes sent in the request
     * @param bytesReceived The number of bytes received in the response
     * @param responseBody  (Optional) The response body of the HTTP response.
     * @param params        (Optional) Additional parameters
     */
    void trackHttpTransaction(String url, int statusCode, long startTime, long endTime, long bytesSent,
                              long bytesReceived, String responseBody, Map<String, String> params);

    /**
     * A method to record network failures. Two parameters can be used a generic exception or a
     * NetworkFailure from NewRelic SDK. If exception will be null than the second parameter will be
     * used.
     *
     * @param url       The url of the request
     * @param startTime The start time of the request in milliseconds
     * @param endTime   The end time of the request in milliseconds
     * @param exception (Exclusive) The exception that occurred
     * @param failure   (Exclusive) The type of NetworkFailure that occurred
     */
    void trackNetworkFailure(String url, long startTime, long endTime, Exception exception,
                             NetworkFailure failure);
}
