/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * If an OMAS uses Generic types to implement it's Java API, then the responses can implement this interface.
 */
public interface GenericResponse<R> extends FFDCResponse {

    /**
     * Add several results for the response
     * @param results collection with results
     * */
    void addAllResults(Collection<? extends R> results);

    /**
     * Add single result for the response
     * @param result - one result
     * */
    void addResult(R result);

    /**
     * Get all results of the response
     *
     * @return results
     **/
    List<R> results();

    /**
     * Get head element from result array.
     * Needed when we know for sure that the answer is single object
     *
     * @return result
     **/
    default Optional<R> head() {
        final List<R> results = results();
        if (results != null && !results.isEmpty()) {
            return Optional.of(results.get(0));
        }

        return Optional.empty();
    }
}