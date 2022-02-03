/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions;

import org.odpi.openmetadata.userinterface.uichassis.springboot.api.UserInterfaceErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends HttpServerErrorException {


    private UserInterfaceErrorCodes errorCode;
    /**
     *
     * @param errorCode the error ui error code associated with the exception
     * @param message text to display to client
     */
    public BadRequestException(UserInterfaceErrorCodes errorCode, String message) {
        super(HttpStatus.BAD_REQUEST, message);
        this.errorCode = errorCode;
    }

    public UserInterfaceErrorCodes getErrorCode() {
        return errorCode;
    }
}
