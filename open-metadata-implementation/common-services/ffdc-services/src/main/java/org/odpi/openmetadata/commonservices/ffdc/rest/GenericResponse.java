/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import java.util.Collection;
import java.util.List;


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
    List<R> getResult();

    /**
     * Get head element from result array.
     * Needed when we know for sure that the answer is single object
     *
     * @return result
     **/
    default R getHead() {
        List<R> result = getResult();
        if (result != null && !result.isEmpty()) {
            return result.get(0);
        }

        return null;
    }
}