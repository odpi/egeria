/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions.rex;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadTypeException extends RuntimeException {

    private String message;

    public BadTypeException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
