/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.api.exceptions;

public class LineageNotFoundException extends RuntimeException {

    public LineageNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
