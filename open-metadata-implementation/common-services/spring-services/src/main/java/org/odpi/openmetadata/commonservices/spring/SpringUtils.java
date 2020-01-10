/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.spring;

import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

/**
 * Utility methods for Spring.
 */
public class SpringUtils {

    /**
     * Check the response for any error and handle accordingly, or return the response as-is if no error.
     *
     * @param response the response to check
     * @param <T> the response type
     * @return the response as-is, if no error
     * @throws SpringErrorResponseException if there is any error in the response
     */
    public static <T extends FFDCResponseBase> T createSpringResponse(T response) throws SpringErrorResponseException {
        // TODO: is it sufficient to simply look for non-200 or an exception class name?
        if (response.getRelatedHTTPCode() != 200 || response.getExceptionClassName() != null) {
            throw new SpringErrorResponseException(response.getRelatedHTTPCode(), response);
        }
        return response;
    }

}
