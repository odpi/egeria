/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.spring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * A general exception handler for translating Spring exceptions into appropriate REST API responses.
 */
@ControllerAdvice
public class SpringExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle any Spring-specific error response exception to return an appropriate HttpStatus code and exception
     * body.
     *
     * @param ex - raised exception to be handled
     * @param request - the initial web request - the initial web request
     * @return the entity containing the response exception
     */
    @ExceptionHandler(value = {SpringErrorResponseException.class})
    protected ResponseEntity<Object> handleSpringErrorResponseException(SpringErrorResponseException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getResponse(), ex.getStatus());
    }

}
