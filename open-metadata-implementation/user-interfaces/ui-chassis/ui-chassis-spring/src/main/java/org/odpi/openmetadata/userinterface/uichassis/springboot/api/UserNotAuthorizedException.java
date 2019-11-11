/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;


@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UserNotAuthorizedException extends HttpServerErrorException {

    /**
     *
     * @param message text to display to client
     */
    public UserNotAuthorizedException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }

}
