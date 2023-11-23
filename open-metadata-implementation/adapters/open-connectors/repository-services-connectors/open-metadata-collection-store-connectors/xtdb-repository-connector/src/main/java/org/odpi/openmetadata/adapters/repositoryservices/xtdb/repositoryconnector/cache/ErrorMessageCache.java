/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A thread-safe way of capturing errors for a given transaction for error-handling purposes across threads.
 * (Basically so we can still throw meaningful errors back to API calls even though the back-end processing
 * happens in a different thread, asynchronously, from the API call itself.)
 */
public class ErrorMessageCache {

    private static final ConcurrentMap<Long, Exception> map = new ConcurrentHashMap<>();

    /**
     * Add an error for a given transaction.
     * @param txId transaction for which to add the error
     * @param error error to record
     * @return Exception error that was added
     */
    public static Exception add(Long txId, Exception error) {
        map.put(txId, error);
        return error;
    }

    /**
     * Retrieve any error for the given transaction.
     * @param txId transaction for which to retrieve error
     * @return Exception if there was any error, or null if there was no error
     */
    public static Exception get(Long txId) {
        // Also remove the error once we've thrown it, to minimize memory usage
        return map.remove(txId);
    }

}
