/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.spring;

import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.web.server.ResponseStatusException;

/**
 * General exception that should be used by any Spring controllers in order to ensure an error response is generated
 * with an appropriate HttpStatus code.
 */
public class SpringErrorResponseException extends ResponseStatusException {

    private FFDCResponseBase response;

    /**
     * Construct a new Spring error response.
     *
     * @param status the HTTP status code to use for the response
     * @param response the response body
     */
    public SpringErrorResponseException(int status, FFDCResponseBase response) {
        this(status, response, null);
    }

    /**
     * Construct a new Spring error response.
     *
     * @param status the HTTP status code to use for the response
     * @param response the response body
     * @param cause the underlying cause of the error (optional)
     */
    public SpringErrorResponseException(int status, FFDCResponseBase response, @Nullable Throwable cause) {
        super(HttpStatus.valueOf(status), null, cause);
        this.response = response;
    }

    /**
     * Retrieve the response that generated this error.
     *
     * @return FFDCResponseBase
     */
    public FFDCResponseBase getResponse() {
        return response;
    }

}
